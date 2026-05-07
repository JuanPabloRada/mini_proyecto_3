import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CampoBatalla {
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugadorActivo;
    private boolean esPrimerTurno = true;
    private int turnoActual = 0;

    public CampoBatalla(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public void iniciarDuelo() {
        repartirCartasIniciales();
        // randomiza quien empieza el duelo
        Random random = new Random();
        jugadorActivo = random.nextBoolean() ? jugador1 : jugador2;
    }

    private void repartirCartasIniciales() {
        // se llama a la fabrica una sola vez y se mezcla todo junto antes de repartir
        List<Carta> mazoCompleto = FabricaDeCartas.crearMazoCompleto();
        Collections.shuffle(mazoCompleto);

        // se divide la lista ya mezclada en dos mitades, una para cada jugador
        List<Carta> mazo1 = new ArrayList<>(mazoCompleto.subList(0, 25));
        List<Carta> mazo2 = new ArrayList<>(mazoCompleto.subList(25, 50));

        jugador1.getMazo().agregarCartas(mazo1);
        jugador2.getMazo().agregarCartas(mazo2);

        // se reparten 5 cartas iniciales a la mano de cada jugador
        jugador1.getMano().addAll(jugador1.getMazo().repartir(5));
        jugador2.getMano().addAll(jugador2.getMazo().repartir(5));
    }

    /**
     * Prepara el turno del jugador activo (resetea flags, roba carta).
     * La GUI llama a este metodo antes de habilitar acciones.
     * Retorna un mensaje con lo que ocurrio al inicio del turno.
     */
    public String prepararTurno() {
        turnoActual++;
        jugadorActivo.resetTurno();

        StringBuilder log = new StringBuilder();
        log.append("=== TURNO ").append(turnoActual)
           .append(" : ").append(jugadorActivo.getNombre()).append(" ===\n");

        if (esPrimerTurno) {
            log.append("[Primer turno] ").append(jugadorActivo.getNombre())
               .append(" no roba carta y no puede atacar.\n");
            // se marca cada monstruo en campo para que no pueda atacar en el primer turno
            for (CartaMonstruo m : jugadorActivo.getCampo()) {
                m.marcarComoAtacado();
            }
        } else {
            // si se queda sin cartas en el mazo al robar, pierde el duelo
            if (!jugadorActivo.tieneCartasEnMazo()) {
                log.append(jugadorActivo.getNombre())
                   .append(" no tiene cartas en el mazo. ¡Pierde el duelo!\n");
                return log.toString();
            }
            jugadorActivo.robarCarta();
            log.append(jugadorActivo.getNombre()).append(" robó una carta.\n");

            // al iniciar el turno se quitan los boosts temporales de las cartas magicas del turno anterior
            for (CartaMonstruo m : jugadorActivo.getCampo()) {
                m.decrementarMejora();
            }
        }
        return log.toString();
    }

    public void terminarTurno() {
        // cambia al otro jugador y marca que ya no es el primer turno
        jugadorActivo = (jugadorActivo == jugador1) ? jugador2 : jugador1;
        esPrimerTurno = false;
    }

    // recibe las dos cartas involucradas y los dos jugadores para poder modificar sus campos y LP
    public String resolverCombate(CartaMonstruo atacante, CartaMonstruo defensor,
                                   Jugador jugActivo, Jugador oponente) {
        StringBuilder log = new StringBuilder();
        log.append(atacante.getNombre()).append(" ataca a ").append(defensor.getNombre()).append("!\n");

        int atkAtacante = atacante.getAtk();

        if (defensor.estaEnModoDefensa()) {
            int defDefensor = defensor.getDef();
            log.append(defensor.getNombre()).append(" está en MODO DEFENSA (DEF: ").append(defDefensor).append(").\n");
            if (atkAtacante > defDefensor) {
                eliminarMonstruo(defensor, oponente);
                log.append(defensor.getNombre()).append(" fue destruido en modo defensa.\n");
            } else {
                log.append("¡Ataque bloqueado! La defensa de ").append(defensor.getNombre()).append(" es demasiado alta.\n");
            }
        } else {
            // si el ATK del atacante es mayor, destruye al defensor y la diferencia va a los LP del oponente
            if (atkAtacante > defensor.getAtk()) {
                int danio = atkAtacante - defensor.getAtk();
                eliminarMonstruo(defensor, oponente);
                oponente.recibirDanio(danio);
                log.append(defensor.getNombre()).append(" destruido. ")
                   .append(oponente.getNombre()).append(" pierde ").append(danio).append(" LP.\n");

            } else if (atkAtacante == defensor.getAtk()) {
                // si hay empate, los dos monstruos se destruyen y nadie recibe daño
                eliminarMonstruo(defensor, oponente);
                eliminarMonstruo(atacante, jugActivo);
                log.append("¡Empate! Ambos monstruos fueron destruidos.\n");

            } else {
                // si el atacante pierde, la diferencia de ATK se le descuenta a sus propios LP
                int danio = defensor.getAtk() - atkAtacante;
                jugActivo.recibirDanio(danio);
                log.append(atacante.getNombre()).append(" fue repelido. ")
                   .append(jugActivo.getNombre()).append(" pierde ").append(danio).append(" LP.\n");
            }
        }

        // se marca el atacante para que no pueda volver a atacar este turno
        atacante.marcarComoAtacado();
        return log.toString();
    }

    public String ataqueDirecto(CartaMonstruo atacante, Jugador oponente) {
        oponente.recibirDanio(atacante.getAtk());
        atacante.marcarComoAtacado();
        return atacante.getNombre() + " ataca directamente a " + oponente.getNombre()
               + "! Pierde " + atacante.getAtk() + " LP.\n";
    }

    public void aplicarBoostAtk(Jugador j, short boost) {
        if (!j.getCampo().isEmpty()) {
            j.getCampo().get(0).aplicarBoostAtk(boost);
        }
    }

    public void aplicarBoostDef(Jugador j, short boost) {
        if (!j.getCampo().isEmpty()) {
            j.getCampo().get(0).aplicarBoostDef(boost);
        }
    }

    // busca el monstruo con menor ATK en el campo del oponente y lo destruye
    public void destruirMenorAtkOponente(Jugador jugActivo) {
        Jugador oponente = (jugActivo == jugador1) ? jugador2 : jugador1;
        if (oponente.getCampo().isEmpty()) return;
        CartaMonstruo menor = oponente.getCampo().get(0);
        for (CartaMonstruo m : oponente.getCampo()) {
            if (m.getAtk() < menor.getAtk()) menor = m;
        }
        eliminarMonstruo(menor, oponente);
    }

    public void eliminarMonstruo(CartaMonstruo m, Jugador j) {
        j.getCampo().remove(m);
    }

    public boolean hayGanador() {
        return jugador1.getLp() <= 0
            || jugador2.getLp() <= 0
            || !jugador1.tieneCartasEnMazo()
            || !jugador2.tieneCartasEnMazo();
    }

    public Jugador getGanador() {
        if (jugador1.getLp() <= 0 || !jugador1.tieneCartasEnMazo()) return jugador2;
        if (jugador2.getLp() <= 0 || !jugador2.tieneCartasEnMazo()) return jugador1;
        return null;
    }

    public Jugador getJugadorActivo() { return jugadorActivo; }

    public Jugador getOponente() {
        return (jugadorActivo == jugador1) ? jugador2 : jugador1;
    }

    public Jugador getJugador1() { return jugador1; }

    public Jugador getJugador2() { return jugador2; }

    public int getTurnoActual() { return turnoActual; }

    public boolean isEsPrimerTurno() { return esPrimerTurno; }
}
