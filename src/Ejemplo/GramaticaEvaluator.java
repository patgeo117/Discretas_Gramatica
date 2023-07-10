package Ejemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GramaticaEvaluator {
    private List<String> V; // No Terminales
    private List<String> T; // Terminales
    private String S; // simbolo iniclal
    private List<String> P; // producciones

    public GramaticaEvaluator() {
        V = new ArrayList<>();
        T = new ArrayList<>();
        P = new ArrayList<>();
    }
    // abababa
    // ba
    public void ingresarDatos() {
        // instanciar el objecto para la entrada de datos
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los símbolos No terminales (V):");
        // guardar los simbolos
        String noTerminales = scanner.nextLine();
        // Recorro cada termino separado por ,
        for (String simbolo : noTerminales.split(",")) {
            V.add(simbolo.trim());
        }

        System.out.println("Ingrese los símbolos terminales (T):");
        String terminales = scanner.nextLine();
        for (String simbolo : terminales.split(",")) {
            T.add(simbolo.trim());
        }

        System.out.println("Ingrese el símbolo inicial (S):");
        S = scanner.nextLine().trim();

        System.out.println("Ingrese las reglas de producción (P)");
        System.out.println("Ingrese 'fin' para terminar la entrada de reglas.");
        System.out.println("Ejemplo de regla: S -> ABa");
        String regla;
        while (true) {
            regla = scanner.nextLine().trim();
            if (regla.equals("fin")) {
                break;
            }
            P.add(regla); // agrego cada regla a proucciones
        }
        System.out.println(P);
    }

   /* public boolean perteneceGramatica(String frase) {
        List<String> pila = new ArrayList<>(); // pila (FISRT IN, FIRST OUT)
        pila.add(S); // agrego el simbolo inicial a la pila

        List<String> cadena = new ArrayList<>();

        StringBuilder cadenaResult = new StringBuilder(); // guardar terminal encontrada
        cadenaResult.reverse().toString();

        for(String c: P){

        }

        return false;
    } */

    private String obtenerReemplazo(String nt) {
        for (String produccion : P) {
            String[] partes = produccion.split("->");
            if (partes[0].trim().equals(nt)) {
                return partes[1].trim();
            }
        }
        return null;
    }

    public void generarFrases(int cantidad) {
        System.out.println("Frases generadas:");
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            Thread thread = new Thread(() -> {
                List<String> pila = new ArrayList<>();
                pila.add(S);

                StringBuilder frase = new StringBuilder(); // Crear un StringBuilder por cada hilo

                while (!pila.isEmpty()) {
                    String produccion = pila.remove(pila.size() - 1);
                    String[] partes = produccion.split("");

                    for (String parte : partes) {
                        if (V.contains(parte)) {
                            String reemplazo = obtenerReemplazo(parte);
                            if (reemplazo != null) {
                                pila.add(reemplazo);
                            }
                        } else if (T.contains(parte)) {
                            frase.append(parte);
                        }
                    }
                }

                System.out.println(frase.toString());
            });

            threads.add(thread);
            thread.start();
        }

        // Esperar a que todos los hilos terminen
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        GramaticaEvaluator evaluator = new GramaticaEvaluator();
        evaluator.ingresarDatos();

        Scanner scanner = new Scanner(System.in);


        System.out.println("Ingrese la cantidad de frases a generar:");
        int cantidad = scanner.nextInt();
        evaluator.generarFrases(cantidad);
    }
}
