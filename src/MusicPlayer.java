import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MusicPlayer {
    // almacena los detalles de la cancion

    private Song currentSong;

    // use la libreria jlayer para crear  un objeto advancedPlayer que se encargara de reproducir la musica
    private AdvancedPlayer advancedPlayer ;

    // constructor
    public MusicPlayer(){


    }
    public void loadSong(Song song){
        currentSong  = song;

        // reproducir la cansion actual si no es nulo
        if (currentSong != null){
            playCurrentSong();
        }
    }

    public void playCurrentSong(){
        try{
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilepath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // crear un nuevo jugador avanzado
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);

            //comenzar musica
            startMusicThread();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // cree un hilo que se encargara de reproducir la musica
    private void startMusicThread(){
      new Thread(new Runnable() {
          @Override
          public void run() {
              try  {
                // comenzar la musica
                  advancedPlayer.play();
              }catch (Exception e){
                  e.printStackTrace();
              }
          }
      }).start();

    }
}
