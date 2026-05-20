// Clase base para todas las cartas del juego
// aqui estan el nombre y el tipo general de cada carta
package model;

public abstract class Carta {

    // nombre que aparece en la carta
    private String nombre;

    public Carta(String nombre) {
        this.nombre = nombre;
    }

    // devuelve el nombre de la carta
    public String getNombre() {
        return nombre;
    }

    // cada carta dice si es monstruo magica o trampa
    public abstract String getTipo();
    // describe la carta en texto para mostrarla
    public abstract String toString();
}
