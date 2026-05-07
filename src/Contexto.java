// agrupa toda la informacion relevante del estado actual del duelo para pasarsela a las cartas al activarse
public class Contexto {

    private Jugador jugActivo;
    private Jugador oponente;
    private CampoBatalla campo;

    // solo se asigna durante la fase de respuesta de trampas en un ataque, fuera de ahi es null
    private CartaMonstruo monstruoAtacante;

    public Contexto(Jugador jugActivo, Jugador oponente, CampoBatalla campo) {
        this.jugActivo = jugActivo;
        this.oponente = oponente;
        this.campo = campo;
        this.monstruoAtacante = null;
    }

    public Jugador getJugadorActivo() {
        return jugActivo;
    }

    public Jugador getOponente() {
        return oponente;
    }

    public CampoBatalla getCampo() {
        return campo;
    }

    public int getTurno() {
        return campo.getTurnoActual();
    }

    public CartaMonstruo getMonstruoAtacante() {
        return monstruoAtacante;
    }

    // se llama antes de ofrecer la respuesta de trampas para que sepan quien esta atacando
    public void setMonstruoAtacante(CartaMonstruo monstruoAtacante) {
        this.monstruoAtacante = monstruoAtacante;
    }
}
