// carta magica que destruye al monstruo mas debil del oponente
// es util cuando quieres limpiar el campo rival
package model.cards.magic;

import model.*;

public class Fisura extends CartaMagica {
    public Fisura() { super("Fisura", "Destruye al monstruo con menor ATK del oponente."); }
    @Override
    public void activar(Contexto ctx) {
        // hace que el campo destruya al monstruo con menor ataque del enemigo
        ctx.getCampo().destruirMenorAtkOponente(ctx.getJugadorActivo());
    }
}
