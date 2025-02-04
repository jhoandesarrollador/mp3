import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class MusicPlayerGUI extends JFrame {
    // Configuraciones de color
    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private MusicPlayer musicPlayer;
    private JFileChooser jFileChooser;
    private JLabel songTitle, songArtist;
    private JPanel playbackButtons;

    public MusicPlayerGUI() {
        super("Reproductor de Música");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer = new MusicPlayer();
        jFileChooser = new JFileChooser();
        configureFileChooser();

        addGuiComponents();
    }

    private void configureFileChooser() {
        jFileChooser.setCurrentDirectory(new File("src/assets"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Archivos MP3", "mp3"));
    }

    private void addGuiComponents() {
        addToolbar();
        addAlbumArt();
        addSongInfo();
        addPlaybackSlider();
        addPlaybackButtons();
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);
        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu songMenu = new JMenu("Canción");

        JMenuItem loadSongItem = new JMenuItem("Cargar Canción");
        loadSongItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    Song song = new Song(selectedFile.getPath());
                    musicPlayer.loadSong(song);
                    updateSongInfo(song);
                    togglePlaybackButtons(true);
                }
            }
        });

        songMenu.add(loadSongItem);
        menuBar.add(songMenu);
        toolBar.add(menuBar);
        add(toolBar);
    }

    private void addAlbumArt() {
        JLabel albumArt = new JLabel(loadImage("src/assets/record.png"));
        albumArt.setBounds(0, 50, getWidth() - 20, 225);
        add(albumArt);
    }

    private void addSongInfo() {
        songTitle = new JLabel("Título no disponible");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        songArtist = new JLabel("Artista desconocido");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 18));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);
    }

    private void addPlaybackSlider() {
        JSlider progressSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        progressSlider.setBounds(getWidth()/2 - 300/2, 365, 300, 40);
        progressSlider.setBackground(null);
        add(progressSlider);
    }

    private void addPlaybackButtons() {
        playbackButtons = new JPanel();
        playbackButtons.setBounds(0, 435, getWidth() - 10, 80);
        playbackButtons.setBackground(null);

        // Botón Anterior
        JButton prevButton = createIconButton("src/assets/previous.png");
        playbackButtons.add(prevButton);

        // Botón Play/Pause
        JButton playButton = createIconButton("src/assets/play.png");
        JButton pauseButton = createIconButton("src/assets/pause.png");
        pauseButton.setVisible(false);

        playButton.addActionListener(e -> {
            musicPlayer.playCurrentSong();
            togglePlaybackButtons(true);
        });

        pauseButton.addActionListener(e -> {
            musicPlayer.pause();
            togglePlaybackButtons(false);
        });

        playbackButtons.add(playButton);
        playbackButtons.add(pauseButton);

        // Botón Siguiente
        JButton nextButton = createIconButton("src/assets/next.png");
        playbackButtons.add(nextButton);

        add(playbackButtons);
    }

    private JButton createIconButton(String imagePath) {
        JButton button = new JButton(loadImage(imagePath));
        button.setBorderPainted(false);
        button.setBackground(null);
        button.setFocusPainted(false);
        return button;
    }

    private void updateSongInfo(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    private void togglePlaybackButtons(boolean isPlaying) {
        JButton playButton = (JButton) playbackButtons.getComponent(1);
        JButton pauseButton = (JButton) playbackButtons.getComponent(2);

        playButton.setVisible(!isPlaying);
        playButton.setEnabled(!isPlaying);
        pauseButton.setVisible(isPlaying);
        pauseButton.setEnabled(isPlaying);
    }

    private ImageIcon loadImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}