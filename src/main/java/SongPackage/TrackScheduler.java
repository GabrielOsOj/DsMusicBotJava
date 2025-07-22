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

	private BlockingQueue<String> songNameQueue;

	private final AudioPlayer player;

	public IFsongFinish listener;

	public TrackScheduler(AudioPlayer player) {

		this.player = player;
		this.songQueue = new LinkedBlockingQueue<>();
		this.songNameQueue = new LinkedBlockingQueue<>();

	}

	public void SetListener(IFsongFinish listener) {

		this.listener = listener;

	}

	public void queue(AudioTrack song, String title) {

		if (!player.startTrack(song, true)) {
			songQueue.offer(song);
			songNameQueue.offer(title);

			this.listener.onSongAddedToQueue(title);

		}
		else {
			this.listener.onSongStarts(title);
		}
	}/*
		 * Si queres que se vea el nombre bien, hace otra pila en donde almaces el nombre
		 * que te viene por title en queue y a emedidad que van pasando los temas los vas
		 * trayendo, suerte :D
		 */

	public void nextTrack() {

		AudioTrack track = this.songQueue.poll();

		if (track != null) {

			this.listener.onSongStarts(this.songNameQueue.poll());
			this.player.startTrack(track.makeClone(), false);

		}
		else {

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
