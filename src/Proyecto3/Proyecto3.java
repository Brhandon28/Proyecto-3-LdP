package Proyecto3;

import java.io.*;

public class Proyecto3 {
    public static void main(String[] args) {
        // 1. Procesar el archivo de entrada para obtener los datos de inicio
        if (args.length == 0) {
            System.err.println("Error: Debe ingresar nombre y ruta del archivo de entrada.");
            return;
        } else {
            try {
                // Nro de estudiantes y proveedores que se leeran del archivo
                int nEstudiantes = 0, nProveedores = 0;
                Barrel[] barrels = new Barrel[3];

                String line;
                String columnDelimiter = ",";
                // Nombre de archivo relativo a la carpeta principal del proyecto

                // --------------------------------------
                // TEMPORAL
                // OJO: Quizas falta corregir la ruta de entrada del archivo para no
                // tener que hardcodear "proyecto3" y que pueda conseguir
                // cualquier ruta que se le ingrese.
                String inputFile = args[0];

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));

                int cont = 0;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(columnDelimiter);

                    // Data de los barriles (3 primeras lineas)
                    if (cont < 3) {

                        if (data.length < 3) {
                            System.err.println("Error: El archivo no posee el formato correcto.");
                            return;
                        }

                        // Id del barril
                        if (!data[0].isEmpty() && !data[0].trim().equals("A")
                                && !data[0].trim().equals("B") && !data[0].trim().equals("C")) {
                            System.err.println("Error: El archivo no posee el formato correcto.");
                            return;
                        }

                        // Capacidad del barril
                        int capacity = 0;
                        if (!data[1].isEmpty()) {
                            try {

                                capacity = Integer.parseInt(data[1].trim());

                            } catch (NumberFormatException e) {
                                System.err.println("Error: El archivo no posee el formato correcto.");
                                return;
                            }

                        }

                        // Contenido del barril
                        int beers = 0;
                        if (!data[2].isEmpty()) {
                            try {

                                beers = Integer.parseInt(data[2].trim());

                            } catch (NumberFormatException e) {
                                System.err.println("Error: El archivo no posee el formato correcto.");
                                return;
                            }

                        }

                        String id = data[0].trim();

                        // 2. Instanciar los barriles obtenidos de la lectura del archivo
                        if (id.equals("A")) {
                            barrels[0] = new Barrel(id, capacity, beers);
                        } else if (id.equals("B")) {
                            barrels[1] = new Barrel(id, capacity, beers);
                        } else if (id.equals("C")) {
                            barrels[2] = new Barrel(id, capacity, beers);
                        }

                    } else if (cont < 5) {

                        if (data.length < 2) {
                            System.err.println("Error: El archivo no posee el formato correcto.");
                            return;
                        }

                        if (!data[0].isEmpty() && !data[0].trim().equals("Estudiantes")
                                && !data[0].trim().equals("Proveedores")) {
                            System.err.println("(empty)-Error: El archivo no posee el formato correcto.");
                            return;
                        }

                        // Cantidad de estudiantes o proveedores
                        if (!data[1].isEmpty()) {
                            try {

                                if (data[0].trim().equals("Estudiantes")) {
                                    nEstudiantes = Integer.parseInt(data[1].trim());

                                } else if (data[0].trim().equals("Proveedores")) {
                                    nProveedores = Integer.parseInt(data[1].trim());
                                }

                            } catch (NumberFormatException e) {
                                System.err.println("Error: El archivo no posee el formato correcto.");
                                return;
                            }
                        }

                    } else {
                        break;
                    }

                    cont++;
                }

                // Verifica que se hayan leido al menos 5 lineas del archivo de entrada
                if (cont < 5) {
                    System.err.println("Error: El archivo no posee el formato correcto.");
                    return;
                }

                System.out.println("Nro. de Estudiantes: " + nEstudiantes + "\n" +
                                   "Nro. de Proveedores: " + nProveedores + "\n" +
                                    "----------------------------------------");

                // 4. Se instancia el "sistema de barriles" y se gestiona el
                //    posible desborde inicial.
                BeerSystem bs = new BeerSystem(barrels);
                int desborde = bs.initialOverflow();

                if (bs.invalidBarrelsState()) {
                    System.err.println("--------------------------------------\n" +
                            "Lo sentimos, no es posible realizar la fiesta "+
                                       "porque el sistema de barriles presenta una falla que "+
                                       "no permite servir las cervezas.\n" +
                            "--------------------------------------");
                    System.out.println(bs + "Desborde: " + desborde);
                    return;
                }

                // 5. Se crea el objeto Monitor (DraftBeerParty): Este objeto tiene
                // como atributos al contador de estudiantes y proveedores
                DraftBeerParty party = new DraftBeerParty(bs, nEstudiantes, nProveedores);

                // 6. Se crean los arreglos de Threads (estudiantes y proveedores),
                // se instancian y lanzan a ejecucion

                Thread[] proveedores;
                if(nProveedores > 0) {

                    proveedores = new Thread[nProveedores];
                    for (int i = 0; i < proveedores.length; i++) {
                        proveedores[i] = new Thread(new Proveedor(party), ""+(i+1));
                        proveedores[i].start();
                    }

                }else {
                    System.out.println("Por alguna razon no pudieron asistir los "+
                            "proveedores de cerveza a la fiesta.\n" +
                            "Si llegan estudiantes se les serviran unicamente las "+
                            "cervezas que hayan en los barriles.\n" +
                            "Al terminarse esa cerveza se terminará la fiesta.\n");

                    proveedores = new Thread[0];
                }


                Thread[] estudiantes;
                if(nEstudiantes > 0) {
                    estudiantes = new Thread[nEstudiantes];
                    System.out.println("Llegan a la fiesta los siguientes estudiantes:\n"+
                                      "-------------------------------------------------");
                    for (int i = 0; i < estudiantes.length; i++) {
                        Estudiante e = new Estudiante(party);
                        System.out.println((i+1) + ". " + e);
                        estudiantes[i] = new Thread(e, e.getName());
                        estudiantes[i].start();
                    }
                }else {
                    System.out.println("Por alguna razon no asistieron estudiantes a la fiesta.\n");
                    estudiantes = new Thread[0];
                }

                // 7. Se hacen los join() para que el "main" espere a que finalicen
                // los Proveedores (en caso que hubiesen proveedores)
                for (int i = 0; i < proveedores.length; i++) {
                    try {
                        proveedores[i].join();
                    } catch (InterruptedException e) {
                    }
                }

                // Si no hay proveedores pero hay estudiantes entonces
                // espera es por los estudiantes
                if((nProveedores <= 0) && (nEstudiantes > 0)){

                    for (int i = 0; i < estudiantes.length; i++) {
                        try {
                            estudiantes[i].join();
                        } catch (InterruptedException e) {
                        }
                    }
                }

                // 8. Se imprime la cantidad desbordada
                System.out.println("\nSe termina la fiesta.\n" +
                        bs + "\n" +
                        "El sistema de barriles reporta una perdida por desborde de: " + bs.getBeerLost());

            } catch (IOException e) {
                System.err.println("Ocurrió un Error al intentar leer el archivo.");
                // e.printStackTrace();
            }

        }

    }
}