// carta magica que sube ataque a un monstruo en tu campo
// es un buff rapido para ganar eno combate
package model.cards.magic;

import model.*;

public class EspadaDeZeus extends CartaMagica {
    private short boostAtk = 500;
    public EspadaDeZeus() { super("Espada de Zeus", "Aumenta +500 ATK a un monstruo en tu campo."); }
    @Override
    public void activar(Contexto ctx) {
        // aplica un aumento de ataque al monstruo del jugador activo
        ctx.getCampo().aplicarBoostAtk(ctx.getJugadorActivo(), boostAtk);
    }
}
