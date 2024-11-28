package SongPackage.SongFileManager;

public class SongDownloadedFile {

    private String title;
    private String songPath;
    private String songYtId;
    
    public SongDownloadedFile(){
     
    }
    
    public SongDownloadedFile(String title){
     
        this.title = title;
        this.songPath = null;
        
    }
    
    public String getTitle() {
        return title;
    }

    public SongDownloadedFile setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSongPath() {
        return songPath;
    }

    public SongDownloadedFile setSongPath(String songPath) {
        this.songPath = songPath;
        return this;
    }

    public String getSongYtId() {
        return songYtId;
    }

    public void setSongYtId(String songYtId) {
        this.songYtId = songYtId;
    }
    
    
}
