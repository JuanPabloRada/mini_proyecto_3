// Carta de monstruo para tener niveles fuerza y defensa
// los monstruos pueden atacar defenderse y cambiar de posicion
package model;

public class CartaMonstruo extends Carta {

    // nivel que sirve para saber si necesita sacrificios
    private byte nivelCarta;
    // ataque base de la carta
    private short atk;
    // defensa base de la carta
    private short def;
    // bonificacion temporal de ataque
    private short boostAtk;
    // bonificacion temporal de defensa
    private short boostDef;
    // si puede atacar en este turno
    private boolean puedeAtacar;
    // si esta en modo defensa o ataque
    private boolean enModoDefensa;

    public CartaMonstruo(String nombre, byte nivelCarta, short atk, short def) {
        super(nombre);
        this.nivelCarta = nivelCarta;
        this.atk = atk;
        this.def = def;
        this.boostAtk = 0;
        this.boostDef = 0;
        this.puedeAtacar = false;
        this.enModoDefensa = false;
    }

    // devuelve el nivel de la carta
    public int getnivelCarta() { return nivelCarta; }

    // devuelve ataque con boosts aplicados
    public int getAtk() { return atk + boostAtk; }

    // devuelve defensa con boosts aplicados
    public int getDef() { return def + boostDef; }

    // comprueba si el monstruo puede atacar este turno
    public boolean puedeAtacar() { return puedeAtacar; }

    // fija si el monstruo puede atacar
    public void setPuedeAtacar(boolean puedeAtacar) { this.puedeAtacar = puedeAtacar; }

    // devuelve si el monstruo esta en modo defensa
    public boolean estaEnModoDefensa() { return enModoDefensa; }

    // cambia la carta de modo ataque a defensa o viceversa
    public void cambiarPosicion() {
        this.enModoDefensa = !this.enModoDefensa;
    }

    // fija el boost de ataque actual
    // fija el boost de ataque actual
    public void aplicarBoostAtk(short incremento) { boostAtk = incremento; }

    // fija el boost de defensa actual
    public void aplicarBoostDef(short incremento) { boostDef = incremento; }

    // elimina los boosts temporales del monstruo
    public void resetBoosts() {
        boostAtk = 0;
        boostDef = 0;
    }

    // reinicia el ataque y borra boosts del turno
    public void reiniciarAtaques() {
        puedeAtacar = true;
        resetBoosts();
    }

    // marca que ya ataco para que no pueda atacar otra vez
    public void marcarComoAtacado() { puedeAtacar = false; }

    // se llama al inicio del turno para poder atacar otra vez
    public void resetTurno() { puedeAtacar = true; }

    // quita cualquier mejora de ataque y defensa al final del turno
    public void decrementarMejora() {
        boostAtk = 0;
        boostDef = 0;
    }

    @Override
    public String getTipo() { return "MONSTRUO"; }

    @Override
    public String toString() {
        String modo = enModoDefensa ? "[DEF]" : "[ATK]";
        return modo + " " + getNombre()
            + " Lv" + nivelCarta
            + " ATK:" + getAtk()
            + " DEF:" + getDef();
    }
}
