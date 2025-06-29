public class Barrel {
    private String id;
    private int capacity;
    private int beer;

    public Barrel(String id, int capacity, int beer) {
        this.id = id;
        this.capacity = capacity;
        this.beer = beer;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBeer() {
        return beer;
    }

    public void addBeer(int cantidad) {
        if (beer + cantidad <= capacity) {
            beer += cantidad;
        }
        // else {
        // // Crear funcion para traspasar exceso
        // }
    }

    // public void vaciar(int cantidad) {
    // if (beer - cantidad >= 0) {
    // beer -= cantidad;
    // } else {
    // System.out.println("No se puede vaciar más allá de la cantidad actual.");
    // }
    // }
}