package SongPackage.SongFileManager;

import SongPackage.SongFileManager.DownloadPackage.DwnManager;
import SongPackage.SongFileManager.SearchPackage.SchManager;
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
        this.downloadMG = new DwnManager(System.getProperty("user.dir") + "\\cache");
    }

    public SongDownloadedFile SAD(String songName) throws InterruptedException, ExecutionException {

        DTOsearchResultYt ytResult
                = this.searchMG.addToSearchQueue(songName).get();
       
        SongDownloadedFile f = this.downloadMG.addSongToDownloadQueue(ytResult).get();
        
        return new SongDownloadedFile()
                .setSongPath(f.getSongPath())
                .setTitle(f.getTitle());
    }

}
