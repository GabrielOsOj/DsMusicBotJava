package SongPackage.SongFileManager.DownloadPackage;

import SongPackage.SongFileManager.DTOsearchResultYt;
import SongPackage.SongFileManager.SongDownloadedFile;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class DwnManager {

    final private DwnDownload Dwn;
    final private Queue<DTOsearchResultYt> songToDownload;

    public DwnManager(String FilePath) {

        this.Dwn = new DwnDownload(FilePath);
        this.songToDownload = new LinkedList<DTOsearchResultYt>();

    }

    public CompletableFuture<SongDownloadedFile> addSongToDownloadQueue(DTOsearchResultYt songToDownload) {

        this.songToDownload.add(songToDownload);
        return CompletableFuture.supplyAsync(() -> this.songDownloadService());

    }

    private SongDownloadedFile songDownloadService() {

        if (!this.songToDownload.isEmpty()) {
            
            return this.Dwn.download(
                    this.songToDownload.poll().getYtIdResponse());
        }

        return null;
    }

}
