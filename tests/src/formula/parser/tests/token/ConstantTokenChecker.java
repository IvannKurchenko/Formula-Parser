package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.constants.Constants;
import formula.parser.token.FormulaTokenCheckers;
import org.junit.Test;

public class ConstantTokenChecker extends FormulaTokenCheckerTest {

    private static final double DEFAULT_CONSTANT_TEST_VALUE = Constants.PI.getValue();
    private static final String DEFAULT_TEST_STRING = Constants.PI.getSigns()[0];

    public ConstantTokenChecker() {
        super(FormulaTokenCheckers.CONSTANT_CHECKER);
    }

    @Test
    public void shouldReturnDigitItem() {
        assertShouldReturnItemWithType(DEFAULT_TEST_STRING, FormulaItem.Type.DIGIT);
    }

    @Test
    public void shouldReturnEqualValueForConstantString() {
        assertShouldReturnEqualValue(DEFAULT_TEST_STRING, DEFAULT_CONSTANT_TEST_VALUE);
    }

    @Test
    public void shouldReturnEqualValueForConstantStringWithDigits() {
        assertShouldReturnEqualValue(DEFAULT_TEST_STRING + "3.14", DEFAULT_CONSTANT_TEST_VALUE);
    }

    @Test
    public void shouldReturnNullForUnknownConstant() {
        assertShouldReturnNullToken("const" + DEFAULT_TEST_STRING);
    }
}
