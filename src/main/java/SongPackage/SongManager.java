package SongPackage;

import SongPackage.SongFileManager.SongDownloadedFile;
import SongPackage.SongFileManager.SongSADmanager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class SongManager {

    final private AudioPlayerManager pm;
    final private GuildPlayer player;
    final private SongSADmanager SAD;

    public SongManager() {

        this.SAD = new SongSADmanager();

        pm = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(pm);
        AudioSourceManagers.registerRemoteSources(pm);

        this.player = new GuildPlayer(pm);

    }

    public void searchSong(String songName, Guild channel) throws InterruptedException, ExecutionException {

        GuildPlayer musicPlayer = this.player;
       SongDownloadedFile song = this.searchAndDownload(songName);

        this.pm.loadItemOrdered(musicPlayer, song.getSongPath(), new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack at) {
                playSong(at, channel);
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
    
//    public AudioSendHandler createAudioHandler() {
//
//        if (this.audioSendHanlder == null) {
//
//            this.audioSendHanlder = new AudioPlayerProvider();
//
//        }
//        
//        return this.audioSendHanlder;
//    }
        
    //Control metods
    public void playSong(AudioTrack at, Guild channel) {

        //change channel
        this.joinVoiceChannel(channel.getAudioManager(),
                this.player.getSendHandler());
        
        player.scheduler.queue(at);
    }

    
    private void joinVoiceChannel(AudioManager manager, 
            AudioSendHandler soundHandler){
        
        if(!manager.isConnected()){
            for(VoiceChannel vc : manager.getGuild().getVoiceChannels()){
                
                manager.openAudioConnection(vc);
                manager.setSendingHandler(soundHandler);
                break;
                
            }
        }
    }
    
    
    public void pause() {
        this.player.player.setPaused(true);
    }

    public void pauseAndPlay() {

        if (this.player.player.isPaused()) {

            this.player.player.setPaused(false);
            return;
        }

        this.player.player.setPaused(true);
    }

    ;
 
    public void nextTrack() {

        this.player.scheduler.nextTrack();

    }

    private SongDownloadedFile searchAndDownload(String songName) throws InterruptedException, ExecutionException {

        return this.SAD.SAD(songName);

    }
}