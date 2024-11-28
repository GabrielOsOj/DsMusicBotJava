package DiscordBotPackage;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotManager extends ListenerAdapter{
    
    JDA bot;
     
    public void run(String token){
         
         this.bot = JDABuilder.createDefault(token)
                 .enableIntents(GatewayIntent.MESSAGE_CONTENT,
                         GatewayIntent.GUILD_MEMBERS,
                         GatewayIntent.GUILD_MESSAGES,
                         GatewayIntent.GUILD_VOICE_STATES)
                 .build();
        this.bot.addEventListener(new BotListeners());      
    }
    

};

    

