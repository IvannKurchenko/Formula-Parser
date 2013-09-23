package formula.parser.tests.token;

import formula.parser.tests.util.DefaultResolverProvider;
import formula.parser.tree.FormulaItem;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.Operation;
import formula.parser.operation.OperationResolver;
import formula.parser.operation.UnaryOperations;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;
import org.junit.Test;

public class OperationTokenCheckerTest extends FormulaTokenCheckerTest {

    private static final Operation DEFAULT_TEST_OPERATION = BinaryOperations.ADDITION;
    private static final String DEFAULT_TEST_STRING = DEFAULT_TEST_OPERATION.getSigns()[0];

    public OperationTokenCheckerTest() {
        super(FormulaTokenCheckers.OPERATION_CHECKER);
    }

    @Test
    public void shouldReturnOperationItem() {
        assertShouldReturnItemWithType(DEFAULT_TEST_STRING, FormulaItem.Type.OPERATION);
    }

    @Test
    public void shouldReturnTokenWithCorrectSize() {
        assertShouldReturnTokenWithCorrectSize(DEFAULT_TEST_STRING, DEFAULT_TEST_STRING.length());
    }

    @Test
    public void shouldReturnTokenWithEqualOperation() {
        assertReturnEqualOperation(DEFAULT_TEST_STRING, DEFAULT_TEST_OPERATION);
    }

    @Test
    public void shouldReturnTokenWithEqualOperationForStringWithDigitCharsInTheEnd() {
        assertReturnEqualOperation(DEFAULT_TEST_STRING + "123", DEFAULT_TEST_OPERATION);
    }

    @Test
    public void shouldReturnTokenWithEqualOperationForStringWithLiteralCharsInTheEnd() {
        assertReturnEqualOperation(DEFAULT_TEST_STRING + "xyz", DEFAULT_TEST_OPERATION);
    }

    @Test
    public void shouldReturnTokenWithCorrectSizeForStringWithLetterCharsInTheEnd() {
        assertShouldReturnTokenWithCorrectSize(DEFAULT_TEST_STRING + "xyz", DEFAULT_TEST_STRING.length());
    }

    @Test
    public void shouldReturnTokenWithCorrectSizeForStringWithDigitCharsInTheEnd() {
        assertShouldReturnTokenWithCorrectSize(DEFAULT_TEST_STRING + "123", DEFAULT_TEST_STRING.length());
    }

    @Test
    public void shouldReturnTokenForAllSupportedUnaryOperations() {
        assertReturnTokenForOperations(UnaryOperations.values());
    }

    @Test
    public void shouldReturnTokenForAllSupportedBinaryOperations() {
        assertReturnTokenForOperations(BinaryOperations.values());
    }

    @Test
    public void shouldReturnNullTokenForStringWithDigitCharAtStart() {
        assertShouldReturnNullToken("1" + DEFAULT_TEST_STRING);
    }

    @Test
    public void shouldReturnNullTokenForUnknownOperation() {
        assertShouldReturnNullToken(UnaryOperations.SIN + "operation");
    }

    @Test
    public void shouldReturnNullTokenForAbsentOperation() {
        String absentOperation = "operation";
        Assert.assertNull("Operation present", DefaultResolverProvider.OPERATION_RESOLVER.findOperationBySign(absentOperation));
        assertShouldReturnNullToken(absentOperation);
    }

    private void assertReturnEqualOperation(String testString, Operation requiredOperation) {
        FormulaToken operationToken = checkToken(testString);
        Assert.assertEquals("Operation token has wrong operation",
                operationToken.getItem().getOperation(), requiredOperation);
    }

    private void assertReturnTokenForOperations(Operation... operations) {
        for (Operation operation : operations) {
            for (String operationSign : operation.getSigns()) {
                assertReturnEqualOperation(operationSign, operation);
            }
        }
    }
}
