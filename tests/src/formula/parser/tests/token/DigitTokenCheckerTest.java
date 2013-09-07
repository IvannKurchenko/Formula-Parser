package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;
import org.junit.Test;

public class DigitTokenCheckerTest extends FormulaTokenCheckerTest {

    private static final double DEFAULT_DIGIT_TEST_VALUE = 1.2;
    private static final String DEFAULT_TEST_STRING = Double.toString(DEFAULT_DIGIT_TEST_VALUE);

    public DigitTokenCheckerTest() {
        super(FormulaTokenCheckers.DIGIT_CHECKER);
    }

    @Test
    public void shouldReturnDigitItem() {
        assertShouldReturnItemWithType(DEFAULT_TEST_STRING, FormulaItem.Type.DIGIT);
    }

    @Test
    public void shouldReturnEqualValueForDigitString() {
        assertShouldReturnEqualValue(DEFAULT_TEST_STRING, DEFAULT_DIGIT_TEST_VALUE);
    }

    @Test
    public void shouldReturnEqualValueForStringWithNonDigitCharsInEnd() {
        String testString = DEFAULT_TEST_STRING + "digit";
        assertShouldReturnEqualValue(testString, DEFAULT_DIGIT_TEST_VALUE);
    }

    @Test
    public void shouldReturnTokenWithCorrectSizeForDigitString() {
        assertShouldReturnTokenWithCorrectSize(DEFAULT_TEST_STRING, DEFAULT_TEST_STRING.length());
    }

    @Test
    public void shouldReturnTokenWithCorrectSizeForStringWithNonDigitCharsInEnd() {
        String testString = DEFAULT_TEST_STRING + "digit";
        assertShouldReturnTokenWithCorrectSize(testString, DEFAULT_TEST_STRING.length());
    }

    @Test
    public void shouldReturnNullForNonDigitString() {
        assertShouldReturnNullToken("digit");
    }

    @Test
    public void shouldReturnNullForDigitWithMultiplePoints() {
        assertShouldReturnNullToken("1.2.3");
    }

    private void assertShouldReturnEqualValue(String testString, double testValue) {
        FormulaToken digitToken = checkToken(testString);
        Assert.assertTrue("Digit token has wrong item value", testValue == digitToken.getItem().getDigitLiteralValue());
    }


    private void assertShouldReturnTokenWithCorrectSize(String testString, int requiredTokenSize) {
        FormulaToken digitToken = checkToken(testString);
        Assert.assertEquals("Digit token has wrong size", digitToken.getTokenSize(), requiredTokenSize);
    }

    private void assertShouldReturnNullToken(String testString) {
        FormulaToken digitToken = checkToken(testString);
        Assert.assertNull("Digit token should be null for non digit string", digitToken);
    }
}
