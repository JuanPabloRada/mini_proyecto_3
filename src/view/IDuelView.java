package view;


public interface IDuelView {

    
    void agregarLog(String texto);


    void actualizarUI();

    void mostrarGanador();

   
    int pedirSeleccion(String titulo, String mensaje, String[] opciones);


    void mostrarMensaje(String titulo, String mensaje);
}
