package controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.*;
import view.IDuelView;

// controlador del duelo une modelo y vista y guarda la logica

public class DuelController {

    private final CampoBatalla campo;
    private IDuelView vista;

    public DuelController(CampoBatalla campo) {
        this.campo = campo;
    }

    // asigna la vista que va a mostrar el duelo
    public void setVista(IDuelView vista) {
        // guardo la vista para poder actualizarla luego
        this.vista = vista;
    }

    // inicio del turno

    // prepara el primer turno y actualiza la vista
    public void iniciarPrimerTurno() {
        String log = campo.prepararTurno();
        vista.agregarLog(log);
        vista.actualizarUI();
    }

    // acciones de juego

    // maneja la accion de jugar una carta desde la mano
    public void accionJugarCarta() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        Contexto ctx     = new Contexto(activo, oponente, campo);
        List<Carta> mano = activo.getMano();

        if (mano.isEmpty()) { vista.agregarLog("No tienes cartas en la mano."); return; }
        if (activo.isYaJugoCartaEsteTurno()) { vista.agregarLog("Ya jugaste una carta este turno."); return; }

        // armo las opciones de seleccion para mostrar la mano
        String[] opciones = new String[mano.size() + 1];
        for (int i = 0; i < mano.size(); i++) opciones[i] = (i + 1) + ". " + mano.get(i).toString();
        opciones[mano.size()] = "Cancelar";

        int idx = vista.pedirSeleccion(
            "🃏 Tu mano — " + activo.getNombre(),
            "Elige una carta para jugar:",
            opciones);

        if (idx < 0 || idx >= mano.size()) return; // cancelo

        Carta carta = mano.get(idx);

        int indiceSacrificio = -1;
        if (carta.getTipo().equals("MONSTRUO")) {
            CartaMonstruo mon = (CartaMonstruo) carta;
            if (mon.getnivelCarta() > 4) {
                if (activo.getCampo().isEmpty()) {
                    vista.agregarLog("Necesitas sacrificar un monstruo para invocar " + mon.getNombre()
                             + " (nivel " + mon.getnivelCarta() + "), pero no tienes monstruos en campo.");
                    return;
                }
                String[] opSac = new String[activo.getCampo().size()];
                for (int i = 0; i < activo.getCampo().size(); i++)
                    opSac[i] = (i + 1) + ". " + activo.getCampo().get(i).getNombre();

                indiceSacrificio = vista.pedirSeleccion(
                    "⚰ Sacrificio requerido",
                    mon.getNombre() + " (Lv" + mon.getnivelCarta() + ") requiere un sacrificio.\nElige el monstruo a sacrificar:",
                    opSac);
                if (indiceSacrificio < 0) return;
            }
        }

