import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Gramatica extends JFrame {
    private JTextField vocabularioTextField;
    private JTextField terminosFinalesTextField;
    private JTextField simboloInicialTextField;
    private JTextArea reglasProduccionTextArea;
    private JTextField buscarTextField;
    private JButton saveButton;
    private JButton clearButton;
    private JButton searchButton;
    private File frasesModificadasFile;
    public Gramatica()
    {
        setTitle("Guardar datos en archivo");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        vocabularioTextField = new JTextField();
        terminosFinalesTextField = new JTextField();
        simboloInicialTextField = new JTextField();
        reglasProduccionTextArea = new JTextArea();
        saveButton = new JButton("Guardar");
        clearButton = new JButton("Limpiar");
        buscarTextField = new JTextField();
        searchButton = new JButton("Buscar");
        setLayout(new GridLayout(8, 2));
        add(new JLabel("Vocabulario:"));
        add(vocabularioTextField);
        add(new JLabel("Términos finales:"));
        add(terminosFinalesTextField);
        add(new JLabel("Símbolo inicial:"));
        add(simboloInicialTextField);
        add(new JLabel("Reglas de producción:"));
        add(new JScrollPane(reglasProduccionTextArea));
        add(new JLabel(""));
        add(saveButton);
        add(new JLabel(""));
        add(clearButton);
        add(new JLabel("Buscar:"));
        add(buscarTextField);
        add(new JLabel(""));
        add(searchButton);
        saveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                guardarDatos();
            }
        });
        clearButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                limpiarDatos();
            }
        });
        searchButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buscarTexto();
            }
        });
        frasesModificadasFile = new File("frases_modificadas.txt");
    }
    private void guardarDatos()
    {
        String vocabulario = vocabularioTextField.getText();
        String terminosFinales = terminosFinalesTextField.getText();
        String simboloInicial = simboloInicialTextField.getText();
        String reglasProduccion = reglasProduccionTextArea.getText();
        if (!esValido(vocabulario, terminosFinales) || !esValido(vocabulario, simboloInicial) || !esValidoCaracterPorCaracter(vocabulario, reglasProduccion))
        {
            JOptionPane.showMessageDialog(this, "Error: se han ingresado términos que no están en el vocabulario");
            return;
        }
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("datos.txt"));
            writer.write("Vocabulario:");
            writer.newLine();
            writer.write(vocabulario.replace(",", "\n"));
            writer.newLine();
            writer.newLine();
            writer.write("Términos finales:");
            writer.newLine();
            writer.write(terminosFinales.replace(",", "\n"));
            writer.newLine();
            writer.newLine();
            writer.write("Símbolo inicial:");
            writer.newLine();
            writer.write(simboloInicial);
            writer.newLine();
            writer.newLine();
            writer.write("Reglas de producción:");
            writer.newLine();
            String[] reglas = reglasProduccion.split(",");
            for (String regla : reglas)
            {
                writer.write(regla.replace(",", ",\n"));
                writer.newLine();
            }
            writer.close();
            JOptionPane.showMessageDialog(this, "Los datos se han guardado correctamente en datos.txt");
            GeneradorFrases generador = new GeneradorFrases();
            generador.generarFrases(reglasProduccion, simboloInicialTextField.getText());
            ModificadorFrases modificador = new ModificadorFrases();
            modificador.modificarFrases(reglasProduccion);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + e.getMessage());
        }
    }
    private boolean esValidoCaracterPorCaracter(String vocabulario, String reglas)
    {
        String[] vocabularioArray = vocabulario.split(",");
        String[] reglasArray = reglas.split(",");

        for (String regla : reglasArray) {
            String reglaTrimmed = regla.trim();

            if (reglaTrimmed.isEmpty())
            {
                continue;
            }
            String[] reglaChars = reglaTrimmed.split("");
            for (String c : reglaChars)
            {
                if (!c.equals(",") && !c.equals("=") && !containsIgnoreCase(vocabularioArray, c))
                {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean esValido(String vocabulario, String terminos)
    {
        String[] vocabularioArray = vocabulario.split(",");
        String[] terminosArray = terminos.split(",");
        for (String termino : terminosArray)
        {
            if (!containsIgnoreCase(vocabularioArray, termino.trim()))
            {
                return false;
            }
        }
        return true;
    }
    private boolean containsIgnoreCase(String[] array, String term)
    {
        for (String element : array) {
            if (element.trim().equalsIgnoreCase(term.trim()))
            {
                return true;
            }
        }
        return false;
    }
    private void limpiarDatos()
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("datos.txt"));
            writer.write("");
            writer.close();
            JOptionPane.showMessageDialog(this, "los datos se ha limpiado correctamente");
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "Error al limpiar los datos: " + e.getMessage());
        }
    }
    private void buscarTexto() {
        String textoBuscar = buscarTextField.getText();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(frasesModificadasFile));
            String line;
            boolean encontrado = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals(textoBuscar))
                {
                    encontrado = true;
                    break;
                }
            }
            reader.close();
            if (encontrado) {
                JOptionPane.showMessageDialog(this, "La frase pertenece a la gramatica");
            } else {
                JOptionPane.showMessageDialog(this, "La frase no está en la gramatica");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el texto: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Gramatica().setVisible(true);
            }
        });
    }

}
