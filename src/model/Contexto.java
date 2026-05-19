// Contexto del duelo para pasar informacion entre cartas y jugadores
// aqui se guarda quien es el jugador activo el oponente y el campo
package model;

public class Contexto {

    // jugador que juega ahora mismo
    private Jugador jugActivo;
    // jugador rival en el duelo
    private Jugador oponente;
    // referencia al campo de batalla global
    private CampoBatalla campo;
    // monstruo que esta atacando actualmente
    private CartaMonstruo monstruoAtacante;

    public Contexto(Jugador jugActivo, Jugador oponente, CampoBatalla campo) {
        this.jugActivo = jugActivo;
        this.oponente = oponente;
        this.campo = campo;
        this.monstruoAtacante = null;
    }

    // devuelve el jugador que esta moviendo en este instante
    public Jugador getJugadorActivo() { return jugActivo; }
    // devuelve el jugador rival del duelo
    public Jugador getOponente()      { return oponente; }
    // devuelve el campo de batalla para consultar el estado global
    public CampoBatalla getCampo()    { return campo; }

    // devuelve el turno actual del campo
    public int getTurno() { return campo.getTurnoActual(); }

    public CartaMonstruo getMonstruoAtacante() { return monstruoAtacante; }

    // guarda el monstruo que esta atacando para las trampas de contraataque
    public void setMonstruoAtacante(CartaMonstruo monstruoAtacante) {
        this.monstruoAtacante = monstruoAtacante;
    }
}
