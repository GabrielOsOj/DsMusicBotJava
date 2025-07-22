package SongPackage;

import DiscordBotPackage.BotFinalMessages;
import SongPackage.Interfaces.IFsongFinish;
import SongPackage.SongFileManager.SongDownloadedFile;
import SongPackage.SongFileManager.SongSADmanager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;

public class SongManager implements IFsongFinish {

	final private AudioPlayerManager pm;

	final private GuildPlayer player;

	final private SongSADmanager SAD;

	private MessageChannel textChannel;

	private Queue<SongDownloadedFile> songQueue = new LinkedList<>();

	public SongManager() {

		this.SAD = new SongSADmanager();

		pm = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerLocalSource(pm);
		AudioSourceManagers.registerRemoteSources(pm);

		this.player = new GuildPlayer(pm);
		this.player.scheduler.SetListener(this);
	}

	public void setTextChannel(MessageChannel textChannel) {
		this.textChannel = textChannel;
	};

	public void searchSong(String songName, VoiceChannel voiceChannel) throws InterruptedException, ExecutionException {

		GuildPlayer musicPlayer = this.player;
		SongDownloadedFile song = this.searchAndDownload(songName);

		this.songQueue.add(song);
		this.pm.loadItemOrdered(musicPlayer, song.getSongPath(), new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack at) {
				
				playSong(at, voiceChannel,song.getTitle());
			}

			@Override
			public void playlistLoaded(AudioPlaylist ap) {
			}

			@Override
			public void noMatches() {
			}

			@Override
			public void loadFailed(FriendlyException fe) {
				System.out.println("Song Load failed");
			}

		});

	}

	// Control metods
	public void playSong(AudioTrack at, VoiceChannel channel, String title) {

		this.joinVoiceChannel(channel, this.player.getSendHandler());

		player.scheduler.queue(at,title);
	}

	private void joinVoiceChannel(VoiceChannel manager, AudioSendHandler soundHandler) {

		AudioManager audioManager = manager.getGuild().getAudioManager();

		if (!audioManager.isConnected()) {

			audioManager.openAudioConnection(manager);
			audioManager.setSendingHandler(soundHandler);

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
	};

	public void nextTrack() {

		this.player.scheduler.nextTrack();

	}

	private SongDownloadedFile searchAndDownload(String songName) throws InterruptedException, ExecutionException {

		return this.SAD.SAD(songName);

	}

	@Override
	public void onSongFinish() {

		SongDownloadedFile song = this.songQueue.poll();

		if (song == null) {
			return;
		}

		try {

			this.SAD.SongDelete(song);

		}
		catch (Exception e) {

			System.err.println("Program cannot delete song from cache!");

		}
	}

	@Override
	public void onSongStarts(String songName) {
		this.textChannel.sendMessage(BotFinalMessages.MSG_RESPONSE_PLAYING + songName).queue();
	};

	@Override
	public void onSongAddedToQueue(String songName) {
		this.textChannel.sendMessage(BotFinalMessages.MSG_RESPONSE_ADD_TO_QUEUE + songName).queue();
	};
	
	@Override
	public void onSongsEnded(){
		this.textChannel.sendMessage(BotFinalMessages.MSG_RESPONSE_NO_MORE_SONGS);
	}

}
