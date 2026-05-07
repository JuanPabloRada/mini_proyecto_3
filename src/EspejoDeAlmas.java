public class EspejoDeAlmas extends CartaTrampa {

    public EspejoDeAlmas() {
        super("Espejo de Almas", "Inflige daño igual al ATK del monstruo más fuerte del oponente.");
    }

    @Override
    public boolean puedoActivarme(Contexto ctx) {
        // Solo funciona si el oponente tiene monstruos
        return !ctx.getOponente().getCampo().isEmpty();
    }

    @Override
    public void activar(Contexto ctx) {
        Jugador oponente = ctx.getOponente();
        if (oponente.getCampo().isEmpty()) return; // verificacion por si algo

        int maxAtk = 0;
        // Recorre todos los monstruos del oponente buscando el de mayor ATK
        for (CartaMonstruo m : oponente.getCampo()) {
            if (m.getAtk() > maxAtk) maxAtk = m.getAtk();
        }

        int danio = maxAtk / 2; // ataca la mitad del ATK (para no ser demasiado poderosa)
        oponente.recibirDanio(danio);
    }
}