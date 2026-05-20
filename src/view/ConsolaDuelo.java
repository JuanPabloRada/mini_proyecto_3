package view;

import controller.DuelController;
import model.*;

import java.util.List;
import java.util.Scanner;

/*ConsolaDuelo — Vista del duelo para modo TERMINAL.
 */
public class ConsolaDuelo implements IDuelView {

    private final Scanner scanner;

    
    
    public ConsolaDuelo(DuelController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner    = scanner;
    }

    
    public void iniciarJuego() {
        
    }

    
    
   
    @Override
    public void agregarLog(String texto) {
        
    }

   
    @Override
    public void actualizarUI() {
        
    }

   
    @Override
    public void mostrarGanador() {
       
    }

    
    @Override
    public int pedirSeleccion(String titulo, String mensaje, String[] opciones) {
       
    }

    
    @Override
    public void mostrarMensaje(String titulo, String mensaje) {
      
    }
}
