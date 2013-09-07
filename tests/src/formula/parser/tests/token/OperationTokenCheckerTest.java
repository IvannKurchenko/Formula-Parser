package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.Operation;
import formula.parser.operation.OperationResolver;
import formula.parser.operation.UnaryOperations;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenCheckers;
import junit.framework.Assert;
import org.junit.Test;

public class OperationTokenCheckerTest {

    private static final Operation DEFAULT_TEST_OPERATION = BinaryOperations.ADDITION;
    private static final String DEFAULT_TEST_STRING = DEFAULT_TEST_OPERATION.getSigns()[0];

    @Test
    public void shouldReturnOperationItem() {
        FormulaToken operationToken = checkToken(DEFAULT_TEST_STRING);
        Assert.assertEquals(operationToken.getItem().getType(), FormulaItem.Type.OPERATION);
    }

    @Test
    public void shouldReturnTokenWithCorrectSize() {
        assertTokenSize(DEFAULT_TEST_STRING, DEFAULT_TEST_STRING.length());
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
        assertTokenSize(DEFAULT_TEST_STRING + "xyz", DEFAULT_TEST_STRING.length());
    }

    @Test
    public void shouldReturnTokenWithCorrectSizeForStringWithDigitCharsInTheEnd() {
        assertTokenSize(DEFAULT_TEST_STRING + "123", DEFAULT_TEST_STRING.length());
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
        assertReturnNullToken("1" + DEFAULT_TEST_STRING);
    }

    @Test
    public void shouldReturnNullTokenForAbsentOperation() {
        String absentOperation = "operation";
        Assert.assertNull("Operation present", OperationResolver.findOperationBySign(absentOperation));
        assertReturnNullToken(absentOperation);
    }

    private void assertReturnNullToken(String testString) {
        FormulaToken operationToken = checkToken(testString);
        Assert.assertNull("Operation token should be null for : " + testString, operationToken);
    }

    private void assertTokenSize(String testString, int requiredTokenSize) {
        FormulaToken operationToken = checkToken(testString);
        Assert.assertEquals("Operation token size is wrong for : " + testString,
                operationToken.getTokenSize(), requiredTokenSize);
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

    private FormulaToken checkToken(String testString) {
        return FormulaTokenCheckers.OPERATION_CHECKER.checkToken(testString, 0);
    }
}
