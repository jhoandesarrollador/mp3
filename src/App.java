import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear una instancia de MusicPlayerGUI y hacerla visible
                MusicPlayerGUI musicPlayerGUI = new MusicPlayerGUI();
                musicPlayerGUI.setVisible(true);
            }
        });
    }
}