// carta trampa que cura al jugador activo cuando esta con poca vida
// se activa solo si los LP estan debajo de 3000
package model.cards.trap;

import model.*;

public class RenacerDelFenix extends CartaTrampa {
    public RenacerDelFenix() { super("Renacer del Fénix", "Recupera 1500 LP cuando tus puntos de vida bajen de 3000."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return ctx.getJugadorActivo().getLp() < 3000;
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador j = ctx.getJugadorActivo();
        j.setLp(j.getLp() + 1500);
    }
}
