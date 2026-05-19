// carta trampa que protege a todos tus monstruos con defensa extra
// se usa cuando tienes monstruos en campo para aguantar mas ataques
package model.cards.trap;

import model.*;

public class EscudoSagrado extends CartaTrampa {
    public EscudoSagrado() { super("Escudo Sagrado", "+1000 DEF a todos tus monstruos en campo este turno."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // solo si el jugador activo tiene monstruos en campo
        return !ctx.getJugadorActivo().getCampo().isEmpty();
    }
    @Override
    public void activar(Contexto ctx) {
        for (CartaMonstruo m : ctx.getJugadorActivo().getCampo()) {
            m.aplicarBoostDef((short) 1000);
        }
    }
}
