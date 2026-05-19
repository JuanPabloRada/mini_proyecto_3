// carta trampa que obliga al rival a descartar una carta de su mano
// se usa cuando el oponente tiene cartas para quitarle ventaja
package model.cards.trap;

import model.*;

public class RoboForzado extends CartaTrampa {
    public RoboForzado() { super("Robo Forzado", "El oponente descarta 1 carta de su mano."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return !ctx.getOponente().getMano().isEmpty();
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        if (!oponente.getMano().isEmpty()) oponente.getMano().remove(0);
    }
}
