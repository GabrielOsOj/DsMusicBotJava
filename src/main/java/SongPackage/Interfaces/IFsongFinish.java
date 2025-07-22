
package SongPackage.Interfaces;

public interface IFsongFinish {
    
    void onSongFinish();
	void onSongStarts(String songName);
	void onSongAddedToQueue(String songName);
    void onSongsEnded();
}
