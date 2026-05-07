import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// o sea aqui estamos importando todo lo necesario pa que funcione la ventana
// javax.swing.* trae los componentes graficos tipo botones, labels, campos de texto y eso
// java.awt.* es pa los colores, fuentes y los layouts
// java.awt.event.* es pa manejar cuando el usuario hace clic o presiona Enter
// sin estos imports no arranca nada, son obligatorios

/**
 * Pantalla inicial: pide los nombres de los dos duelistas y lanza el duelo.
 */
public class VentanaInicio extends JFrame {
// aqui la clase hereda de JFrame, o sea VentanaInicio ES una ventana
// no es que "tiene" una ventana, sino que ella misma es la ventana, eso es herencia

    private JTextField campoNombre1;
    private JTextField campoNombre2;
    // estos son los cajoncitos de texto donde los jugadores escriben sus nombres
    // se declaran aca arriba (como atributos) pa que todos los metodos de la clase puedan usarlos
    // si los declarabamos dentro del constructor no los podriamos usar en iniciarDuelo() por ejemplo

    public VentanaInicio() {
        setTitle("Yu-Gi-Oh! — Duelo");
        // esto pone el texto que aparece en la barra de titulo de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // esto hace que cuando cierres la ventana se cierre toda la aplicacion
        // si no pones esto la ventana se cierra pero el programa sigue corriendo en segundo plano
        setResizable(false);
        // bloquea que el usuario pueda cambiar el tamanio de la ventana arrastrando los bordes
        // pa que no se rompa el diseno

        // ── Panel principal ────────────────────────────────────────────────────
        JPanel panel = new JPanel(new GridBagLayout());
        // GridBagLayout es el layout mas flexible de Java pero tambien el mas complicado
        // basicamente te deja poner componentes en una cuadricula y controlar exactamente donde va cada cosa
        panel.setBackground(new Color(15, 15, 40));
        // el color de fondo es RGB (15,15,40), que es un azul marino casi negro, tipo oscuro y epico
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        // esto le mete padding interno al panel: 30px arriba/abajo y 40px a los lados
        // pa que los elementos no queden pegados a los bordes de la ventana

        GridBagConstraints gbc = new GridBagConstraints();
        // este objeto controla como se posiciona cada componente dentro del GridBagLayout
        // es como el "configurador" de posiciones, medio molesto pero necesario
        gbc.insets = new Insets(8, 8, 8, 8);
        // Insets es el espacio entre componentes, tipo el margin en CSS si lo piensas asi
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // esto hace que los componentes se estiren horizontalmente pa llenar el espacio disponible

        // Título
        JLabel titulo = new JLabel("⚔  DUELO DE YU-GI-OH!  ⚔", SwingConstants.CENTER);
        // JLabel es un texto no editable, SwingConstants.CENTER lo centra horizontalmente
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(new Color(255, 215, 0));
        // color dorado, el tipico de Yu-Gi-Oh, RGB (255, 215, 0)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        // gridx=0 y gridy=0 es la primera fila primera columna
        // gridwidth=2 hace que el label ocupe dos columnas pa que quede centrado en todo el ancho
        panel.add(titulo, gbc);

        JLabel subtitulo = new JLabel("\"Confía en el corazón de las cartas\"", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Serif", Font.ITALIC, 13));
        // fuente cursiva y mas pequeña pa la frasecita iconica
        subtitulo.setForeground(new Color(180, 180, 220));
        // color lavanda suavecito, contrasta bien con el fondo oscuro
        gbc.gridy = 1;
        // bajamos a la siguiente fila de la cuadricula
        panel.add(subtitulo, gbc);

        JSeparator sep = new JSeparator();
        // una linea horizontal decorativa pa separar el encabezado de los campos
        sep.setForeground(new Color(255, 215, 0));
        // dorada tambien pa que combine con el titulo
        gbc.gridy = 2; gbc.insets = new Insets(4, 0, 16, 0);
        // temporalmente cambian los margenes pa que haya mas espacio debajo del separador
        panel.add(sep, gbc);
        gbc.insets = new Insets(8, 8, 8, 8);
        // y luego vuelven a los margenes normales pa los siguientes elementos

        // Jugador 1
        gbc.gridwidth = 1; gbc.gridy = 3; gbc.gridx = 0;
        // ahora gridwidth vuelve a 1 pa que cada cosa ocupe solo su columna
        JLabel lbl1 = new JLabel("Jugador 1:");
        lbl1.setForeground(Color.WHITE);
        lbl1.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lbl1, gbc);
        // el label "Jugador 1:" va en la columna izquierda (gridx=0)

        campoNombre1 = new JTextField("Yugi", 16);
        // el campo de texto ya viene con "Yugi" como valor por defecto, bonito detalle
        // el 16 es el ancho en columnas de caracteres
        estilizarCampo(campoNombre1);
        // llamamos al metodo de abajo pa aplicarle el estilo oscuro y dorado al campo
        gbc.gridx = 1;
        // el campo va en la columna derecha (gridx=1)
        panel.add(campoNombre1, gbc);

        // Jugador 2
        gbc.gridy = 4; gbc.gridx = 0;
        // bajamos una fila mas pa el jugador 2
        JLabel lbl2 = new JLabel("Jugador 2:");
        lbl2.setForeground(Color.WHITE);
        lbl2.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lbl2, gbc);

