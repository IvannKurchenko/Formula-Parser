package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;
import org.junit.Test;

public class VariableTokenCheckerTest {

    private static final String DEFAULT_VARIABLE = "x";

    @Test
    public void shouldReturnVariableTypeItem(){
        FormulaToken variableToken = checkToken(DEFAULT_VARIABLE);
        Assert.assertEquals("Variable token has wrong type", variableToken.getItem().getType(), FormulaItem.Type.VARIABLE);
    }

    @Test
    public void shouldReturnEqualVariableToken(){
        assertReturnEqualVariableToken(DEFAULT_VARIABLE, DEFAULT_VARIABLE.charAt(0));
    }

    @Test
    public void shouldReturnEqualVariableTokenWithNonLetterChar(){
        String testString = DEFAULT_VARIABLE + "+";
        assertReturnEqualVariableToken(testString, DEFAULT_VARIABLE.charAt(0));
    }

    @Test
    public void shouldReturnCorrectTokenSize(){
        assertVariableTokenSize(DEFAULT_VARIABLE);
    }

    @Test
    public void shouldReturnCorrectTokenSizeWithNonLetterChar(){
        assertVariableTokenSize(DEFAULT_VARIABLE + "+");
    }

    @Test
    public void shouldReturnNullVariableTokenWithNonLetterCharAtStart(){
        assertReturnNullVariableToken("+" + DEFAULT_VARIABLE);
    }

    @Test
    public void shouldReturnNullVariableTokenWithDigitChartAtStart(){
        assertReturnNullVariableToken("2" + DEFAULT_VARIABLE);
    }

    @Test
    public void shouldReturnNullVariableTokenWithDigitCharAtEnd(){
        assertReturnNullVariableToken(DEFAULT_VARIABLE + "2");
    }

    private void assertReturnEqualVariableToken(String testString, char variableName){
        FormulaToken variableToken = checkToken(testString);
        Assert.assertEquals("Variable token has wrong name",
                variableToken.getItem().getVariableName(), variableName);
    }

    private void assertVariableTokenSize(String testString){
        FormulaToken variableToken = checkToken(testString);
        Assert.assertEquals("Variable token has wrong size", variableToken.getTokenSize(), 1);
    }

    private void assertReturnNullVariableToken(String testString){
        FormulaToken variableToken = checkToken(testString);
        Assert.assertNull("Variable token should be null for : " + testString, variableToken);
    }
    private FormulaToken checkToken(String testString){
        return FormulaTokenCheckers.VARIABLE_CHECKER.checkToken(testString,0);
    }
}
