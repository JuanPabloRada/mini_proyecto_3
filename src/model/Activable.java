// Interfaz para cartas que se pueden activar en el juego
// sirve para magia o trampas que hacen algo cuando se usan
package model;

public interface Activable {
    // metodo que se llama cuando la carta se usa en el duelo
    void activar(Contexto ctx);
}
