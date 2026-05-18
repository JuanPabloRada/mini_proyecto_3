import javax.swing.SwingUtilities;
import view.VentanaInicio;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventana = new VentanaInicio();
            ventana.setVisible(true);
        });
    }
}
