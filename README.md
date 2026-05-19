# Simulación de Duelo Yu-Gi-Oh! — Interfaz Gráfica + MVC

> *"Confía en el corazón de las cartas"* — Yugi Muto

**Mini Proyecto 3** — Programación Orientada a Eventos | Universidad | Java 21

---

## Descripción del Proyecto

Este proyecto es la **evolución del Mini Proyecto 2**. Se mantiene toda la funcionalidad del duelo de Yu-Gi-Oh! con una **interfaz gráfica completa** construida con Java Swing, pero ahora implementado utilizando el patrón de arquitectura **Modelo-Vista-Controlador (MVC)**.

El principal objetivo del MP3 fue refactorizar el código anterior para lograr una **mejor separación de responsabilidades**, mayor mantenibilidad y una estructura más profesional y escalable.

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

## Cómo Compilar y Ejecutar

### Desde la raíz del proyecto:

```bash
# 1. Compilar
javac -d bin src/**/*.java

# 2. Ejecutar
java -cp bin App


src/
├── App.java
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
    ├── VentanaInicio.java
    └── VentanaDuelo.java



    