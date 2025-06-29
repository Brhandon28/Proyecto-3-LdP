import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

public class LeerArchivo {
    public static class DatosArchivo {
        public Barrel[] barriles;
        public int estudiantes;
        public int proveedores;

        public DatosArchivo(Barrel[] barriles, int estudiantes, int proveedores) {
            this.barriles = barriles;
            this.estudiantes = estudiantes;
            this.proveedores = proveedores;
        }
    }

    public static DatosArchivo leer(String nombreArchivo) {
        Barrel[] barriles = new Barrel[3]; // Barriles A, B y C
        int estudiantes = 0, proveedores = 0;

        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            System.err.println("El nombre del archivo no puede ser nulo o vacío.");
            return null;
        }

        try {
            List<String> lineas = Files.readAllLines(Paths.get(nombreArchivo));
            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] datos = linea.split(",");

                if (i < 3) {
                    String id = datos[0].trim();
                    int capacidad = Integer.parseInt(datos[1].trim());
                    int cerveza = Integer.parseInt(datos[2].trim());

                    if (id == null || id.isEmpty() || !(id.equals("A") || id.equals("B") || id.equals("C"))) {
                        System.err.println("El ID del barril no puede ser nulo, vacio o diferente a A, B y C.");
                        System.exit(1);
                    }
                    if (capacidad <= 0) {
                        System.err.println("La capacidad del barril debe ser un número entero positivo.");
                        System.exit(1);
                    }
                    if (cerveza < 0) {
                        System.err.println("La cantidad de cerveza debe ser un número entero positivo.");
                        System.exit(1);
                    }
                    switch (id) {
                        case "A":
                            barriles[0] = new Barrel(id, capacidad, cerveza);
                            break;
                        case "B":
                            barriles[1] = new Barrel(id, capacidad, cerveza);
                            break;
                        case "C":
                            barriles[2] = new Barrel(id, capacidad, cerveza);
                            break;
                    }
                } else {
                    if (datos[0].trim().equals("Estudiantes")) {
                        estudiantes = Integer.parseInt(datos[1].trim());
                    } else if (datos[0].trim().equals("Proveedores")) {
                        proveedores = Integer.parseInt(datos[1].trim());
                    }
                }
            }
            return new DatosArchivo(barriles, estudiantes, proveedores);

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return null;
    }
}