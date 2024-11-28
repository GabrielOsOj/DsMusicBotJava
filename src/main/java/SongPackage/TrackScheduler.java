package SongPackage;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TrackScheduler extends AudioEventAdapter{
    
    private final Queue<AudioTrack> songQueue;
    private AudioPlayer player;
    
    public TrackScheduler(AudioPlayer player){
        
        this.songQueue = new ConcurrentLinkedQueue();
        this.player = player;
        
    }
    
    public void queue(AudioTrack song){
        
        if(!this.player.startTrack(song, true)){        
            this.songQueue.offer(song);
        }
        
    }
    
    public boolean nextTrack(){
    
        return player.startTrack(this.songQueue.poll(), true);
      
    }
   
    
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
       
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
       
    }
    
}