# Simulación de Duelo Yu-Gi-Oh! — Interfaz Gráfica + MVC

> *"Confía en el corazón de las cartas"* — Yugi Muto

**Mini Proyecto 3** — Programación Orientada a Eventos | Universidad | Java 21

---

## Descripción del Proyecto

Este proyecto es la **evolución del Mini Proyecto 1 y Mini Proyecto 2**:

- **Mini Proyecto 1:** implementación del duelo Yu-Gi-Oh! en modo consola (terminal).
- **Mini Proyecto 2:** evolución del MP1 con una interfaz gráfica completa construida con Java Swing.
- **Mini Proyecto 3 (este proyecto):** refactorización de ambos modos bajo el patrón de arquitectura **Modelo-Vista-Controlador (MVC)**, unificando consola y GUI en una misma base de código mediante el contrato `IDuelView`.

El principal objetivo del MP3 fue lograr una **mejor separación de responsabilidades**, mayor mantenibilidad y una estructura más profesional y escalable, permitiendo elegir el modo de juego al iniciar la aplicación.

---

## Integrantes del Grupo

| Nombre                |
|-----------------------|
| Joseph Andrey Puerta  |
| Juan Pablo Rada       |
| Jean Carlo Ospina     |

---

## Requisitos Técnicos

- **Java Development Kit (JDK) 21** o superior
- Solo librerías estándar de Java (`javax.swing`, `java.awt`, `java.util`, etc.)
- No se permiten librerías externas

---

## Modos de ejecución

Al iniciar la aplicación aparece un menú en consola para elegir el modo:

- **[1] Modo GUI** *(Mini Proyecto 2 con MVC)*: lanza la ventana gráfica con interfaz Swing
- **[2] Modo Consola** *(Mini Proyecto 1 con MVC)*: juega directamente desde la terminal

Ambos modos comparten exactamente la misma lógica de juego (modelo y controlador). Solo cambia la vista.

---

## Cómo Compilar y Ejecutar

### Desde la raíz del proyecto:

```bash
# 1. Compilar
javac -d bin src/**/*.java

# 2. Ejecutar
java -cp bin App
```

Al ejecutar aparece el menú para elegir el modo de juego.

---

## Arquitectura MVC

El proyecto sigue el patrón **Modelo - Vista - Controlador**:

```
App.java
  └── Menú de selección de modo
        ├── [1] GUI     → VentanaInicio → InicioController → DuelController
        └── [2] Consola → ConsolaDuelo  →                  → DuelController
                              ↕
                          IDuelView (contrato)
                         ┌────────────────────┐
                         │  VentanaDuelo      │  ← MP2 con MVC (GUI)
                         │  ConsolaDuelo      │  ← MP1 con MVC (consola)
                         └────────────────────┘
                              ↕
                          CampoBatalla (modelo)
                         ┌────────────────────┐
                         │  Jugador           │
                         │  CartaMonstruo     │
                         │  CartaTrampa       │
                         │  CartaMagica       │
                         └────────────────────┘
```

---

## Estructura del Proyecto

```
src/
├── App.java                  ← punto de entrada + menú de selección de modo
│
├── controller/
│   ├── InicioController.java
│   └── DuelController.java
│
├── model/
│   ├── CampoBatalla.java
│   ├── Jugador.java
│   ├── Mazo.java
│   ├── Contexto.java
│   ├── FabricaDeCartas.java
│   └── cards/
│       ├── Carta.java
│       ├── CartaMonstruo.java
│       ├── CartaMagica.java
│       ├── CartaTrampa.java
│       ├── Activable.java
│       ├── magic/
│       └── trap/
│
└── view/
    ├── IDuelView.java        ← contrato que unifica ambas vistas
    ├── VentanaInicio.java
    ├── VentanaDuelo.java     ← vista GUI (MP2 con MVC)
    └── ConsolaDuelo.java     ← vista consola (MP1 con MVC)
```