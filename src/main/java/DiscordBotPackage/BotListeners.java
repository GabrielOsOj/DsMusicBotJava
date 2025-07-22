package DiscordBotPackage;

import SongPackage.SongManager;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListeners extends ListenerAdapter {

	private SongManager sg;

	private MessageChannel msgChat;

	public BotListeners() {
		this.sg = new SongManager();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		try {
			this.msgChat = event.getChannel();
			this.sg.setTextChannel(msgChat);
			this.msgFilter(event);
		}
		catch (InterruptedException | ExecutionException ex) {
			Logger.getLogger(BotListeners.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void msgFilter(MessageReceivedEvent msg) throws InterruptedException, ExecutionException {

		String msgText = msg.getMessage().getContentRaw();

		if (!msgText.contains(BotFinalMessages.MSG_INPUT_SYMBOL)) {
			return;
		}

		String[] msgSplitted = msgText.toLowerCase().split(BotFinalMessages.MSG_SPACE);

		if (msgSplitted[0].contains(BotFinalMessages.MSG_INPUT_PING)) {

			this.msgChat.sendMessage(BotFinalMessages.MSG_RESPONSE_PONG).queue();
		}

		if(isMusicCommand(msgText)&&!this.isConnectedOnVoiceChat(msg.getMember())){
			this.msgChat.sendMessage(BotFinalMessages.MSG_RESPONSE_NOT_ON_VOICE_CHANNEL).queue();
			return;
		}
		
		if (msgSplitted[0].contains(BotFinalMessages.MSG_INPUT_PLAY)) {
			// if length = 1, msg = !play
			if (msgSplitted.length == 1) {
				this.sg.pauseAndPlay();
				this.msgChat.sendMessage(BotFinalMessages.MSG_RESPONSE_RESUMING).queue();
				return;
			}

			String songName = "";
			for (int i = 1; i < msgSplitted.length; i++) {
				songName = songName.concat(msgSplitted[i]) + BotFinalMessages.MSG_SPACE;
			}

			this.msgChat.sendMessage(BotFinalMessages.MSG_RESPONSE_SEARCHING + songName).queue();

			System.out.println(BotFinalMessages.MSG_RESPONSE_SEARCHING + songName);

			this.sg.searchSong(songName, msg.getMember().getVoiceState().getChannel().asVoiceChannel());
			return;
		}

		if (msgSplitted[0].contains(BotFinalMessages.MSG_INPUT_PAUSE)
				|| msgSplitted[0].equalsIgnoreCase(BotFinalMessages.MSG_INPUT_PAUSE_SHORTHAND)) {
			this.msgChat.sendMessage(BotFinalMessages.MSG_RESPONSE_PAUSE).queue();
			this.sg.pause();
			return;
		}

		if (msgSplitted[0].contains(BotFinalMessages.MSG_INPUT_NEXT)
				|| msgSplitted[0].contains(BotFinalMessages.MSG_INPUT_NEXT_SHORTHAND)) {
			this.msgChat.sendMessage(BotFinalMessages.MSG_RESPONSE_NEXT).queue();
			this.sg.nextTrack();
			return;

		}

	}

	// utils
	private boolean isConnectedOnVoiceChat(Member member) {

		return member.getVoiceState().getChannel() != null;

	}

	private boolean isMusicCommand(String msg) {

		return msg.contains(BotFinalMessages.MSG_INPUT_PLAY) || msg.contains(BotFinalMessages.MSG_INPUT_PAUSE)
				|| msg.contains(BotFinalMessages.MSG_INPUT_PAUSE_SHORTHAND)
				|| msg.contains(BotFinalMessages.MSG_INPUT_NEXT_SHORTHAND)
				|| msg.contains(BotFinalMessages.MSG_INPUT_NEXT);
	}

}