        campoNombre2 = new JTextField("Kaiba", 16);
        // "Kaiba" como valor por defecto pa el rival, perfecto
        estilizarCampo(campoNombre2);
        gbc.gridx = 1;
        panel.add(campoNombre2, gbc);

        // Botón Iniciar
        JButton btnIniciar = new JButton("¡INICIAR DUELO!");
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnIniciar.setBackground(new Color(180, 0, 0));
        // fondo rojo oscuro, tipo de peligro, es el boton de accion principal
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        // esto quita el rectangulito feo que aparece alrededor del texto cuando el boton tiene foco
        btnIniciar.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        // borde dorado de 2px pa que combine con el resto del diseno
        btnIniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // esto cambia el cursor a una manito cuando pasas el mouse por encima, detallito UX
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        // mas margen arriba del boton pa que no quede tan pegado a los campos
        panel.add(btnIniciar, gbc);

        btnIniciar.addActionListener(e -> iniciarDuelo());
        // aqui conectamos el boton con el metodo iniciarDuelo()
        // cuando el usuario haga clic, se llama ese metodo, eso es un ActionListener con lambda

        // Permitir Enter en los campos
        ActionListener enterAction = e -> iniciarDuelo();
        campoNombre1.addActionListener(enterAction);
        campoNombre2.addActionListener(enterAction);
        // esto hace que si el usuario presiona Enter estando en cualquiera de los campos
        // tambien se dispara iniciarDuelo(), pa no tener que usar el mouse obligatoriamente

        setContentPane(panel);
        // le decimos a la ventana que use nuestro panel como contenido principal
        pack();
        // esto ajusta automaticamente el tamanio de la ventana pa que quepan todos los componentes
        setLocationRelativeTo(null);
        // centra la ventana en la pantalla, si le pasas null se centra respecto al monitor
    }

    private void estilizarCampo(JTextField campo) {
        // este metodo recibe un campo de texto y le aplica el estilo oscuro/dorado
        // lo hicieron como metodo aparte pa no repetir el mismo codigo dos veces (una por cada campo)
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setBackground(new Color(30, 30, 60));
        // fondo azul marino oscuro, combina con el panel principal
        campo.setForeground(Color.WHITE);
        // texto en blanco pa que se vea sobre el fondo oscuro
        campo.setCaretColor(Color.WHITE);
        // el cursor de escritura tambien en blanco, pa que se vea donde estas escribiendo
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        // borde dorado por fuera + padding interno pa que el texto no quede pegado al borde
        // createCompoundBorder combina dos bordes: el exterior decorativo y el interior de espacio
    }

    private void iniciarDuelo() {
        String n1 = campoNombre1.getText().trim();
        String n2 = campoNombre2.getText().trim();
        // .getText() obtiene lo que escribio el usuario y .trim() quita espacios al inicio y al final
        if (n1.isEmpty()) n1 = "Jugador 1";
        if (n2.isEmpty()) n2 = "Jugador 2";
        // si el usuario borro el nombre y dejo el campo vacio, se le asigna un nombre por defecto
        // pa que no entren al duelo como nombres vacios

        Jugador j1 = new Jugador(n1);
        Jugador j2 = new Jugador(n2);
        // se crean los dos objetos Jugador con los nombres ingresados
        CampoBatalla campo = new CampoBatalla(j1, j2);
        // se crea el campo de batalla pasandole los dos jugadores
        campo.iniciarDuelo();
        // se inicializa la logica del duelo (seguramente reparte cartas o algo asi)

        VentanaDuelo ventanaDuelo = new VentanaDuelo(campo);
        ventanaDuelo.setVisible(true);
        // se crea y se muestra la ventana del duelo como tal
        this.dispose();
        // y esta ventana de inicio se destruye pa liberar memoria, ya no se necesita
    }
}