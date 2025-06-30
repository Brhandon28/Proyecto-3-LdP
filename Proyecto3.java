public class Proyecto3 {

    public static int excess(Barrel[] barriles) {
        excessRecursive(barriles, 0);
        if (barriles[0].excessBarrel() > 0 || barriles[1].excessBarrel() > 0 || barriles[2].excessBarrel() > 0) {
            return barriles[0].excessBarrel() + barriles[1].excessBarrel() + barriles[2].excessBarrel();
        }
        return 0;
    }

    private static void excessRecursive(Barrel[] barriles, int index) {
        if (index >= barriles.length) {
            return;
        }
        Barrel barril = barriles[index];
        int exceso = barril.excessBarrel();
        if (exceso > 0) {
            // Buscar el siguiente barril disponible para agregar el exceso
            switch (barril.getId()) {
                case "A":
                    barriles[0].removeBeer(exceso);
                    barriles[1].addBeer(exceso);
                    break;
                case "B":
                    barriles[1].removeBeer(exceso);
                    if (barriles[0].getBeer() <= barriles[2].getBeer()) {
                        barriles[0].addBeer(exceso);
                    } else {
                        barriles[2].addBeer(exceso);
                    }
                    break;
                case "C":
                    barriles[2].removeBeer(exceso);
                    barriles[1].addBeer(exceso);
                    break;
            }
        }
        excessRecursive(barriles, index + 1);
    }

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
        int perdido = excess(barriles);

        // Imprimir barriles
        for (Barrel barril : barriles) {
            if (barril != null) {
                barril.print();
            }
        }

        System.out.println("Estudiantes: " + estudiantes);
        System.out.println("Proveedores: " + proveedores);

        System.out.println("Cerveza perdida: " + perdido);
    }
}