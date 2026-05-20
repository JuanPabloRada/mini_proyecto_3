// carta trampa que frena un ataque enemigo en el instante
// destruye al monstruo que esta atacando si puede activarse
package model.cards.trap;

import model.*;

public class ContraAtaque extends CartaTrampa {
    public ContraAtaque() { super("Contra-Ataque", "Niega un ataque del oponente y destruye al monstruo atacante."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // solo puede activarse si hay un monstruo atacando ahora mismo
        return ctx.getMonstruoAtacante() != null;
    }
    @Override
    public void activar(Contexto ctx) {
        CartaMonstruo atacante = ctx.getMonstruoAtacante();
        if (atacante == null) return;
        Jugador jugadorAtacante = ctx.getOponente();
        ctx.getCampo().eliminarMonstruo(atacante, jugadorAtacante);
    }
}
