import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    // lista de objetos Carta
    private List<Carta> cartas;

    public Mazo(boolean usarFabrica) {
        this.cartas = new ArrayList<>();
        if(usarFabrica){
            this.agregarCartas(FabricaDeCartas.crearMazoCompleto());
            this.barajar();
        }
    }

    // mezcla las cartas aleatoriamente
    public void barajar() {
        Collections.shuffle(cartas); 
        System.out.println("El mazo ha sido barajado.");
    }

    // saca la primera carta del mazo
    public Carta robar() {
        if (estaVacio()) {
            return null; 
        }
        return cartas.remove(0); // quita la de arriba y la entrega
    }

    // revisa si quedan cartas
    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    // cuantas cartas quedan
    public int tamano() {
        return cartas.size();
    }

    public List<Carta> repartir(int n) {
        List<Carta> manoRepartida = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            Carta cartaRobada = this.robar(); // reutiliza el metodo robar() ya que maneja el remove(0)
            
            if (cartaRobada != null) {
                manoRepartida.add(cartaRobada);
            } else {
                // si el mazo se vacia antes de terminar de repartir, deja de iterar
                break; 
            }
        }
        
        return manoRepartida;
    }

    // para meter las que vienen de la fabrica
    public void agregarCartas(List<? extends Carta> nuevasCartas) {
        this.cartas.addAll(nuevasCartas);
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }
} 
