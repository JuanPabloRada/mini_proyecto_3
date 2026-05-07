import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Lanza la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventana = new VentanaInicio();
            ventana.setVisible(true);
        });
    }
}
