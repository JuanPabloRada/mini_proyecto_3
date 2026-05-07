public class EscudoDeAtenea extends CartaMagica {
    private short boostDef = 800;

    public EscudoDeAtenea() {
        super("Escudo de Atenea", "Aumenta +800 DEF a un monstruo en tu campo.");
    }

    @Override
    public void activar(Contexto ctx) {
        System.out.println(">>> Escudo de Atenea otorga +" + boostDef + " DEF.");
        ctx.getCampo().aplicarBoostDef(ctx.getJugadorActivo(), boostDef);
    }
}