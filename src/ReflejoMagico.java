public class ReflejoMagico extends CartaTrampa {

    public ReflejoMagico() {
        super("Reflejo Mágico", "Inflige 500 LP de daño directo al oponente.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return true; // Siempre se puede activar, sin condiciones
    }

    @Override
    public void activar(Contexto ctx) {
        // el oponente recibe 500 de daño, sin más
        ctx.getOponente().recibirDanio(500);
    }
}