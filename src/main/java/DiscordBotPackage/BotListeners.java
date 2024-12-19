package DiscordBotPackage;

import SongPackage.SongManager;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListeners extends ListenerAdapter {

    private SongManager sg;

    public BotListeners() {
        this.sg = new SongManager();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        try {
            this.msgFilter(event);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(BotListeners.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void msgFilter(MessageReceivedEvent msg) throws InterruptedException, ExecutionException {

        String msgText = msg.getMessage().getContentRaw();

        if (!msgText.contains("!")) {
            return;
        }

        String[] msgSplitted = msgText.toLowerCase().split(" ");

        if (msgSplitted[0].contains("ping")) {

            MessageChannel chln = msg.getChannel();
            chln.sendMessage("pong!").queue();

        }

        if (msgSplitted[0].contains("play")) {

            //if length = 1, msg = !play
            if (msgSplitted.length == 1) {

                this.sg.pauseAndPlay();

            } else {

                String songName="";
                for(int i=1;i<msgSplitted.length;i++){
                
                   songName = songName.concat(msgSplitted[i])+" ";
                   
                }
                
                System.out.println("Buscando " + songName);

                this.sg.searchSong(songName, msg.getGuild());
                
            }

        }
        
        if(msgSplitted[0].contains("pause")){
            this.sg.pause();
        }
        
        
        if(msgSplitted[0].contains("next")||msgSplitted[0].contains("n")){                
            this.sg.nextTrack();
            
        }
                  
    }

}
