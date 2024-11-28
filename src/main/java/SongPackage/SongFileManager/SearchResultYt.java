package SongPackage.SongFileManager;


public class SearchResultYt {
    
    final private String discordSearchParam;
    final private String ytIdResponse;
    
    
    public SearchResultYt(String userDiscordParamSearch, String ytIdResponse){
               
        this.discordSearchParam = userDiscordParamSearch;
        this.ytIdResponse = ytIdResponse;
              
    }

    public String getDiscordSearchParam() {
        return discordSearchParam;
    }

    
    public String getYtIdResponse() {
        return ytIdResponse;
    }
   
}
