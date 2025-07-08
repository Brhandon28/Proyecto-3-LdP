**Descripción de la solución:**
-------------------------------

Basándonos en un esquema Productor-Consumidor donde los estudiantes tienen el rol de consumidor al solicitar distintas cantidades de cervezas al sistema y los proveedores asumen el rol de productor al ingresar distintas cantidades de cerveza al sistema de barriles.

Se implementó un objeto Monitor llamado "**DraftBeerParty**" encargado de velar por la sincronización entre los hilos y donde se crean, entre otros, los métodos **consume** y **fillBarrelSystem**.

El método **consume** es utilizado por los objetos “**estudiantes**” para solicitar una cantidad aleatoria de cerveza que va desde una (1) cerveza hasta máximo la cantidad de tickets que posea el estudiante. El funcionamiento de este método es el siguiente:

*   si todos los barriles están vacíos el estudiante espera (wait)
    

*   si al menos un barril tiene cerveza el estudiante procede a pedir la cantidad de cerveza. Para esto se usa **serveBeer** el cual retorna la cantidad que se haya podido servir que en el escenario normal teniendo al menos un proveedor debería ser la cantidad total que se pidió.
    

*   si el sistema no puede servir por un barril todas las cervezas que solicitó el estudiante en el primer intento entonces se entra en un bucle en el cual, si los barriles no están vacíos, se pide esa cantidad faltante.
    

*   si por el contrario los barriles están vacíos y existe al menos un proveedor en el sistema entonces el estudiante se queda en espera de que el proveedor ingrese cervezas al sistema de barriles nuevamente para volver a intentarlo.
    

*   según nuestra implementación puede existir el caso en el que se vacíen los barriles y no haya ningún proveedor. En ese caso el estudiante no se queda a la espera sino que finaliza la ejecución de este método consume.
    

*   una vez que el estudiante recibe la cerveza notifica al resto de hilos para activar nuevamente a los que estén en espera.
    

*   el método **consume** retorna la cantidad de cerveza servida la cual normalmente será igual a la cantidad que haya solicitado inicialmente el estudiante a menos que sea el caso en el cual no hay proveedores presentes en el sistema y los estudiantes consumen solo hasta vaciar los tres barriles y se retiran de la fiesta sin importar si aún le quedan tickets disponibles.
    

El método **fillBarrelSystem** es utilizado por los proveedores para ingresar una cantidad aleatoria de cervezas al sistema de barriles. Este método se implementó de la siguiente forma:

*   si los barriles están llenos todos y hay estudiantes todavía en la fiesta entonces el proveedor espera.
    

*   una vez que al menos a uno de los barriles le falte cerveza entonces el proveedor solicita ingresar una cantidad aleatoria de cervezas al sistema usando el método **fillBarrels** de la clase **Barrel**.
    

*   el método **fillBarrels** retorna la cantidad de cerveza que se haya desbordado por lo que vamos acumulando ese valor para ser mostrado al final de la ejecución del programa.
    

*   luego de haber ingresado las cervezas el proveedor notifica para activar a todos los que se encuentren en espera.
    

Es importante destacar que el monitor tiene como atributos a la cantidad de estudiantes y a la cantidad de proveedores y se sincronizan las consultas y actualizaciones de los mismos ya que para los estudiantes es importante saber si hay al menos un proveedor en el sistema antes de ponerse en espera y debido a que los estudiantes se van a ir retirando a medida que se les agoten los tickets es importante para el proveedor saber cuando ya el ultimo estudiante ha finalizado.

Cuando el ultimo estudiante se retira llama a el método "**endDraftBeerParty**" del monitor cuya función básicamente es despertar a todos los hilos proveedores que se hayan puesto en espera.

En el método "main" de la clase principal "Proyecto3" se hace "join" a los proveedores debido a que según la logica del programa serían los últimos en terminar su ejecución. En caso que no haya proveedores se espera es por los estudiantes.

**Entidades**:
---------------

**Estudiante**:

Esta clase implementa a los hilos cuyo rol es el de "Consumidor". Para asignarles el nombre se usa una lista (static) como atributo de clase y que contiene un conjunto de nombres ya predefinidos y que se seleccionan de manera aleatoria en el constructor del objeto y una vez tomado el nombre se elimina de la lista.

La clase implementa la interfaz Runnable y la implementación del método "run" es como sigue:

*   Verifica la edad del estudiante, la cual se genera de manera aleatoria en el constructor, para determinar si el hilo puede o no continuar con la ejecución. Si la edad es menor a 18 el hilo finaliza su ejecución indicando que no puede continuar y la cantidad de tickets que tenía.
    

