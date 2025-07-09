package Proyecto3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Estudiante implements Runnable {
  private DraftBeerParty party;
  private String name;
  private int age;
  private int ticket;

  public static ArrayList<String> nombres = new ArrayList<>(Arrays.asList(
          "Lionel", "Pepe", "Neymar", "Roberto", "Kevin", "Ronald",
          "Luis", "Virgil", "Karim", "Luka", "James", "Harry",
          "Jesus", "Peter", "Juan", "Antoine", "Alexia", "Megan",
          "Sam", "Ada", "Marta", "Aitana", "Vivianne", "Lucy", "Wendie",
          "Carolina", "Cristina", "Francisca", "Paula", "Maria", "Alex",
          "Patricia", "Jennifer", "Ivanna"));

  public Estudiante(DraftBeerParty monitor) {

    this.party = monitor;

    Random rand = new Random();
    int randomIndex = rand.nextInt(nombres.size());
    this.name = nombres.get(randomIndex); // Selecciona un nombre aleatorio de la lista
    nombres.remove(randomIndex); // Elimina el nombre seleccionado de la lista

    this.age = rand.nextInt(25) + 16; // Edad entre 16 y 40

    this.ticket = rand.nextInt(31) + 1; // Ticket entre 1 y 30

  }

  @Override
  public void run() {
    if(getAge() < 18) {
      // Es menor de edad. Se tiene que retirar de la fiesta
      party.decreaseParticipants();
      int participantes = party.getParticipants();

      System.out.println(
              "\n---------------------------\n" +
              "Soy " + getName() + " (estudiante), " +
              "tenia " + getTicket() + " tickets " +
              "pero me retiro porque tengo " + getAge() + " años.\n"+
              "Ahora quedan "+ participantes + " participantes en la fiesta."+
              "\n---------------------------\n");

      return;
    }

    try {
      // si hay proveedores en la fiesta
      if(this.party.getSuppliers() > 0){

        // se queda en la fiesta pidiendo cervezas hasta que se le agoten los tickets
        while(getTicket() > 0) {
          // Estudiante pide una cantidad aleatoria de cervezas
          // entre 1 y la cantidad de tickets que tenga en ese momento
          int order = (new Random()).nextInt(this.getTicket() + 1) + 1;

          //int quantity = getTicket();
          //int consumedTickets = this.party.consume(order);
          this.party.consume(order);

          // Decrementa el numero de tickets
          //this.decreaseTickets(consumedTickets);
          this.decreaseTickets(order);

          if(getTicket() > 0) {
            System.out.println("Soy " + this.getName() + " ahora me " +
                    ((getTicket() == 1) ? "queda 1 ticket.\n" : ("quedan " + getTicket()) + " tickets.\n"));
          } else {
            System.out.println("Soy "+ this.getName() +
                    ", ya se me agotaron los tickets.\n");
            continue;
          }

          // Se añade algo de espera aleatoria para la simulación
          Thread.sleep((int)(Math.random() * 2500));
        }

      // si por el contrario no hay proveedores en la fiesta
      }else {
        // Se queda en la fiesta pidiendo cerveza hasta que los barriles esten vacios
        while(!this.party.beerSystemEmpty() && getTicket() > 0) {
          int order = (new Random()).nextInt(this.getTicket() + 1) + 1;
          int ticketsConsumed = this.party.consume(order);

          // Decrementa el numero de tickets
          this.decreaseTickets(ticketsConsumed);

          if(getTicket() > 0) {
            System.out.println("Soy " + this.getName() + " ahora me " +
                    ((getTicket() == 1) ? "queda 1 ticket.\n" : ("quedan " + getTicket()) + " tickets.\n"));
          } else {
            System.out.println("Soy "+ this.getName() +
                    ", ya se me agotaron los tickets.\n");
            continue;
          }

          // Se añade algo de espera aleatoria para la simulación
          Thread.sleep((int)(Math.random() * 1800));
        }
      }

      this.party.decreaseParticipants();
      int participantes = this.party.getParticipants();

      System.out.println("Soy "+ this.getName() + " y ya me retiro de la fiesta.\n" +
              "Ahora " + ((participantes == 1) ? "queda 1 participante " : ("quedan " + participantes) + " participantes ") +
              "en la fiesta.\n" + this);

      //*
      // Si es el ultimo estudiante despierta a todos por si
      // acaso los proveedores estan todos en cola
      if(participantes == 0) {
        party.endDraftBeerParty();
      }
      //*/

    }catch (InterruptedException e){}


  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public int getTicket() {
    return ticket;
  }

  public void decreaseTickets(int spent) {
    this.ticket = (spent > this.ticket) ? 0 : this.ticket - spent;
  }

  @Override
  public String toString() {
    return "Estudiante { " +
            "nombre: '" + getName() + '\'' +
            ", edad: " + getAge() +
            ", tickets: " + getTicket() + " }\n";
  }

}
