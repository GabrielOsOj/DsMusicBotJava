package SongPackage.SongFileManager.DownloadPackage;

import SongPackage.SongFileManager.SongDownloadedFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DwnDownload {

	final private File cachePath;

	private SongDownloadedFile tempFile;

	private final int ATTEMPS_COUNT = 10;

	private final String MSG_ERROR = "ERROR GETTING AUDIO SOURCE URL";

	public DwnDownload(String cachePath) {
		this.createCacheFolder(cachePath);
		this.cachePath = new File(cachePath);
	}

	private void createCacheFolder(String path) {
		new File(path).mkdir();
	}

	protected SongDownloadedFile download(String songId) {
		// this return songDownloadFile, contains the path and the name of song
		String[] audioSourceUrl = this.urlAudioSource(songId);
		Long audioSize = this.getAudioSize(audioSourceUrl[1]);
		this.downloadInOnePart(URI.create(audioSourceUrl[1]), audioSize, audioSourceUrl[0]);

		return this.tempFile;
	}

	private String[] urlAudioSource(String url) {

		ProcessBuilder builder = new ProcessBuilder("yt-dlp", "-f", "bestaudio", "--get-url", url, "--get-title");

		builder.redirectErrorStream(true);

		try {
			Process p = builder.start();
			// p.getInputStream().transferTo(System.out);
			p.waitFor();

			BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			StringBuilder output = new StringBuilder();

			while ((line = bf.readLine()) != null) {
				output.append(line);
				output.append("\n");
			}
			return output.toString().split("\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Long getAudioSize(String audioSourceUrl) {
		Long tamanioAudioSource = 0l;
		int attempts = 0;
		String audioSource;
		do {
			try {
				Thread.sleep(1000);
				URI uri = URI.create(audioSourceUrl);
				HttpRequest request = HttpRequest.newBuilder(uri).GET().build();

				HttpResponse rp = HttpClient.newHttpClient().send(request, BodyHandlers.ofInputStream());

				tamanioAudioSource = rp.headers().firstValueAsLong("Content-Length").getAsLong();

				if (tamanioAudioSource == 0l) {
					attempts++;
					System.out.println("Could not get size, retrying");
					Thread.sleep(1000);
				}

			}
			catch (Exception e) {
				e.printStackTrace();
			}

			if (attempts > this.ATTEMPS_COUNT) {
				throw new RuntimeException(this.MSG_ERROR);
			}

		}
		while (tamanioAudioSource == 0l);
		return tamanioAudioSource;
	}

	private void downloadInOnePart(URI uri, long end, String songName) {
		HttpRequest req = HttpRequest.newBuilder().uri(uri).header("Range", "bytes=" + 0 + "-" + end).build();

		try {
			String name = this.cachePath + "\\" + songName + ".m4a";
			Files.deleteIfExists(Paths.get(name));
			File file = new File(name);

			HttpResponse resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofFile(file.toPath()));

			this.tempFile = new SongDownloadedFile()
					.setSongPath(file.getAbsolutePath())
					.setTitle(songName);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}