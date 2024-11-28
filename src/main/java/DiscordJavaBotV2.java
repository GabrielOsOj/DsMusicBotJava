import Constantes.Constantes;
import DiscordBotPackage.BotManager;


public class DiscordJavaBotV2 {

    public static void main(String[] args) {
               
        Constantes cts = new Constantes();        
        BotManager bot = new BotManager();
        bot.run(cts.getKey("DISCORD_BOT_TOKEN"));
        
    }
}
