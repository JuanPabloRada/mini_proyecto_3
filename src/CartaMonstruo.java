public class CartaMonstruo extends Carta {

    private byte nivelCarta;
    private short atk;
    private short def;
    // boostAtk y boostDef guardan el incremento temporal que dan las cartas mágicas
    private short boostAtk;
    private short boostDef;
    private boolean puedeAtacar;
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

    public int getnivelCarta() {
        return nivelCarta;
    }

    // retorna el ataque real sumando el boost temporal
    public int getAtk() {
        return atk + boostAtk;
    }

    // retorna la defensa real sumando el boost temporal
    public int getDef() {
        return def + boostDef;
    }

    public boolean puedeAtacar() {
        return puedeAtacar;
    }

    public void setPuedeAtacar(boolean puedeAtacar) {
        this.puedeAtacar = puedeAtacar;
    }

    public boolean estaEnModoDefensa() {
        return enModoDefensa;
    }

    public void cambiarPosicion() {
        // si era true pasa a false y viceversa
        this.enModoDefensa = !this.enModoDefensa;
        // condicional comprimido para imprimir en cual de los dos modos quedo
        String modo = enModoDefensa ? "DEFENSA" : "ATAQUE";
        System.out.println(">>> " + getNombre() + " cambió a modo " + modo + ".");
    }

    public void aplicarBoostAtk(short incremento) {
        boostAtk = incremento;
    }

    public void aplicarBoostDef(short incremento) {
        boostDef = incremento;
    }

    public void resetBoosts() {
        boostAtk = 0;
        boostDef = 0;
    }

    public void reiniciarAtaques() {
        puedeAtacar = true;
        resetBoosts();
    }

    public void marcarComoAtacado() {
        puedeAtacar = false;
    }

    public void resetTurno() {
        puedeAtacar = true;
    }

    public void decrementarMejora() {
        boostAtk = 0;
        boostDef = 0;
    }

    @Override
    public String getTipo() {
        return "MONSTRUO";
    }

    @Override
    public String toString() {
        // condicional comprimido para mostrar el modo actual de la carta
        String modo = enModoDefensa ? "[DEF]" : "[ATK]";
        return modo + " " + getNombre()
            + " Lv" + nivelCarta
            + " ATK:" + getAtk()
            + " DEF:" + getDef();
    }
}
