package view;


public interface IDuelView {

    
    void agregarLog(String texto);

    /* Refresca TODOS los componentes visuales con el estado actual del CampoBatalla (LP, mano, mazo, trampas, monstruos en campo)
     En GUi Tambien actualiza etiquetas, paneles y botones.
     En consola imprime una tabla compacta del estado.*/
    void actualizarUI();

    void mostrarGanador();

   
    int pedirSeleccion(String titulo, String mensaje, String[] opciones);


    void mostrarMensaje(String titulo, String mensaje);
}
