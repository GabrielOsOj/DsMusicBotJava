package Constantes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Constantes {
    
    /*
    
        This class loads keys from a file named keys.properties, 
        this file must be saved in the same directory as the main file
        final private String RUTA = "DiscodJavaBotV2\\src\\main\\java\\keys.properties";
    
    */
    private String RUTA;
    final private Properties props = new Properties();
    
    //messages
    final private String MESSAGE_LOAD_FAILED="KEYS LOAD FAILED!";    
    final private String MESSAGE_LOAD_SUCESSFULL= "KEYS LOAD SUCESSFULLY";
    final private String MESSAGE_PROPERTIES_LOAD_FAILED ="READ KEYS FAILED!";
    final private String MESSAGE_INVALD_KEY="INVALID KEY OR NOT EXISTS";
   
    public Constantes(){
        
        this.RUTA = System.getProperty("user.dir")+
                "\\src\\main\\java\\keys.properties";
            
        try{            
            FileInputStream file = new FileInputStream(RUTA);         
            this.props.load(file);
            System.out.println(MESSAGE_LOAD_SUCESSFULL);
        }
        catch(FileNotFoundException e){
            System.out.println(MESSAGE_LOAD_FAILED);
            e.printStackTrace();
            System.exit(1);
        }        
        catch(IOException e){
            System.out.println(MESSAGE_PROPERTIES_LOAD_FAILED);
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
    public String getKey(String keyName){
        
       String key = this.props.getProperty(keyName);
       
       if(key == null){
           System.out.println(MESSAGE_INVALD_KEY);
           System.exit(1);
       }       
       
       return key;       
              
    }
    
    
    
}
