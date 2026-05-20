package view;
 
import controller.DuelController;
import model.*;
 
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
 
/**
 VentanaDuelo: interfaz principal donde se visualiza el duelo entre dos jugadores.
 
 Responsabilidad:
  - Mostrar la informacion del campo (jugadores, LP, manos, mazos, trampas).
  - Refrescar la vista cuando el DuelController indique cambios en el modelo.
  - Delegar acciones de usuario (jugar carta, atacar, etc.) al controller.
 
  Notas:
  - Esta clase no implementa logica de juego; solo refleja el estado y envia
    comandos al DuelController.
 */
public class VentanaDuelo extends JFrame implements IDuelView {
 
    // Referencia al controller que centraliza la logica del duelo.
    // La vista solo consulta datos y pide acciones al controller.
    private final DuelController controller;
 
    // Componentes de la UI 
    // Etiqueta que muestra el numero de turno y a quien le toca.
    private JLabel lblTurno;
    // Area de texto que registra eventos y mensajes del duelo.
    private JTextArea areaLog;
 
    // Componentes que muestran informacion de los dos jugadores (activo y oponente).
    private JLabel lblNombreJ1, lblLpJ1, lblManoJ1, lblMazoJ1, lblTrampasJ1;
    private JPanel panelCampoJ1; // panel que contiene tarjetas de monstruos del jugador 1
 
    private JLabel lblNombreJ2, lblLpJ2, lblManoJ2, lblMazoJ2, lblTrampasJ2;
    private JPanel panelCampoJ2; // panel que contiene tarjetas de monstruos del jugador 2
 
    // Botones de acciones que el jugador puede realizar desde la UI.
    private JButton btnJugarCarta;
    private JButton btnAtacar;
    private JButton btnActivarTrampa;
    private JButton btnCambiarPosicion;
    private JButton btnTerminarTurno;
 
    // Constantes de color usadas
    private static final Color BG_DARK      = new Color(10, 10, 30);
    private static final Color BG_FIELD     = new Color(0, 60, 30);
    private static final Color COLOR_GOLD   = new Color(255, 215, 0);
    private static final Color COLOR_RED    = new Color(200, 20, 20);
    private static final Color COLOR_BLUE   = new Color(20, 100, 200);
    private static final Color COLOR_GREEN  = new Color(30, 160, 30);
    private static final Color COLOR_PURPLE = new Color(120, 30, 160);
    private static final Color COLOR_GRAY   = new Color(80, 80, 100);
    private static final Color FG_WHITE     = Color.WHITE;
 
    public VentanaDuelo(DuelController controller) {
        // Guardar la referencia al controller y construir la UI.
        this.controller = controller;
        construirUI();
        // iniciarPrimerTurno() se invoca desde el controller/InicioController
        // despues de que la vista sea registrada en el controller si es necesario.
    }
 
    // Construccion de la UI 
 
    private void construirUI() {
        // Configuracion de la ventana principal
        setTitle("Yu-Gi-Oh! — Duelo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(6, 6));
        getContentPane().setBackground(BG_DARK);
 
        // Añadir las distintas regiones: superior (turno), centro (campo),
        // ubicaciones (registro/log).
        add(construirPanelSuperior(),  BorderLayout.NORTH);
        add(construirPanelCampo(),     BorderLayout.CENTER);
        add(construirPanelAcciones(),  BorderLayout.EAST);
        add(construirPanelLog(),       BorderLayout.SOUTH);
 
        // Tamaño por defecto y minimo para hacer la ventana usable.
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
    }
 
    private JPanel construirPanelSuperior() {
        // Panel simple que muestra a quien le toca el turno.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
 
        lblTurno = new JLabel("TURNO 0", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Serif", Font.BOLD, 18));
        lblTurno.setForeground(COLOR_GOLD);
        panel.add(lblTurno, BorderLayout.CENTER);
        return panel;
    }
 
    private JPanel construirPanelCampo() {
        // Panel que contiene dos subpaneles: el oponente arriba y el jugador
        // activo abajo. Usamos GridLayout(2,1) para apilar las zonas.
        JPanel panelTotal = new JPanel(new GridLayout(2, 1, 4, 4));
        panelTotal.setBackground(BG_DARK);
        panelTotal.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
 
        // Zona J2 (arriba - oponente): contiene informacion (nombre, LP, mano, mazo)
        // y un panel con las cartas/monstruos en su campo.
        JPanel zonaJ2 = new JPanel(new BorderLayout(4, 4));
        zonaJ2.setBackground(new Color(20, 20, 60));
        zonaJ2.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BLUE), " Oponente ",
            TitledBorder.LEFT, TitledBorder.TOP, null, COLOR_BLUE));
 
