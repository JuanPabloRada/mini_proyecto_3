public class ContraAtaque extends CartaTrampa {

    public ContraAtaque() {
        super("Contra-Ataque", "Niega un ataque del oponente y destruye al monstruo atacante.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // Solo se puede usar si hay un monstruo atacando en este momento
        return ctx.getMonstruoAtacante() != null;
    }

    @Override
    public void activar(Contexto ctx) {
        CartaMonstruo atacante = ctx.getMonstruoAtacante();
        if (atacante == null) return; // Seguridad extra

        // Obtiene al jugador que esta atacando (el oponente desde la perspectiva del defensor)
        Jugador jugadorAtacante = ctx.getOponente();

        // Elimina al monstruo atacante del campo
        ctx.getCampo().eliminarMonstruo(atacante, jugadorAtacante);
    }
}