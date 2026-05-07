public class LlamadaDelAbismo extends CartaMagica {
    private short costo = 500;

    public LlamadaDelAbismo() {
        super("Llamada del Abismo", "Robas 1 carta, pero pierdes 500 LP.");
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador j = ctx.getJugadorActivo();
        System.out.println(">>> Llamada del Abismo: Pagando " + costo + " LP para robar.");
        j.recibirDanio(costo); // Usa recibirDanio para que LP no baje de 0
        j.robarCarta();
        
        if (j.getLp() <= 0) {
            System.out.println(">>> " + j.getNombre()
                + " quedó sin LP al activar Llamada del Abismo. ¡Pierde!");
        }
    }
}