// carta magica que da defensa extra a un monstruo en campo
// sirve para hacer al monstruo mas duro por un turno
package model.cards.magic;

import model.*;

public class EscudoDeAtenea extends CartaMagica {
    private short boostDef = 800;
    public EscudoDeAtenea() { super("Escudo de Atenea", "Aumenta +800 DEF a un monstruo en tu campo."); }
    @Override
    public void activar(Contexto ctx) {
        // aplica un aumento de defensa al monstruo del jugador activo
        ctx.getCampo().aplicarBoostDef(ctx.getJugadorActivo(), boostDef);
    }
}
