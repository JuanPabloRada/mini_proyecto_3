public class DestinoInexorable extends CartaTrampa {

    public DestinoInexorable() {
        super("Destino Inexorable", "El oponente pierde 800 LP y no puede jugar cartas el próximo turno.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        return true; // Se puede usar siempre
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        oponente.recibirDanio(800);                    // Le quita 800 LP
        oponente.bloquearJugarCartaProximoTurno();     // Le impide jugar cartas el siguiente turno
    }
}