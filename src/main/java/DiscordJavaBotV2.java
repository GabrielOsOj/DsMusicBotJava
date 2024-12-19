import Constantes.Constantes;
import DiscordBotPackage.BotManager;
import SongPackage.SongFileManager.DTOsearchResultYt;
import SongPackage.SongFileManager.DownloadPackage.DwnManager;


public class DiscordJavaBotV2 {

    public static void main(String[] args) {
              
        Constantes cts = new Constantes();        
        BotManager bot = new BotManager();
        bot.run(cts.getKey("DISCORD_BOT_TOKEN"));
        
        
        /*test*/
        /*
        System.out.println("Running test");
        DwnManager dw = new DwnManager(System.getProperty("user.dir") + "\\cache");
        dw.addSongToDownloadQueue
        (new DTOsearchResultYt("ss", "8obld3JrBNM"));
        
        try{
        Thread.sleep(5000);
        
        }catch(Exception e){
            e.printStackTrace();
                  
        }*/
        
    }
}
