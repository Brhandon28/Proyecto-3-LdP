package Proyecto3;

import java.util.Random;

// Monitor
public class DraftBeerParty {
    private BeerSystem barrelSystem;
    private int participants;
    private int suppliers;

    public DraftBeerParty(BeerSystem bs, int numEstudiantes, int numProveedores) {
        this.barrelSystem = bs;
        // Contador de estudiantes
        this.participants = numEstudiantes;
        // Cant. de Proveedores
        this.suppliers = numProveedores;
    }

    public synchronized void fillBarrelSystem() throws InterruptedException {
        // Si todos los barriles estan llenos debe esperar
        while(barrelSystem.allBarrelsFull() && this.getParticipants() > 0) {
            wait();
        }

        if(!barrelSystem.allBarrelsFull()){
            // El proveedor ingresa una cantidad aleatoria de cerveza
            // al sistema entre 1 y 20
            int quantity = (new Random()).nextInt(21) + 1;

            int overflow = barrelSystem.fillBarrels(quantity);

            barrelSystem.setBeerLost(overflow);

            System.out.println("Proveedor (" + Thread.currentThread().getName() + "): recarga el sistema de barriles " +
                    "con " + quantity + ((quantity == 1) ? " cerveza.\n" : " cervezas.\n") +
                    this.barrelSystem + //"\n"+
                    "Desborde: "+  overflow + "\n");
        }

        notifyAll();
    }

    public synchronized int consume(int quantity) throws InterruptedException {

        // Si todos los barriles estan vacios debe esperar
        while(barrelSystem.allBarrelsEmpty()) {
            wait();
        }

        int beers = barrelSystem.serveBeer(quantity);
        int beersRemain = quantity - beers;
        // Mientras no se complete la cantidad de cervezas pedidas
        while(beersRemain > 0) {
            /*
            System.out.println(Thread.currentThread().getName() +
                    " beersRemain: " + beersRemain + "\n");
            //*/

            if(!barrelSystem.allBarrelsEmpty()) {
                beers = barrelSystem.serveBeer(beersRemain);
                beersRemain -= beers;
                notifyAll();
            }else {
                // Espera si hay proveedores en la fiesta
                if(this.getSuppliers() > 0) {
                    wait();
                // Sino termina
                }else {
                    break;
                }
            }

        }

        // En este punto ya fue servida la cantidad total
        // de cervezas que habia pedido.
        notifyAll();

        System.out.println("Soy "+ Thread.currentThread().getName() +
                    ": 'pedi' " + quantity + ((quantity == 1) ? " cerveza " : " cervezas ") +
                    "y ya me sirvieron " + (beersRemain > 0 ? "todas las que pudieron.\n" : "todas.\n") +
                    this.barrelSystem.toString());

        // Si me quedaron cervezas pendientes actualiza nuevamente
        // la cantidad de tickets del estudiante
        if(beersRemain > 0) {
            return quantity - beersRemain;
        }

        return beers;
    }

    public synchronized int getParticipants() {
        return this.participants;
    }

    public synchronized void decreaseParticipants() {
        this.participants--;
    }

    // Retorna la cantidad de proveedores presentes
    public synchronized int getSuppliers() {
        return this.suppliers;
    }

    public synchronized boolean beerSystemEmpty() {
        return this.barrelSystem.allBarrelsEmpty();
    }

    public synchronized void endDraftBeerParty() {
        System.out.println("Ultimo participante se retira.\n" +
                "Se anuncia que la fiesta terminó.\n" +
                this.barrelSystem);
        notifyAll();
    }

}
