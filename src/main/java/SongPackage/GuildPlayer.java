/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SongPackage;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 *
 * @author cuent
 */
public class GuildPlayer {
    
    public final AudioPlayer player;
    public final TrackScheduler scheduler;

    public GuildPlayer(AudioPlayerManager Pm) {
        
        this.player = Pm.createPlayer();
        this.scheduler = new TrackScheduler(player);
        this.player.addListener(scheduler);
        
    }
    
    public AudioPlayerProvider getSendHandler(){
        return new AudioPlayerProvider(this.player);
    }
    
}
