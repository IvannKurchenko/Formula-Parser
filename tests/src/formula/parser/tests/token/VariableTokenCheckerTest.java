package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class VariableTokenCheckerTest extends FormulaTokenCheckerTest {

    private static final String DEFAULT_VARIABLE = "x";

    public VariableTokenCheckerTest() {
        super(FormulaTokenCheckers.VARIABLE_CHECKER);
    }

    @Test
    public void shouldReturnVariableTypeItem() {
        assertShouldReturnItemWithType(DEFAULT_VARIABLE, FormulaItem.Type.VARIABLE);
    }

    @Test
    public void shouldReturnEqualVariableToken() {
        assertReturnEqualVariableToken(DEFAULT_VARIABLE, DEFAULT_VARIABLE.charAt(0));
    }

    @Test
    public void shouldReturnEqualVariableTokenWithNonLetterChar() {
        String testString = DEFAULT_VARIABLE + "+";
        assertReturnEqualVariableToken(testString, DEFAULT_VARIABLE.charAt(0));
    }

    @Test
    public void shouldReturnVariableAtTheEnd(){
        String testString = "2 + " + DEFAULT_VARIABLE;
        FormulaToken variableToken = checkToken(testString, testString.length()-1);
        assertEquals(variableToken.getItem().getVariableName(), DEFAULT_VARIABLE.charAt(0));
    }

    @Test
    public void shouldReturnCorrectTokenSize() {
        assertVariableTokenSize(DEFAULT_VARIABLE);
    }

    @Test
    public void shouldReturnCorrectTokenSizeWithNonLetterChar() {
        assertVariableTokenSize(DEFAULT_VARIABLE + "+");
    }

    @Test
    public void shouldReturnNullVariableTokenWithNonLetterCharAtStart() {
        assertReturnNullVariableToken("+" + DEFAULT_VARIABLE);
    }

    @Test
    public void shouldReturnNullVariableTokenWithDigitChartAtStart() {
        assertReturnNullVariableToken("2" + DEFAULT_VARIABLE);
    }

    @Test
    public void shouldReturnNullVariableTokenWithDigitCharAtEnd() {
        assertReturnNullVariableToken(DEFAULT_VARIABLE + "2");
    }

    @Test
    public void shouldReturnNullVariableTokenWithDigitAndOperationAtStart(){
        assertReturnNullVariableToken("2 + " + DEFAULT_VARIABLE);
    }

    private void assertReturnEqualVariableToken(String testString, char variableName) {
        FormulaToken variableToken = checkToken(testString);
        assertEquals("Variable token has wrong name",
                variableToken.getItem().getVariableName(), variableName);
    }

    private void assertVariableTokenSize(String testString) {
        FormulaToken variableToken = checkToken(testString);
        assertEquals("Variable token has wrong size", variableToken.getTokenSize(), 1);
    }

    private void assertReturnNullVariableToken(String testString) {
        FormulaToken variableToken = checkToken(testString);
        assertNull("Variable token should be null for : " + testString, variableToken);
    }
}
