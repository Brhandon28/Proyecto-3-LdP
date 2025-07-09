package Proyecto3;

public class Proveedor implements Runnable {

    private DraftBeerParty party;

    public Proveedor(DraftBeerParty monitor){
        this.party = monitor;
    }
    @Override
    public void run() {
        try {
            while(this.party.getParticipants() > 0) {
                party.fillBarrelSystem();

                // Se añade algo de espera aleatoria para la simulación
                Thread.sleep((int)(Math.random() * 700));
            }

            System.out.println("Proveedor ("+ Thread.currentThread().getName() +"):  Finaliza su jornada y se retira.");

        }catch (InterruptedException e){}

    }
}
