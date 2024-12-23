package SongPackage;

import SongPackage.Interfaces.IFsongFinish;
import SongPackage.SongFileManager.SongSADmanager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final BlockingQueue<AudioTrack> songQueue;
    private final AudioPlayer player;
    public IFsongFinish listener;

    public TrackScheduler(AudioPlayer player) {

        this.player = player;
        this.songQueue = new LinkedBlockingQueue<>();

    }

    public void SetListener(IFsongFinish listener) {

        this.listener = listener;

    }

    public void queue(AudioTrack song) {

        if (!player.startTrack(song, true)) {
            songQueue.offer(song);
        }

    }

    public void nextTrack() {

        AudioTrack track = this.songQueue.poll();

        if (track != null) {

            this.player.startTrack(track.makeClone(), false);     

        } else {

            this.player.stopTrack();

        }
        
        this.listener.onSongFinish();

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if (endReason == AudioTrackEndReason.FINISHED) {

            this.nextTrack();
            
        }
    }

}
