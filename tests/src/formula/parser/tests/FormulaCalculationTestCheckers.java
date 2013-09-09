package formula.parser.tests;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum  FormulaCalculationTestCheckers implements FormulaCalculationTestChecker{

    SIMPLE_FORMULA_TEST("x + y / 2 - 10 * x", 'x', 'y'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            double y = arguments.get('y');
            return checkNegativeZero ( x + y / 2 - 10 * x ) ;
        }
    },

    BRACKET_FORMULA_TEST("(x + y) / (2 - 10) * x", 'x', 'y'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            double y = arguments.get('y');
            return checkNegativeZero( (x + y) / (2 - 10) * x ) ;
        }
    },

    MAIN_FORMULA_TEST("x + sin(2) + y / 2.5 - z * (10 * 5 - 2) - z * (2 + x )", 'x', 'y', 'z'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            double y = arguments.get('y');
            double z = arguments.get('z');
            return checkNegativeZero( x + Math.sin(2) + y / 2.5 - z * (10 * 5 - 2) - z*(2+x) );
        }
    };

    private String formulaString;
    private Set<Character> variables;

    FormulaCalculationTestCheckers(String formulaString, Character... variables){
        this.formulaString = formulaString;
        this.variables = new HashSet<Character>();
        Collections.addAll(this.variables, variables);
    }

    @Override
    public String getFormulaString() {
        return formulaString;
    }

    @Override
    public Set<Character> variables() {
        return variables;
    }

    protected double checkNegativeZero(double value){
        return value == -0.0 ? 0.0 : value;
    }
}
