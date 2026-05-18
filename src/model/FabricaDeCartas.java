// Fabrica que crea todos los tipos de cartas del mazo
// aqui se arma el mazo completo con monstruos magicas y trampas
package model;

import java.util.ArrayList;
import java.util.List;
import model.cards.magic.*;
import model.cards.trap.*;

public class FabricaDeCartas {

    // crea la lista completa del mazo con todos los tipos de cartas
    public static List<Carta> crearMazoCompleto() {
        List<Carta> mazo = new ArrayList<>();
        // agregar monstruos, magicas y trampas al mazo completo
        mazo.addAll(crearMonstruos());
        mazo.addAll(crearMagicas());
        mazo.addAll(crearTrampas());
        return mazo;
    }

    // crea todas las cartas de monstruo del mazo
    public static List<CartaMonstruo> crearMonstruos() {
        List<CartaMonstruo> lista = new ArrayList<>();
        // crear varias copias del mismo monstruo para el mazo
        for (int i = 0; i < 6; i++)
            lista.add(new CartaMonstruo("Guerrero De La Luz",  (byte) 3, (short) 1200, (short) 1000));
        for (int i = 0; i < 6; i++)
            lista.add(new CartaMonstruo("Bestia del Bosque",   (byte) 4, (short) 1500, (short) 1200));
        for (int i = 0; i < 5; i++)
            lista.add(new CartaMonstruo("Guardian del Hierro", (byte) 5, (short) 1000, (short) 2000));
        for (int i = 0; i < 5; i++)
            lista.add(new CartaMonstruo("Hechicero del Caos",  (byte) 4, (short) 1800, (short) 1500));
        for (int i = 0; i < 5; i++)
            lista.add(new CartaMonstruo("Caballero Real",      (byte) 6, (short) 2300, (short) 2000));
        for (int i = 0; i < 3; i++)
            lista.add(new CartaMonstruo("Dragon Ancestral",    (byte) 8, (short) 3000, (short) 2500));
        return lista;
    }

    // crea las cartas magicas que se pueden activar
    public static List<CartaMagica> crearMagicas() {
        List<CartaMagica> lista = new ArrayList<>();
        // cada carta magica se instancia y se agrega al mazo
        lista.add(new PotOfGreed());
        lista.add(new PotOfGreed());
        lista.add(new EspadaDeZeus());
        lista.add(new EspadaDeZeus());
        lista.add(new EscudoDeAtenea());
        lista.add(new EscudoDeAtenea());
        lista.add(new CuraMilagrosa());
        lista.add(new CuraMilagrosa());
        lista.add(new Fisura());
        lista.add(new LlamadaDelAbismo());
        return lista;
    }

    // crea las cartas trampa que se colocan en el campo
    public static List<CartaTrampa> crearTrampas() {
        List<CartaTrampa> lista = new ArrayList<>();
        // cada trampa se instancia y se agrega a la lista final
        lista.add(new ContraAtaque());
        lista.add(new CampoMinado());
        lista.add(new ReflejoMagico());
        lista.add(new RenacerDelFenix());
        lista.add(new TormentaDeTruenos());
        lista.add(new DestinoInexorable());
        lista.add(new BoltDivino());
        lista.add(new RoboForzado());
        lista.add(new EscudoSagrado());
        lista.add(new EspejoDeAlmas());
        return lista;
    }
}
