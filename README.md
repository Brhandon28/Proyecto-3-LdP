# Proyecto 3 - LdP: Draft Beer Party 🍺🍻

## Descripción General

Este proyecto simula una fiesta universitaria donde estudiantes (consumidores) y proveedores (productores) interactúan con un sistema de barriles de cerveza, modelando el clásico problema de concurrencia Productor-Consumidor. El sistema gestiona la sincronización de los hilos, el control de acceso a los recursos compartidos (barriles) y el manejo de condiciones especiales como desbordes y agotamiento de recursos.

---

## Esquema de Solución

El sistema se basa en un esquema Productor-Consumidor:

- **Estudiantes:** Consumidores que solicitan distintas cantidades de cervezas al sistema.
- **Proveedores:** Productores que ingresan distintas cantidades de cerveza al sistema de barriles.

Se implementa un objeto Monitor llamado **DraftBeerParty** encargado de la sincronización entre los hilos, con métodos clave como `consume` y `fillBarrelSystem`.

---

## Flujo de Ejecución

1. **Lectura de Archivo de Entrada:**  
   El sistema lee la configuración inicial (capacidad y contenido de los barriles, número de estudiantes y proveedores) desde un archivo de texto.

2. **Inicialización:**  
   Se crean los objetos `Barrel`, se resuelven posibles desbordes iniciales y se instancia el monitor `DraftBeerParty`.

3. **Arranque de Hilos:**  
   Se lanzan los hilos de estudiantes y proveedores, quienes interactúan concurrentemente bajo la supervisión del monitor.

4. **Consumo y Producción:**

   - Los estudiantes solicitan cerveza según sus tickets y disponibilidad.
   - Los proveedores recargan los barriles cuando hay espacio disponible.

5. **Finalización:**
   - Cuando los estudiantes agotan sus tickets o la cerveza, se retiran.
   - Los proveedores finalizan cuando ya no quedan estudiantes.
   - El sistema reporta la cantidad total de cerveza perdida por desborde.

---

## Entidades Principales

### 🧑‍🎓 Estudiante

- **Rol:** Consumidor.
- **Nombre:** Seleccionado aleatoriamente de una lista de futbolistas.
- **Edad:** Aleatoria entre 16 y 40 años.
- **Tickets:** Aleatorio entre 1 y 30, cada uno válido para una cerveza.
- **Comportamiento:**
  - Si es menor de 18 años, se retira inmediatamente.
  - Si hay proveedores, consume cerveza hasta agotar sus tickets.
  - Si no hay proveedores, consume hasta que los barriles se vacíen.
  - Al retirarse, notifica al sistema y, si es el último, despierta a los proveedores para finalizar la fiesta.

### 🍺 Proveedor

- **Rol:** Productor.
- **Comportamiento:**
  - Mientras haya estudiantes en la fiesta, intenta recargar los barriles con una cantidad aleatoria de cerveza (1 a 20).
  - Si todos los barriles están llenos, espera hasta que algún estudiante consuma cerveza.
  - Al finalizar su labor, notifica su retiro.

### 🛢️ Barrel

Objetos que contienen la información de un barril específico.

**Métodos principales:**

- **addBeer:** Agrega una cantidad de cerveza al barril. Retorna la cantidad de cerveza desbordada o cero si no hubo desborde.
- **getBeers:** Sirve una cantidad de cerveza especificada, o una cantidad menor si el barril no tiene suficiente contenido. Actualiza el contenido y retorna la cantidad servida.
- **hasOverflow:** Retorna verdadero si ha habido desborde.
- **getOverflow:** Retorna la cantidad de cerveza desbordada.
- **hasEnough:** Retorna verdadero si el contenido del barril es mayor o igual a la cantidad solicitada.
- **isEmpty:** Retorna verdadero si el contenido del barril es igual a cero.
- **isFull:** Retorna verdadero si el contenido del barril alcanzó su capacidad.

### 🍻 BeerSystem

Implementa el sistema que contiene los tres barriles. Modela el comportamiento del sistema de barriles.

**Métodos principales:**

- **initialOverflow:** Inicializa los barriles y gestiona transferencias y pérdidas por desborde.
- **transferBeer:** Ejecuta la lógica de transferencia entre barriles. Usado por `initialOverflow` y `fillBarrels`. Retorna la cantidad de cerveza perdida por desborde.
- **serveBeer:** Ejecuta el proceso de servir las cervezas solicitadas. Retorna la cantidad que se logró servir.
- **calcQuantBeer:** Suma todas las capacidades de los barriles y utiliza ese valor para hacer el cálculo de la cantidad aleatoria de cerveza que va a agregar un proveedor.
- **evaluateQuantity:** Retorna verdadero si alguno de los tres barriles tiene suficiente contenido para satisfacer la cantidad solicitada completamente.
- **getOptimalChoice:** Dada una cantidad de cerveza, decide por cuál barril es más eficiente servirla, basándose en el que tenga más contenido.
- **aOrC:** Determina si se debe transferir la cerveza al barril "A" o "C" cuando hay desborde en "B", aplicando reglas de transferencia.
- **allBarrelsFull:** Retorna verdadero si los tres barriles han alcanzado su capacidad.
- **allBarrelsEmpty:** Retorna verdadero si los tres barriles tienen 0 de contenido.

### 🕹️ DraftBeerParty (Monitor)

Implementa la clase monitor que gestiona y sincroniza el sistema de barriles.

**Métodos principales:**

- **consume:** Usado por los estudiantes para solicitar cerveza. Gestiona la lógica de espera, consumo y notificación.
- **fillBarrelSystem:** Usado por los proveedores para recargar los barriles. Gestiona la lógica de espera, recarga y notificación.
- **getParticipants:** Devuelve la cantidad de estudiantes presentes en la fiesta.
- **decreaseParticipants:** Decrementa en uno la cantidad de estudiantes presentes.
- **getSuppliers:** Retorna el número de proveedores presentes.
- **endDraftBeerParty:** Lo llama el último estudiante para activar a los proveedores en espera y permitirles finalizar.

---

## Consideraciones

- La entrada debe cumplir exactamente el formato de ejemplo, cualquier alteración al mismo se tomará como falla. 
- Los barriles no pueden tener 0 de capacidad.
- El archivo de entrada debe estar en el mismo directorio src.
- Si el número de proveedores es menor o igual a cero, los estudiantes se ejecutan mientras haya cerveza en los barriles. Al agotarse, los hilos estudiantes terminan su ejecución.
- Los proveedores añaden un cantidad de cerveza aleatoria entre 1 y la suma total de las capacidades de los barriles.
- Los estudiantes pueden tener hasta 30 tickets máximo, garantizando así un enfoque más realista
---

## Ejemplo de Formato de Archivo de Entrada

```
A, 20, 15
B, 30, 25
C, 25, 10
Estudiantes, 10
Proveedores, 3
```

---

## Ejecución

Para una mayor comodidad y automatización del proceso, se ha provisto un archivo Makefile. Este archivo permite compilar y ejecutar el programa de la siguiente manera:

```sh
make
make run FILE=prueba1.txt
```

Donde "FILE" es la variable que se sustituye por el nombre del archivo a usar

---
