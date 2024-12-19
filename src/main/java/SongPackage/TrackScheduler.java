package SongPackage;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> songQueue;
    private final AudioPlayer player;

    public TrackScheduler(AudioPlayer player) {

        this.player = player;
        this.songQueue = new LinkedBlockingQueue<>();

    }

    public void queue(AudioTrack song) {

        if(!player.startTrack(song, true)) {
            songQueue.offer(song);
        }
        

    }

    public void nextTrack() {

        AudioTrack track = this.songQueue.poll();
        
        if(track!=null){
            
            this.player.startTrack(track.makeClone(), false);
            
        }

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.nextTrack();
    }

}