// carta trampa que lanza una tormenta de daño por cada monstruo rival
// hace que el enemigo pierda vida segun cuantos monstruos tenga en campo
package model.cards.trap;

import model.*;

public class TormentaDeTruenos extends CartaTrampa {
    public TormentaDeTruenos() { super("Tormenta de Truenos", "Inflige 300 de daño al oponente por cada monstruo en su campo."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return !ctx.getOponente().getCampo().isEmpty();
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        int danio = 300 * oponente.getCampo().size();
        if (danio > 0) oponente.recibirDanio(danio);
    }
}
