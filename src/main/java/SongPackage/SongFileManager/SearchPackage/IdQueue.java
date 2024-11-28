
package SongPackage.SongFileManager.SearchPackage;

import SongPackage.SongFileManager.SearchResultYt;
import java.util.LinkedList;
import java.util.Queue;


public class IdQueue {

    private Queue<SearchResultYt> ytRespose;
    private SearchResultYt lastId;

       
    public void IdQueue(){
        
        this.ytRespose = new LinkedList<SearchResultYt>();
        
    }
    
    
    public SearchResultYt getNextElement(){
              
     return this.ytRespose.poll();
     
    }
    
    public void addSearchResult(SearchResultYt searchResult){
        
        this.ytRespose.add(searchResult);
        
    }
    
    public boolean hasNextId(){
        
        return this.ytRespose.isEmpty();
        
    }
    
    
    
    
}