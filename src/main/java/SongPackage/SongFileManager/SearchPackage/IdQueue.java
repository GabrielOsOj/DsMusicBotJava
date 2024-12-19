
package SongPackage.SongFileManager.SearchPackage;

import SongPackage.SongFileManager.DTOsearchResultYt;
import java.util.LinkedList;
import java.util.Queue;


public class IdQueue {

    private Queue<DTOsearchResultYt> ytRespose;
    private DTOsearchResultYt lastId;

       
    public void IdQueue(){
        
        this.ytRespose = new LinkedList<DTOsearchResultYt>();
        
    }
    
    
    public DTOsearchResultYt getNextElement(){
              
     return this.ytRespose.poll();
     
    }
    
    public void addSearchResult(DTOsearchResultYt searchResult){
        
        this.ytRespose.add(searchResult);
        
    }
    
    public boolean hasNextId(){
        
        return this.ytRespose.isEmpty();
        
    }
    
    
    
    
}