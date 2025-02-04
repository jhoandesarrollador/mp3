import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class Song {
    private String songTitle;   // Título de la canción
    private String songArtist;  // Artista de la canción
    private String assets;  // Duración de la canción
    private String filepath;    // Ruta del archivo de la canción

    /**
     * Constructor de la clase Song.
     * Lee los metadatos de la canción (título y artista) utilizando JAudioTagger.
     * @param filepath Ruta del archivo de la canción.
     */
    public Song(String filepath) {
        this.filepath = filepath;

        try {
            // Leer el archivo de audio
            File file = new File(filepath);
            AudioFile audioFile = AudioFileIO.read(file);

            // Obtener los metadatos (etiquetas) del archivo
            Tag tag = audioFile.getTag();

            if (tag != null) {
                // Extraer el título y el artista de los metadatos
                songTitle = tag.getFirst(FieldKey.TITLE);
                songArtist = tag.getFirst(FieldKey.ARTIST);
            } else {
                // Si no hay metadatos, usar valores predeterminados
                songTitle = "Desconocido";
                songArtist = "Desconocido";
            }

            // Obtener la duración de la canción (en segundos)
            assets = String.valueOf(audioFile.getAudioHeader().getTrackLength());

        } catch (Exception e) {
            e.printStackTrace();

            songTitle = "Desconocido";
            songArtist = "Desconocido";
            assets = "Desconocido";
        }
    }

    // Getters
    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getAssets() {
        return assets;
    }

    public String getFilepath() {
        return filepath;
    }
}
