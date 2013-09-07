package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;

public class FormulaTokenCheckerTest {

    private FormulaTokenCheckers testTokenChecker;

    public FormulaTokenCheckerTest(FormulaTokenCheckers testTokenChecker) {
        this.testTokenChecker = testTokenChecker;
    }

    protected void assertShouldReturnItemWithType(String testString, FormulaItem.Type requiredType) {
        FormulaToken formulaToken = checkToken(testString);
        Assert.assertEquals(formulaToken.getItem().getType(), requiredType);
    }

    protected FormulaToken checkToken(String testString) {
        return checkToken(testString, 0);
    }


    protected FormulaToken checkToken(String testString, int pos) {
        return testTokenChecker.checkToken(testString, pos);
    }
}
