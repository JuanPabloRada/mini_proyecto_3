// carta trampa que hace dano directo al oponente sin condicion
// es un efecto simple de daño al jugador rival
package model.cards.trap;

import model.*;

public class ReflejoMagico extends CartaTrampa {
    public ReflejoMagico() { super("Reflejo Mágico", "Inflige 500 LP de daño directo al oponente."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // siempre se puede activar si esta en la zona de trampas
        return true;
    }
    @Override
    public void activar(Contexto ctx) {
        ctx.getOponente().recibirDanio(500);
    }
}
