# Simulación de Duelo Yu-Gi-Oh! — Interfaz Gráfica

> *"Confía en el corazón de las cartas"* — Yugi Muto

Mini Proyecto 2 — Programación Orientada a Eventos | Universidad | Java 21

---

## Descripción del Proyecto

Este proyecto es una evolución del Mini Proyecto 1: la simulación de duelo de Yu-Gi-Oh! ahora cuenta con una **interfaz gráfica completa construida con Java Swing**, reemplazando por completo la interacción por consola.

El objetivo fue migrar el sistema funcional del Mini Proyecto 1 a un entorno gráfico de eventos, aplicando los principios de **Programación Orientada a Eventos**: manejo de listeners, actualización reactiva de la UI, diálogos modales y separación entre modelo y vista. Se añadieron además las **cartas de trampa** como nuevo tipo de carta con mecánicas propias de activación y respuesta.

Todo fue implementado en **Java 21** usando únicamente `javax.swing` y `java.awt`, sin librerías externas.

---

## Integrantes del Grupo

| Nombre |
|---|
| Joseph Andrey Puerta |
| Juan Pablo Rada |
| Jean Carlo Ospina |

---

## Requisitos Técnicos

- **Java Development Kit (JDK) 21** o superior
- Terminal o consola del sistema operativo (CMD, PowerShell, bash, zsh, etc.)
- No se requieren dependencias externas ni frameworks adicionales

Para verificar tu versión de Java instalada:

```bash
java -version
```

---

## Cómo Compilar y Ejecutar

### 1. Compilar todos los archivos `.java`

Desde la raíz del proyecto, ejecuta:

```bash
javac *.java
```

### 2. Ejecutar el juego

```bash
java App
```

Al iniciar, se abrirá una ventana gráfica donde cada jugador ingresa su nombre. Al presionar **¡INICIAR DUELO!** (o Enter), comienza el duelo en una nueva ventana.

---

## Características Implementadas

### Interfaz Gráfica (Swing)
- **Pantalla de inicio** (`VentanaInicio`): permite ingresar los nombres de los dos duelistas con campos de texto estilizados y tema oscuro dorado
- **Ventana de duelo** (`VentanaDuelo`): muestra en tiempo real el campo de ambos jugadores, LP, mano, mazo y trampas
- **Tarjetas de monstruo** visuales con nombre, nivel, ATK/DEF, modo (ATK/DEF) y estado de ataque
- **Log de batalla** en tiempo real con scroll automático, estilo consola
- **Coloreado de LP** dinámico: verde (> 4000), amarillo (> 1500), rojo (≤ 1500)
- **Botones de acción** habilitados/deshabilitados automáticamente según el estado del turno
- **Diálogo de victoria** modal con animación de trofeo y opción de nueva partida

### Sistema de Cartas
- Clase abstracta `Carta` como base de la jerarquía
- `CartaMonstruo`: con atributos ATK, DEF, nivel y modo ataque/defensa
- `CartaMagica`: abstracta, implementa la interfaz `Activable`
- `CartaTrampa`: abstracta, implementa `Activable` con condición de activación
- **6 cartas mágicas** únicas con efectos distintos:
  -  **Pot of Greed** — Roba 2 cartas del mazo
  -  **Espada de Zeus** — +500 ATK a un monstruo propio
  -  **Escudo de Atenea** — +800 DEF a un monstruo propio
  -  **Cura Milagrosa** — Recupera 1000 LP
  -  **Fisura** — Destruye el monstruo con menor ATK del oponente
  -  **Llamada del Abismo** — Roba 1 carta, pero pierdes 500 LP
- **10 cartas trampa** con efectos y condiciones de activación propias:
  -  **Contra-Ataque** — Niega un ataque y destruye al monstruo atacante
  -  **Campo Minado** — Destruye todos los monstruos del oponente con ATK < 1000
  -  **Reflejo Mágico** — Inflige 500 LP de daño directo al oponente
  -  **Renacer del Fénix** — Recupera 1500 LP cuando tus LP bajan de 3000
  -  **Tormenta de Truenos** — Inflige 300 LP por cada monstruo del oponente en campo
  -  **Destino Inexorable** — El oponente pierde 800 LP y no puede jugar cartas el próximo turno
  -  **Bolt Divino** — Destruye un monstruo aleatorio del oponente
  -  **Robo Forzado** — El oponente descarta 1 carta de su mano
  - **Escudo Sagrado** — +1000 DEF a todos tus monstruos en campo este turno
  -  **Espejo de Almas** — Inflige daño igual a la mitad del ATK del monstruo más fuerte del oponente

### Sistema de Trampas
- Las trampas se colocan **boca abajo** en la zona de trampas al jugarlas
- El jugador activo puede **activar trampas en su turno** desde el botón correspondiente
- El jugador **defensor puede activar trampas en respuesta a un ataque** declarado, antes de resolver el combate
- Cada trampa define su propia condición `puedoActivarme(ctx)` para validar si puede dispararse
- El contexto de activación usa **roles invertidos** (defensor como "activo") para que la lógica de trampas funcione correctamente sin modificar las demás clases

