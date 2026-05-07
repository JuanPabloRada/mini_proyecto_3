// clase base y abstracta de la que heredan todos los tipos de carta
public abstract class Carta {

    private String nombre;

    public Carta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    // cada subclase define su propio tipo (MONSTRUO, MAGICA, TRAMPA)
    public abstract String getTipo();
    public abstract String toString();
    
}
