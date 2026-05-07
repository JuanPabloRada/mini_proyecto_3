public class EscudoSagrado extends CartaTrampa {

    public EscudoSagrado() {
        super("Escudo Sagrado", "+1000 DEF a todos tus monstruos en campo este turno.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // Solo si yo tengo monstruos en campo
        return !ctx.getJugadorActivo().getCampo().isEmpty();
    }

    @Override
    public void activar(Contexto ctx) {
        // Le aplica +1000 de def a cada monstruo propio
        for (CartaMonstruo m : ctx.getJugadorActivo().getCampo()) {
            m.aplicarBoostDef((short) 1000);
        }
    }
}