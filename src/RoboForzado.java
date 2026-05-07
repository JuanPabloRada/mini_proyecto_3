public class RoboForzado extends CartaTrampa {

    public RoboForzado() {
        super("Robo Forzado", "El oponente descarta 1 carta de su mano.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // Solo si el oponente tiene cartas en la mano
        return !ctx.getOponente().getMano().isEmpty();
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        if (!oponente.getMano().isEmpty()) {
            // Quita y devuelve la primera carta de la mano (indice inicia en 0)
            Carta descartada = oponente.getMano().remove(0);
            // Muestra en consola que carta fue descartada
            System.out.println(">>> " + oponente.getNombre() + " descartó: " + descartada.getNombre());
        }
    }
}