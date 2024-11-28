package SongPackage.SongFileManager.DownloadPackage;

import java.io.File;

/**
 * this class manages temporary music files 
 */

public class FileManager {

    private String cachePath;
    
    public FileManager(String path){
        
        this.cachePath = path;
        this.createCacheDirectory(path);
                
    }
    
    
    public File createFile(String songName){
        
        return new File(this.cachePath+"\\"+songName);
        
    }
    
    public void deleteSong(String songId){
        
    }
    
    
    private void createCacheDirectory(String mainPath){
        new File(mainPath).mkdir();
    }
    
}
