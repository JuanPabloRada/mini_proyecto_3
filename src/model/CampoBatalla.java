// Esta clase es el lugar donde se juega el duelo
// controla quien ataca quien roba y como avanza el turno
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CampoBatalla {

    // jugador 1 del duelo
    private Jugador jugador1;
    // jugador 2 del duelo
    private Jugador jugador2;
    // jugador que esta jugando ahora
    private Jugador jugadorActivo;
    // indica si es el primer turno del juego
    private boolean esPrimerTurno = true;
    // numero de turno actual
    private int turnoActual = 0;

    public CampoBatalla(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    // arranca el duelo reparte cartas y elige quien comienza
    public void iniciarDuelo() {
        repartirCartasIniciales();
        Random random = new Random();
        jugadorActivo = random.nextBoolean() ? jugador1 : jugador2;
    }

    // reparte 25 cartas a cada jugador y les da mano inicial
    private void repartirCartasIniciales() {
        List<Carta> mazoCompleto = FabricaDeCartas.crearMazoCompleto();
        Collections.shuffle(mazoCompleto);

        // dividir el mazo completo en dos mazos de 25 cartas cada uno
        List<Carta> mazo1 = new ArrayList<>(mazoCompleto.subList(0, 25));
        List<Carta> mazo2 = new ArrayList<>(mazoCompleto.subList(25, 50));

        // llenar los mazos de los jugadores con las cartas asignadas
        jugador1.getMazo().agregarCartas(mazo1);
        jugador2.getMazo().agregarCartas(mazo2);

        // dar la mano inicial de 5 cartas a cada jugador
        jugador1.getMano().addAll(jugador1.getMazo().repartir(5));
        jugador2.getMano().addAll(jugador2.getMazo().repartir(5));
    }

    // prepara el turno actual roba carta o aplica primera regla del juego
    public String prepararTurno() {
        turnoActual++;
        jugadorActivo.resetTurno();

        StringBuilder log = new StringBuilder();
        log.append("=== TURNO ").append(turnoActual)
           .append(" : ").append(jugadorActivo.getNombre()).append(" ===\n");

        if (esPrimerTurno) {
            log.append("[Primer turno] ").append(jugadorActivo.getNombre())
               .append(" no roba carta y no puede atacar.\n");
            // en el primer turno los monstruos no pueden atacar
            for (CartaMonstruo m : jugadorActivo.getCampo()) {
                m.marcarComoAtacado();
            }
        } else {
            if (!jugadorActivo.tieneCartasEnMazo()) {
                log.append(jugadorActivo.getNombre())
                   .append(" no tiene cartas en el mazo. ¡Pierde el duelo!\n");
                return log.toString();
            }
            jugadorActivo.robarCarta();
            log.append(jugadorActivo.getNombre()).append(" robó una carta.\n");

            // quitar mejoras temporales de los monstruos al iniciar el turno
            for (CartaMonstruo m : jugadorActivo.getCampo()) {
                m.decrementarMejora();
            }
        }
        return log.toString();
    }

    // cambia el jugador activo para terminar el turno
    public void terminarTurno() {
        jugadorActivo = (jugadorActivo == jugador1) ? jugador2 : jugador1;
        esPrimerTurno = false;
    }

    // resuelve el combate entre un monstruo atacante y uno defensor
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
            if (atkAtacante > defensor.getAtk()) {
                int danio = atkAtacante - defensor.getAtk();
                eliminarMonstruo(defensor, oponente);
                oponente.recibirDanio(danio);
                log.append(defensor.getNombre()).append(" destruido. ")
                   .append(oponente.getNombre()).append(" pierde ").append(danio).append(" LP.\n");
            } else if (atkAtacante == defensor.getAtk()) {
                eliminarMonstruo(defensor, oponente);
                eliminarMonstruo(atacante, jugActivo);
                log.append("¡Empate! Ambos monstruos fueron destruidos.\n");
            } else {
                int danio = defensor.getAtk() - atkAtacante;
                jugActivo.recibirDanio(danio);
                log.append(atacante.getNombre()).append(" fue repelido. ")
                   .append(jugActivo.getNombre()).append(" pierde ").append(danio).append(" LP.\n");
            }
        }

        atacante.marcarComoAtacado();
        return log.toString();
    }

    // ataca al jugador sin monstruos en campo
    public String ataqueDirecto(CartaMonstruo atacante, Jugador oponente) {
        oponente.recibirDanio(atacante.getAtk());
        atacante.marcarComoAtacado();
        return atacante.getNombre() + " ataca directamente a " + oponente.getNombre()
               + "! Pierde " + atacante.getAtk() + " LP.\n";
    }

    // da un boost de ataque al primer monstruo en el campo
    // aplica boost de ataque al primer monstruo del jugador
    public void aplicarBoostAtk(Jugador j, short boost) {
        if (!j.getCampo().isEmpty()) j.getCampo().get(0).aplicarBoostAtk(boost);
    }

    // aplica boost de defensa al primer monstruo del jugador
    public void aplicarBoostDef(Jugador j, short boost) {
        if (!j.getCampo().isEmpty()) j.getCampo().get(0).aplicarBoostDef(boost);
    }

    public void destruirMenorAtkOponente(Jugador jugActivo) {
        Jugador oponente = (jugActivo == jugador1) ? jugador2 : jugador1;
        if (oponente.getCampo().isEmpty()) return;
        // buscar el monstruo mas debil en ataque del oponente
        CartaMonstruo menor = oponente.getCampo().get(0);
        for (CartaMonstruo m : oponente.getCampo()) {
            if (m.getAtk() < menor.getAtk()) menor = m;
        }
        eliminarMonstruo(menor, oponente);
    }

    public void eliminarMonstruo(CartaMonstruo m, Jugador j) {
        j.getCampo().remove(m);
    }

    // revisa si alguno de los jugadores ya perdio por LP o por no tener mazo
    public boolean hayGanador() {
        return jugador1.getLp() <= 0
            || jugador2.getLp() <= 0
            || !jugador1.tieneCartasEnMazo()
            || !jugador2.tieneCartasEnMazo();
    }

    // devuelve quien gano el duelo segun las condiciones de victoria
    public Jugador getGanador() {
        if (jugador1.getLp() <= 0 || !jugador1.tieneCartasEnMazo()) return jugador2;
        if (jugador2.getLp() <= 0 || !jugador2.tieneCartasEnMazo()) return jugador1;
        return null;
    }

    public Jugador getJugadorActivo() { return jugadorActivo; }

    // devuelve el jugador que no esta activo ahora
    public Jugador getOponente() {
        return (jugadorActivo == jugador1) ? jugador2 : jugador1;
    }

    public Jugador getJugador1()    { return jugador1; }
    public Jugador getJugador2()    { return jugador2; }
    public int getTurnoActual()     { return turnoActual; }
    public boolean isEsPrimerTurno(){ return esPrimerTurno; }
}
