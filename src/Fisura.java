public class Fisura extends CartaMagica {
    public Fisura() {
        super("Fisura", "Destruye al monstruo con menor ATK del oponente.");
    }

    @Override
    public void activar(Contexto ctx) {
        System.out.println(">>> Activando Fisura: Destruyendo al monstruo más débil del rival.");
        ctx.getCampo().destruirMenorAtkOponente(ctx.getJugadorActivo());
    }
}