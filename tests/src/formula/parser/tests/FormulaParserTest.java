package formula.parser.tests;

import formula.parser.Formula;
import formula.parser.FormulaParseException;
import formula.parser.FormulaParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class FormulaParserTest {

    private static final double TEST_CALCULATION_RANGE_MIN_VALUE = -100;
    private static final double TEST_CALCULATION_RANGE_MAX_VALUE = 100;
    private static final double TEST_CALCULATION_STEP = 0.5;

    private static void testFormula(FormulaCalculationTestChecker expected) throws FormulaParseException {
        Formula actual = parse(expected.getFormulaString());
        assertFormulaVariablesEquals(expected, actual);
        assertFormulaCalculationSame(expected, actual);
    }

    private static void assertFormulaVariablesEquals(FormulaCalculationTestChecker expected, Formula actual) {
        assertEquals(expected.variables(), actual.variables());
    }

    private static void assertFormulaCalculationSame(FormulaCalculationTestChecker expected, Formula actual) {
        Set<Character> variables = expected.variables();
        Map<Character, Double> argumentsMap = newArgumentsMap(variables);

        for (Character variable : variables) {
            for (double i = TEST_CALCULATION_RANGE_MIN_VALUE; i <= TEST_CALCULATION_RANGE_MAX_VALUE; i += TEST_CALCULATION_STEP) {
                argumentsMap.put(variable, i);
                assertEquals("Calculation failed for : " + argumentsMap, expected.calculate(argumentsMap), actual.calculate(argumentsMap));
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
        return FormulaParser.parse(testString);
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
    public void shouldRightCalculateMainFormula() throws FormulaParseException {
        testFormula(FormulaCalculationTestCheckers.MAIN_FORMULA_TEST);
    }

    @Test(expected = FormulaParseException.class)
    public void shouldFailParseForEmptyString() throws FormulaParseException {
        parse("");
    }

    @Test(expected = FormulaParseException.class)
    public void shouldFailParseForNull() throws FormulaParseException {
        parse(null);
    }
}
