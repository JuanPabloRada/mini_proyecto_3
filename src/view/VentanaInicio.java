package view;

import controller.InicioController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class VentanaInicio extends JFrame {


    private JTextField campoNombre1;
    private JTextField campoNombre2;


    private InicioController controller;

    public VentanaInicio() {

        controller = new InicioController(this);
        construirUI();
    }

    private void construirUI() {
      
        setTitle("Yu-Gi-Oh! — Duelo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        // Fondo oscuro para separacion.
        panel.setBackground(new Color(15, 15, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //  Titulo: texto grande centrado 
       
        JLabel titulo = new JLabel("  DUELO DE YU-GI-OH!  ", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(new Color(255, 215, 0)); 
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        // Subtitulo con cita en cursiva.
        JLabel subtitulo = new JLabel("\"Confia en el corazon de las cartas\"", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Serif", Font.ITALIC, 13));
        subtitulo.setForeground(new Color(180, 180, 220));
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);

        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 215, 0));
        gbc.gridy = 2; gbc.insets = new Insets(4, 0, 16, 0);
        panel.add(sep, gbc);
        // Restaurar los insets por defecto para las siguientes filas.
        gbc.insets = new Insets(8, 8, 8, 8);

        
        gbc.gridwidth = 1; gbc.gridy = 3; gbc.gridx = 0;
        JLabel lbl1 = new JLabel("Jugador 1:");
        lbl1.setForeground(Color.WHITE);
        lbl1.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lbl1, gbc);

        
        campoNombre1 = new JTextField("Yugi", 16);
        estilizarCampo(campoNombre1); // aplica estilo consistente a los campos
        gbc.gridx = 1;
        panel.add(campoNombre1, gbc);

        //  Fila: Jugador 2 
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lbl2 = new JLabel("Jugador 2:");
        lbl2.setForeground(Color.WHITE);
        lbl2.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lbl2, gbc);

        // Campo de texto con valor por defecto "Kaiba".
        campoNombre2 = new JTextField("Kaiba", 16);
        estilizarCampo(campoNombre2);
        gbc.gridx = 1;
        panel.add(campoNombre2, gbc);

    
        JButton btnIniciar = new JButton("¡INICIAR DUELO!");
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnIniciar.setBackground(new Color(180, 0, 0));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        btnIniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(btnIniciar, gbc);

        // Cuando se pulsa el boton o se presiona Enter en alguno de los campos, se llama al metodo iniciarDuelo() que delega al controller.
        btnIniciar.addActionListener(e -> iniciarDuelo());

        ActionListener enterAction = e -> iniciarDuelo();
        campoNombre1.addActionListener(enterAction);
        campoNombre2.addActionListener(enterAction);

        // Establecer el panel principal, ajustar tamaño y centrar la ventana.
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private void estilizarCampo(JTextField campo) {
        // Estilos visuales que dejan los campos con aspecto coherente al tema.
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setBackground(new Color(30, 30, 60));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE); // color del cursor de texto
        // Border compuesto: linea dorada exterior + padding interno.
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void iniciarDuelo() {
        
        String n1 = campoNombre1.getText().trim();
        String n2 = campoNombre2.getText().trim();
        controller.iniciarDuelo(n1, n2);
    }
}
