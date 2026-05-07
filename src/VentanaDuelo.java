import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
// los de siempre: swing pa la interfaz, border pa los bordes fancy,
// awt pa colores y layouts, event pa los clics, y List de java.util pa las listas de cartas

/**
 * Ventana principal del duelo.
 * Muestra campo, mano, LP de ambos jugadores y botones de acción.
 * Toda interacción ocurre mediante eventos Swing (sin Scanner).
 */
public class VentanaDuelo extends JFrame {
// igual que VentanaInicio, hereda de JFrame o sea ella misma ES la ventana del duelo

    // ── Modelo ────────────────────────────────────────────────────────────────
    private final CampoBatalla campo;
    // este es el "cerebro" del juego, tiene toda la logica del duelo
    // es final pa que no se pueda reasignar accidentalmente despues de crearlo

    // ── Paneles de información ────────────────────────────────────────────────
    private JLabel lblTurno;
    // el label que muestra de quien es el turno arriba de todo
    private JTextArea areaLog;
    // el area de texto abajo donde se van imprimiendo todos los eventos del duelo

    // Panel jugador 1 (abajo)
    private JLabel lblNombreJ1, lblLpJ1, lblManoJ1, lblMazoJ1;
    private JPanel panelCampoJ1;
    // todos los labels del jugador activo (el que esta jugando ahorita)
    // se declaran como atributos pa poder actualizarlos desde actualizarUI()

    // Panel jugador 2 (arriba)
    private JLabel lblNombreJ2, lblLpJ2, lblManoJ2, lblMazoJ2;
    private JPanel panelCampoJ2;
    // misma vaina pero pa el oponente que se muestra arriba

    // Panel de trampas
    private JLabel lblTrampasJ1, lblTrampasJ2;
    // labels que muestran cuantas trampas tiene cada quien en campo

    // Botones de acción
    private JButton btnJugarCarta;
    private JButton btnAtacar;
    private JButton btnActivarTrampa;
    private JButton btnCambiarPosicion;
    private JButton btnTerminarTurno;
    // los 5 botones de accion del panel derecho, se guardan como atributos
    // pa poder habilitarlos/deshabilitarlos segun lo que se pueda hacer en cada momento

    // ── Colores temáticos ─────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(10, 10, 30);
    private static final Color BG_FIELD     = new Color(0, 60, 30);
    private static final Color COLOR_GOLD   = new Color(255, 215, 0);
    private static final Color COLOR_RED    = new Color(200, 20, 20);
    private static final Color COLOR_BLUE   = new Color(20, 100, 200);
    private static final Color COLOR_GREEN  = new Color(30, 160, 30);
    private static final Color COLOR_PURPLE = new Color(120, 30, 160);
    private static final Color COLOR_GRAY   = new Color(80, 80, 100);
    private static final Color FG_WHITE     = Color.WHITE;
    // aca definieron todos los colores del tema como constantes static final
    // o sea son valores fijos que no cambian nunca y se pueden usar en cualquier metodo
    // es buena practica pa no andar repitiendo new Color(255,215,0) en 20 lugares distintos

    public VentanaDuelo(CampoBatalla campo) {
        this.campo = campo;
        // guarda el campo de batalla recibido como atributo
        construirUI();
        // arma toda la interfaz grafica
        iniciarPrimerTurno();
        // prepara el primer turno del duelo (reparte cartas, setea el log inicial, etc)
    }

    // ── Construcción de la UI ─────────────────────────────────────────────────

