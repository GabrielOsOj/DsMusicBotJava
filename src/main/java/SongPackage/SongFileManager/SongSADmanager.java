package SongPackage.SongFileManager;

import SongPackage.SongFileManager.DownloadPackage.DwnManager;
import SongPackage.SongFileManager.SearchPackage.SchManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author cuent This class joins search package and download package SAD
 * (search and download)
 */
public class SongSADmanager {

    final private SchManager searchMG;
    final private DwnManager downloadMG;

    public SongSADmanager() {

        this.searchMG = new SchManager();
        this.downloadMG = new DwnManager(System.getProperty("user.dir") + File.separator+ "cache"+File.separator);
    }

    public SongDownloadedFile SAD(String songName) throws InterruptedException, ExecutionException {

        DTOsearchResultYt ytResult
                = this.searchMG.addToSearchQueue(songName).get();
        SongDownloadedFile f = this.downloadMG.addSongToDownloadQueue(ytResult).get();
		
		return new SongDownloadedFile()
                .setSongPath(f.getSongPath())
                .setTitle(f.getTitle());
    }

    public boolean SongDelete(SongDownloadedFile song) {

        try {
            
            Files.deleteIfExists(Paths.get(song.getSongPath()));
            System.out.println("Song: " + song.getTitle() + " as delete");

        } catch (IOException e) {
            System.out.println("Program cant delete song named: "+song.getTitle());
            e.printStackTrace();
            
        }
        return true;

    }
}