### Sistema de Turnos
- Inicio al azar con anuncio en diálogo
- Robo automático de 1 carta al inicio de cada turno
- El primer turno no permite atacar ni robar carta
- Límite de **1 carta por turno** (monstruo, magia o trampa)
- Los boosts de ATK/DEF de cartas mágicas duran **1 turno** y se decrementan automáticamente

### Sistema de Combate
- Combate entre monstruos: ATK vs ATK con daño a LP si aplica
- Modo defensa: el atacante no inflige daño a LP si no supera la DEF
- Ataque directo cuando el oponente no tiene monstruos en campo
- Fase de respuesta de trampas **antes de resolver** cada combate
- Cada monstruo puede atacar **una sola vez** por turno

### Mecánica de Sacrificio
- Los monstruos de **nivel 5 o superior** requieren sacrificar un monstruo propio para ser invocados
- La GUI solicita la elección del sacrificio mediante un diálogo antes de completar la invocación

### Condiciones de Victoria
- Un jugador llega a **0 LP**
- Un jugador intenta robar y su **mazo está vacío**

---

## Estructura del Proyecto

```
proyecto-yugioh/
├── App.java                   # Punto de entrada, lanza la VentanaInicio en el hilo de Swing
├── VentanaInicio.java         # Pantalla inicial: ingreso de nombres y arranque del duelo
├── VentanaDuelo.java          # Ventana principal del duelo con toda la lógica de UI
├── Carta.java                 # Clase abstracta base de la jerarquía de cartas
├── CartaMonstruo.java         # Subclase concreta con ATK, DEF, nivel y modo
├── CartaMagica.java           # Subclase abstracta que implementa Activable
├── CartaTrampa.java           # Subclase abstracta con condición de activación
├── Activable.java             # Interfaz con método activar(Contexto)
├── Contexto.java              # Agrupa jugador activo, oponente y campo para efectos de cartas
├── Jugador.java               # Estado del jugador: mano, campo, LP, zona de trampas y lógica de turno
├── Mazo.java                  # Gestión del mazo: barajar, robar, repartir
├── CampoBatalla.java          # Lógica del duelo: turnos, combate, condiciones de victoria
├── FabricaDeCartas.java       # Crea el mazo completo de 50 cartas (30 monstruos, 10 mágicas, 10 trampas)
│
├── — Cartas Mágicas —
├── PotOfGreed.java            # Roba 2 cartas
├── EspadaDeZeus.java          # +500 ATK temporal
├── EscudoDeAtenea.java        # +800 DEF temporal
├── CuraMilagrosa.java         # +1000 LP
├── Fisura.java                # Destruye el monstruo con menor ATK del oponente
├── LlamadaDelAbismo.java      # Roba 1 carta / -500 LP
│
└── — Cartas Trampa —
├── ContraAtaque.java          # Niega ataque y destruye al atacante
├── CampoMinado.java           # Destruye monstruos del oponente con ATK < 1000
├── ReflejoMagico.java         # 500 LP de daño directo
├── RenacerDelFenix.java       # +1500 LP cuando LP < 3000
├── TormentaDeTruenos.java     # 300 LP de daño por cada monstruo del oponente
├── DestinoInexorable.java     # -800 LP al oponente + bloquea jugar cartas el próximo turno
├── BoltDivino.java            # Destruye un monstruo aleatorio del oponente
├── RoboForzado.java           # El oponente descarta 1 carta de su mano
├── EscudoSagrado.java         # +1000 DEF a todos los monstruos propios
└── EspejoDeAlmas.java         # Daño igual a la mitad del ATK del monstruo más fuerte del oponente
```

---

## Jerarquía de Clases

```
Carta  (abstracta)
 ├── CartaMonstruo
 ├── CartaMagica  (abstracta, implements Activable)
 │    ├── PotOfGreed
 │    ├── EspadaDeZeus
 │    ├── EscudoDeAtenea
 │    ├── CuraMilagrosa
 │    ├── Fisura
 │    └── LlamadaDelAbismo
 └── CartaTrampa  (abstracta, implements Activable)
      ├── ContraAtaque
      ├── CampoMinado
      ├── ReflejoMagico
      ├── RenacerDelFenix
      ├── TormentaDeTruenos
      ├── DestinoInexorable
      ├── BoltDivino
      ├── RoboForzado
      ├── EscudoSagrado
      └── EspejoDeAlmas
```

La interfaz `Activable` define el contrato `activar(Contexto ctx)` que comparten cartas mágicas y trampas. El objeto `Contexto` encapsula el jugador activo, el oponente y el campo de batalla, permitiendo que cada carta resuelva su efecto sin acoplarse directamente a las demás clases.
