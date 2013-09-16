package formula.parser.tests;


import formula.parser.operation.UnaryOperations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sin;

public enum  FormulaCalculationTestCheckers implements FormulaCalculationTestChecker{

    SIMPLE_NON_VARIABLE_TEST("3+2*1-6/3"){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            return 6;
        }
    },

    MINUS_FORMULA_TEST("-x+2",'x'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            return 2-x;
        }
    },

    TWO_MINUSES_FORMULA_TEST("7--2"){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            return 9;
        }
    },

    FACTORIAL_FORMULA_TEST("5! - x",'x'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            return UnaryOperations.FACTORIAL.operate(5) - x;
        }
    },

    FACTORIAL_BRACKET_FORMULA_TEST("(10 - 5)! - x",'x'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            return UnaryOperations.FACTORIAL.operate(10 - 5) - x;
        }
    },

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

    INNER_BRACKET_FORMULA_TEST("((x + 3) / z + sin(2 * x) ) / (10 - z)", 'x', 'z'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            double z = arguments.get('z');
            return ((x + 3) / z + sin(2 * x) ) / (10 - z);
        }
    },

    BRACKET_OPERATIONS_FORMULA_TEST("( x * 2 + sin(y) - 5) / (y - 10 + x * 3 ^ 2 )", 'x', 'y'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            double y = arguments.get('y');
            return ( x * 2 + sin(y) - 5) / (y - 10 + x * pow(3 , 2) );
        }
    },

    ARGUMENT_WITHOUT_MULTIPLY_OPERATION_TEST("3x + x(x +1 ) + 3(x - 1)", 'x'){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            double x = arguments.get('x');
            return 3*x + x*(x +1 ) + 3*(x - 1);
        }
    },

    FORMULA_WITH_CONSTANTS("10 * pi - e/5"){
        @Override
        public double calculate(Map<Character, Double> arguments) {
            return 10 * Math.PI - Math.E/5;
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
