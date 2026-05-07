import java.util.ArrayList;
import java.util.List;

// crea exactamente 50 cartas: 30 monstruos, 10 magicas, 10 trampas
public class FabricaDeCartas {

    // une los tres grupos en una sola lista de tipo Carta para poder mezclarlos
    public static List<Carta> crearMazoCompleto() {
        List<Carta> mazo = new ArrayList<>();
        mazo.addAll(crearMonstruos());  // 30
        mazo.addAll(crearMagicas());    // 10
        mazo.addAll(crearTrampas());    // 10
        return mazo;                    // total = 50
    }

    // se encarga de fabricar las 30 cartas de tipo monstruo
    public static List<CartaMonstruo> crearMonstruos() {
        List<CartaMonstruo> lista = new ArrayList<>();

        // 6 copias de cada uno de los monstruos de niveles bajos
        for (int i = 0; i < 6; i++)
            lista.add(new CartaMonstruo("Guerrero De La Luz",  (byte) 3, (short) 1200, (short) 1000));

        for (int i = 0; i < 6; i++)
            lista.add(new CartaMonstruo("Bestia del Bosque",   (byte) 4, (short) 1500, (short) 1200));

        // nivel 5 en adelante requiere sacrificio para invocar
        for (int i = 0; i < 5; i++)
            lista.add(new CartaMonstruo("Guardian del Hierro", (byte) 5, (short) 1000, (short) 2000));

        for (int i = 0; i < 5; i++)
            lista.add(new CartaMonstruo("Hechicero del Caos",  (byte) 4, (short) 1800, (short) 1500));

        for (int i = 0; i < 5; i++)
            lista.add(new CartaMonstruo("Caballero Real",      (byte) 6, (short) 2300, (short) 2000));

        for (int i = 0; i < 3; i++)
            lista.add(new CartaMonstruo("Dragon Ancestral",    (byte) 8, (short) 3000, (short) 2500));

        return lista; // 6+6+5+5+5+3 = 30
    }

    // se encarga de fabricar las 10 cartas magicas, cada una es su propia clase
    public static List<CartaMagica> crearMagicas() {
        List<CartaMagica> lista = new ArrayList<>();
        lista.add(new PotOfGreed());
        lista.add(new PotOfGreed());
        lista.add(new EspadaDeZeus());
        lista.add(new EspadaDeZeus());
        lista.add(new EscudoDeAtenea());
        lista.add(new EscudoDeAtenea());
        lista.add(new CuraMilagrosa());
        lista.add(new CuraMilagrosa());
        // estas dos solo van con una copia
        lista.add(new Fisura());
        lista.add(new LlamadaDelAbismo());
        return lista; // 10
    }

    // se encarga de fabricar las 10 cartas trampa
    public static List<CartaTrampa> crearTrampas() {
        List<CartaTrampa> lista = new ArrayList<>();
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
        return lista; // 10
    }
}
