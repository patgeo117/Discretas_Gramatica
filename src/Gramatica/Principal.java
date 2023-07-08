package Gramatica;

import Ejemplo.GramaticaEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private List<String> V; // Vocabulario
    private List<String> T; // Terminales
    private String S; // simbolo inicial
    private List<String> P; // producciones

    Principal(){
        V = new ArrayList<>();
        T = new ArrayList<>();
        P = new ArrayList<>();
    }
    public void ingresarDatos() {
        // instanciar el objecto para la entrada de datos
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el vocabulario: ");
        // guardar los simbolos
        String vocabulario = scanner.nextLine();
        // Recorro cada termino separado por ,
        for (String simbolo : vocabulario.split(",")) {
            //agrego los elementos la lista
            V.add(simbolo.trim());
        }

        System.out.println("Ingrese los símbolos terminales (a,b):");
        String terminales = scanner.nextLine();
        for (String simbolo : terminales.split(",")) {
            T.add(simbolo.trim());
        }

        System.out.println("Ingrese el símbolo inicial (S):");
        S = scanner.nextLine().trim();

        System.out.println("Ingrese las reglas de producción (P)");
        System.out.println("Ejemplo de regla: S -> ABa, A -> BB");

        String rules = scanner.nextLine();
        for (String reglas : rules.split(",")) {
            P.add(reglas.trim());
        }
        System.out.println(P);
        System.out.println(V);
        System.out.println(S);
        System.out.println(T);
    }

    public static void main(String[] args) {
        GramaticaEvaluator evaluator = new GramaticaEvaluator();
        evaluator.ingresarDatos();
    }
}