        // Panel con etiquetas informativas del oponente.
        JPanel infoJ2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        infoJ2.setBackground(new Color(20, 20, 60));
        lblNombreJ2 = infoLabel("", Font.BOLD, 14, COLOR_BLUE);
        lblLpJ2     = infoLabel("LP: 8000", Font.BOLD, 13, new Color(100, 200, 100));
        lblManoJ2   = infoLabel("Mano: 5", Font.PLAIN, 12, FG_WHITE);
        lblMazoJ2   = infoLabel("Mazo: 20", Font.PLAIN, 12, FG_WHITE);
        lblTrampasJ2= infoLabel("Trampas: 0", Font.PLAIN, 12, new Color(180, 100, 220));
        // Añadir las etiquetas al panel de información.
        infoJ2.add(lblNombreJ2); infoJ2.add(lblLpJ2);
        infoJ2.add(lblManoJ2);  infoJ2.add(lblMazoJ2); infoJ2.add(lblTrampasJ2);
 
        // Panel que mostrara las tarjetas/monstruos del oponente.
        panelCampoJ2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        panelCampoJ2.setBackground(BG_FIELD);
        panelCampoJ2.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BG_FIELD), "Campo",
            TitledBorder.LEFT, TitledBorder.TOP, null, Color.LIGHT_GRAY));
 
        zonaJ2.add(infoJ2, BorderLayout.NORTH);
        zonaJ2.add(new JScrollPane(panelCampoJ2), BorderLayout.CENTER);
        panelTotal.add(zonaJ2);
 
        // Zona J1 (abajo - jugador activo): similar a la zona del oponente pero
        // con colores y etiquetas para el jugador activo.
        JPanel zonaJ1 = new JPanel(new BorderLayout(4, 4));
        zonaJ1.setBackground(new Color(40, 10, 10));
        zonaJ1.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_RED), " Tu zona ",
            TitledBorder.LEFT, TitledBorder.TOP, null, COLOR_RED));
 
        JPanel infoJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        infoJ1.setBackground(new Color(40, 10, 10));
        lblNombreJ1 = infoLabel("", Font.BOLD, 14, COLOR_RED);
        lblLpJ1     = infoLabel("LP: 8000", Font.BOLD, 13, new Color(100, 200, 100));
        lblManoJ1   = infoLabel("Mano: 5", Font.PLAIN, 12, FG_WHITE);
        lblMazoJ1   = infoLabel("Mazo: 20", Font.PLAIN, 12, FG_WHITE);
        lblTrampasJ1= infoLabel("Trampas: 0", Font.PLAIN, 12, new Color(180, 100, 220));
        infoJ1.add(lblNombreJ1); infoJ1.add(lblLpJ1);
        infoJ1.add(lblManoJ1);  infoJ1.add(lblMazoJ1); infoJ1.add(lblTrampasJ1);
 
        panelCampoJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        panelCampoJ1.setBackground(BG_FIELD);
        panelCampoJ1.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BG_FIELD), "Campo",
            TitledBorder.LEFT, TitledBorder.TOP, null, Color.LIGHT_GRAY));
 
        zonaJ1.add(infoJ1, BorderLayout.NORTH);
        zonaJ1.add(new JScrollPane(panelCampoJ1), BorderLayout.CENTER);
        panelTotal.add(zonaJ1);
 
        return panelTotal;
    }
 
    private JPanel construirPanelAcciones() {
        // Panel vertical con botones de accion. Cada boton invoca un metodo
        // del controller para ejecutar la accion correspondiente.
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(20, 20, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GOLD),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)));
        panel.setPreferredSize(new Dimension(170, 0));
 
        JLabel titulo = new JLabel("ACCIONES", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 15));
        titulo.setForeground(COLOR_GOLD);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(14));
 
        // Crear los botones de accion con colores distintivos.
        btnJugarCarta      = crearBotonAccion("🃏 Jugar Carta",     COLOR_GREEN);
        btnAtacar          = crearBotonAccion("⚔ Atacar",           COLOR_RED);
        btnActivarTrampa   = crearBotonAccion("🕳 Activar Trampa",  COLOR_PURPLE);
        btnCambiarPosicion = crearBotonAccion("🔄 Cambiar Posición", COLOR_GRAY);
        btnTerminarTurno   = crearBotonAccion("✅ Terminar Turno",   new Color(80, 80, 20));
 
        panel.add(btnJugarCarta);      panel.add(Box.createVerticalStrut(8));
        panel.add(btnAtacar);          panel.add(Box.createVerticalStrut(8));
        panel.add(btnActivarTrampa);   panel.add(Box.createVerticalStrut(8));
        panel.add(btnCambiarPosicion); panel.add(Box.createVerticalStrut(8));
        panel.add(Box.createVerticalGlue());
        panel.add(btnTerminarTurno);
 
        // Conectar botones con el controller 
        btnJugarCarta.addActionListener(e -> controller.accionJugarCarta());
        btnAtacar.addActionListener(e -> controller.accionAtacar());
        btnActivarTrampa.addActionListener(e -> controller.accionActivarTrampa());
        btnCambiarPosicion.addActionListener(e -> controller.accionCambiarPosicion());
        btnTerminarTurno.addActionListener(e -> controller.accionTerminarTurno());
 
        return panel;
    }
 
    private JPanel construirPanelLog() {
        // Panel con un JTextArea de solo lectura que actua como registro del duelo.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_GOLD), " Registro de batalla ",
            TitledBorder.LEFT, TitledBorder.TOP, null, COLOR_GOLD));
 
        areaLog = new JTextArea(6, 80);
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(5, 5, 20));
        areaLog.setForeground(new Color(180, 220, 180));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
 
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setPreferredSize(new Dimension(0, 130));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
 
    // Helpers de construccion 
 
    // Crea una etiqueta con estilo uniforme para mostrar informacion (LP, mano, etc.)
    private JLabel infoLabel(String texto, int estilo, int size, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", estilo, size));
        lbl.setForeground(color);
        return lbl;
    }
 
    // Crea un boton estilizado para la columna de acciones.
    private JButton crearBotonAccion(String texto, Color bg) {
        JButton btn = new JButton("<html><center>" + texto + "</center></html>");
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(150, 42));
        btn.setPreferredSize(new Dimension(150, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }
 
    // Metodos publicos — implementacion de IDuelView
 
    // Añade una linea al registro de batalla.
    @Override
    public void agregarLog(String texto) {
        if (texto == null || texto.isBlank()) return;
        areaLog.append(texto);
        areaLog.append("\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
 
    // Refresca toda la interfaz tomando el estado actual del CampoBatalla.
    @Override
    public void actualizarUI() {
        CampoBatalla campo = controller.getCampo();
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
 
        lblTurno.setText("Turno " + campo.getTurnoActual() + "  —  Turno de: " + activo.getNombre().toUpperCase());
 
        lblNombreJ1.setText("⚔ " + activo.getNombre());
        lblLpJ1.setText("LP: " + activo.getLp());
        lblManoJ1.setText("Mano: " + activo.getMano().size());
        lblMazoJ1.setText("Mazo: " + activo.getMazo().tamano());
        lblTrampasJ1.setText("Trampas: " + activo.getZonaTrampas().size());
 
        lblNombreJ2.setText("👤 " + oponente.getNombre());
        lblLpJ2.setText("LP: " + oponente.getLp());
        lblManoJ2.setText("Mano: " + oponente.getMano().size());
        lblMazoJ2.setText("Mazo: " + oponente.getMazo().tamano());
        lblTrampasJ2.setText("Trampas: " + oponente.getZonaTrampas().size());
 
        colorearLP(lblLpJ1, activo.getLp());
        colorearLP(lblLpJ2, oponente.getLp());
 
        refrescarPanelCampo(panelCampoJ1, activo.getCampo());
        refrescarPanelCampo(panelCampoJ2, oponente.getCampo());
 
        boolean puedoJugar  = !activo.isYaJugoCartaEsteTurno() && !activo.getMano().isEmpty();
        boolean hayAtacantes = activo.getCampo().stream().anyMatch(CartaMonstruo::puedeAtacar);
        Contexto ctx        = new Contexto(activo, oponente, campo);
        boolean hayTrampas  = activo.hayTrampaActivable(ctx);
 
        btnJugarCarta.setEnabled(puedoJugar);
        btnAtacar.setEnabled(hayAtacantes && !activo.isYaAtacoEsteTurno());
        btnActivarTrampa.setEnabled(hayTrampas);
        btnCambiarPosicion.setEnabled(!activo.getCampo().isEmpty());
        btnTerminarTurno.setEnabled(true);
    }
 
    // Muestra un dialogo modal anunciando el ganador.
    @Override
    public void mostrarGanador() {
        btnJugarCarta.setEnabled(false);
        btnAtacar.setEnabled(false);
        btnActivarTrampa.setEnabled(false);
        btnCambiarPosicion.setEnabled(false);
        btnTerminarTurno.setEnabled(false);
 
        Jugador ganador = controller.getCampo().getGanador();
        String nombre = (ganador != null) ? ganador.getNombre() : "Nadie";
 
        actualizarUI();
 
        JDialog dialogo = new JDialog(this, "¡DUELO TERMINADO!", true);
        dialogo.setLayout(new BorderLayout());
        dialogo.getContentPane().setBackground(new Color(10, 10, 30));
 
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(10, 10, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(8, 0, 8, 0);
 
        JLabel trofeo = new JLabel("🏆", SwingConstants.CENTER);
        trofeo.setFont(new Font("Serif", Font.PLAIN, 60));
        panel.add(trofeo, gbc);
 
        gbc.gridy = 1;
        JLabel lblGana = new JLabel("¡" + nombre.toUpperCase() + " GANA EL DUELO!", SwingConstants.CENTER);
        lblGana.setFont(new Font("Serif", Font.BOLD, 22));
        lblGana.setForeground(COLOR_GOLD);
        panel.add(lblGana, gbc);
 
        gbc.gridy = 2;
        JLabel cita = new JLabel("\"Confía en el corazón de las cartas\" — Yugi Muto", SwingConstants.CENTER);
        cita.setFont(new Font("Serif", Font.ITALIC, 13));
        cita.setForeground(new Color(180, 180, 220));
        panel.add(cita, gbc);
 
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 8, 0);
        JButton btnNuevo = new JButton("Nueva partida");
        btnNuevo.setBackground(COLOR_GREEN);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNuevo.setFocusPainted(false);
        btnNuevo.addActionListener(e -> {
            dialogo.dispose();
            new VentanaInicio().setVisible(true);
            VentanaDuelo.this.dispose();
        });
        panel.add(btnNuevo, gbc);
 
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
 
    // Muestra un JOptionPane con opciones y retorna el indice elegido.
    // Retorna -1 si el usuario cancela.
    @Override
    public int pedirSeleccion(String titulo, String mensaje, String[] opciones) {
        String elegida = (String) JOptionPane.showInputDialog(
            this, mensaje, titulo,
            JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (elegida == null) return -1;
        return java.util.Arrays.asList(opciones).indexOf(elegida);
    }
 
    // Muestra un dialogo informativo con titulo y mensaje.
    @Override
    public void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
 
    // Metodos privados de apoyo
 
    private void colorearLP(JLabel lbl, int lp) {
        if (lp > 4000)      lbl.setForeground(new Color(80, 220, 80));
        else if (lp > 1500) lbl.setForeground(new Color(240, 200, 40));
        else                lbl.setForeground(new Color(240, 60, 60));
    }
 
    private void refrescarPanelCampo(JPanel panel, List<CartaMonstruo> monstruos) {
        panel.removeAll();
        if (monstruos.isEmpty()) {
            JLabel vacio = new JLabel("(campo vacío)");
            vacio.setForeground(Color.GRAY);
            panel.add(vacio);
        } else {
            for (int i = 0; i < monstruos.size(); i++) {
                panel.add(crearTarjetaMonstruo(monstruos.get(i)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }
 
    private JPanel crearTarjetaMonstruo(CartaMonstruo m) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(105, 115));
        card.setMaximumSize(new Dimension(105, 115));
 
        Color borde = m.estaEnModoDefensa() ? new Color(80, 140, 220) : new Color(220, 80, 80);
        card.setBorder(BorderFactory.createLineBorder(borde, 2));
        card.setBackground(new Color(20, 40, 20));
 
        JLabel nombre = new JLabel("<html><center>" + m.getNombre() + "</center></html>", SwingConstants.CENTER);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 10));
        nombre.setForeground(COLOR_GOLD);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel stats = new JLabel("ATK:" + m.getAtk() + " DEF:" + m.getDef(), SwingConstants.CENTER);
        stats.setFont(new Font("Monospaced", Font.PLAIN, 10));
        stats.setForeground(Color.WHITE);
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        String modoStr  = m.estaEnModoDefensa() ? "DEF" : "ATK";
        Color modoColor = m.estaEnModoDefensa() ? new Color(80, 140, 220) : new Color(220, 80, 80);
        JLabel modo = new JLabel("[ " + modoStr + " ]", SwingConstants.CENTER);
        modo.setFont(new Font("SansSerif", Font.BOLD, 10));
        modo.setForeground(modoColor);
        modo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        String ataqueStr = m.puedeAtacar() ? " puede atacar" : " ya ataco";
        JLabel ataque = new JLabel(ataqueStr, SwingConstants.CENTER);
        ataque.setFont(new Font("SansSerif", Font.PLAIN, 9));
        ataque.setForeground(m.puedeAtacar() ? new Color(100, 200, 100) : Color.GRAY);
        ataque.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel nivel = new JLabel("Lv" + m.getnivelCarta(), SwingConstants.CENTER);
        nivel.setFont(new Font("SansSerif", Font.PLAIN, 9));
        nivel.setForeground(new Color(200, 200, 100));
        nivel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        card.add(Box.createVerticalStrut(4));
        card.add(nombre);
        card.add(Box.createVerticalStrut(3));
        card.add(nivel);
        card.add(stats);
        card.add(modo);
        card.add(ataque);
        card.add(Box.createVerticalStrut(4));
 
        return card;
    }
}