package Gramatica;
import java.util.*;

public class Principal {
    private ArrayList<String> V; // Vocabulario
    private ArrayList<String> T; // Terminales
    private String S; // simbol inicial
    private ArrayList<String> P; // producciones
    private ArrayList<String> F; // Frases Generadas
    private HashSet<String> newCadenas;
    private  String buscarText;

    //IngresarDatos ingresarDatos = new IngresarDatos();

    private Principal(){
        V = new ArrayList<>();
        T = new ArrayList<>();
        P = new ArrayList<>();
        F = new ArrayList<>();
        newCadenas = new HashSet<>();
    }
    public void ingresoVocabulario(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el vocabulario (A,B,d,c,S): ");
        // guardar los simbolos
        String vocabulario = scanner.nextLine();
        // Recorro cada termino separado por ,
        for (String simbolo : vocabulario.split(",")) {
            //agrego los elementos la lista
            V.add(simbolo.trim());
        }
    }
    public void ingresoTerminales(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los terminales (a,b):");
        String terminales = scanner.nextLine();
        for (String terminal : terminales.split(",")) {
            //agrego los elementos la lista
            T.add(terminal.trim());
        }
    }

    public void ingresoSimboloinicial(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el termino inicial (S): ");
        // guardar los simbolos
        this.S = scanner.nextLine();
    }

    public void ingresoReglas(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese las reglas de producción (P)");
        System.out.println("Ejemplo de regla: S -> ABa, A -> BB");

        String rules = scanner.nextLine();
        for (String reglas : rules.split(",")) {
            P.add(reglas.trim());
        }
    }

    public void data() {
        Scanner scanner = new Scanner(System.in);
        ingresoVocabulario();
        ingresoTerminales();
        ingresoSimboloinicial();

        if(validarGramatica(V,T)){
            ingresoReglas();
            System.out.print(P);
            System.out.print(V);
            System.out.print(S);
            System.out.print(T + "\n");

            boolean Status = true;
            while (Status){
                newCadenas.clear();

                System.out.println("1 --> Para generar Frases \n"
                        + "2 --> Modificar gramática \n" + "3 --> Exit");
                String accion = scanner.nextLine();

                switch (accion) {
                    case "1":
                        generarFrases(P, S);
                        modificarFrases(P);
                        System.out.print(newCadenas + "\n");
                        System.out.println("a -> Para validar si la cadena pertenece a la gramática ");
                        String valid = scanner.nextLine();
                        if(Objects.equals(valid, "a")){
                            System.out.println("Ingresa la cadena a evaluar: ");
                            this.buscarText = scanner.nextLine();
                            buscarTexto();
                        }
                        break;
                    case "2":
                        data();
                    case "3":
                        Status = false;
                        break;
                }
                F.clear();
            }

        }else {
            System.out.println("Un elemento NO pertenece al vocabulario");
        }
    }

    public boolean validarGramatica(ArrayList<String> vocabulario, List<String> terminos) {
        for (String termino : terminos) {
            if (!vocabulario.contains(termino)) {
                return false;
            }
        }
        return vocabulario.contains(S);
    }

    public void generarFrases(ArrayList<String> reglasProduccion, String simboloInicial)
    {
        ArrayList<String> writer = F;
        for (String regla : reglasProduccion) {
            String[] partes = regla.trim().split("->");
            if (partes.length == 2) {
                partes[0] = partes[0].trim();
                partes[1] = partes[1].trim();
                if (partes[0].equals(simboloInicial)) {
                    generarFrases("", partes[1], writer);
                }
            }
        }
        System.out.println("Las frases generadas se han guardado");
    }

    private void generarFrases(String fraseActual,String regla, ArrayList<String> writer) {
        if (esTerminal(regla)) {
            writer.add(fraseActual + regla);
        } else {
            // subreglas
            String[] opciones = regla.split("\\|");
            System.out.println(Arrays.toString(opciones  )+ " opciones");
            for (String opcion : opciones) {
                String[] subreglas = opcion.trim().split(" ");
                for (String subregla : subreglas) {
                    System.out.print(fraseActual);
                    generarFrases(fraseActual + subregla + " ", subregla, writer);
                }
            }
        }
    }

    private boolean esTerminal(String simbolo) {
        // Matches método para verificar expresiones regulares
        // (.) Contiene un character sin contar el \n (salto de línea) y + que contiene el character anterior (.) o más
        return simbolo.matches(".+");
    }
    public void modificarFrases(ArrayList<String> reglasProduccion) // Recibe las Reglas de producción
    {
        StringBuilder frases = new StringBuilder(); // Se crea un objeto StringBuilder (cadena mutable)
        for(String linea: F){
            String fraseModificada = aplicarReglasProduccion(reglasProduccion, linea); // Se llama al método que evalua las reglas
            frases.append(fraseModificada).append(","); // Se guarda la frase modificada
        }
        newCadenas.add(frases.toString());
    }

    private String aplicarReglasProduccion(ArrayList<String> reglasProduccion, String frase)
    {
        String fraseModificada = frase;
        for (String regla : reglasProduccion)
        {
            String[] partesRegla = regla.trim().split("->");
            String simbolo = partesRegla[0].trim();
            String sustituto = partesRegla[1].trim();
            fraseModificada = fraseModificada.replaceAll(simbolo, sustituto);
        }

        return fraseModificada;

    }

    private void buscarTexto() {
        String textoBuscar = buscarText;

        String cadenamodificada = buscarText;
        for (String s : newCadenas) {
            String[] sa = s.trim().split(",");

            for (String c : sa) {
                if (c.equals(textoBuscar)) {
                    System.out.println("\n" + c);
                    System.out.println("La frase pertenece a la gramatica");
                    return; // Terminar la búsqueda una vez se encuentra la regla
                }
            }
        }

        System.out.println("La frase no está en la gramatica");
    }

    public static void main(String[] args) {
        Principal evaluator = new Principal();
        evaluator.data();
    }
}
