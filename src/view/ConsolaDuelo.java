package view;

import controller.DuelController;
import model.*;

import java.util.List;
import java.util.Scanner;

/*ConsolaDuelo — Vista del duelo para modo TERMINAL.
 */
public class ConsolaDuelo implements IDuelView {

    private final DuelController controller;
    private final Scanner scanner;

    // Banderita para saber cuándo terminar el bucle de juego
    private boolean dueloTerminado = false;

    
    public ConsolaDuelo(DuelController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner    = scanner;
    }

    
    /*Inicia el duelo en modo consola.
Llama a iniciarPrimerTurno() en el controlador y luego
 ejecuta el bucle de menú hasta que haya un ganador. */
    public void iniciarJuego() {
        controller.iniciarPrimerTurno();   // dibuja estado inicial

        // Bucle principal de turnos
        while (!dueloTerminado) {
            mostrarMenuTurno();
        }
    }

    
    private void mostrarMenuTurno() {
        if (dueloTerminado) return;

        CampoBatalla campo = controller.getCampo();
        Jugador activo = campo.getJugadorActivo();

        System.out.println();
        System.out.println("┌────────────────────────────────────────┐");
        System.out.printf( "│  Turno %-3d  ─  %-26s│%n",
            campo.getTurnoActual(), activo.getNombre().toUpperCase());
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│  1. Jugar Carta                        │");
        System.out.println("│  2. Atacar                             │");
        System.out.println("│  3. Activar Trampa                     │");
        System.out.println("│  4. Cambiar Posición de Monstruo       │");
        System.out.println("│  5. Ver estado del campo               │");
        System.out.println("│  6. Terminar Turno                     │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("  Opción: ");

        String linea = scanner.nextLine().trim();
        int opcion;
        try {
            opcion = Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Opción no válida, intenta de nuevo.");
            return;
        }

        switch (opcion) {
            case 1 -> controller.accionJugarCarta();
            case 2 -> controller.accionAtacar();
            case 3 -> controller.accionActivarTrampa();
            case 4 -> controller.accionCambiarPosicion();
            case 5 -> actualizarUI();   // muestra el estado sin consumir turno
            case 6 -> controller.accionTerminarTurno();
            default -> System.out.println("  [!] Opción no válida.");
        }
    }

   
    @Override
    public void agregarLog(String texto) {
        if (texto == null || texto.isBlank()) return;
        // Imprimir línea a línea para respetar los saltos del modelo
        for (String linea : texto.split("\n")) {
            if (!linea.isBlank()) System.out.println("  [>>] " + linea.trim());
        }
    }

   
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

    /* Imprime una fila del estado de un jugador de forma compacta. */
    private void imprimirEstadoJugador(String etiqueta, Jugador j, String extra) {
        System.out.printf("  ║ %s %s%s%n", etiqueta, j.getNombre(), extra);
        System.out.printf("  ║   LP: %-5d  Mano: %d  Mazo: %d  Trampas: %d%n",
            j.getLp(),
            j.getMano().size(),
            j.getMazo().tamano(),
            j.getZonaTrampas().size());
        System.out.println("  ║   Campo: " + describirCampo(j.getCampo()));
    }

    /* Convierte la lista de monstruos en una cadena legible de texto. */
    private String describirCampo(List<CartaMonstruo> monstruos) {
        if (monstruos.isEmpty()) return "(vacío)";
        StringBuilder sb = new StringBuilder();
        for (CartaMonstruo m : monstruos) {
            sb.append("[")
              .append(m.getNombre())
              .append(" ATK:").append(m.getAtk())
              .append("/DEF:").append(m.getDef())
              .append(" ").append(m.estaEnModoDefensa() ? "DEF" : "ATK")
              .append(m.puedeAtacar() ? "✓" : "✗")
              .append("] ");
        }
        return sb.toString().trim();
    }
    /*
 Imprime la pantalla de ganador y marca el duelo como terminado
      para que el bucle en iniciarJuego() finalice.
     */
    @Override
    public void mostrarGanador() {
        dueloTerminado = true;   // detiene el bucle principal
        Jugador ganador = controller.getCampo().getGanador();
        String nombre = (ganador != null) ? ganador.getNombre() : "Nadie";

        actualizarUI();   // mostrar estado final antes del anuncio
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
        // Imprimir el mensaje respetando saltos de línea
        for (String linea : mensaje.split("\n")) {
            System.out.println("  " + linea);
        }
        System.out.println();
        for (int i = 0; i < opciones.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, opciones[i]);
        }
        System.out.print("  Elige (1-" + opciones.length + ", 0=cancelar): ");

        String linea = scanner.nextLine().trim();
        int eleccion;
        try {
            eleccion = Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Entrada no válida. Acción cancelada.");
            return -1;
        }

        if (eleccion == 0) return -1;                    // cancelación explícita
        if (eleccion < 1 || eleccion > opciones.length) {
            System.out.println("  [!] Número fuera de rango. Acción cancelada.");
            return -1;
        }
        return eleccion - 1;   /
    }

    /*espera que el jugador presione ENTER.
    para anunciar el cambio de turno entre jugadores que comparten el mismo teclado.
     */
    @Override
    public void mostrarMensaje(String titulo, String mensaje) {
        System.out.println();
        System.out.println("  ┌── " + titulo + " ──────────────────────────────");
        for (String linea : mensaje.split("\n")) {
            System.out.println("  │  " + linea);
        }
        System.out.println("  └───────────────────────────────────────────────");
        System.out.print("  (Presione ENTER para continuar...) ");
        scanner.nextLine();
    }
}