public class Proyecto3 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("ERROR: No proporciona ningun archivo.");
            return;
        }

        String nombreArchivo = args[0];
        Barrel[] barriles = new Barrel[3];
        int estudiantes;
        int proveedores;

        LeerArchivo.DatosArchivo datos = LeerArchivo.leer(nombreArchivo);
        if (datos == null) {
            System.err.println("ERROR: No se pudo leer el archivo o los datos son invalidos.");
            return;
        }
        barriles = datos.barriles;
        estudiantes = datos.estudiantes;
        proveedores = datos.proveedores;

        // Imprimir barriles
        for (Barrel barril : barriles) {
            if (barril != null) {
                barril.print();
            }
        }

        System.out.println("Estudiantes: " + estudiantes);
        System.out.println("Proveedores: " + proveedores);
    }
}