package SongPackage;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.audio.AudioSendHandler;


public class SongManager {

    private AudioPlayerManager pm;
    private AudioPlayer player;
    private TrackScheduler trackScheduler;
    
    public SongManager(){
        this.pm = new DefaultAudioPlayerManager();
        
        
        AudioSourceManagers.registerLocalSource(this.pm);
        AudioSourceManagers.registerRemoteSources(this.pm);
        
        this.player = this.pm.createPlayer();
        this.trackScheduler = new TrackScheduler(player);
        this.player.addListener(this.trackScheduler);
               
    }
    
    public void searchSong(String songPath){
        
           
        this.pm.loadItem(songPath, new AudioLoadResultHandler(){
            @Override
            public void trackLoaded(AudioTrack at) {
                
               trackScheduler.queue(at);
            }

            @Override
            public void playlistLoaded(AudioPlaylist ap) {
                System.out.println("playlist");
            }

            @Override
            public void noMatches() {
                System.out.println("Not found");
            }

            @Override
            public void loadFailed(FriendlyException fe) {
                System.out.println("Song Load failed");
            }
        
        
        });
        
    }
    
    public AudioSendHandler createAudioHandler(){
        
        return new AudioPlayerProvider(this.player);
        
    }
    
    //Control metods
    
    public void pauseAndPlay(){
        
        if(this.player.isPaused()){
            
            this.player.setPaused(false);
            return;
        }
    
        this.player.setPaused(true);
    };
 
    public void nextTrack(){
        
        this.trackScheduler.nextTrack();
        
    }
}
