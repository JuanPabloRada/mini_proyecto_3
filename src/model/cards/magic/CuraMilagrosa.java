// carta magica que cura vida al jugador que la activa
// es una carta sencilla para recuperar LP en el duelo
package model.cards.magic;

import model.*;

public class CuraMilagrosa extends CartaMagica {
    public CuraMilagrosa() { super("Cura Milagrosa", "Aumenta +1000 LP a tu contador de vida."); }
    @Override
    public void activar(Contexto ctx) {
        // toma al jugador que esta activo ahora
        Jugador j = ctx.getJugadorActivo();
        // suma vida a su total
        j.setLp(j.getLp() + 1000);
    }
}
