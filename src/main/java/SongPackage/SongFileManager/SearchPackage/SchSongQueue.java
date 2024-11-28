
package MusicManager.searchManager;

import java.util.LinkedList;
import java.util.Queue;

/*
    
    This file save names of songs in queue

    ¡USELESS CLASS!

*/

public class SchSongQueue {
        
        private Queue<String> songQueue;
                   
        public SchSongQueue(){                     
            this.songQueue = new LinkedList<String>();           
        }
        
        public void addSong(String songName){            
            this.songQueue.offer(songName);
        }
        
        public String nextSong(){          
            return this.songQueue.poll();
        }
        
        
        public void showQueue(){
            System.out.println(this.songQueue.toString());
        }
        
        public boolean hasNextSong(){
            return !this.songQueue.isEmpty();
        }
        
}
