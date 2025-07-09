package Proyecto3;

import java.util.Arrays;
import java.util.Random;

public class BeerSystem {
    private Barrel[] barrels = new Barrel[3];
    private int beerLost;

    public BeerSystem(Barrel[] barrels) {
        this.barrels = barrels;
    }

    public BeerSystem(Barrel bA, Barrel bB, Barrel bC) {
        this.barrels[0] = bA;
        this.barrels[1] = bB;
        this.barrels[2] = bC;
    }

    public int getBeerLost() {
        return beerLost;
    }

    public void setBeerLost(int quantity) {
        this.beerLost += quantity;
    }

    public int initialOverflow() {
        int lostBeer = 0;
        boolean overflow = true;

        while (overflow) {
            if (barrels[0].hasOverflow()) {
                lostBeer += transferBeer("A", barrels[0].getOverflow());
                barrels[0].setContent(barrels[0].getCapacity()); // Aseguramos que el barril A no tenga overflow
            } else if (barrels[1].hasOverflow()) {
                lostBeer += transferBeer("B", barrels[1].getOverflow());
                barrels[1].setContent(barrels[1].getCapacity()); // Aseguramos que el barril B no tenga overflow
            } else if (barrels[2].hasOverflow()) {
                lostBeer += transferBeer("C", barrels[2].getOverflow());
                barrels[2].setContent(barrels[2].getCapacity()); // Aseguramos que el barril C no tenga overflow
            } else {
                overflow = false; // Si no hay desbordes, salimos del bucle
            }

        }

        // Actualiza la cantidad de perdida por desborde
        this.setBeerLost(lostBeer);

        return lostBeer;
    }

    // Ejecuta toda la lógica de transferencia entre los barriles y
    // retorna la cantidad de cerveza que se pierde por desborde
    public int transferBeer(String barrelId, int excess) {
        int beerLost = 0;

        if (excess > 0) {
            switch (barrelId) {
                case "A":
                case "C":
                    if (!barrels[1].isFull()) {
                        excess = barrels[1].addBeer(excess);
                        beerLost = transferBeer("B", excess);
                    } else if (barrels[1].isFull() && (!barrels[0].isFull() ||
                            !barrels[2].isFull())) {
                        if (barrelId.equals("A") && barrels[0].isFull()) {
                            if (barrels[0].getContent() > barrels[2].getContent()) {
                                excess = barrels[2].addBeer(excess);
                                beerLost = transferBeer("C", excess);
                            }
                        } else if (barrelId.equals("C") && barrels[2].isFull()) {
                            if (barrels[2].getContent() > barrels[0].getContent()) {
                                excess = barrels[0].addBeer(excess);
                                beerLost = transferBeer("A", excess);
                            }
                        }

                    } else {
                        beerLost += excess;
                        excess = 0;
                    }
                    break;
                case "B":
                    if ((barrels[0].getContent() < barrels[2].getContent()) && !barrels[0].isFull()) {
                        excess = barrels[0].addBeer(excess);
                        beerLost = transferBeer("A", excess);
                    } else if ((barrels[2].getContent() < barrels[0].getContent()) && !barrels[2].isFull()) {
                        excess = barrels[2].addBeer(excess);
                        beerLost = transferBeer("C", excess);
                    } else {
                        beerLost += excess;
                        excess = 0;
                    }
                    break;
                default:
                    break;
            }
        }

        return beerLost;
    }

    private int aOrC() {

        // Barril "A" tiene espacio y el barril "C" esta full
        if (!barrels[0].isFull() && barrels[2].isFull()) {
            return 0;

            // Barril "C" tiene espacio y el barril "A" esta full
        } else if (barrels[0].isFull() && !barrels[2].isFull()) {
            return 2;
            // Ninguno de los dos esta full
        } else if (!barrels[0].isFull() && !barrels[2].isFull()) {
            // Retorna al que le falte mas cerveza
            if (barrels[0].beersToReachCap() >= barrels[2].beersToReachCap()) {
                return 0;
            } else {
                return 2;
            }
        } else {
            return 0;
        }

    }

    // Calcula una cantidad aleatoria de cerveza entre 1 y la suma
    // de las capacidades de los barriles
    public int calcQuantBeer() {
        // Suma de las capacidades de los barriles
        int totalBeer = barrels[0].getCapacity() + barrels[1].getCapacity() + barrels[2].getCapacity();

        // Genera un numero aleatorio entre 1 y la suma de las capacidades
        return (new Random()).nextInt(totalBeer) + 1;
    }