    private void construirUI() {
        setTitle("Yu-Gi-Oh! — Duelo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(6, 6));
        // BorderLayout divide la ventana en 5 zonas: NORTH, SOUTH, EAST, WEST, CENTER
        // el (6,6) es el espacio en pixeles entre zonas
        getContentPane().setBackground(BG_DARK);
        // fondo oscuro pa todo lo que no cubran los paneles

        add(construirPanelSuperior(), BorderLayout.NORTH);
        // arriba va el panel con el turno actual
        add(construirPanelCampo(),   BorderLayout.CENTER);
        // en el centro va el campo de batalla (la parte mas grande)
        add(construirPanelAcciones(),BorderLayout.EAST);
        // a la derecha van los botones de accion
        add(construirPanelLog(),     BorderLayout.SOUTH);
        // abajo va el log de eventos

        setSize(1100, 720);
        // tamanio inicial de la ventana en pixeles
        setMinimumSize(new Dimension(900, 620));
        // tamanio minimo pa que aunque la redimensiones no se vea todo aplastado
        setLocationRelativeTo(null);
        // centra la ventana en la pantalla
    }

    // ── Panel superior: turno + info jugadores ────────────────────────────────

    private JPanel construirPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        // panel azul oscuro con padding interno

        lblTurno = new JLabel("TURNO 0", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Serif", Font.BOLD, 18));
        lblTurno.setForeground(COLOR_GOLD);
        // label dorado centrado que dice de quien es el turno, empieza en 0 y se actualiza despues
        panel.add(lblTurno, BorderLayout.CENTER);
        return panel;
    }

    // ── Panel central: campo de batalla (J2 arriba, J1 abajo) ────────────────

    private JPanel construirPanelCampo() {
        JPanel panelTotal = new JPanel(new GridLayout(2, 1, 4, 4));
        // GridLayout de 2 filas y 1 columna: arriba oponente, abajo jugador activo
        // el (4,4) es el espacio entre las dos zonas
        panelTotal.setBackground(BG_DARK);
        panelTotal.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        // ── Jugador 2 (arriba) ────────────────────────────────────────────────
        JPanel zonaJ2 = new JPanel(new BorderLayout(4, 4));
        zonaJ2.setBackground(new Color(20, 20, 60));
        zonaJ2.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BLUE), " Oponente ",
            TitledBorder.LEFT, TitledBorder.TOP, null, COLOR_BLUE));
        // TitledBorder es ese borde con un titulo encima, tipo la etiquetita que dice "Oponente"
        // va con borde azul pa distinguirlo de la zona del jugador activo que es roja

        JPanel infoJ2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        // FlowLayout pone los componentes en fila de izquierda a derecha
        infoJ2.setBackground(new Color(20, 20, 60));
        lblNombreJ2 = infoLabel("", Font.BOLD, 14, COLOR_BLUE);
        lblLpJ2     = infoLabel("LP: 8000", Font.BOLD, 13, new Color(100, 200, 100));
        lblManoJ2   = infoLabel("Mano: 5", Font.PLAIN, 12, FG_WHITE);
        lblMazoJ2   = infoLabel("Mazo: 20", Font.PLAIN, 12, FG_WHITE);
        lblTrampasJ2= infoLabel("Trampas: 0", Font.PLAIN, 12, new Color(180, 100, 220));
        // infoLabel() es un metodo helper de abajo que crea labels con estilo ya aplicado
        // pa no repetir setFont, setForeground, etc en cada uno
        infoJ2.add(lblNombreJ2); infoJ2.add(lblLpJ2);
        infoJ2.add(lblManoJ2); infoJ2.add(lblMazoJ2); infoJ2.add(lblTrampasJ2);

        panelCampoJ2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        panelCampoJ2.setBackground(BG_FIELD);
        // BG_FIELD es un verde oscuro que simula el tapete del juego de cartas
        panelCampoJ2.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BG_FIELD), "Campo",
            TitledBorder.LEFT, TitledBorder.TOP, null, Color.LIGHT_GRAY));

        zonaJ2.add(infoJ2, BorderLayout.NORTH);
        // la info del jugador (LP, mano, etc) va arriba de su zona
        zonaJ2.add(new JScrollPane(panelCampoJ2), BorderLayout.CENTER);
        // el campo con las cartas va en el centro envuelto en un JScrollPane
        // pa que si hay muchas cartas aparezca scroll y no se rompa el layout
        panelTotal.add(zonaJ2);

        // ── Jugador 1 (abajo) ─────────────────────────────────────────────────
        JPanel zonaJ1 = new JPanel(new BorderLayout(4, 4));
        zonaJ1.setBackground(new Color(40, 10, 10));
        // fondo rojizo pa la zona del jugador activo, contrasta con el azulado del oponente
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
        // misma estructura que J2 pero con colores rojos pa la zona propia
        infoJ1.add(lblNombreJ1); infoJ1.add(lblLpJ1);
        infoJ1.add(lblManoJ1); infoJ1.add(lblMazoJ1); infoJ1.add(lblTrampasJ1);

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

    // ── Panel de acciones (derecha) ───────────────────────────────────────────

    private JPanel construirPanelAcciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // BoxLayout en eje Y apila los componentes de arriba hacia abajo
        panel.setBackground(new Color(20, 20, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GOLD),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)));
        // borde dorado por fuera + padding interno, mismo truco que en VentanaInicio
        panel.setPreferredSize(new Dimension(170, 0));
        // ancho fijo de 170px, el alto lo define el BorderLayout automaticamente

        JLabel titulo = new JLabel("ACCIONES", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 15));
        titulo.setForeground(COLOR_GOLD);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        // CENTER_ALIGNMENT es necesario en BoxLayout pa centrar horizontalmente
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(14));
        // Box.createVerticalStrut() crea un espacio vacio vertical de N pixeles
        // es como ponerle margin-bottom al titulo

        btnJugarCarta     = crearBotonAccion("🃏 Jugar Carta",    COLOR_GREEN);
        btnAtacar         = crearBotonAccion("⚔ Atacar",          COLOR_RED);
        btnActivarTrampa  = crearBotonAccion("🕳 Activar Trampa", COLOR_PURPLE);
        btnCambiarPosicion= crearBotonAccion("🔄 Cambiar Posición",COLOR_GRAY);
        btnTerminarTurno  = crearBotonAccion("✅ Terminar Turno",  new Color(80, 80, 20));
        // crearBotonAccion() es otro helper de abajo, crea botones con el mismo estilo
        // cada boton tiene su propio color pa distinguir visualmente las acciones

        panel.add(btnJugarCarta);      panel.add(Box.createVerticalStrut(8));
        panel.add(btnAtacar);          panel.add(Box.createVerticalStrut(8));
        panel.add(btnActivarTrampa);   panel.add(Box.createVerticalStrut(8));
        panel.add(btnCambiarPosicion); panel.add(Box.createVerticalStrut(8));
        panel.add(Box.createVerticalGlue());
        // createVerticalGlue() es como un resorte que empuja el boton de terminar turno hacia abajo
        panel.add(btnTerminarTurno);
        // terminar turno queda al final del panel separado de los demas

        btnJugarCarta.addActionListener(e -> accionJugarCarta());
        btnAtacar.addActionListener(e -> accionAtacar());
        btnActivarTrampa.addActionListener(e -> accionActivarTrampa());
        btnCambiarPosicion.addActionListener(e -> accionCambiarPosicion());
        btnTerminarTurno.addActionListener(e -> accionTerminarTurno());
        // cada boton se conecta con su metodo correspondiente usando lambdas
        // cuando el usuario haga clic se ejecuta el metodo accion___()

        return panel;
    }

    // ── Panel de log (abajo) ──────────────────────────────────────────────────

    private JPanel construirPanelLog() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_GOLD), " Registro de batalla ",
            TitledBorder.LEFT, TitledBorder.TOP, null, COLOR_GOLD));
        // borde dorado con el titulo "Registro de batalla" en la esquina

        areaLog = new JTextArea(6, 80);
        // 6 filas de alto visible y 80 columnas de ancho
        areaLog.setEditable(false);
        // el log no se puede editar, solo leer, pa que el usuario no borre el historial
        areaLog.setBackground(new Color(5, 5, 20));
        areaLog.setForeground(new Color(180, 220, 180));
        // texto verde clarito sobre fondo casi negro, tipo terminal retro
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        // fuente monoespaciada tipo consola, queda bien pa logs
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        // estas dos juntas hacen que el texto salte de linea automaticamente
        // y que corte por palabras completas, no por la mitad de una palabra

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setPreferredSize(new Dimension(0, 130));
        // altura fija de 130px pa el panel de log, el ancho lo define el BorderLayout
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Helpers de construcción ───────────────────────────────────────────────

    private JLabel infoLabel(String texto, int estilo, int size, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", estilo, size));
        lbl.setForeground(color);
        return lbl;
        // metodo utilitario que crea un JLabel ya con fuente y color aplicados
        // pa no repetir las 3 lineas cada vez que se necesita un label de info
    }

    private JButton crearBotonAccion(String texto, Color bg) {
        JButton btn = new JButton("<html><center>" + texto + "</center></html>");
        // usan HTML dentro del JButton pa poder centrar el texto, es un truquito de Swing
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        // quita el rectangulito feo de foco
        btn.setBorderPainted(false);
        // quita el borde por defecto del boton pa que se vea mas flat y moderno
        btn.setOpaque(true);
        // necesario pa que el color de fondo se muestre bien en algunos sistemas operativos
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // manito al pasar el mouse encima, detallito UX
        btn.setMaximumSize(new Dimension(150, 42));
        btn.setPreferredSize(new Dimension(150, 42));
        // tamanio fijo pa que todos los botones tengan el mismo ancho y alto
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        // necesario pa centrarlos en el BoxLayout
        return btn;
    }

    // ── Flujo de juego ────────────────────────────────────────────────────────

    private void iniciarPrimerTurno() {
        String log = campo.prepararTurno();
        // prepararTurno() en el modelo hace todo lo del inicio: robar cartas, definir quien empieza, etc
        agregarLog(log);
        // manda el resultado al area de log pa que el usuario vea que paso
        actualizarUI();
        // refresca toda la interfaz pa mostrar el estado inicial del duelo
    }

    private void actualizarUI() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        Jugador j1       = campo.getJugador1();
        Jugador j2       = campo.getJugador2();
        // obtiene referencias a los jugadores del modelo pa actualizar la vista

        // El jugador activo siempre se muestra abajo (J1 panel) para mayor claridad
        // Identificamos quién es activo y quién es rival:
        Jugador abajo  = activo;
        Jugador arriba = oponente;
        // truco de diseño: siempre el jugador que esta jugando se muestra abajo
        // independientemente de si es J1 o J2 en el modelo, asi es mas intuitivo

        lblTurno.setText("Turno " + campo.getTurnoActual() + "  —  Turno de: " + activo.getNombre().toUpperCase());
        // actualiza el label del turno con el numero y el nombre del jugador activo

        // Info abajo (activo)
        lblNombreJ1.setText("⚔ " + abajo.getNombre());
        lblLpJ1.setText("LP: " + abajo.getLp());
        lblManoJ1.setText("Mano: " + abajo.getMano().size());
        lblMazoJ1.setText("Mazo: " + abajo.getMazo().tamano());
        lblTrampasJ1.setText("Trampas: " + abajo.getZonaTrampas().size());
        // actualiza todos los labels de info del jugador activo con los datos actuales del modelo

        // Info arriba (rival)
        lblNombreJ2.setText("👤 " + arriba.getNombre());
        lblLpJ2.setText("LP: " + arriba.getLp());
        lblManoJ2.setText("Mano: " + arriba.getMano().size());
        lblMazoJ2.setText("Mazo: " + arriba.getMazo().tamano());
        lblTrampasJ2.setText("Trampas: " + arriba.getZonaTrampas().size());
        // misma vaina pa el oponente

        // Color LP
        colorearLP(lblLpJ1, abajo.getLp());
        colorearLP(lblLpJ2, arriba.getLp());
        // llama al metodo que cambia el color del LP segun cuanto les queda

        // Campos
        refrescarPanelCampo(panelCampoJ1, abajo.getCampo());
        refrescarPanelCampo(panelCampoJ2, arriba.getCampo());
        // redibuja las tarjetas de monstruos en los paneles de campo

        // Habilitar / deshabilitar botones
        boolean miTurno = true; // La GUI siempre muestra el jugador activo abajo
        boolean puedoJugar = !activo.isYaJugoCartaEsteTurno() && !activo.getMano().isEmpty();
        // se puede jugar carta si: no jugaste una ya en este turno Y tienes cartas en mano
        boolean hayAtacantes = activo.getCampo().stream().anyMatch(CartaMonstruo::puedeAtacar);
        // busca si hay al menos un monstruo en campo que pueda atacar, usando streams
        Contexto ctx = new Contexto(activo, oponente, campo);
        boolean hayTrampas = activo.hayTrampaActivable(ctx);
        // verifica si alguna trampa puede activarse en este momento

        btnJugarCarta.setEnabled(puedoJugar);
        btnAtacar.setEnabled(hayAtacantes && !activo.isYaAtacoEsteTurno());
        btnActivarTrampa.setEnabled(hayTrampas);
        btnCambiarPosicion.setEnabled(!activo.getCampo().isEmpty());
        btnTerminarTurno.setEnabled(true);
        // habilita o deshabilita cada boton segun lo que se puede hacer en este momento
        // los botones grises/desactivados le dicen al jugador que esa accion no es posible ahora
    }

    private void colorearLP(JLabel lbl, int lp) {
        if (lp > 4000)      lbl.setForeground(new Color(80, 220, 80));
        // mas de 4000 LP: verde, estas bien
        else if (lp > 1500) lbl.setForeground(new Color(240, 200, 40));
        // entre 1500 y 4000: amarillo, cuidado
        else                lbl.setForeground(new Color(240, 60, 60));
        // menos de 1500: rojo, estas en peligro
        // es un feedback visual buenisimo, como la barra de vida en los videojuegos
    }

    private void refrescarPanelCampo(JPanel panel, List<CartaMonstruo> monstruos) {
        panel.removeAll();
        // borra todas las tarjetas anteriores del panel pa redibujar desde cero
        if (monstruos.isEmpty()) {
            JLabel vacio = new JLabel("(campo vacío)");
            vacio.setForeground(Color.GRAY);
            panel.add(vacio);
            // si no hay monstruos muestra un mensajito de "campo vacio"
        } else {
            for (int i = 0; i < monstruos.size(); i++) {
                panel.add(crearTarjetaMonstruo(monstruos.get(i), i));
                // por cada monstruo en campo crea una tarjetita visual y la agrega al panel
            }
        }
        panel.revalidate();
        // le dice al layout que recalcule las posiciones de los componentes
        panel.repaint();
        // y le dice a Swing que redibuje el panel en pantalla
        // sin estos dos el panel no se actualiza visualmente aunque le hayas agregado cosas
    }

    private JPanel crearTarjetaMonstruo(CartaMonstruo m, int idx) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        // los elementos de la tarjeta se apilan de arriba hacia abajo
        card.setPreferredSize(new Dimension(105, 115));
        card.setMaximumSize(new Dimension(105, 115));
        // tamanio fijo pa todas las tarjetas, tipo las cartas fisicas del juego

        Color borde = m.estaEnModoDefensa() ? new Color(80, 140, 220) : new Color(220, 80, 80);
        card.setBorder(BorderFactory.createLineBorder(borde, 2));
        // borde azul si esta en modo defensa, rojo si esta en modo ataque
        // es el feedback visual de la posicion de la carta
        card.setBackground(new Color(20, 40, 20));
        // fondo verde muy oscuro tipo tapete

        JLabel nombre = new JLabel("<html><center>" + m.getNombre() + "</center></html>", SwingConstants.CENTER);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 10));
        nombre.setForeground(COLOR_GOLD);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        // nombre del monstruo en dorado centrado, usan HTML pa el centrado en BoxLayout

        JLabel stats = new JLabel("ATK:" + m.getAtk() + " DEF:" + m.getDef(), SwingConstants.CENTER);
        stats.setFont(new Font("Monospaced", Font.PLAIN, 10));
        stats.setForeground(Color.WHITE);
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);
        // muestra el ATK y DEF del monstruo en fuente monoespaciada, queda como en el juego real

        String modoStr = m.estaEnModoDefensa() ? "DEF" : "ATK";
        Color modoColor = m.estaEnModoDefensa() ? new Color(80, 140, 220) : new Color(220, 80, 80);
        JLabel modo = new JLabel("[ " + modoStr + " ]", SwingConstants.CENTER);
        modo.setFont(new Font("SansSerif", Font.BOLD, 10));
        modo.setForeground(modoColor);
        modo.setAlignmentX(Component.CENTER_ALIGNMENT);
        // muestra el modo en el color correspondiente, refuerza el borde

        String ataqueStr = m.puedeAtacar() ? "✔ puede atacar" : "✘ ya atacó";
        JLabel ataque = new JLabel(ataqueStr, SwingConstants.CENTER);
        ataque.setFont(new Font("SansSerif", Font.PLAIN, 9));
        ataque.setForeground(m.puedeAtacar() ? new Color(100, 200, 100) : Color.GRAY);
        ataque.setAlignmentX(Component.CENTER_ALIGNMENT);
        // checkmark verde si puede atacar, x gris si ya ataco este turno
        // pa que el jugador sepa de un vistazo cuales monstruos todavia pueden actuar

        JLabel nivel = new JLabel("Lv" + m.getnivelCarta(), SwingConstants.CENTER);
        nivel.setFont(new Font("SansSerif", Font.PLAIN, 9));
        nivel.setForeground(new Color(200, 200, 100));
        nivel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // muestra el nivel de la carta, importante pa saber si necesita sacrificio pa invocar

        card.add(Box.createVerticalStrut(4));
        card.add(nombre);
        card.add(Box.createVerticalStrut(3));
        card.add(nivel);
        card.add(stats);
        card.add(modo);
        card.add(ataque);
        card.add(Box.createVerticalStrut(4));
        // va agregando todos los labels con espaciados entre ellos, de arriba a abajo

        return card;
    }

    private void agregarLog(String texto) {
        if (texto == null || texto.isBlank()) return;
        // si el texto es nulo o solo espacios no hace nada, pa evitar lineas en blanco inutil
        areaLog.append(texto);
        areaLog.append("\n");
        // agrega el texto al area de log con un salto de linea al final
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
        // esto mueve el scroll automaticamente hasta el final del texto
        // pa que siempre se vea el evento mas reciente sin tener que scrollear manualmente
    }

    // ── Acciones de botones ───────────────────────────────────────────────────

    private void accionJugarCarta() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        Contexto ctx     = new Contexto(activo, oponente, campo);
        List<Carta> mano = activo.getMano();
        // obtiene el estado actual del juego pa trabajar con el

        if (mano.isEmpty()) { agregarLog("No tienes cartas en la mano."); return; }
        if (activo.isYaJugoCartaEsteTurno()) { agregarLog("Ya jugaste una carta este turno."); return; }
        // validaciones rapidas antes de mostrar el dialogo, si no se puede jugar se registra en el log y se sale

        // Construir opciones para el JOptionPane
        String[] opciones = new String[mano.size() + 1];
        for (int i = 0; i < mano.size(); i++) opciones[i] = (i + 1) + ". " + mano.get(i).toString();
        opciones[mano.size()] = "Cancelar";
        // crea un arreglo de strings con las cartas de la mano numeradas
        // mas el boton de cancelar al final

        String elegida = (String) JOptionPane.showInputDialog(
            this, "Elige una carta para jugar:",
            "🃏 Tu mano — " + activo.getNombre(),
            JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        // muestra un dialogo con las opciones pa que el jugador elija una carta
        // JOptionPane.showInputDialog con arreglo de opciones muestra un dropdown/lista

        if (elegida == null || elegida.equals("Cancelar")) return;
        // si cancelo o cerro el dialogo, no hace nada

        int idx = java.util.Arrays.asList(opciones).indexOf(elegida);
        if (idx < 0 || idx >= mano.size()) return;
        // busca el indice de la carta elegida en el arreglo, con validacion por si acaso

        Carta carta = mano.get(idx);

        // Si es monstruo nivel > 4, pedir sacrificio
        int indiceSacrificio = -1;
        if (carta.getTipo().equals("MONSTRUO")) {
            CartaMonstruo mon = (CartaMonstruo) carta;
            if (mon.getnivelCarta() > 4) {
                // en Yu-Gi-Oh los monstruos de nivel 5 o mas necesitan un sacrificio pa invocarlos
                if (activo.getCampo().isEmpty()) {
                    agregarLog("Necesitas sacrificar un monstruo para invocar " + mon.getNombre()
                             + " (nivel " + mon.getnivelCarta() + "), pero no tienes monstruos en campo.");
                    return;
                    // si no tienes con que sacrificar, no puedes invocar el monstruo
                }
                String[] opSacrificio = new String[activo.getCampo().size()];
                for (int i = 0; i < activo.getCampo().size(); i++) {
                    opSacrificio[i] = (i + 1) + ". " + activo.getCampo().get(i).getNombre();
                }
                String elegidoSac = (String) JOptionPane.showInputDialog(
                    this,
                    mon.getNombre() + " (Lv" + mon.getnivelCarta() + ") requiere un sacrificio.\nElige el monstruo a sacrificar:",
                    "⚰ Sacrificio requerido",
                    JOptionPane.WARNING_MESSAGE, null, opSacrificio, opSacrificio[0]);
                // segundo dialogo pa elegir cual monstruo sacrificar
                if (elegidoSac == null) return;
                indiceSacrificio = java.util.Arrays.asList(opSacrificio).indexOf(elegidoSac);
            }
        }

        boolean jugado = activo.jugarCarta(idx, ctx, indiceSacrificio);
        // delega la logica de jugar la carta al modelo (Jugador)
        // el -1 en indiceSacrificio significa que no hay sacrificio necesario

        if (jugado) {
            agregarLog(activo.getNombre() + " jugó: " + carta.getNombre());
            verificarGanador();
            // despues de jugar una carta se verifica si alguien gano
        } else {
            agregarLog("No se pudo jugar la carta.");
        }
        actualizarUI();
        // siempre se actualiza la interfaz al final de una accion
    }

    private void accionAtacar() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();

        // Filtrar monstruos que pueden atacar
        List<CartaMonstruo> disponibles = new java.util.ArrayList<>();
        for (CartaMonstruo m : activo.getCampo()) if (m.puedeAtacar()) disponibles.add(m);
        // filtra solo los monstruos que aun no atacaron este turno

        if (disponibles.isEmpty()) { agregarLog("Ningún monstruo puede atacar."); return; }
        if (activo.isYaAtacoEsteTurno()) { agregarLog("Ya atacaste este turno."); return; }

        // ── 1. Elegir atacante ────────────────────────────────────────────────
        String[] opAtacantes = new String[disponibles.size() + 1];
        for (int i = 0; i < disponibles.size(); i++) opAtacantes[i] = (i + 1) + ". " + disponibles.get(i);
        opAtacantes[disponibles.size()] = "Cancelar";

        String elegidoAtac = (String) JOptionPane.showInputDialog(
            this, "Elige el monstruo ATACANTE:",
            "⚔ Ataque — " + activo.getNombre(),
            JOptionPane.PLAIN_MESSAGE, null, opAtacantes, opAtacantes[0]);
        if (elegidoAtac == null || elegidoAtac.equals("Cancelar")) return;

        int idxAtac = java.util.Arrays.asList(opAtacantes).indexOf(elegidoAtac);
        if (idxAtac < 0 || idxAtac >= disponibles.size()) return;
        CartaMonstruo atacante = disponibles.get(idxAtac);
        // primer dialogo: elegir cual de tus monstruos va a atacar

        // ── 2. Elegir defensor (si hay monstruos en campo rival) ──────────────
        CartaMonstruo defensor = null;

        if (!oponente.getCampo().isEmpty()) {
            List<CartaMonstruo> defensores = oponente.getCampo();
            String[] opDefensores = new String[defensores.size() + 1];
            for (int i = 0; i < defensores.size(); i++) opDefensores[i] = (i + 1) + ". " + defensores.get(i);
            opDefensores[defensores.size()] = "Cancelar";

            String elegidoDef = (String) JOptionPane.showInputDialog(
                this, "Elige el monstruo a atacar:",
                "🎯 Selecciona objetivo",
                JOptionPane.PLAIN_MESSAGE, null, opDefensores, opDefensores[0]);
            if (elegidoDef == null || elegidoDef.equals("Cancelar")) return;

            int idxDef = java.util.Arrays.asList(opDefensores).indexOf(elegidoDef);
            if (idxDef < 0 || idxDef >= defensores.size()) return;
            defensor = defensores.get(idxDef);
            // segundo dialogo: elegir cual monstruo del oponente vas a atacar
            // si el oponente no tiene monstruos en campo este paso se salta
        }

        // ── 3. FASE DE RESPUESTA: el defensor puede activar trampas ──────────
        //
        // Se construye el contexto CON ROLES INVERTIDOS:
        //   ctx.getJugadorActivo() = oponente (dueño de las trampas)
        //   ctx.getOponente()      = activo   (el atacante)
        //
        // Esto hace que toda la lógica existente en las cartas trampa funcione
        // correctamente sin modificar ninguna otra clase.
        //
        Contexto ctxDefensa = new Contexto(oponente, activo, campo);
        ctxDefensa.setMonstruoAtacante(atacante);
        // contexto con roles invertidos pa que las trampas del defensor tengan el contexto correcto

        if (oponente.hayTrampaActivable(ctxDefensa)) {
            boolean activoTrampa = ofrecerRespuestaTrampas(oponente, ctxDefensa);
            // si el oponente tiene trampas activables, se le ofrece la opcion de activar una

            if (activoTrampa) {
                verificarGanador();
                if (campo.hayGanador()) { actualizarUI(); return; }
                // si la trampa termino el duelo, se para todo aca

                // ¿El atacante fue destruido por la trampa?
                if (!activo.getCampo().contains(atacante)) {
                    agregarLog("⚡ ¡" + atacante.getNombre() + " fue destruido! El ataque queda cancelado.");
                    activo.setYaAtacoEsteTurno(true);
                    actualizarUI();
                    return;
                    // si la trampa destruyo al atacante, el ataque se cancela
                }
            }
        }

        // ── 4. Resolver combate ───────────────────────────────────────────────
        String logCombate;

        if (oponente.getCampo().isEmpty()) {
            logCombate = campo.ataqueDirecto(atacante, oponente);
            // si el oponente no tiene monstruos, ataque directo a los LP

        } else if (defensor != null && !oponente.getCampo().contains(defensor)) {
            agregarLog("El defensor fue destruido por la trampa. ¡Ataque directo!");
            logCombate = campo.ataqueDirecto(atacante, oponente);
            // si la trampa destruyo al defensor que eligieron, ataque directo tambien

        } else if (defensor != null) {
            logCombate = campo.resolverCombate(atacante, defensor, activo, oponente);
            // combate normal entre los dos monstruos elegidos

        } else {
            logCombate = campo.ataqueDirecto(atacante, oponente);
            // fallback por si algo raro paso, igual se ataca directo
        }

        activo.setYaAtacoEsteTurno(true);
        // marca que el jugador activo ya realizo su ataque este turno
        agregarLog(logCombate);
        verificarGanador();
        actualizarUI();
    }

    /**
     * Muestra al jugador defensor la lista de trampas activables durante un ataque
     * y le permite elegir una para activar.
     *
     * El contexto recibido ya tiene los roles invertidos:
     *   ctx.getJugadorActivo() = defensor (dueño de las trampas)
     *   ctx.getOponente()      = atacante
     *
     * @return true si se activó alguna trampa, false si el jugador pasó.
     */
    private boolean ofrecerRespuestaTrampas(Jugador defensor, Contexto ctx) {
        List<CartaTrampa> trampas = defensor.getZonaTrampas();
        List<Integer>     indices = new java.util.ArrayList<>();
        List<String>      nombres = new java.util.ArrayList<>();
        // listas pa guardar solo las trampas que SI se pueden activar ahora

        for (int i = 0; i < trampas.size(); i++) {
            if (trampas.get(i).puedoActivarme(ctx)) {
                indices.add(i);
                nombres.add((indices.size()) + ". " + trampas.get(i).toString());
            }
        }
        // filtra las trampas activables y guarda sus indices reales pa usar despues
        nombres.add("⛔ No activar");

        String[] ops = nombres.toArray(new String[0]);

        // Preguntar al defensor — se coloca la opción "No activar" como selección por defecto
        String elegida = (String) JOptionPane.showInputDialog(
            this,
            "⚠  ¡ATAQUE DECLARADO!\n\n" +
            defensor.getNombre() + ", ¿deseas activar una trampa en respuesta?\n" +
            "(Si no activas nada, el combate se resuelve normalmente)",
            "🕳  Respuesta de trampas — " + defensor.getNombre(),
            JOptionPane.WARNING_MESSAGE, null, ops, ops[ops.length - 1]);
        // dialogo de advertencia pa que el defensor decida si activa una trampa
        // ops[ops.length-1] es "No activar", que queda seleccionado por defecto

        // Canceló la ventana o eligió "No activar"
        if (elegida == null || elegida.equals("⛔ No activar")) return false;
        // si no quiso activar nada retorna false

        int posLista = nombres.indexOf(elegida);
        if (posLista < 0 || posLista >= indices.size()) return false;

        int idxReal = indices.get(posLista);
        CartaTrampa trampa = trampas.get(idxReal);
        // recupera la trampa real usando el indice que guardamos antes

        agregarLog("🕳 " + defensor.getNombre() + " activó trampa en respuesta: " + trampa.getNombre());
        boolean ok = defensor.activarTrampa(idxReal, ctx);
        if (!ok) {
            agregarLog("La trampa no pudo activarse.");
            return false;
        }
        return true;
        // retorna true si la trampa se activo exitosamente
    }

    private void accionActivarTrampa() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        Contexto ctx     = new Contexto(activo, oponente, campo);

        List<CartaTrampa> trampas = activo.getZonaTrampas();
        if (trampas.isEmpty()) { agregarLog("No tienes trampas colocadas."); return; }

        // Mostrar solo las activables
        java.util.List<Integer> indices = new java.util.ArrayList<>();
        java.util.List<String> nombres = new java.util.ArrayList<>();
        for (int i = 0; i < trampas.size(); i++) {
            if (trampas.get(i).puedoActivarme(ctx)) {
                indices.add(i);
                nombres.add((i + 1) + ". " + trampas.get(i).toString());
            }
        }
        nombres.add("Cancelar");
        // mismo patron que ofrecerRespuestaTrampas: filtra solo las activables y muestra dialogo

        if (indices.isEmpty()) { agregarLog("Ninguna trampa puede activarse ahora."); return; }

        String[] ops = nombres.toArray(new String[0]);
        String elegida = (String) JOptionPane.showInputDialog(
            this, "Elige la trampa a activar:",
            "🕳 Trampas — " + activo.getNombre(),
            JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
        if (elegida == null || elegida.equals("Cancelar")) return;

        int posLista = nombres.indexOf(elegida);
        if (posLista < 0 || posLista >= indices.size()) return;
        int idxReal = indices.get(posLista);

        CartaTrampa trampa = trampas.get(idxReal);
        agregarLog(">>> " + activo.getNombre() + " activó trampa: " + trampa.getNombre());
        boolean ok = activo.activarTrampa(idxReal, ctx);
        if (!ok) agregarLog("La trampa no pudo activarse.");
        // delega la activacion al modelo y registra el resultado en el log

        verificarGanador();
        actualizarUI();
    }

    private void accionCambiarPosicion() {
        Jugador activo = campo.getJugadorActivo();
        if (activo.getCampo().isEmpty()) { agregarLog("No tienes monstruos en campo."); return; }

        String[] ops = new String[activo.getCampo().size() + 1];
        for (int i = 0; i < activo.getCampo().size(); i++) ops[i] = (i + 1) + ". " + activo.getCampo().get(i);
        ops[activo.getCampo().size()] = "Cancelar";
        // lista de monstruos en campo mas cancelar

        String elegida = (String) JOptionPane.showInputDialog(
            this, "Elige el monstruo para cambiar posición:",
            "🔄 Cambiar posición",
            JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
        if (elegida == null || elegida.equals("Cancelar")) return;

        int idx = java.util.Arrays.asList(ops).indexOf(elegida);
        if (idx < 0 || idx >= activo.getCampo().size()) return;

        CartaMonstruo m = activo.getCampo().get(idx);
        m.cambiarPosicion();
        // llama al metodo del monstruo que alterna entre modo ataque y defensa
        agregarLog(">>> " + m.getNombre() + " cambió a modo " + (m.estaEnModoDefensa() ? "DEFENSA" : "ATAQUE") + ".");
        actualizarUI();
    }

    private void accionTerminarTurno() {
        Jugador terminando = campo.getJugadorActivo();
        agregarLog("── " + terminando.getNombre() + " termina su turno ──");
        campo.terminarTurno();
        // le avisa al modelo que el turno termino, el modelo resetea flags de ataque, etc

        if (campo.hayGanador()) { mostrarGanador(); return; }
        // si terminar el turno produjo un ganador (por ejemplo mazo vacio) se muestra el resultado

        String log = campo.prepararTurno();
        agregarLog(log);
        // prepara el siguiente turno: roba carta, cambia el jugador activo, etc

        if (campo.hayGanador()) { mostrarGanador(); return; }
        // segunda verificacion por si al robar carta el mazo se vacio

        actualizarUI();
        // Notificar de quién es el turno con un diálogo no bloqueante
        JOptionPane.showMessageDialog(this,
            "Es el turno de:\n" + campo.getJugadorActivo().getNombre().toUpperCase(),
            "Nuevo Turno", JOptionPane.INFORMATION_MESSAGE);
        // popupito pa avisar de quien es el turno ahora
        // es bloqueante pero es necesario pa que el jugador que viene sepa que ya puede jugar
    }

    // ── Fin del juego ─────────────────────────────────────────────────────────

    private void verificarGanador() {
        if (campo.hayGanador()) mostrarGanador();
        // metodo cortito que simplemente chequea y delega, pa no repetir el if en todos lados
    }

    private void mostrarGanador() {
        // Deshabilitar todos los botones
        btnJugarCarta.setEnabled(false);
        btnAtacar.setEnabled(false);
        btnActivarTrampa.setEnabled(false);
        btnCambiarPosicion.setEnabled(false);
        btnTerminarTurno.setEnabled(false);
        // se deshabilitan todos los botones pa que no se pueda seguir jugando despues del fin

        Jugador ganador = campo.getGanador();
        String nombre = (ganador != null) ? ganador.getNombre() : "Nadie";
        // si por alguna razon no hay ganador definido, muestra "Nadie", pa evitar null pointer

        actualizarUI();

        // Ventana de victoria
        JDialog dialogo = new JDialog(this, "¡DUELO TERMINADO!", true);
        // JDialog es un popup modal (el true), o sea bloquea la ventana principal hasta que se cierre
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
        // emoji de trofeo gigante, es el toque dramatico del final del duelo

        gbc.gridy = 1;
        JLabel lblGana = new JLabel("¡" + nombre.toUpperCase() + " GANA EL DUELO!", SwingConstants.CENTER);
        lblGana.setFont(new Font("Serif", Font.BOLD, 22));
        lblGana.setForeground(COLOR_GOLD);
        panel.add(lblGana, gbc);
        // nombre del ganador en mayusculas y dorado, pa que se vea epico

        gbc.gridy = 2;
        JLabel cita = new JLabel("\"Confía en el corazón de las cartas\" — Yugi Muto", SwingConstants.CENTER);
        cita.setFont(new Font("Serif", Font.ITALIC, 13));
        cita.setForeground(new Color(180, 180, 220));
        panel.add(cita, gbc);
        // la frase emblematica de la serie, toque nostalgico al final

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 8, 0);
        JButton btnNuevo = new JButton("Nueva partida");
        btnNuevo.setBackground(COLOR_GREEN);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnNuevo.setFocusPainted(false);
        btnNuevo.addActionListener(e -> {
            dialogo.dispose();
            // cierra el dialogo de victoria
            new VentanaInicio().setVisible(true);
            // vuelve a la pantalla inicial pa jugar otra vez
            VentanaDuelo.this.dispose();
            // cierra esta ventana del duelo
            // VentanaDuelo.this es necesario pa referenciar la ventana exterior desde la lambda
        });
        panel.add(btnNuevo, gbc);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.pack();
        // ajusta el tamanio del dialogo al contenido
        dialogo.setLocationRelativeTo(this);
        // centra el dialogo respecto a la ventana del duelo
        dialogo.setVisible(true);
        // muestra el dialogo, y como es modal bloquea todo lo demas hasta que el usuario elija
    }
}