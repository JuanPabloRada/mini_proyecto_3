import java.util.ArrayList;
import java.util.List;

public class CampoMinado extends CartaTrampa {

    public CampoMinado() {
        super("Campo Minado", "Destruye todos los monstruos del oponente con ATK < 1000.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return !ctx.getOponente().getCampo().isEmpty();
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();

        // Lista temporal para guardar los monstruos que serán destruidos
        List<CartaMonstruo> aDestruir = new ArrayList<>();

        // Busca todos los monstruos con ATK menor a 1000
        for (CartaMonstruo m : oponente.getCampo()) {
            if (m.getAtk() < 1000) aDestruir.add(m);
        }

        // Los elimina uno por uno del campo
        for (CartaMonstruo m : aDestruir) {
            ctx.getCampo().eliminarMonstruo(m, oponente);
        }

        // Si no habia ninguno debil, la carta se gasta igual (el efecto no se aplica)
    }
}