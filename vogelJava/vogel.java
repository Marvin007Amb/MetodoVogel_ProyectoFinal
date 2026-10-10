import java.util.*;

public class Vogel {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        limpiarPantalla();

        System.out.println("======================================");
        System.out.println("     METODO DE APROXIMACION DE VOGEL");
        System.out.println("======================================");

        // Cantidad de orígenes y destinos
        System.out.print("\nIngrese numero de origenes: ");
        int filas = sc.nextInt();

        System.out.print("Ingrese numero de destinos: ");
        int columnas = sc.nextInt();

        // Matriz de costos
        double[][] costos = new double[filas][columnas];

        System.out.println("\nIngrese la matriz de costos:");

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(
                    "Costo O" + (i + 1) +
                    " -> D" + (j + 1) + ": "
                );
                costos[i][j] = sc.nextDouble();
            }
        }

        // Oferta
        double[] oferta = new double[filas];

        System.out.println("\nIngrese la oferta:");

        for (int i = 0; i < filas; i++) {
            System.out.print("Oferta O" + (i + 1) + ": ");
            oferta[i] = sc.nextDouble();
        }

        // Demanda
        double[] demanda = new double[columnas];

        System.out.println("\nIngrese la demanda:");

        for (int j = 0; j < columnas; j++) {
            System.out.print("Demanda D" + (j + 1) + ": ");
            demanda[j] = sc.nextDouble();
        }

        // Ejecutar Vogel
        metodoVogel(costos, oferta, demanda);

        sc.close();
    }

    public static void metodoVogel(
            double[][] costos,
            double[] oferta,
            double[] demanda) {

        int filas = costos.length;
        int columnas = costos[0].length;

        double[][] asignacion =
                new double[filas][columnas];

        double totalOferta = sumar(oferta);
        double totalDemanda = sumar(demanda);

        // ==========================================
        // BALANCEAR EL PROBLEMA
        // ==========================================

        if (totalOferta != totalDemanda) {

            System.out.println("\nEl problema esta desbalanceado.");

            if (totalOferta > totalDemanda) {

                double diferencia =
                        totalOferta - totalDemanda;

                System.out.println(
                    "Se agregara un destino ficticio."
                );

                double[][] nuevosCostos =
                        new double[filas][columnas + 1];

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        nuevosCostos[i][j] =
                                costos[i][j];
                    }
                }

                costos = nuevosCostos;

                demanda = Arrays.copyOf(
                        demanda,
                        columnas + 1
                );

                demanda[columnas] = diferencia;

                columnas++;

            } else {

                double diferencia =
                        totalDemanda - totalOferta;

                System.out.println(
                    "Se agregara un origen ficticio."
                );

                double[][] nuevosCostos =
                        new double[filas + 1][columnas];

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        nuevosCostos[i][j] =
                                costos[i][j];
                    }
                }

                costos = nuevosCostos;

                oferta = Arrays.copyOf(
                        oferta,
                        filas + 1
                );

                oferta[filas] = diferencia;

                filas++;
            }
        }

        // ==========================================
        // METODO DE VOGEL
        // ==========================================

        int paso = 1;

        while (sumar(oferta) > 0 &&
               sumar(demanda) > 0) {

            System.out.println(
                "\n================================"
            );

            System.out.println(
                "PASO " + paso
            );

            System.out.println(
                "================================"
            );

            // --------------------------------------
            // Penalizaciones de filas
            // --------------------------------------

            double[] penalFila =
                    new double[filas];

            Arrays.fill(penalFila, -1);

            for (int i = 0; i < filas; i++) {

                if (oferta[i] <= 0)
                    continue;

                ArrayList<Double> valores =
                        new ArrayList<>();

                for (int j = 0; j < columnas; j++) {

                    if (demanda[j] > 0) {
                        valores.add(costos[i][j]);
                    }
                }

                if (valores.size() >= 2) {

                    Collections.sort(valores);

                    penalFila[i] =
                            valores.get(1) -
                            valores.get(0);

                } else if (valores.size() == 1) {

                    penalFila[i] =
                            valores.get(0);
                }
            }

            // --------------------------------------
            // Penalizaciones de columnas
            // --------------------------------------

            double[] penalColumna =
                    new double[columnas];

            Arrays.fill(penalColumna, -1);

            for (int j = 0; j < columnas; j++) {

                if (demanda[j] <= 0)
                    continue;

                ArrayList<Double> valores =
                        new ArrayList<>();

                for (int i = 0; i < filas; i++) {

                    if (oferta[i] > 0) {
                        valores.add(costos[i][j]);
                    }
                }

                if (valores.size() >= 2) {

                    Collections.sort(valores);

                    penalColumna[j] =
                            valores.get(1) -
                            valores.get(0);

                } else if (valores.size() == 1) {

                    penalColumna[j] =
                            valores.get(0);
                }
            }

            // --------------------------------------
            // Mostrar penalizaciones
            // --------------------------------------

            System.out.println("\nPenalizaciones de filas:");

            for (int i = 0; i < filas; i++) {
                System.out.println(
                    "F" + (i + 1) +
                    " = " + penalFila[i]
                );
            }

            System.out.println(
                "\nPenalizaciones de columnas:"
            );

            for (int j = 0; j < columnas; j++) {
                System.out.println(
                    "D" + (j + 1) +
                    " = " + penalColumna[j]
                );
            }

            // --------------------------------------
            // Encontrar mayor penalización
            // --------------------------------------

            double mayorFila = -1;
            int filaSeleccionada = -1;

            for (int i = 0; i < filas; i++) {

                if (penalFila[i] > mayorFila) {
                    mayorFila = penalFila[i];
                    filaSeleccionada = i;
                }
            }

            double mayorColumna = -1;
            int columnaSeleccionada = -1;

            for (int j = 0; j < columnas; j++) {

                if (penalColumna[j] > mayorColumna) {
                    mayorColumna =
                            penalColumna[j];

                    columnaSeleccionada = j;
                }
            }

            // --------------------------------------
            // Seleccionar fila o columna
            // --------------------------------------

            int fila;
            int columna;

            if (mayorFila >= mayorColumna) {

                fila = filaSeleccionada;

                columna = -1;

                double menorCosto =
                        Double.MAX_VALUE;

                for (int j = 0; j < columnas; j++) {

                    if (demanda[j] > 0 &&
                        costos[fila][j] <
                        menorCosto) {

                        menorCosto =
                                costos[fila][j];

                        columna = j;
                    }
                }

                System.out.println(
                    "\nSe selecciona Fila " +
                    (fila + 1)
                );

            } else {

                columna = columnaSeleccionada;

                fila = -1;

                double menorCosto =
                        Double.MAX_VALUE;

                for (int i = 0; i < filas; i++) {

                    if (oferta[i] > 0 &&
                        costos[i][columna] <
                        menorCosto) {

                        menorCosto =
                                costos[i][columna];

                        fila = i;
                    }
                }

                System.out.println(
                    "\nSe selecciona Columna " +
                    (columna + 1)
                );
            }

            // --------------------------------------
            // Asignación
            // --------------------------------------

            double cantidad =
                    Math.min(
                        oferta[fila],
                        demanda[columna]
                    );

            asignacion[fila][columna] =
                    cantidad;

            double costo =
                    cantidad *
                    costos[fila][columna];

            System.out.println(
                "Asignacion: O" +
                (fila + 1) +
                " -> D" +
                (columna + 1)
            );

            System.out.println(
                "Cantidad asignada: " +
                cantidad
            );

            System.out.println(
                "Costo: " +
                cantidad +
                " x " +
                costos[fila][columna] +
                " = " +
                costo
            );

            // Actualizar oferta y demanda
            oferta[fila] -= cantidad;
            demanda[columna] -= cantidad;

            paso++;
        }

        // ==========================================
        // MOSTRAR MATRIZ FINAL
        // ==========================================

        System.out.println(
            "\n\n======================================"
        );

        System.out.println(
            "           SOLUCION FINAL"
        );

        System.out.println(
            "======================================"
        );

        double costoTotal = 0;

        for (int i = 0; i < filas; i++) {

            for (int j = 0; j < columnas; j++) {

                if (asignacion[i][j] > 0) {

                    double costo =
                            asignacion[i][j] *
                            costos[i][j];

                    costoTotal += costo;

                    System.out.println(
                        "O" + (i + 1) +
                        " -> D" + (j + 1) +
                        " = " +
                        asignacion[i][j] +
                        " unidades | Costo = " +
                        costo
                    );
                }
            }
        }

        // ==========================================
        // COSTO TOTAL
        // ==========================================

        System.out.println(
            "\n--------------------------------------"
        );

        System.out.println(
            "COSTO TOTAL = " + costoTotal
        );

        System.out.println(
            "--------------------------------------"
        );
    }

    // ==========================================
    // FUNCION PARA SUMAR
    // ==========================================

    public static double sumar(double[] valores) {

        double total = 0;

        for (double valor : valores) {
            total += valor;
        }

        return total;
    }

    public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}