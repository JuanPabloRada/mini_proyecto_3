public class PotOfGreed extends CartaMagica {
    public PotOfGreed() {
        super("Pot of Greed", "Roba 2 cartas de tu mazo inmediatamente.");
    }

    @Override
    public void activar(Contexto ctx) {
        System.out.println(">>> Activando Pot of Greed: Robando 2 cartas...");
        ctx.getJugadorActivo().robarCarta();
        ctx.getJugadorActivo().robarCarta();
    }
}