package view;

import controller.DuelController;
import model.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*Acá no hay lógica del juego, solo mostrar y preguntar, todo lo que tiene que ver con reglas va en el controlador y el modelo.
 */
public class ConsolaDuelo implements IDuelView {

    // Cuántas veces le damos al jugador para que escriba algo válido
    private static final int MAX_INTENTOS = 3;

    // Si el nombre de un monstruo es muy largo lo cortamos para que no rompa la tabla
    private static final int MAX_NOMBRE_MONSTRUO = 18;

    private final DuelController controller;
    private final Scanner scanner;

    // Cuando esto sea true, el bucle de juego para
    private boolean dueloTerminado = false;

    public ConsolaDuelo(DuelController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner    = scanner;
    }

    // ─── Inicio ──────────────────────────────────────────────────────────────

    // Arranca el juego: muestra el banner, prepara el primer turno y entra al bucle
    public void iniciarJuego() {
        mostrarBannerDuelo();
        controller.iniciarPrimerTurno();

        while (!dueloTerminado) {
            mostrarMenuTurno();
        }
    }

    // Pequeño banner para que se vea bien al iniciar el duelo
    private void mostrarBannerDuelo() {
        CampoBatalla campo = controller.getCampo();
        String n1 = campo.getJugadorActivo().getNombre();
        String n2 = campo.getOponente().getNombre();

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║          ⚔  DUELO YU-GI-OH!  ⚔             ║");
        System.out.println("  ╠══════════════════════════════════════════════╣");
        System.out.printf( "  ║  %-20s  VS  %-17s║%n",
            truncar(n1, 20), truncar(n2, 17));
        System.out.println("  ║        \"Confía en el corazón de las cartas\"  ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println();
    }

    
    // Muestra las opciones del turno y ejecuta lo que elija el jugador
    private void mostrarMenuTurno() {
        if (dueloTerminado) return;

        CampoBatalla campo = controller.getCampo();
        Jugador activo = campo.getJugadorActivo();

        // Mostramos los LP arriba del menú para que el jugador siempre los tenga a la vista
        String lpInfo = String.format("LP: %d  |  Mano: %d  |  Mazo: %d",
            activo.getLp(), activo.getMano().size(), activo.getMazo().tamano());

        System.out.println();
        System.out.println("┌────────────────────────────────────────┐");
        System.out.printf( "│  Turno %-3d  ─  %-26s│%n",
            campo.getTurnoActual(), activo.getNombre().toUpperCase());
        System.out.printf( "│  %-38s│%n", lpInfo);
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│  1. Jugar Carta                        │");
        System.out.println("│  2. Atacar                             │");
        System.out.println("│  3. Activar Trampa                     │");
        System.out.println("│  4. Cambiar Posición de Monstruo       │");
        System.out.println("│  5. Ver estado del campo               │");
        System.out.println("│  6. Terminar Turno                     │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("  Opción: ");

        String linea = leerLinea();
        if (linea == null) return;

        int opcion;
        try {
            opcion = Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Eso no es un número, intenta de nuevo.");
            return;
        }

        switch (opcion) {
            case 1 -> controller.accionJugarCarta();
            case 2 -> controller.accionAtacar();
            case 3 -> controller.accionActivarTrampa();
            case 4 -> controller.accionCambiarPosicion();
            case 5 -> actualizarUI();
            case 6 -> controller.accionTerminarTurno();
            default -> System.out.println("  [!] Opción fuera de rango (1-6), intenta de nuevo.");
        }
    }

    
    // Muestra un evento del juego en consola (equivale al log de texto de la GUI)
    @Override
    public void agregarLog(String texto) {
        if (texto == null || texto.isBlank()) return;
        for (String linea : texto.split("\n")) {
            if (!linea.isBlank()) System.out.println("  [>>] " + linea.trim());
        }
    }

    // Imprime el estado actual del campo de ambos jugadores
    @Override
    public void actualizarUI() {
        CampoBatalla campo = controller.getCampo();
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();

        System.out.println();
        System.out.println("  ╔══════════════ ESTADO DEL CAMPO ══════════════╗");
        imprimirEstadoJugador(" TU ZONA  ", activo,   " (activo)");
        System.out.println("  ║──────────────────────────────────────────────║");
        imprimirEstadoJugador(" OPONENTE ", oponente, "");
        System.out.println("  ╚══════════════════════════════════════════════╝");
    }

    // Una fila del estado: nombre, LP, mano, mazo, trampas y monstruos en campo
    private void imprimirEstadoJugador(String etiqueta, Jugador j, String extra) {
        System.out.printf("  ║ %s %s%s%n", etiqueta, j.getNombre(), extra);
        System.out.printf("  ║   LP: %-5d  Mano: %d  Mazo: %d  Trampas: %d%n",
            j.getLp(),
            j.getMano().size(),
            j.getMazo().tamano(),
            j.getZonaTrampas().size());
        System.out.println("  ║   Campo: " + describirCampo(j.getCampo()));
    }

    // Convierte los monstruos del campo en texto legible
    private String describirCampo(List<CartaMonstruo> monstruos) {
        if (monstruos.isEmpty()) return "(vacío)";
        StringBuilder sb = new StringBuilder();
        for (CartaMonstruo m : monstruos) {
            sb.append("[")
              .append(truncar(m.getNombre(), MAX_NOMBRE_MONSTRUO))
              .append(" ATK:").append(m.getAtk())
              .append("/DEF:").append(m.getDef())
              .append(" ").append(m.estaEnModoDefensa() ? "DEF" : "ATK")
              .append(m.puedeAtacar() ? "✓" : "✗")
              .append("] ");
        }
        return sb.toString().trim();
    }

    // Muestra quién ganó y frena el bucle del juego
    @Override
    public void mostrarGanador() {
        dueloTerminado = true;
        Jugador ganador = controller.getCampo().getGanador();
        String nombre = (ganador != null) ? ganador.getNombre() : "Nadie";

        actualizarUI();
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║            ¡¡ DUELO TERMINADO !!            ║");
        System.out.println("  ╠══════════════════════════════════════════════╣");
        System.out.printf( "  ║    ¡%s GANA EL DUELO!%n", nombre.toUpperCase());
        System.out.println("  ║  \"Confía en el corazón de las cartas\"        ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
    }

    @Override
    public int pedirSeleccion(String titulo, String mensaje, String[] opciones) {
        System.out.println();
        System.out.println("  ── " + titulo + " ──");
        for (String linea : mensaje.split("\n")) {
            System.out.println("  " + linea);
        }
        System.out.println();
        for (int i = 0; i < opciones.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, opciones[i]);
        }

        int intentos = 0;
        while (intentos < MAX_INTENTOS) {
            System.out.print("  Elige (1-" + opciones.length + ", 0=cancelar): ");

            // leerLinea() es la única parte con manejo especial — ver método abajo
            String linea = leerLinea();
            if (linea == null) return -1;

            int eleccion;
            try {
                eleccion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                intentos++;
                System.out.println("  [!] Eso no es un número. Intento " + intentos + " de " + MAX_INTENTOS + ".");
                continue;
            }

            if (eleccion == 0) return -1;

            if (eleccion >= 1 && eleccion <= opciones.length) {
                return eleccion - 1;
            }

            intentos++;
            System.out.println("  [!] Número fuera de rango. Intento " + intentos + " de " + MAX_INTENTOS + ".");
        }

        System.out.println("  [!] Demasiados intentos, se cancela la acción.");
        return -1;
    }

    // Muestra un aviso y espera que el jugador presione ENTER para seguir
    @Override
    public void mostrarMensaje(String titulo, String mensaje) {
        System.out.println();
        System.out.println("  ┌── " + titulo + " ──────────────────────────────");
        for (String linea : mensaje.split("\n")) {
            System.out.println("  │  " + linea);
        }
        System.out.println("  └───────────────────────────────────────────────");
        System.out.print("  (Presione ENTER para continuar...) ");
        leerLinea();
    }

    
    private String leerLinea() {
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("\n  [!] Se cerró la entrada. Terminando juego...");
            dueloTerminado = true;
            return null;
        }
    }

    // Corta el texto si es muy largo y le pone "…" al final
    private String truncar(String texto, int max) {
        if (texto.length() <= max) return texto;
        return texto.substring(0, max - 1) + "…";
    }
}