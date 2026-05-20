// carta trampa que destruye un monstruo aleatorio del rival
// se activa solo si el oponente tiene monstruos en campo
package model.cards.trap;

import java.util.Random;
import model.*;

public class BoltDivino extends CartaTrampa {
    public BoltDivino() { super("Bolt Divino", "Destruye un monstruo aleatorio del oponente."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // puede activarse si el oponente tiene algun monstruo
        return !ctx.getOponente().getCampo().isEmpty();
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        if (oponente.getCampo().isEmpty()) return;
        int idx = new Random().nextInt(oponente.getCampo().size());
        CartaMonstruo objetivo = oponente.getCampo().get(idx);
        // elimina el monstruo elegido del campo enemigo
        ctx.getCampo().eliminarMonstruo(objetivo, oponente);
    }
}
