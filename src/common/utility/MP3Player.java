package common.utility;
//이걸 백그라운드에서 돌리려면 thread로 돌린다. 
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MP3Player {
  private String filename;
  private Player player;
  public MP3Player(String filename) {
    this.filename = filename;
  }
  public void play() {
    try {
      FileInputStream fis = new FileInputStream(filename);
      player = new Player(fis);
      player.play();
    } catch (Exception e) {
      System.out.println("Problem playing file: " + filename);
      System.out.println(e);
    }
  }
  public static void main(String[] args) {
    MP3Player mp3Player = new MP3Player("media/music.mp3");
    mp3Player.play();
  }
}