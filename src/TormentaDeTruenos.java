public class TormentaDeTruenos extends CartaTrampa {

    public TormentaDeTruenos() {
        // Le da nombre y descripcion a la carta
        super("Tormenta de Truenos", "Inflige 300 de daño al oponente por cada monstruo en su campo.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // Solo se puede usar si el oponente tiene al menos un monstruo en campo
        return !ctx.getOponente().getCampo().isEmpty();
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        int danio = 300 * oponente.getCampo().size(); // 300 × numero de monstruos
        if (danio > 0) {
            oponente.recibirDanio(danio); // Le aplica el daño total al oponente
        }
    }
}