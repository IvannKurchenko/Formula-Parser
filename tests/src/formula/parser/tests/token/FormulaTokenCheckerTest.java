package formula.parser.tests.token;

import formula.parser.tree.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import static  formula.parser.tests.util.DefaultResolverProvider.PROVIDER;

public class FormulaTokenCheckerTest {

    private FormulaTokenCheckers testTokenChecker;

    public FormulaTokenCheckerTest(FormulaTokenCheckers testTokenChecker) {
        this.testTokenChecker = testTokenChecker;
    }

    protected void assertShouldReturnItemWithType(String testString, FormulaItem.Type requiredType) {
        FormulaToken formulaToken = checkToken(testString);
        assertEquals(formulaToken.getItem().getType(), requiredType);
    }

    protected void assertTokenSize(FormulaToken token) {
        if (token != null) {
            assertTrue("Token size should be greater then 0", token.getTokenSize() > 0);
        }
    }

    protected FormulaToken checkToken(String testString) {
        return checkToken(testString, 0);
    }

    protected FormulaToken checkToken(String testString, int pos) {
        FormulaToken formulaToken = testTokenChecker.checkToken(testString, pos, PROVIDER);
        assertTokenSize(formulaToken);
        return formulaToken;
    }

    protected void assertShouldReturnEqualValue(String testString, double testValue) {
        FormulaToken digitToken = checkToken(testString);
        Assert.assertTrue("Token has wrong item value for : " + testString, testValue == digitToken.getItem().getDigitLiteralValue());
    }


    protected void assertShouldReturnTokenWithCorrectSize(String testString, int requiredTokenSize) {
        FormulaToken digitToken = checkToken(testString);
        Assert.assertEquals("Token has wrong size for : " + testString, digitToken.getTokenSize(), requiredTokenSize);
    }

    protected void assertShouldReturnNullToken(String testString) {
        FormulaToken digitToken = checkToken(testString);
        Assert.assertNull("Token should be null for : " + testString, digitToken);
    }
}
