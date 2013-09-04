package formula.parser;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String... args) {

        String formula = "x+sin(2)+y/2.5-z*(10*5+z*(x-1)-2)";
        //String formula = "x+(2*3)-y/5";
        try {
            Formula f = FormulaParser.parse(formula);
            Map<Character, Double> arguments = new HashMap<Character, Double>();

            for (double i = 1; i < 5; i++) {
                //double res = i+2*3-i/5;
                double res = i + Math.sin(2) + i / 2.5 - i * (10 * 5 - 2);
                for (Character variable : f.variables()) {
                    arguments.put(variable, i);
                }
                System.out.println(String.format("Formula calculation : %f, Real value : %f", f.calculate(arguments), res));
            }
            System.out.println("Formula parsed successfully");
        } catch (FormulaParseException e) {
            e.printStackTrace();
        }

    }
}
