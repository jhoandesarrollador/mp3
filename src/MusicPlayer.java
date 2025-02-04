import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
    private List<Song> playlist; // Lista de reproducción
    private int currentSongIndex; // Índice de la canción actual
    private AdvancedPlayer advancedPlayer; // Reproductor de audio
    private boolean isPlaying; // Indica si hay una canción en reproducción

    // Constructor
    public MusicPlayer() {
        this.playlist = new ArrayList<>();
        this.currentSongIndex = -1; // Ninguna canción cargada al inicio
        this.advancedPlayer = null;
        this.isPlaying = false;
    }

    // Agrega una canción a la lista de reproducción
    public void addSong(Song song) {
        playlist.add(song);
        if (currentSongIndex == -1) {
            currentSongIndex = 0; // Si es la primera canción, selecciónala
        }
    }


    public void loadSong(Song song) {
        if (song == null) return;

        // Buscar la canción en la lista y actualizar el índice
        int index = playlist.indexOf(song);
        if (index != -1) {
            currentSongIndex = index;
        } else {
            playlist.add(song);
            currentSongIndex = playlist.size() - 1;
        }

        stop(); // Detener la canción actual antes de cambiar
        playCurrentSong();
    }

    public void playCurrentSong() {
        if (currentSongIndex == -1 || playlist.isEmpty()) return;

        try {
            Song currentSong = playlist.get(currentSongIndex);
            FileInputStream fileInputStream = new FileInputStream(currentSong.getFilepath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            isPlaying = true;


            new Thread(() -> {
                try {
                    advancedPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isPlaying = false;
                }
            }).start();

            System.out.println("Reproduciendo: " + currentSong.getSongTitle());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void nextSong() {
        if (playlist.isEmpty()) return;

        currentSongIndex = (currentSongIndex + 1) % playlist.size();
        stop();
        playCurrentSong();
    }

    // Reproduce la canción anterior en la lista.

    public void previousSong() {
        if (playlist.isEmpty()) return;

        currentSongIndex = (currentSongIndex - 1 + playlist.size()) % playlist.size();
        stop();
        playCurrentSong();
    }

    //Detiene la reproducción de la canción actual.
    public void stop() {
        if (advancedPlayer != null) {
            advancedPlayer.close();
            isPlaying = false;
        }
    }

    // Simula una pausa deteniendo la reproducción.

    public void pause() {
        if (advancedPlayer != null && isPlaying) {
            advancedPlayer.close();
            isPlaying = false;
        }
    }

    //Obtiene la canción actual.

    public Song getCurrentSong() {
        if (currentSongIndex == -1 || playlist.isEmpty()) return null;
        return playlist.get(currentSongIndex);
    }

    //Verifica si hay una canción en reproducción.

    public boolean isPlaying() {
        return isPlaying;
    }
}
