// carta trampa que explota contra los monstruos debiles del enemigo
// destruye todos los monstruos con ataque menor a 1000
package model.cards.trap;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class CampoMinado extends CartaTrampa {
    public CampoMinado() { super("Campo Minado", "Destruye todos los monstruos del oponente con ATK < 1000."); }
    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // se puede usar cuando el oponente tiene monstruos en campo
        return !ctx.getOponente().getCampo().isEmpty();
    }
    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        List<CartaMonstruo> aDestruir = new ArrayList<>();
        for (CartaMonstruo m : oponente.getCampo()) {
            if (m.getAtk() < 1000) aDestruir.add(m);
        }
        for (CartaMonstruo m : aDestruir) {
            ctx.getCampo().eliminarMonstruo(m, oponente);
        }
    }
}
