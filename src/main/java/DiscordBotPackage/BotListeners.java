package DiscordBotPackage;

import SongPackage.AudioPlayerProvider;
import SongPackage.SongManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class BotListeners extends ListenerAdapter{

    private SongManager sg;
    
    public BotListeners(){
        this.sg = new SongManager();
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
    
        this.msgFilter(event);
        
    
    }
       
    private void msgFilter(MessageReceivedEvent msg){
        
        String msgText = msg.getMessage().getContentRaw();
        
        if(!msgText.contains("!")){
            return;
        }

        String[] msgSplitted = msgText.toLowerCase().split(" ");
        
        if(msgSplitted[0].contains("ping")){
            
            MessageChannel chln = msg.getChannel();
            chln.sendMessage("pong!").queue();
            
        }
        
        if(msgSplitted[0].contains("play")){
                 
            //if length = 1, msg = !play
            if(msgSplitted.length == 1){
                this.sg.pauseAndPlay();
            }else{
                
                System.out.println("Buscando "+msgSplitted[1]);
                
                String tmpPath = "C:\\Users\\cuent\\OneDrive\\Escritorio\\DS_JAVA_MS_bot\\DS_JAVA_MS_bot\\cache\\e1c49cad-9771-4880-afae-eeafa0cbc358.m4a";
                this.sg.searchSong(tmpPath);
                
                Guild guild = msg.getGuild();
                //change channel name
                VoiceChannel chnnl = guild.getVoiceChannelsByName("general",true).get(0);                
                AudioManager manager = guild.getAudioManager();
                
                manager.setSendingHandler(this.sg.createAudioHandler());
                manager.openAudioConnection((AudioChannel) chnnl);
                
                
            }
             
        }
        
         
    }
    
    
}
