public class Barrel {
    private String id;
    private int capacity;
    private int beer;

    public int excessBarrel() {
        if (beer > capacity) {
            return beer - capacity;
        }

        return 0;
    }

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
        beer += cantidad;
    }

    public void removeBeer(int cantidad) {
        beer -= cantidad;
    }

    public void print() {
        System.out.println("Barril ID: " + id + ", Capacidad: " + capacity + ", Cerveza actual: " + beer);
    }
}