    public int fillBarrels(int totalBeer) {
        // int totalBeer = calcQuantBeer();
        int barrel = aOrC();
        int excess;
        int lostBeer = 0;

        excess = barrels[barrel].addBeer(totalBeer);

        if (excess > 0) {
            lostBeer = transferBeer(barrels[barrel].getId(), excess);
        }

        return lostBeer;
    }

    public int serveBeer(int quantity) {

        if (!allBarrelsEmpty()) {

            if (evaluateQuantity(quantity)) {
                // Entra aqui si al menos 1 barril tiene suficiente
                // cerveza para servir la cantidad solicitada

                // Barril "A"
                if (barrels[0].hasEnough(quantity)) {
                    System.out
                            .println("Barril 'A' sirvio " + quantity + ((quantity == 1) ? " cerveza." : " cervezas."));
                    return barrels[0].getBeers(quantity);

                    // Barril "C"
                } else if (barrels[2].hasEnough(quantity)) {
                    System.out
                            .println("Barril 'C' sirvio " + quantity + ((quantity == 1) ? " cerveza." : " cervezas."));
                    return barrels[2].getBeers(quantity);

                    // Barril "B"
                } else if (barrels[1].hasEnough(quantity)) {
                    System.out
                            .println("Barril 'B' sirvio " + quantity + ((quantity == 1) ? " cerveza." : " cervezas."));
                    return barrels[1].getBeers(quantity);
                }

            } else {
                // Si entra aqui entonces busca la solucion mas optima
                // que viene dada por el barril que pueda servir la mayor
                // cantidad de cerveza
                return getOptimalChoice(quantity);

            }

        }

        // Llegaria hasta aqui si todos los barriles estan vacios
        return 0;
    }

    // Devuelve verdadero si alguno de los 3 barriles tiene
    // contenido suficiente para servir la cantidad de cerveza
    // indicada
    public boolean evaluateQuantity(int quantity) {

        return (barrels[0].hasEnough(quantity) ||
                barrels[1].hasEnough(quantity) ||
                barrels[2].hasEnough(quantity));

    }

    // Dado un identificador de barril devuelve el
    // barril correspondiente del arreglo de barriles
    public Barrel getBarrel(String id) {

        if (!id.isEmpty() && !id.equals("A") && !id.equals("B") && !id.equals("C")) {
            return null;
        }

        int index = 0;

        if (id.equals("A")) {
            ;
        } else if (id.equals("B")) {
            index = 1;
        } else if (id.equals("C")) {
            index = 2;
        }

        return barrels[index];
    }

    // Dada una cantidad de cervezas se selecciona el barril
    // que mayor contenido tenga en ese momento y se sirve
    // esa cantidad y se actuializa el estado del barril.
    private int getOptimalChoice(int quantity) {

        // Si devulve cero es porque no pudo servir por
        // ninguno de los 3 barriles
        int result = 0, indiceMayor = 0;

        // Asume que la mejor opcion es el barril "A"
        result = barrels[0].getContent();

        if (barrels[1].getContent() > result) {
            result = barrels[1].getContent();
            indiceMayor = 1;
        }

        if (barrels[2].getContent() > result) {
            result = barrels[2].getContent();
            indiceMayor = 2;
        }

        // Devuelve la cantidad de cerveza servida
        barrels[indiceMayor].getBeers(quantity);

        System.out.println("No habia suficiente cerveza para completar la orden (" + quantity + " cervezas).\n" +
                "Barril '" + barrels[indiceMayor].getId() + "' sirvio " + result + " cervezas.");

        return result;
    }

    // Devuelve verdadero si los 3 barriles estan vacios
    public boolean allBarrelsEmpty() {
        return (barrels[0].isEmpty() && barrels[1].isEmpty() && barrels[2].isEmpty());
    }

    public boolean allBarrelsFull() {
        return (barrels[0].isFull() && barrels[1].isFull() && barrels[2].isFull());
    }

    // Si los tres barriles tienen capacidad cero (0) entonces
    // no se pueden llenar ni tampoco se puede servir por ningun barril
    public boolean invalidBarrelsState() {
        return (barrels[0].getCapacity() == 0 ||
                barrels[1].getCapacity() == 0 ||
                barrels[2].getCapacity() == 0);
    }

    @Override
    public String toString() {
        return "-----------\n" +
                " Barriles:\n" +
                "-----------\n" +
                Arrays.toString(barrels) + "\n";
    }
}
