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

    private MusicPlayer musicPlayer ;

    // permitir utilizar el explorador de archivos en la app
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
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
        JSlider playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 -300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        add(playbackSlider);


        // botones de reproducion , anterior , siguiente y pausa
        addPlaybackBtns();

    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);

        // Evita que la barra de herramientas se mueva
        toolBar.setFloatable(false);

        // Agrega un menú desplegable
        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        // Agregar menú de canciones donde colocaremos la opción de cargar
        JMenu songMenu = new JMenu("Cancion");
        menuBar.add(songMenu);

        // Agregar elemento "Cargar Canción" en el menú de canciones
        JMenuItem loadSong = new JMenuItem("Cargar cancion"); // Cambiar a JMenuItem
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre el diálogo de selección de archivos y muestra el título
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                // Verifica si el usuario seleccionó un archivo
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    if (selectedFile != null) {
                        // Crear un objeto Song basado en el archivo seleccionado
                        Song song = new Song(selectedFile.getPath());
                        // Cargar la canción en el reproductor
                        musicPlayer.loadSong(song);
                        // actualizar el titulo de la cancion y el artista
                        updateSongTitleArtist(song);
                        // activar el boton de pausa y desativar el boton de reproducion
                        enablePauseButtonDisablePlayButton();
                    }
                }
            }

        });
        songMenu.add(loadSong);

        // Ahora agregamos el menú de lista de reproducción
        JMenu playlistsMenu = new JMenu("Reproducir cancion");
        menuBar.add(playlistsMenu);
        // Luego, agregar elementos al menú de lista de reproducción
        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        playlistsMenu.add(createPlaylist);
        JMenuItem loadPlaylist = new JMenuItem("Cargar cancion");
        playlistsMenu.add(loadPlaylist);
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

}
