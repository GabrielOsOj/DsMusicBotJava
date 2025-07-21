
package SongPackage;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import java.nio.ByteBuffer;
import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerProvider implements AudioSendHandler {

	private final AudioPlayer player;

	private AudioFrame lastFrame;

	public AudioPlayerProvider(AudioPlayer p) {

		this.player = p;

	}

	@Override
	public boolean canProvide() {

		this.lastFrame = this.player.provide();
		return this.lastFrame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {

		return ByteBuffer.wrap(this.lastFrame.getData());

	}

	@Override
	public boolean isOpus() {
		return true;
	}

}
