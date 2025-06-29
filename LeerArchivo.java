import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

public class LeerArchivo {
    public static void main(String[] args) {
        if (args.length == 0 || args[0] == null || args[0].isEmpty()) {
            System.err.println("El nombre del archivo no puede ser nulo o vacío.");
            return;
        }
        try {
            List<String> lineas = Files.readAllLines(Paths.get(args[0]));
            for (String linea : lineas) {
                String[] datos = linea.split(",");

                // Leer los barriles
                if (lineas.indexOf(linea) < 3) {
                    String id = datos[0].trim();
                    int capacidad = Integer.parseInt(datos[1].trim());
                    int cerveza = Integer.parseInt(datos[2].trim());
                    // System.out.println(id + ", " + datos[1].trim() + ", " + datos[2].trim());

                    // Validar el ID del barril
                    if (id == null || id.isEmpty() || !(id.equals("A") || id.equals("B") || id.equals("C"))) {
                        System.err.println("El ID del barril no puede ser nulo, vacio o diferente a A, B y C.");
                        System.exit(1);
                    }
                    // Leer los demas datos
                } else {
                    if (datos[0].trim().equals("Estudiantes")) {
                        int estudiantes = Integer.parseInt(datos[1].trim());
                    } else if (datos[0].trim().equals("Proveedores")) {
                        int proveedores = Integer.parseInt(datos[1].trim());
                    }

                
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}