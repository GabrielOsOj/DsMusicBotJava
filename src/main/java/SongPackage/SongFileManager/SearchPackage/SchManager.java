package SongPackage.SongFileManager.SearchPackage;

import MusicManager.searchManager.SchSongQueue;
import SongPackage.SongFileManager.DTOsearchResultYt;
import java.util.concurrent.CompletableFuture;

public class SchManager {
    
    final private SchSongQueue songToSearch;
    final private SchSearch search;
    
    public SchManager(){
    
        this.songToSearch = new SchSongQueue();
        this.search = new SchSearch();
           
    }
    
    public CompletableFuture<DTOsearchResultYt> addToSearchQueue(String SongName){
        
        this.songToSearch.addSong(SongName);
        return CompletableFuture.supplyAsync(()->searchSongService());
             
    }
    
    private DTOsearchResultYt searchSongService(){
        
        if(this.songToSearch.hasNextSong()){
            
           String songName = this.songToSearch.nextSong();
           String id = this.searchId(songName);
            
           return new DTOsearchResultYt(songName,id);
            
        }       
        return null;             
    }
    
    private String searchId(String songName){
        
        return this.search.searchSongId(songName);
        
    }
    
}
