package formula.parser.tests.api;

import formula.parser.api.Formula;
import formula.parser.api.FormulaParseException;
import formula.parser.api.FormulaParser;
import formula.parser.tests.util.CustomTestOperations;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class FormulaParserTest {

    public static final String TEST_CUSTOM_CONSTANT_NAME = "CONST";
    public static final double TEST_CUSTOM_CONSTANT_VALUE = 3;


    private static final double TEST_CALCULATION_RANGE_MIN_VALUE = -100;
    private static final double TEST_CALCULATION_RANGE_MAX_VALUE = 100;
    private static final double TEST_CALCULATION_STEP = 0.5;

    private static void testFormula(FormulaCalculationTestChecker expected) throws FormulaParseException {
        testFormula(expected, new FormulaParser());
    }

    private static void testFormula(FormulaCalculationTestChecker expected, FormulaParser parser) throws FormulaParseException {
        Formula actual = parser.parse(expected.getFormulaString());
        assertFormulaVariablesEquals(expected, actual);
        assertFormulaCalculationSame(expected, actual);
    }

    private static void assertFormulaVariablesEquals(FormulaCalculationTestChecker expected, Formula actual) {
        assertEquals(expected.variables(), actual.getVariables());
    }

    private static void assertFormulaCalculationSame(FormulaCalculationTestChecker expected, Formula actual) {
        Set<Character> variables = expected.variables();
        Map<Character, Double> argumentsMap = newArgumentsMap(variables);

        for (Character variable : variables) {
            for (double i = TEST_CALCULATION_RANGE_MIN_VALUE; i <= TEST_CALCULATION_RANGE_MAX_VALUE; i += TEST_CALCULATION_STEP) {
                argumentsMap.put(variable, i);

                for(Map.Entry<Character, Double> variableEntry : argumentsMap.entrySet()){
                    actual.setVariableValue(variableEntry.getKey(), variableEntry.getValue());
                }

                assertEquals("Calculation failed for : " + argumentsMap, expected.calculate(argumentsMap), actual.calculate());
            }
        }
    }

    private static Map<Character, Double> newArgumentsMap(Set<Character> variables) {
        Map<Character, Double> argumentsMap = new HashMap<Character, Double>(variables.size());
        for (Character variable : variables) {
            argumentsMap.put(variable, 0.0);
        }
        return argumentsMap;
    }

    private static Formula parse(String testString) throws FormulaParseException {
        return new FormulaParser().parse(testString);
    }

    @Test
    public void shouldRightCalculateSimpleNonVariableFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.SIMPLE_NON_VARIABLE_TEST);
    }

    @Test
    public void shouldRightCalculateMinusDigitFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.MINUS_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateTwoMinusesDigitFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.TWO_MINUSES_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateFactorialFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.FACTORIAL_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateFactorialWithBracketFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.FACTORIAL_BRACKET_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateSimpleFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.SIMPLE_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateBracketFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.BRACKET_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateInnerBracketFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.INNER_BRACKET_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateMainFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.MAIN_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateBracketsOperationsFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.BRACKET_OPERATIONS_FORMULA_TEST);
    }

    @Test
    public void shouldRightCalculateArgumentsWithoutMultiplyFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.ARGUMENT_WITHOUT_MULTIPLY_OPERATION_TEST);
    }

    @Test
    public void shouldRightCalculateFormulaWithConstants() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.FORMULA_WITH_CONSTANTS);
    }

    @Test
    public void shouldRightCalculateFormulaWithCustomConstants() throws FormulaParseException {
        FormulaParser parser = new FormulaParser();
        parser.addConstant(TEST_CUSTOM_CONSTANT_NAME, TEST_CUSTOM_CONSTANT_VALUE);
        testFormula(FormulaCalculationTestCheckers.FORMULA_WITH_CUSTOM_CONSTANTS, parser);
    }

    @Test
    public void shouldRightCalculateFormulaWithCustomOperations() throws FormulaParseException {
        FormulaParser parser = new FormulaParser();
        parser.addOperation(CustomTestOperations.EXP_UNARY_OPERATION);
        parser.addOperation(CustomTestOperations.MOD_BINARY_OPERATION);
        testFormula(FormulaCalculationTestCheckers.FORMULA_WITH_CUSTOM_OPERATIONS, parser);
    }

    @Test(expected = FormulaParseException.class)
    public void shouldFailParseForEmptyString() throws FormulaParseException {
        parse("");
    }

    @Test(expected = FormulaParseException.class)
    public void shouldFailParseForNull() throws FormulaParseException {
        parse(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void variablesChangingShouldFail() throws FormulaParseException {
        Formula formula = parse("x");
        formula.getVariables().clear();
    }
}
