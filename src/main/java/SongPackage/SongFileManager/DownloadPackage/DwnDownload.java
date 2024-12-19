package SongPackage.SongFileManager.DownloadPackage;

import SongPackage.SongFileManager.SongDownloadedFile;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.client.ClientType;
import com.github.kiulian.downloader.downloader.client.Clients;
import com.github.kiulian.downloader.downloader.request.Request;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import java.io.File;

public class DwnDownload {

    final private YoutubeDownloader dw;
    final private File cachePath;
    private SongDownloadedFile tempFile;

    public DwnDownload(String cachePath) {

        this.dw = new YoutubeDownloader();
        this.createCacheFolder(cachePath);
        this.cachePath = new File(cachePath);
    }

    private void createCacheFolder(String path) {
        new File(path).mkdir();
    }

    protected SongDownloadedFile download(String songId) {

        
        //esto tiene que retornarme un songDownloadedFile, debe contener la ruta y el nombre de la cancion idealmente; 
        //petition        
        AudioFormat audioFormat = this.getAudioFormat(songId);
        
        //preparing to download
        RequestVideoFileDownload requestDW
                = new RequestVideoFileDownload(audioFormat)
                        .clientType(ClientType.ANDROID_VR)
                        .saveTo(this.cachePath);

        Response<File> song = this.dw.downloadVideoFile(requestDW);
        
        File songDownload = song.data();
        this.tempFile.setSongPath(songDownload.getPath())
                .setSongYtId(songId);

        return this.tempFile;
    }

    private AudioFormat getAudioFormat(String songId) {

        RequestVideoInfo request = new RequestVideoInfo(songId)
                .clientType(ClientType.ANDROID_VR);
        
        Response<VideoInfo> response = this.dw.getVideoInfo(request);
//        System.out.println("Status: "+response.status());
        VideoInfo video = response.data();
        
//      this.tempFile = new SongDownloadedFile(video.details().title());
        this.tempFile = new SongDownloadedFile("aa");

        //return response.data().bestAudioFormat();
        return video.bestAudioFormat();
    }

}
