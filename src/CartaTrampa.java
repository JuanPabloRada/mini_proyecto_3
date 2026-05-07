public abstract class CartaTrampa extends Carta implements Activable {

    private String descripcion;
    private boolean activada; // empieza en false, pasa a true cuando ya fue usada

    public CartaTrampa(String nombre, String descripcion) {
        super(nombre);
        this.descripcion = descripcion;
        this.activada = false;
    }

    // cada trampa define bajo que condicion puede activarse
    public abstract boolean puedoActivarme(Contexto ctx);

    // cada trampa define que efecto ejecuta al activarse
    @Override
    public abstract void activar(Contexto ctx);

    public String getDescripcion() { return descripcion; }
    public boolean isActivada()    { return activada; }
    public void setActivada(boolean activada) { this.activada = activada; }

    @Override
    public String getTipo() { return "TRAMPA"; }

    @Override
    public String toString() {
        return "[TRAMPA] " + getNombre() + ": " + descripcion;
    }
}
