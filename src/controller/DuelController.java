package controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.*;
import view.VentanaDuelo;

// controlador del duelo une modelo y vista y guarda la logica

public class DuelController {

    private final CampoBatalla campo;
    private VentanaDuelo vista;

    public DuelController(CampoBatalla campo) {
        this.campo = campo;
    }

    // asigna la vista que va a mostrar el duelo
    public void setVista(VentanaDuelo vista) {
        // guardo la ventana para poder actualizarla luego
        this.vista = vista;
    }

    // inicio del turno

    // prepara el primer turno y actualiza la vista
    public void iniciarPrimerTurno() {
        // el modelo arma lo que pasa al inicio de cada turno
        String log = campo.prepararTurno();
        // muestro el mensaje en el log de la vista
        vista.agregarLog(log);
        // refresco la interfaz despues de preparar el turno
        vista.actualizarUI();
    }

    // acciones de juego

    // maneja la accion de jugar una carta desde la mano
    public void accionJugarCarta() {
        // tomo el jugador activo y al oponente del campo
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        // creo el contexto con informacion del turno actual
        Contexto ctx     = new Contexto(activo, oponente, campo);
        // obtengo la mano del jugador que intenta jugar
        List<Carta> mano = activo.getMano();

        // si no hay cartas no se puede jugar nada
        if (mano.isEmpty()) { vista.agregarLog("No tienes cartas en la mano."); return; }
        // si ya jugo carta este turno no puede volver a jugar
        if (activo.isYaJugoCartaEsteTurno()) { vista.agregarLog("Ya jugaste una carta este turno."); return; }

        // armo las opciones de seleccion para mostrar la mano
        String[] opciones = new String[mano.size() + 1];
        for (int i = 0; i < mano.size(); i++) opciones[i] = (i + 1) + ". " + mano.get(i).toString();
        opciones[mano.size()] = "Cancelar";

        // muestro el dialogo para elegir la carta a jugar
        String elegida = (String) JOptionPane.showInputDialog(
            vista, "Elige una carta para jugar:",
            "🃏 Tu mano — " + activo.getNombre(),
            JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        // si cancela no pasa nada
        if (elegida == null || elegida.equals("Cancelar")) return;

        // convierto la opcion elegida en indice de la mano
        int idx = java.util.Arrays.asList(opciones).indexOf(elegida);
        if (idx < 0 || idx >= mano.size()) return;

        // obtengo la carta seleccionada
        Carta carta = mano.get(idx);

        int indiceSacrificio = -1;
        if (carta.getTipo().equals("MONSTRUO")) {
            CartaMonstruo mon = (CartaMonstruo) carta;
            if (mon.getnivelCarta() > 4) {
                // si es monstruo alto necesita sacrificio
                if (activo.getCampo().isEmpty()) {
                    vista.agregarLog("Necesitas sacrificar un monstruo para invocar " + mon.getNombre()
                             + " (nivel " + mon.getnivelCarta() + "), pero no tienes monstruos en campo.");
                    return;
                }
                // preparo las opciones de sacrificio
                String[] opSac = new String[activo.getCampo().size()];
                for (int i = 0; i < activo.getCampo().size(); i++)
                    opSac[i] = (i + 1) + ". " + activo.getCampo().get(i).getNombre();

                // pido que elija el monstruo a sacrificar
                String elegidoSac = (String) JOptionPane.showInputDialog(
                    vista,
                    mon.getNombre() + " (Lv" + mon.getnivelCarta() + ") requiere un sacrificio.\nElige el monstruo a sacrificar:",
                    "⚰ Sacrificio requerido",
                    JOptionPane.WARNING_MESSAGE, null, opSac, opSac[0]);
                if (elegidoSac == null) return;
                indiceSacrificio = java.util.Arrays.asList(opSac).indexOf(elegidoSac);
            }
        }

        // intento jugar la carta seleccionada
        boolean jugado = activo.jugarCarta(idx, ctx, indiceSacrificio);
        if (jugado) {
            vista.agregarLog(activo.getNombre() + " jugó: " + carta.getNombre());
            verificarGanador();
        } else {
            vista.agregarLog("No se pudo jugar la carta.");
        }
        // siempre actualizo la interfaz al final
        vista.actualizarUI();
    }

    // maneja la accion de atacar con un monstruo
    public void accionAtacar() {
        // tomo el jugador activo y el oponente
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();

        // busco los monstruos que pueden atacar
        List<CartaMonstruo> disponibles = new ArrayList<>();
        for (CartaMonstruo m : activo.getCampo()) if (m.puedeAtacar()) disponibles.add(m);

        // si no hay atacantes o ya ataco no puedo seguir
        if (disponibles.isEmpty()) { vista.agregarLog("Ningún monstruo puede atacar."); return; }
        if (activo.isYaAtacoEsteTurno()) { vista.agregarLog("Ya atacaste este turno."); return; }

        // armo las opciones para elegir atacante
        String[] opAtacantes = new String[disponibles.size() + 1];
        for (int i = 0; i < disponibles.size(); i++) opAtacantes[i] = (i + 1) + ". " + disponibles.get(i);
        opAtacantes[disponibles.size()] = "Cancelar";

        // pido que el jugador elija su monstruo atacante
        String elegidoAtac = (String) JOptionPane.showInputDialog(
            vista, "Elige el monstruo ATACANTE:",
            "⚔ Ataque — " + activo.getNombre(),
            JOptionPane.PLAIN_MESSAGE, null, opAtacantes, opAtacantes[0]);
        if (elegidoAtac == null || elegidoAtac.equals("Cancelar")) return;

        // convierto la opcion en indice de la lista de monstruos
        int idxAtac = java.util.Arrays.asList(opAtacantes).indexOf(elegidoAtac);
        if (idxAtac < 0 || idxAtac >= disponibles.size()) return;
        CartaMonstruo atacante = disponibles.get(idxAtac);

        // luego elijo el defensor si el oponente tiene monstruos
        CartaMonstruo defensor = null;
        if (!oponente.getCampo().isEmpty()) {
            List<CartaMonstruo> defensores = oponente.getCampo();
            String[] opDef = new String[defensores.size() + 1];
            for (int i = 0; i < defensores.size(); i++) opDef[i] = (i + 1) + ". " + defensores.get(i);
            opDef[defensores.size()] = "Cancelar";

            String elegidoDef = (String) JOptionPane.showInputDialog(
                vista, "Elige el monstruo a atacar:",
                " Selecciona objetivo",
                JOptionPane.PLAIN_MESSAGE, null, opDef, opDef[0]);
            if (elegidoDef == null || elegidoDef.equals("Cancelar")) return;

            int idxDef = java.util.Arrays.asList(opDef).indexOf(elegidoDef);
            if (idxDef < 0 || idxDef >= defensores.size()) return;
            defensor = defensores.get(idxDef);
        }

        // creo el contexto de defensa para posibles trampas del rival
        Contexto ctxDefensa = new Contexto(oponente, activo, campo);
        ctxDefensa.setMonstruoAtacante(atacante);

        // si el oponente tiene trampa activable le ofrezco usarla
        if (oponente.hayTrampaActivable(ctxDefensa)) {
            boolean activoTrampa = ofrecerRespuestaTrampas(oponente, ctxDefensa);
            if (activoTrampa) {
                verificarGanador();
                if (campo.hayGanador()) { vista.actualizarUI(); return; }
                if (!activo.getCampo().contains(atacante)) {
                    vista.agregarLog(atacante.getNombre() + " fue destruido El ataque queda cancelado");
                    activo.setYaAtacoEsteTurno(true);
                    vista.actualizarUI();
                    return;
                }
            }
        }

        // ahora resuelvo el combate segun si hay defensor o ataque directo
        String logCombate;
        if (oponente.getCampo().isEmpty()) {
            logCombate = campo.ataqueDirecto(atacante, oponente);
        } else if (defensor != null && !oponente.getCampo().contains(defensor)) {
            vista.agregarLog("El defensor fue destruido por la trampa Ataque directo");
            logCombate = campo.ataqueDirecto(atacante, oponente);
        } else if (defensor != null) {
            logCombate = campo.resolverCombate(atacante, defensor, activo, oponente);
        } else {
            logCombate = campo.ataqueDirecto(atacante, oponente);
        }

        // marco que este monstruo ya ataco y muestro el resultado
        activo.setYaAtacoEsteTurno(true);
        vista.agregarLog(logCombate);
        verificarGanador();
        vista.actualizarUI();
    }

    private boolean ofrecerRespuestaTrampas(Jugador defensor, Contexto ctx) {
        List<CartaTrampa> trampas = defensor.getZonaTrampas();
        List<Integer> indices = new ArrayList<>();
        List<String>  nombres = new ArrayList<>();

        for (int i = 0; i < trampas.size(); i++) {
            if (trampas.get(i).puedoActivarme(ctx)) {
                indices.add(i);
                nombres.add((indices.size()) + ". " + trampas.get(i).toString());
            }
        }
        nombres.add(" No activar");

        String[] ops = nombres.toArray(new String[0]);
        String elegida = (String) JOptionPane.showInputDialog(
            vista,
            "  ¡ATAQUE DECLARADO!\n\n" +
            defensor.getNombre() + ", ¿deseas activar una trampa en respuesta?\n" +
            "(Si no activas nada, el combate se resuelve normalmente)",
            "🕳  Respuesta de trampas — " + defensor.getNombre(),
            JOptionPane.WARNING_MESSAGE, null, ops, ops[ops.length - 1]);

        if (elegida == null || elegida.equals(" No activar")) return false;

        // busco el indice real de la trampa en la lista
        int posLista = nombres.indexOf(elegida);
        if (posLista < 0 || posLista >= indices.size()) return false;

        int idxReal = indices.get(posLista);
        CartaTrampa trampa = trampas.get(idxReal);

        // pongo el mensaje de que el defensor uso una trampa
        vista.agregarLog( defensor.getNombre() + " activó trampa en respuesta: " + trampa.getNombre());
        boolean ok = defensor.activarTrampa(idxReal, ctx);
        if (!ok) { vista.agregarLog("La trampa no pudo activarse."); return false; }
        return true;
    }

    // activa una trampa que ya esta colocada en el campo
    public void accionActivarTrampa() {
        // tomo al jugador activo y su oponente
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        Contexto ctx     = new Contexto(activo, oponente, campo);

        // obtengo las trampas que el jugador tiene en su zona
        List<CartaTrampa> trampas = activo.getZonaTrampas();
        if (trampas.isEmpty()) { vista.agregarLog("No tienes trampas colocadas."); return; }

        // preparo la lista de trampas que se pueden activar ahora
        List<Integer> indices = new ArrayList<>();
        List<String>  nombres = new ArrayList<>();
        for (int i = 0; i < trampas.size(); i++) {
            if (trampas.get(i).puedoActivarme(ctx)) {
                indices.add(i);
                nombres.add((i + 1) + ". " + trampas.get(i).toString());
            }
        }
        nombres.add("Cancelar");

        if (indices.isEmpty()) { vista.agregarLog("Ninguna trampa puede activarse ahora."); return; }

        String[] ops = nombres.toArray(new String[0]);
        // pido al jugador que elija la trampa a activar
        String elegida = (String) JOptionPane.showInputDialog(
            vista, "Elige la trampa a activar:",
            " Trampas — " + activo.getNombre(),
            JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
        if (elegida == null || elegida.equals("Cancelar")) return;

        int posLista = nombres.indexOf(elegida);
        if (posLista < 0 || posLista >= indices.size()) return;
        int idxReal = indices.get(posLista);

        CartaTrampa trampa = trampas.get(idxReal);
        vista.agregarLog(">>> " + activo.getNombre() + " activo trampa: " + trampa.getNombre());
        boolean ok = activo.activarTrampa(idxReal, ctx);
        if (!ok) vista.agregarLog("La trampa no pudo activarse.");

        // luego verifico si con eso hay ganador y actualizo la interfaz
        verificarGanador();
        vista.actualizarUI();
    }

    // cambia la posicion de ataque o defensa de un monstruo
    public void accionCambiarPosicion() {
        // tomo el jugador activo
        Jugador activo = campo.getJugadorActivo();
        if (activo.getCampo().isEmpty()) { vista.agregarLog("No tienes monstruos en campo."); return; }

        // armo las opciones con los monstruos en campo
        String[] ops = new String[activo.getCampo().size() + 1];
        for (int i = 0; i < activo.getCampo().size(); i++) ops[i] = (i + 1) + ". " + activo.getCampo().get(i);
        ops[activo.getCampo().size()] = "Cancelar";

        // pido que el jugador elija cual cambiar
        String elegida = (String) JOptionPane.showInputDialog(
            vista, "Elige el monstruo para cambiar posición:",
            " Cambiar posición",
            JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
        if (elegida == null || elegida.equals("Cancelar")) return;

        int idx = java.util.Arrays.asList(ops).indexOf(elegida);
        if (idx < 0 || idx >= activo.getCampo().size()) return;

        // cambio la posicion del monstruo elegido
        CartaMonstruo m = activo.getCampo().get(idx);
        m.cambiarPosicion();
        vista.agregarLog(">>> " + m.getNombre() + " cambió a modo " + (m.estaEnModoDefensa() ? "DEFENSA" : "ATAQUE") + ".");
        vista.actualizarUI();
    }

    // termina el turno actual y pasa al siguiente jugador
    public void accionTerminarTurno() {
        // aviso que el jugador actual acaba su turno
        Jugador terminando = campo.getJugadorActivo();
        vista.agregarLog("── " + terminando.getNombre() + " termina su turno ──");
        campo.terminarTurno();

        // si ya hay un ganador muestro el resultado
        if (campo.hayGanador()) { vista.mostrarGanador(); return; }

        // preparo el siguiente turno y muestro el mensaje
        String log = campo.prepararTurno();
        vista.agregarLog(log);

        if (campo.hayGanador()) { vista.mostrarGanador(); return; }

        vista.actualizarUI();
        JOptionPane.showMessageDialog(vista,
            "Es el turno de:\n" + campo.getJugadorActivo().getNombre().toUpperCase(),
            "Nuevo Turno", JOptionPane.INFORMATION_MESSAGE);
    }

    // utilidades

    private void verificarGanador() {
        // si hay ganador muestro la pantalla de fin
        if (campo.hayGanador()) vista.mostrarGanador();
    }

    // devuelve el campo de batalla del duelo
    public CampoBatalla getCampo() { return campo; }
}
