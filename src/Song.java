import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

// Esta clase se usa para describir una canción
public class Song {
    private String songTitle;
    private String songArtist;
    private String songLength;
    private String filepath;

    public Song(String filepath) {
        this.filepath = filepath;
        try {
            // Usa la biblioteca jAudioTagger para crear un AudioFile
            AudioFile audioFile = AudioFileIO.read(new File(filepath));
            // Leer los metadatos del archivo de audio
            Tag tag = audioFile.getTag();
            if (tag != null) {
                songTitle = tag.getFirst(FieldKey.TITLE);
                songArtist = tag.getFirst(FieldKey.ARTIST);
            } else {
                // No se puede leer el meta del archivo mp3
                songTitle = "N/A";
                songArtist = "N/A";
            }
            // Obtener la duración en segundos
            songLength = String.valueOf(audioFile.getAudioHeader().getTrackLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters
    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongLength() {
        return songLength;
    }

    public String getFilepath() {
        return filepath;
    }
}
