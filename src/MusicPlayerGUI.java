import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class MusicPlayerGUI extends JFrame {
    // configuracion de color

    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color FRAME_COLOR_DARK = Color.BLACK;
    public static final Color TEXT_COLOR_DARK = Color.WHITE;
    public static final Color FRAME_COLOR_LIGHT = Color.WHITE;
    public static final Color TEXT_COLOR_LIGHT = Color.BLACK;
    private boolean darkMode = true;

    private MusicPlayer musicPlayer ;

    // permitir utilizar el explorador de archivos en la app
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;
    private JLabel timeLabel;
    private Timer progressTimer;
    private int songDurationSeconds = 0;
    private int currentSecond = 0;

    public MusicPlayerGUI() {
        // llama al constructor JFrame para configurar la interfaz grafica  de usuario
        super("Reproductor");

        // establecer el ancho y el alto
        setSize(400, 600);

        // finalizar el proceso cuando la app este cerrada

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // inicie la aplicacion en el centro de la pantalla
        setLocationRelativeTo(null);

        // evita que se cambie el tamaño de  la aplicacion
        setResizable(false);

        // establecer el diseño en nulo lo que nos permite controlar las cordenadas,
        // (x, y) de nuestros componentes y tambien establecer la altura y ancho.

        setLayout(null);
        // cambiar el color del marco
        getContentPane().setBackground(FRAME_COLOR);

        jFileChooser= new JFileChooser();
        musicPlayer = new MusicPlayer();

        // establecer una ruta predeterminada para el explorador de archivos
        jFileChooser.setCurrentDirectory(new File("src/assets"));

        //selecionar archivo de filtro mp3
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3 Files", "mp3"));

        addGuiComponents();
    }
    private void addGuiComponents() {

        // agregar barra de herramienta
        addToolbar();

        //cargar imagen de registro ( el disco)
        JLabel songImage = new JLabel(loadImage( "src/assets/record.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);
        // titulo de la cancion
        songTitle = new JLabel("titulo de la cancion");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD,  24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);
        // artista de la cancion
        songArtist = new JLabel("artista");
        songArtist.setBounds(0, 315, getWidth() -10 , 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN,  24));
        songArtist.setForeground(TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        // CONTROL DESLIZANTE DE REPRODUCION
        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 -300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.setEnabled(false);
        playbackSlider.addChangeListener(e -> {
            if (playbackSlider.getValueIsAdjusting() && songDurationSeconds > 0) {
                int newSecond = (int) ((playbackSlider.getValue() / 100.0) * songDurationSeconds);
                currentSecond = newSecond;
                updateTimeLabel();
                // Aquí podrías implementar el salto real en la reproducción si la librería lo permite
            }
        });
        add(playbackSlider);

        // Etiqueta para mostrar el tiempo transcurrido y total
        timeLabel = new JLabel("00:00 / 00:00");
        timeLabel.setBounds(getWidth()/2 - 75, 410, 150, 20);
        timeLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        timeLabel.setForeground(TEXT_COLOR);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timeLabel);

        // botones de reproducion , anterior , siguiente y pausa
        addPlaybackBtns();

    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);
        toolBar.setFloatable(false);
        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);
        JMenu songMenu = new JMenu("Cancion");
        menuBar.add(songMenu);
        JMenuItem loadSong = new JMenuItem("Cargar cancion");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    if (selectedFile != null) {
                        Song song = new Song(selectedFile.getPath());
                        musicPlayer.loadSong(song);
                        updateSongTitleArtist(song);
                        enablePauseButtonDisablePlayButton();
                    }
                }
            }
        });
        songMenu.add(loadSong);
        JMenu playlistsMenu = new JMenu("Reproducir cancion");
        menuBar.add(playlistsMenu);
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        playlistsMenu.add(createPlaylist);
        JMenuItem loadPlaylist = new JMenuItem("Cargar cancion");
        playlistsMenu.add(loadPlaylist);
        // Menú de tema
        JMenu themeMenu = new JMenu("Tema");
        menuBar.add(themeMenu);
        JMenuItem darkItem = new JMenuItem("Oscuro");
        JMenuItem lightItem = new JMenuItem("Claro");
        themeMenu.add(darkItem);
        themeMenu.add(lightItem);
        darkItem.addActionListener(e -> setTheme(true));
        lightItem.addActionListener(e -> setTheme(false));
        add(toolBar);
    }
    private  void addPlaybackBtns() {
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth() - 10, 80);
        playbackBtns.setBackground(null);

        // Botón de anterior
        JButton prevButton = new JButton(loadImage("src/assets/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        playbackBtns.add(prevButton);

        // boton de play
        JButton playButton = new JButton(loadImage("src/assets/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playbackBtns.add(playButton);

        // boton de pausa
        JButton pauseButton = new JButton(loadImage("src/assets/pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        playbackBtns.add(pauseButton);
        // boton de siguiente
        JButton nextButton = new JButton(loadImage("src/assets/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        playbackBtns.add(nextButton);
        add(playbackBtns);
    }

    private void updateSongTitleArtist(Song song){
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
        // Obtener duración y reiniciar progreso
        try {
            songDurationSeconds = Integer.parseInt(song.getSongLength());
        } catch (Exception e) {
            songDurationSeconds = 0;
        }
        currentSecond = 0;
        updateTimeLabel();
        playbackSlider.setValue(0);
        playbackSlider.setEnabled(songDurationSeconds > 0);
        if (progressTimer != null) progressTimer.stop();
        if (songDurationSeconds > 0) {
            progressTimer = new Timer(1000, evt -> {
                if (currentSecond < songDurationSeconds) {
                    currentSecond++;
                    int percent = (int) ((currentSecond * 100.0) / songDurationSeconds);
                    playbackSlider.setValue(percent);
                    updateTimeLabel();
                } else {
                    progressTimer.stop();
                }
            });
            progressTimer.start();
        }
    }

    private void enablePauseButtonDisablePlayButton(){
     //recupera la referencia al boton del panel de reproduccionBtns
     JButton playButton = (JButton) playbackBtns.getComponent(1) ;
     JButton pauseButton = ( JButton) playbackBtns.getComponent(2) ;

     // agregar el boton de reproducion
         playButton.setVisible(false);
         playButton.setEnabled(false);
         // actiavr el boton de pausa
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    private void enablePlayButtonDisablePauseButton() {
        //recupera la referencia al boton del panel de reproduccionBtns
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        // agregar el boton de reproducion
        playButton.setVisible(true);
        playButton.setEnabled(true);
        // desativar el boton de pausa
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private void updateTimeLabel() {
        String elapsed = formatSeconds(currentSecond);
        String total = formatSeconds(songDurationSeconds);
        timeLabel.setText(elapsed + " / " + total);
    }

    private String formatSeconds(int secs) {
        int min = secs / 60;
        int sec = secs % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private ImageIcon loadImage(String imagePath) {
        try {

            // lee el archivo dado
            BufferedImage image = ImageIO.read(new File(imagePath));
            // devuelve el icon de imagen
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // no se puede encontrar el recurso
        return null;
    }

    private void setTheme(boolean dark) {
        darkMode = dark;
        Color frameColor = dark ? FRAME_COLOR_DARK : FRAME_COLOR_LIGHT;
        Color textColor = dark ? TEXT_COLOR_DARK : TEXT_COLOR_LIGHT;
        getContentPane().setBackground(frameColor);
        songTitle.setForeground(textColor);
        songArtist.setForeground(textColor);
        timeLabel.setForeground(textColor);
        playbackBtns.setBackground(null);
        // Cambiar color de los botones
        for (Component c : playbackBtns.getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(null);
            }
        }
        // Cambiar color del slider
        playbackSlider.setBackground(null);
        // Forzar repintado
        repaint();
    }

}