*   En caso que pueda continuar se debe verificar si existen proveedores ya que en caso negativo el estudiante se mantiene en ejecución mientras los barriles no se vacien. Si hay proveedores entonces el hilo estudiante estará en ejecución hasta que agote la cantidad de tickets generados aleatoriamente al momento de crearse.
    

*   En ambos casos el estudiante generará un pedido de cervezas al sistema con una cantidad aleatoria que va desde una (1) cerveza hasta máximo la cantidad de tickets que posea.
    

**Proveedor**:

Esta clase implementa a los hilos cuyo rol es el de "Productor". La clase implementa la interfaz Runnable y en el método "run" simplemente va a llamar al método **fillBarrelSystem** para intentar recargar los barriles del sistema mientras haya estudiantes.

**Barrel**:

Objetos que contienen la información de un barril especifico. Los métodos mas importantes de esta clase son:

*   **addBeer** usado para agregar una cantidad de cerveza pasada por parámetro al barril. El método retorna la cantidad de cerveza desbordada o cero (0) en caso que no hubiere.
    

*   **getBeers** se usa para servir una cantidad de cerveza especificada, o una cantidad menor si el barril no tiene el contenido suficiente. Actualiza el contenido del barril y retorna la cantidad que pudo ser servida.
    

*   **hasOverflow** retorna verdadero si ha habido desborde.
    

*   **getOverflow** para obtener la cantidad de cerveza que se ha desbordado.
    

*   **hasEnough** retorna verdadero si el contenido del barril es mayor o igual a la cantidad solicitada.
    

*   **isEmpty** retorna verdadero si el contenido del barril es igual a cero (0).
    

*   **isFull** retorna verdadero si el contenido del barril alcanzo su capacidad.
    

**BeerSystem**:

Implementa al sistema que contiene a los tres (3) barriles. Cuenta con una variedad de métodos para modelar el comportamiento del sistema de barriles. A continuación una breve descripción de los métodos mas importantes de esta clase:

*   **initialOverflow** para inicializar los barriles y gestionar las transferencias y las perdidas por desborde.
    

*   **transferBeer** ejecuta toda la lógica de transferencia entre los barriles. Este método es usado por **initialOverflow** al momento de inicializar el sistema y por **fillBarrels** para implementar la recarga de los barriles. Retorna la cantidad de cerveza que se pierde por desborde luego de la transferencia.
    

*   **serveBeer** este método es el que finalmente ejecuta el proceso de servir las cervezas solicitadas. Dependiendo de la disponibilidad de los barriles pueden ser todas las cervezas o una cantidad menor. El método retorna la cantidad que se logro servir.
    

*   **evaluateQuantity** retorna verdadero si alguno de los tres barriles tiene suficiente contenido como para satisfacer la cantidad solicitada completamente.
    

*   **getOptimalChoice** el cual dada una cantidad de cerveza se usa para decidir por cual barril es mas "eficiente" servirla basándose en el que tenga mas contenido.
    

*   **aOrC** se usa para determinar si se debe transferir la cerveza al barril "A" o al barril "C" cuando hay un desborde en el barril "B" evaluando el contenido de ambos barriles y aplicando las reglas de transferencia de cerveza entre los barriles.
    

*   **allBarrelsFull** retorna verdadero si los tres barriles han alcanzado su capacidad.
    

*   **allBarrelsEmpty** retorna verdadero si los tres barriles tienen 0 de contenido.
    

**DraftBeerParty (Monitor):**

Implementa la clase monitor que se encarga de mantener la gestión y sincronización del sistema de barriles.

*   **getParticipants** devuelve la cantidad de estudiantes que se encuentran todavía en la fiesta. Este método es “synchronized”.
    

*   **decreaseParticipants** decrementa en uno la cantidad de estudiantes presentes en la fiesta. Es usado cuando un estudiante va a finalizar su ejecución para ir actualizando la cantidad de estudiantes restantes en la fiesta. Este método es “synchronized”.
    

*   **getSuppliers** es un método “getter ” que retorna el numero de proveedores presentes en la fiesta.
    

*   **endDraftBeerParty** lo llama el ultimo estudiante presente en la fiesta cuando va a finalizar su ejecución con la finalidad de activar a los proveedores que se encuentren en estado de espera para que puedan finalizar también su ejecución cuando detecten que ya no quedan estudiantes.
    

**Consideraciones**:
--------------------

Se asume que el archivo con los datos de entrada se encuentra en el mismo directorio donde se encuentra la clase principal con el método main.

Si se pasa un número de proveedores menor o igual a cero se crean igual los estudiantes quienes se ejecutaran mientras existan cervezas el sistema de barriles. Una vez agotadas los hilos estudiantes terminarán su ejecución.