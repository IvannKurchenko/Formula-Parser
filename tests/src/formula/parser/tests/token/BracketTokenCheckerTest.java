package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;
import org.junit.Test;

public class BracketTokenCheckerTest {

    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";

    @Test
    public void shouldReturnOpenBracketItem() {
        FormulaToken openBracketToken = checkToken(OPEN_BRACKET);
        Assert.assertEquals(openBracketToken.getItem().getType(), FormulaItem.Type.OPEN_BRACKET);
    }

    @Test
    public void shouldReturnCloseBracketItem() {
        FormulaToken openBracketToken = checkToken(CLOSE_BRACKET);
        Assert.assertEquals(openBracketToken.getItem().getType(), FormulaItem.Type.CLOSE_BRACKET);
    }

    private FormulaToken checkToken(String testString) {
        return FormulaTokenCheckers.BRACKET_CHECKER.checkToken(testString, 0);
    }
}
