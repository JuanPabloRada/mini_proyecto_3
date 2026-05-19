// carta magica que roba una carta a cambio de vida
// es una jugada de riesgo para ganar ventaja en mano
package model.cards.magic;

import model.*;

public class LlamadaDelAbismo extends CartaMagica {
    private short costo = 500;
    public LlamadaDelAbismo() { super("Llamada del Abismo", "Robas 1 carta, pero pierdes 500 LP."); }
    @Override
    public void activar(Contexto ctx) {
        // quita vida del jugador activo y luego roba una carta
        Jugador j = ctx.getJugadorActivo();
        j.recibirDanio(costo);
        j.robarCarta();
    }
}
