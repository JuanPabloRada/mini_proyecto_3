// carta trampa que refleja parte del ataque del monstruo mas fuerte enemigo
// hace dano directo al oponente segun el monstruo mas poderoso
package model.cards.trap;

import model.*;

public class EspejoDeAlmas extends CartaTrampa {
    public EspejoDeAlmas() { super("Espejo de Almas", "Inflige daño igual al ATK del monstruo más fuerte del oponente."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // puede activarse si el enemigo tiene monstruos en campo
        return !ctx.getOponente().getCampo().isEmpty();
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        if (oponente.getCampo().isEmpty()) return;
        int maxAtk = 0;
        for (CartaMonstruo m : oponente.getCampo()) {
            if (m.getAtk() > maxAtk) maxAtk = m.getAtk();
        }
        // el oponente recibe la mitad del ataque mas alto
        oponente.recibirDanio(maxAtk / 2);
    }
}
