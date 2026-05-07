import java.util.Random;

public class BoltDivino extends CartaTrampa {

    public BoltDivino() {
        super("Bolt Divino", "Destruye un monstruo aleatorio del oponente.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return !ctx.getOponente().getCampo().isEmpty();
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        if (oponente.getCampo().isEmpty()) return;//Verificacion

        Random rnd = new Random();
        // Escoge un indice al azar entre 0 y el nuemro de monstruos - 1
        int idx = rnd.nextInt(oponente.getCampo().size());

        CartaMonstruo objetivo = oponente.getCampo().get(idx); // Toma ese monstruo
        ctx.getCampo().eliminarMonstruo(objetivo, oponente);   // Lo elimina
    }
}