        boolean jugado = activo.jugarCarta(idx, ctx, indiceSacrificio);
        if (jugado) {
            vista.agregarLog(activo.getNombre() + " jugó: " + carta.getNombre());
            verificarGanador();
        } else {
            vista.agregarLog("No se pudo jugar la carta.");
        }
        vista.actualizarUI();
    }

    // maneja la accion de atacar con un monstruo
    public void accionAtacar() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();

        List<CartaMonstruo> disponibles = new ArrayList<>();
        for (CartaMonstruo m : activo.getCampo()) if (m.puedeAtacar()) disponibles.add(m);

        if (disponibles.isEmpty()) { vista.agregarLog("Ningún monstruo puede atacar."); return; }
        if (activo.isYaAtacoEsteTurno()) { vista.agregarLog("Ya atacaste este turno."); return; }

        // elegir atacante
        String[] opAtacantes = new String[disponibles.size() + 1];
        for (int i = 0; i < disponibles.size(); i++) opAtacantes[i] = (i + 1) + ". " + disponibles.get(i);
        opAtacantes[disponibles.size()] = "Cancelar";

        int idxAtac = vista.pedirSeleccion(
            "⚔ Ataque — " + activo.getNombre(),
            "Elige el monstruo ATACANTE:",
            opAtacantes);
        if (idxAtac < 0 || idxAtac >= disponibles.size()) return;
        CartaMonstruo atacante = disponibles.get(idxAtac);

        // elegir defensor si el oponente tiene monstruos
        CartaMonstruo defensor = null;
        if (!oponente.getCampo().isEmpty()) {
            List<CartaMonstruo> defensores = oponente.getCampo();
            String[] opDef = new String[defensores.size() + 1];
            for (int i = 0; i < defensores.size(); i++) opDef[i] = (i + 1) + ". " + defensores.get(i);
            opDef[defensores.size()] = "Cancelar";

            int idxDef = vista.pedirSeleccion(
                " Selecciona objetivo",
                "Elige el monstruo a atacar:",
                opDef);
            if (idxDef < 0 || idxDef >= defensores.size()) return;
            defensor = defensores.get(idxDef);
        }

        // verificar trampas del oponente
        Contexto ctxDefensa = new Contexto(oponente, activo, campo);
        ctxDefensa.setMonstruoAtacante(atacante);

        if (oponente.hayTrampaActivable(ctxDefensa)) {
            boolean activoTrampa = ofrecerRespuestaTrampas(oponente, ctxDefensa);
            if (activoTrampa) {
                verificarGanador();
                if (campo.hayGanador()) { vista.actualizarUI(); return; }
                if (!activo.getCampo().contains(atacante)) {
                    vista.agregarLog(atacante.getNombre() + " fue destruido. El ataque queda cancelado.");
                    activo.setYaAtacoEsteTurno(true);
                    vista.actualizarUI();
                    return;
                }
            }
        }

        // resolver combate
        String logCombate;
        if (oponente.getCampo().isEmpty()) {
            logCombate = campo.ataqueDirecto(atacante, oponente);
        } else if (defensor != null && !oponente.getCampo().contains(defensor)) {
            vista.agregarLog("El defensor fue destruido por la trampa. Ataque directo.");
            logCombate = campo.ataqueDirecto(atacante, oponente);
        } else if (defensor != null) {
            logCombate = campo.resolverCombate(atacante, defensor, activo, oponente);
        } else {
            logCombate = campo.ataqueDirecto(atacante, oponente);
        }

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
        int elegida = vista.pedirSeleccion(
            "🕳  Respuesta de trampas — " + defensor.getNombre(),
            "  ¡ATAQUE DECLARADO!\n\n" +
            defensor.getNombre() + ", ¿deseas activar una trampa en respuesta?\n" +
            "(Si no activas nada, el combate se resuelve normalmente)",
            ops);

        // la ultima opcion es "No activar"
        if (elegida < 0 || elegida >= indices.size()) return false;

        int idxReal = indices.get(elegida);
        CartaTrampa trampa = trampas.get(idxReal);

        vista.agregarLog(defensor.getNombre() + " activó trampa en respuesta: " + trampa.getNombre());
        boolean ok = defensor.activarTrampa(idxReal, ctx);
        if (!ok) { vista.agregarLog("La trampa no pudo activarse."); return false; }
        return true;
    }

    // activa una trampa que ya esta colocada en el campo
    public void accionActivarTrampa() {
        Jugador activo   = campo.getJugadorActivo();
        Jugador oponente = campo.getOponente();
        Contexto ctx     = new Contexto(activo, oponente, campo);

        List<CartaTrampa> trampas = activo.getZonaTrampas();
        if (trampas.isEmpty()) { vista.agregarLog("No tienes trampas colocadas."); return; }

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
        int posLista = vista.pedirSeleccion(
            " Trampas — " + activo.getNombre(),
            "Elige la trampa a activar:",
            ops);

        if (posLista < 0 || posLista >= indices.size()) return;
        int idxReal = indices.get(posLista);

        CartaTrampa trampa = trampas.get(idxReal);
        vista.agregarLog(">>> " + activo.getNombre() + " activo trampa: " + trampa.getNombre());
        boolean ok = activo.activarTrampa(idxReal, ctx);
        if (!ok) vista.agregarLog("La trampa no pudo activarse.");

        verificarGanador();
        vista.actualizarUI();
    }

    // cambia la posicion de ataque o defensa de un monstruo
    public void accionCambiarPosicion() {
        Jugador activo = campo.getJugadorActivo();
        if (activo.getCampo().isEmpty()) { vista.agregarLog("No tienes monstruos en campo."); return; }

        String[] ops = new String[activo.getCampo().size() + 1];
        for (int i = 0; i < activo.getCampo().size(); i++) ops[i] = (i + 1) + ". " + activo.getCampo().get(i);
        ops[activo.getCampo().size()] = "Cancelar";

        int idx = vista.pedirSeleccion(
            " Cambiar posición",
            "Elige el monstruo para cambiar posición:",
            ops);

        if (idx < 0 || idx >= activo.getCampo().size()) return;

        CartaMonstruo m = activo.getCampo().get(idx);
        m.cambiarPosicion();
        vista.agregarLog(">>> " + m.getNombre() + " cambió a modo " + (m.estaEnModoDefensa() ? "DEFENSA" : "ATAQUE") + ".");
        vista.actualizarUI();
    }

    // termina el turno actual y pasa al siguiente jugador
    public void accionTerminarTurno() {
        Jugador terminando = campo.getJugadorActivo();
        vista.agregarLog("── " + terminando.getNombre() + " termina su turno ──");
        campo.terminarTurno();

        if (campo.hayGanador()) { vista.mostrarGanador(); return; }

        String log = campo.prepararTurno();
        vista.agregarLog(log);

        if (campo.hayGanador()) { vista.mostrarGanador(); return; }

        vista.actualizarUI();
        vista.mostrarMensaje("Nuevo Turno",
            "Es el turno de:\n" + campo.getJugadorActivo().getNombre().toUpperCase());
    }

    // utilidades

    private void verificarGanador() {
        if (campo.hayGanador()) vista.mostrarGanador();
    }

    // devuelve el campo de batalla del duelo
    public CampoBatalla getCampo() { return campo; }
}