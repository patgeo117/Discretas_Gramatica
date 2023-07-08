import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorFrases {
    private String simboloInicial;

    public void generarFrases(String reglasProduccion, String simboloInicial) {
        this.simboloInicial = simboloInicial.trim();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("frases.txt"));
            String[] reglas = reglasProduccion.split(",");
            for (String regla : reglas) {
                String[] partes = regla.trim().split("=");
                if (partes.length == 2) {
                    partes[0] = partes[0].trim();
                    partes[1] = partes[1].trim();
                    if (partes[0].equals(this.simboloInicial)) {
                        generarFrases("", partes[1], writer);
                    }
                }
            }
            writer.close();
            System.out.println("Las frases generadas se han guardado");
        } catch (IOException e) {
            System.out.println("Error al generar las frases: " + e.getMessage());
        }
    }

    private void generarFrases(String fraseActual, String regla, BufferedWriter writer) throws IOException {
        if (esTerminal(regla)) {
            writer.write(fraseActual + regla);
            writer.newLine();
        } else {
            String[] opciones = regla.split("\\|");
            for (String opcion : opciones) {
                String[] subreglas = opcion.trim().split(" ");
                for (String subregla : subreglas) {
                    generarFrases(fraseActual + subregla + " ", subregla, writer);
                }
            }
        }
    }

    private boolean esTerminal(String simbolo) {
        return simbolo.matches(".+");
    }

}
