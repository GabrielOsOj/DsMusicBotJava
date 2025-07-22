import Constantes.Constantes;
import DiscordBotPackage.BotManager;
import SongPackage.SongFileManager.DTOsearchResultYt;
import SongPackage.SongFileManager.DownloadPackage.DwnManager;
import SongPackage.SongManager;
import java.util.Scanner;

public class DiscordJavaBotV2 {

	public static void main(String[] args) {

		String key;
		if (args.length == 0) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Paste you discord key to start the bot");
			key = scanner.next();
		}
		else {
			key = args[0];
		}
		
		BotManager bot = new BotManager();
		bot.run(key);

		/* TEST */

//		 Constantes cts = new Constantes();
//		 BotManager bot = new BotManager();
//		 bot.run(cts.getKey("DISCORD_BOT_TOKEN"));

		// SongManager sg = new SongManager();
		//
		// System.out.println("Testeando...");
		// Constantes cts = new Constantes();
		// BotManager bot = new BotManager();
		// bot.run(cts.getKey("DISCORD_BOT_TOKEN"));

		// try {
		// sg.searchSong("dangerous limi", null);
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
