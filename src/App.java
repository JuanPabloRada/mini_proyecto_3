// Clase principal que arranca la aplicacion de duelos
// aqui se crea la ventana inicial y se lanza en el hilo de Swing
import javax.swing.SwingUtilities;
import view.VentanaInicio;

public class App {
    public static void main(String[] args) {
        // Ejecuta el UI en el hilo correcto de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventana = new VentanaInicio();
            ventana.setVisible(true);
        });
    }
}
