public class RenacerDelFenix extends CartaTrampa {

    public RenacerDelFenix() {
        super("Renacer del Fénix", "Recupera 1500 LP cuando tus puntos de vida bajen de 3000.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // Solo se puede usar si tu vida esta por debajo de 3000
        return ctx.getJugadorActivo().getLp() < 3000;
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador j = ctx.getJugadorActivo();
        j.setLp(j.getLp() + 1500); // Suma 1500 LP a los que ya tienes
    }
}