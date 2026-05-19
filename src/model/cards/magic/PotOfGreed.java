// carta magica que roba dos cartas al instante
// da una ventaja rapida en la mano del jugador activo
package model.cards.magic;

import model.*;

public class PotOfGreed extends CartaMagica {
    public PotOfGreed() { super("Pot of Greed", "Roba 2 cartas de tu mazo inmediatamente."); }
    @Override
    public void activar(Contexto ctx) {
        // el jugador activo roba dos veces
        ctx.getJugadorActivo().robarCarta();
        ctx.getJugadorActivo().robarCarta();
    }
}
