// carta trampa que castiga al oponente con daño y bloqueo
// hace que el rival pierda vida y no pueda poner cartas el siguiente turno
package model.cards.trap;

import model.*;

public class DestinoInexorable extends CartaTrampa {
    public DestinoInexorable() { super("Destino Inexorable", "El oponente pierde 800 LP y no puede jugar cartas el próximo turno."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // siempre puede activarse cuando esta en la zona de trampas
        return true;
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        oponente.recibirDanio(800);
        oponente.bloquearJugarCartaProximoTurno();
    }
}
