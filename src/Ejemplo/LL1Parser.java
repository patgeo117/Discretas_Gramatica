package Ejemplo;

import java.util.*;
public class LL1Parser {
    private Map<String, Map<String, String>> parsingTable;
    private Stack<String> stack;

    public LL1Parser(Map<String, Map<String, String>> parsingTable) {
        this.parsingTable = parsingTable;
        this.stack = new Stack<>();
        this.stack.push("$");
        this.stack.push("S");
    }

    public boolean parse(String input) {
        int i = 0;
        String symbol = stack.peek();

        while (!stack.empty() && i < input.length()) {
            if (symbol.equals("$")) {
                return false;
            } else if (isTerminal(symbol)) {
                if (symbol.equals(Character.toString(input.charAt(i)))) {
                    stack.pop();
                    i++;
                } else {
                    return false;
                }
            } else {
                Map<String, String> row = parsingTable.get(symbol);
                String lookahead = Character.toString(input.charAt(i));
                String production = row.get(lookahead);
                if (production == null) {
                    return false;
                } else {
                    stack.pop();
                    if (!production.equals("epsilon")) {
                        String[] symbols = production.split(" ");
                        for (int j = symbols.length - 1; j >= 0; j--) {
                            stack.push(symbols[j]);
                        }
                    }
                }
            }

            symbol = stack.peek();
        }

        return stack.empty() && i == input.length();
    }

    private boolean isTerminal(String symbol) {
        return !parsingTable.containsKey(symbol);
    }

    public static void main(String[] args) {
        Map<String, Map<String, String>> parsingTable = new HashMap<>();

        // Agregar reglas de producción y acciones de análisis para cada símbolo no terminal
        Map<String, String> sProduction = new HashMap<>();
        sProduction.put("a", "aB");
        sProduction.put("c", "c");
        parsingTable.put("S", sProduction);

        Map<String, String> bProduction = new HashMap<>();
        bProduction.put("b", "b");
        bProduction.put("c", "epsilon");
        parsingTable.put("B", bProduction);

        // Agregar acciones de análisis para símbolos terminales
        Map<String, String> terminalActions = new HashMap<>();
        terminalActions.put("a", "shift");
        terminalActions.put("b", "shift");
        terminalActions.put("c", "reduce");
        parsingTable.put("$", terminalActions);

        LL1Parser parser = new LL1Parser(parsingTable);
        String input = "";
        boolean isValid = parser.parse(input);
        System.out.println("Is valid? " + isValid);
    }
}
