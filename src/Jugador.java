import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private int lp = 8000;
    private boolean yaJugoCartaEsteTurno = false;
    private boolean yaAtacoEsteTurno = false;
    private boolean bloqueadoProximoTurno = false; // flag que activa DestinoInexorable para bloquear el siguiente turno
    private List<Carta> mano;
    private Mazo mazo;
    private List<CartaMonstruo> campo;
    private List<CartaTrampa> zonaTrampas; // trampas colocadas boca abajo esperando activarse

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mazo = new Mazo(false);
        this.lp = 8000;
        this.mano = new ArrayList<>();
        this.campo = new ArrayList<>();
        this.zonaTrampas = new ArrayList<>();
        this.yaJugoCartaEsteTurno = false;
        this.yaAtacoEsteTurno = false;
        this.bloqueadoProximoTurno = false;
    }

    public String getNombre() { return nombre; }

    public int getLp() { return lp; }

    public void setLp(int lp) {
        this.lp = lp;
        if (this.lp < 0) this.lp = 0;
    }

    public List<Carta> getMano() { return mano; }

    public List<CartaMonstruo> getCampo() { return campo; }

    public Mazo getMazo() { return mazo; }

    public void setMazo(Mazo mazo) { this.mazo = mazo; }

    public List<CartaTrampa> getZonaTrampas() { return zonaTrampas; }

    public void robarCarta() {
        if (mazo != null) {
            Carta c = mazo.robar();
            if (c != null) mano.add(c);
        }
    }

    public void recibirDanio(int pts) {
        lp -= pts;
        if (lp < 0) lp = 0;
    }

    public void curarDanio(int pts) {
        lp += pts;
    }

    public boolean tieneMonstruosEnCampo() { return !campo.isEmpty(); }

    public boolean tieneCartasEnMazo() { return mazo != null && !mazo.estaVacio(); }

    public boolean puedeJugarCarta() { return !yaJugoCartaEsteTurno; }

    // llamado por DestinoInexorable: el proximo turno el jugador no puede jugar cartas
    public void bloquearJugarCartaProximoTurno() {
        bloqueadoProximoTurno = true;
    }

    public boolean isBloqueadoProximoTurno() { return bloqueadoProximoTurno; }

    public void resetTurno() {
        // si estaba bloqueado, aplica el bloqueo este turno y lo consume para que no se repita
        if (bloqueadoProximoTurno) {
            yaJugoCartaEsteTurno = true;
            bloqueadoProximoTurno = false;
        } else {
            yaJugoCartaEsteTurno = false;
        }
        yaAtacoEsteTurno = false;
        for (CartaMonstruo m : campo) {
            m.setPuedeAtacar(true);
        }
    }

    /**
     * Intenta jugar la carta en la posicion indicada de la mano.
     * Para monstruos nivel > 4 se requiere un sacrificio; el indice del
     * monstruo a sacrificar se pasa en indiceSacrificio (-1 si no se necesita).
     * Retorna true si la carta fue jugada con exito.
     */
    public boolean jugarCarta(int indice, Contexto ctx, int indiceSacrificio) {
        if (indice < 0 || indice >= mano.size()) return false;
        if (yaJugoCartaEsteTurno) return false;

        Carta carta = mano.get(indice);

        if (carta.getTipo().equals("MONSTRUO")) {
            // se hace el casteo para poder acceder a los metodos y atributos propios de CartaMonstruo
            CartaMonstruo monstruo = (CartaMonstruo) carta;

            // los monstruos de nivel mayor a 4 necesitan que se sacrifique uno del campo primero
            if (monstruo.getnivelCarta() > 4) {
                if (campo.isEmpty()) return false;
                if (indiceSacrificio < 0 || indiceSacrificio >= campo.size()) return false;
                CartaMonstruo sacrificado = campo.remove(indiceSacrificio);
                System.out.println(">>> " + nombre + " sacrificó a " + sacrificado.getNombre()
                        + " para invocar " + monstruo.getNombre() + ".");
            }

            campo.add(monstruo);
            mano.remove(indice);
            yaJugoCartaEsteTurno = true;
            // los monstruos invocados en el primer turno no pueden atacar
            monstruo.setPuedeAtacar(!ctx.getCampo().isEsPrimerTurno());
            return true;

        } else if (carta.getTipo().equals("MAGICA")) {
            // se aprovecha que las magicas implementan Activable para poder llamar directamente a activar()
            if (carta instanceof Activable) {
                // el casteo es necesario para acceder al metodo activar() de la interfaz
                ((Activable) carta).activar(ctx);
                mano.remove(indice);
                yaJugoCartaEsteTurno = true;
                return true;
            }

        } else if (carta.getTipo().equals("TRAMPA")) {
            // colocar la trampa boca abajo en la zona de trampas, no se activa todavia
            CartaTrampa trampa = (CartaTrampa) carta;
            zonaTrampas.add(trampa);
            mano.remove(indice);
            yaJugoCartaEsteTurno = true;
            System.out.println(">>> " + nombre + " colocó una trampa boca abajo.");
            return true;
        }

        return false;
    }

    // activa una trampa de la zona de trampas, primero verifica si se puede activar en el contexto actual
    public boolean activarTrampa(int indiceTrampa, Contexto ctx) {
        if (indiceTrampa < 0 || indiceTrampa >= zonaTrampas.size()) return false;
        CartaTrampa trampa = zonaTrampas.get(indiceTrampa);
        if (!trampa.puedoActivarme(ctx)) return false;
        trampa.activar(ctx);
        // se remueve de la zona de trampas porque ya fue usada
        zonaTrampas.remove(indiceTrampa);
        return true;
    }

    // recorre todas las trampas y retorna true si al menos una puede activarse ahora
    public boolean hayTrampaActivable(Contexto ctx) {
        for (CartaTrampa t : zonaTrampas) {
            if (t.puedoActivarme(ctx)) return true;
        }
        return false;
    }

    public boolean isYaAtacoEsteTurno() { return yaAtacoEsteTurno; }

    public void setYaAtacoEsteTurno(boolean v) { yaAtacoEsteTurno = v; }

    public boolean isYaJugoCartaEsteTurno() { return yaJugoCartaEsteTurno; }
}
