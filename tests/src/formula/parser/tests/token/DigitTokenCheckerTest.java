package formula.parser.tests.token;

import formula.parser.tree.FormulaItem;
import formula.parser.token.FormulaTokenCheckers;
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
}
