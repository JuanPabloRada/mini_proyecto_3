// Clase base para las cartas magicas
// estas cartas se activan y hacen cosas con el contexto del duelo
package model;

public abstract class CartaMagica extends Carta implements Activable {

    // texto con efecto o descripcion de la carta
    private String descripcion;

    public CartaMagica(String nombre, String descripcion) {
        super(nombre);
        this.descripcion = descripcion;
    }

    // este metodo lo implementan las cartas magicas con su efecto propio
    @Override
    public abstract void activar(Contexto ctx);

    // devuelve la descripcion del efecto de la carta
    public String getDescripcion() { return this.descripcion; }

    // devuelve el tipo fijo de esta carta para que se sepa que es magica
    @Override
    public String getTipo() { return "MAGICA"; }

    @Override
    public String toString() {
        return "[" + getTipo() + "] " + getNombre() + ": " + descripcion;
    }
}
