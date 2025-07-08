package Proyecto3;

public class Barrel {
    private String id;
    private int capacity;
    private int content;

    public Barrel(String id, int capacity, int content) {
        this.id = id;
        this.capacity = capacity;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int newCapacity) {
        if (newCapacity >= 0)
            this.capacity = newCapacity;
    }

    public int getContent() {
        return this.content;
    }

    public void setContent(int newContent) {

        if(newContent < 0) return;

        this.content = Math.min(newContent, this.capacity);
    }

    public int addBeer(int newcontent) {
        int transfer = 0;

        if (newcontent >= 0) {
            content += newcontent;
        }

        if (content > capacity) {
            transfer = content - capacity;
            content = capacity;
        }

        return transfer;
    }


    // Devuelve la cantidad que le falta al barril para llenarse
    public int beersToReachCap() {
        return capacity - content;
    }


    // Se usa para servir una cantidad "quantity" de cerveza
    // por este barril y actualizar el contenido.
    public int getBeers(int quantity) {

        // Si retorna 0 es porque no pudo servir ninguna cerveza
        int result = 0;

        if (!isEmpty()) {
            if (hasEnough(quantity)) {
                // Tiene suficientes cervezas como para servir toda
                // la orden.
                this.setContent(this.getContent() - quantity);
                result = quantity;
            }else {
                // Devuelve la cantidad que sirvió (el contenido completo)
                // y actualiza el contenido del barril
                result = getContent();
                this.setContent(0); // El barril se vacia
            }
        }

        return result;
    }

    public boolean isFull() {
        return content >= capacity;
    }

    public boolean hasOverflow() {
        return content > capacity;
    }

    // Retorna un numero positivo mayor que cero cuando la
    // capacidad del barril ha sido superada
    public int getOverflow() {
        return this.content - this.capacity;
    }

    public boolean isEmpty() {
        return content == 0;
    }

    public boolean hasEnough(int quantity) {
        return this.getContent() >= quantity;
    }

    public void print() {
        System.out.println("Barril ID: " + id + ", Capacidad: " + capacity + ", Contenido: " + content);
    }

    @Override
    public String toString() {
        return "Barril {" +
                "Id: '" + getId() + '\'' +
                ", Capacidad: " + getCapacity() +
                ", Contenido: " + getContent() +
                '}';
    }
}