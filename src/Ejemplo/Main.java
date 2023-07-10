package Ejemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int NUM_THREADS = 4; // Número de hilos a utilizar

    private List<String> V; // No Terminales
    private List<String> T; // Terminales
    private String S; // símbolo inicial
    private List<String> P; // producciones

    public Main() {
        V = new ArrayList<>();
        T = new ArrayList<>();
        P = new ArrayList<>();
    }

    public void ingresarDatos() {
        // instanciar el objeto para la entrada de datos
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los símbolos no terminales (V):");
        // guardar los símbolos
        String noTerminales = scanner.nextLine();
        // Recorro cada término separado por ","
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
            P.add(regla); // agrego cada regla a producciones
        }
        System.out.println(P);
    }

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
        Main evaluator = new Main();
        evaluator.ingresarDatos();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la cantidad de frases a generar:");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea después de leer el entero

        evaluator.generarFrases(cantidad);
        evaluateRule(evaluator.S);
    }

    public static void evaluateRule(String input) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Creamos un objeto CountDownLatch para sincronizar los hilos
        CountDownLatch latch = new CountDownLatch(input.length());

        // Iteramos sobre cada símbolo del input y creamos un hilo para evaluar la regla correspondiente
        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);
            Runnable worker = new RuleEvaluatorWorker(symbol, latch);
            executor.execute(worker);
        }

        try {
            // Esperamos a que todos los hilos terminen su ejecución
            latch.await();
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class RuleEvaluatorWorker implements Runnable {
        private char symbol;
        private CountDownLatch latch;

        public RuleEvaluatorWorker(char symbol, CountDownLatch latch) {
            this.symbol = symbol;
            this.latch = latch;
        }

        @Override
        public void run() {
            // Evaluar la regla correspondiente al símbolo
            switch (symbol) {
                case 'S':
                    System.out.println("S -> ABa");
                    break;
                case 'A':
                    System.out.println("A -> BB");
                    break;
                case 'B':
                    System.out.println("B -> ab");
                    break;
                case 'X':
                    System.out.println("AB -> b");
                    break;
                default:
                    System.out.println("Regla no encontrada para el símbolo: " + symbol);
                    break;
            }

            // Contamos el hilo actual como completado
            latch.countDown();
        }
    }
}
