import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModificadorFrases {
    public void modificarFrases(String reglasProduccion)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("frases.txt"));
            StringBuilder frasesBuilder = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null)
            {
                String fraseModificada = aplicarReglasProduccion(reglasProduccion, linea);
                frasesBuilder.append(fraseModificada).append("\n");
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter("frases_modificadas.txt"));
            writer.write(frasesBuilder.toString());
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private String aplicarReglasProduccion(String reglasProduccion, String frase)
    {
        String[] reglas = reglasProduccion.split(",");
        String fraseModificada = frase;
        for (String regla : reglas)
        {
            String[] partesRegla = regla.trim().split("=");
            String simbolo = partesRegla[0].trim();
            String sustituto = partesRegla[1].trim();

            fraseModificada = fraseModificada.replaceAll(simbolo, sustituto);
        }
        return fraseModificada;
    }
}
