public class CuraMilagrosa extends CartaMagica {
    public CuraMilagrosa() {
        super("Cura Milagrosa", "Aumenta +1000 LP a tu contador de vida.");
    }

    @Override
    public void activar(Contexto ctx) {
        int cura = 1000;
        Jugador j = ctx.getJugadorActivo();
        j.setLp(j.getLp() + cura);
        System.out.println(">>> " + j.getNombre() + " ha recuperado " + cura + " LP.");
    }
}