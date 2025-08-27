package SongPackage.SongFileManager.DownloadPackage;

import SongPackage.SongFileManager.SongDownloadedFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

	private final int CHUNK_SIZE = 128 * 1024;

	private final int ATTEMPS_COUNT = 1;

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

		try {

			// this.downloadWithoutSize(URI.create(audioSourceUrl[1]), audioSourceUrl[0]);
			this.downloadWithFfmpeg(audioSourceUrl[2], audioSourceUrl[1]);
			// this.downloadWithFfmpeg(temp, audioSourceUrl[1]);
			// this.downloadWithFfmpeg(temp, "testSong - 1");

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return this.tempFile;
	}

	private String[] urlAudioSource(String url) {

		ProcessBuilder builder = new ProcessBuilder("yt-dlp", "-f", "bestaudio", "--get-url", url, "--get-title");

		builder.redirectErrorStream(true);

		try {
			Process p = builder.start();
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

	private void downloadWithFfmpeg(String uri, String songName) {

		// String outputPath =
		// Paths.get(this.cachePath.getAbsolutePath(),songName+".m4a").toString();
		String outputPath = Paths.get(this.cachePath.getPath(), songName + ".m4a").toString();

		// String outputPath = this.cachePath + File.separator + songName + ".m4a";

		System.out.println(uri);
		System.out.println(songName);

		// ProcessBuilder builder = new ProcessBuilder("ffmpeg", "-i", uri, "-c", "copy",
		// outputPath, "-y");
		ProcessBuilder builder = new ProcessBuilder("ffmpeg", "-i", uri, "-c:a", "aac", outputPath, "-y");

		builder.redirectOutput(new File("logs.log"));
		builder.redirectError(new File("error_logs.log"));

		try {
			Process p = builder.start();
			p.waitFor();
			this.tempFile = new SongDownloadedFile().setSongPath(outputPath).setTitle(songName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	private void downloadWithoutSize(URI uri, String songName) throws IOException, InterruptedException {
		int start = 0;
		boolean hasEnd = false;

		String name = this.cachePath + "\\" + songName + ".m4a";
		try {
			Files.deleteIfExists(Paths.get(name));
		}
		catch (Exception e) {
		}
		File file = new File(name);
		FileOutputStream songData = new FileOutputStream(file);

		while (hasEnd != true) {

			int end = start + this.CHUNK_SIZE - 1;

			HttpRequest req = HttpRequest.newBuilder()
				.uri(uri)
				.header("Range", "bytes=" + start + "-" + end)
				.header("Accept-Encoding", "identity")
				.build();

			HttpResponse resp = HttpClient.newHttpClient().send(req, BodyHandlers.ofByteArray());

			if (resp.statusCode() == 302) {
				uri = URI.create(resp.headers().firstValue("location").orElse(null));
				continue;
			}

			byte[] b = (byte[]) resp.body();
			songData.write(b);

			if (b.length < this.CHUNK_SIZE) {

				hasEnd = true;
			}

			start += this.CHUNK_SIZE;

		}

		this.tempFile = new SongDownloadedFile().setSongPath(file.getAbsolutePath()).setTitle(songName);

	}

}