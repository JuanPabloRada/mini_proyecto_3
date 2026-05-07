public class EspadaDeZeus extends CartaMagica {
    private short boostAtk = 500;

    public EspadaDeZeus() {
        super("Espada de Zeus", "Aumenta +500 ATK a un monstruo en tu campo.");
    }

    @Override
    public void activar(Contexto ctx) {
        System.out.println(">>> Espada de Zeus otorga +" + boostAtk + " ATK.");
        ctx.getCampo().aplicarBoostAtk(ctx.getJugadorActivo(), boostAtk);
    }
}