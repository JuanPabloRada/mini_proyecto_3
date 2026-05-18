// Clase base para cartas trampa del juego
// estas cartas se colocan y se activan cuando se cumple la condicion
package model;

public abstract class CartaTrampa extends Carta implements Activable {

    // descripcion del efecto de la trampa
    private String descripcion;
    // si la trampa ya se activo o no
    private boolean activada;

    public CartaTrampa(String nombre, String descripcion) {
        super(nombre);
        this.descripcion = descripcion;
        this.activada = false;
    }

    // checa si la carta puede activarse en el momento actual
    public abstract boolean puedoActivarme(Contexto ctx);

    // efecto que ocurre cuando la carta se usa
    @Override
    public abstract void activar(Contexto ctx);

    // devuelve la descripcion que muestra la carta en la UI
    public String getDescripcion() { return descripcion; }
    // revisa si la trampa ya se ha activado antes
    public boolean isActivada()    { return activada; }
    // marca la trampa como activada o no
    public void setActivada(boolean activada) { this.activada = activada; }

    @Override
    public String getTipo() { return "TRAMPA"; }

    @Override
    public String toString() {
        return "[TRAMPA] " + getNombre() + ": " + descripcion;
    }
}
