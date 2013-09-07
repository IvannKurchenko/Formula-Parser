package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FormulaTokenCheckerTest {

    private FormulaTokenCheckers testTokenChecker;

    public FormulaTokenCheckerTest(FormulaTokenCheckers testTokenChecker) {
        this.testTokenChecker = testTokenChecker;
    }

    protected void assertShouldReturnItemWithType(String testString, FormulaItem.Type requiredType) {
        FormulaToken formulaToken = checkToken(testString);
        assertEquals(formulaToken.getItem().getType(), requiredType);
    }

    protected void assertTokenSize(FormulaToken token){
        if(token != null){
            assertTrue("Token size should be greater then 0", token.getTokenSize()>0);
        }
    }

    protected FormulaToken checkToken(String testString) {
        return checkToken(testString, 0);
    }

    protected FormulaToken checkToken(String testString, int pos) {
        FormulaToken formulaToken = testTokenChecker.checkToken(testString, pos);
        assertTokenSize(formulaToken);
        return formulaToken;
    }


}
