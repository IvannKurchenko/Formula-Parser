package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.UnaryOperations;
import formula.parser.token.FormulaToken;
import org.junit.Test;

import static formula.parser.tests.util.AssetUtil.assertHashCodeEquals;
import static formula.parser.tests.util.AssetUtil.assertNotEquals;
import static junit.framework.Assert.assertEquals;

public class FormulaTokenTest {

    private static final FormulaItem DEFAULT_DIGIT_ITEM = FormulaItem.newDigitItem(1);
    private static final FormulaItem DEFAULT_VARIABLE_ITEM = FormulaItem.newVariableItem('x');
    private static final FormulaItem DEFAULT_OPERATION_ITEM = FormulaItem.newOperationItem(BinaryOperations.ADDITION);

    private static final int DEFAULT_TOKEN_SIZE = 1;
    private static final int DEFAULT_TOKEN_POSITION = 0;

    private static FormulaToken newToken(FormulaItem item, int size, int pos) {
        return new FormulaToken(item, size, pos);
    }

    private static FormulaToken newToken(FormulaItem item) {
        return new FormulaToken(item, DEFAULT_TOKEN_SIZE, DEFAULT_TOKEN_POSITION);
    }

    @Test
    public void digitTokensShouldBeEqual() {
        assertEquals(newToken(DEFAULT_DIGIT_ITEM), newToken(DEFAULT_DIGIT_ITEM));
    }

    @Test
    public void digitTokensHashCodeShouldBeEqual() {
        assertHashCodeEquals(newToken(DEFAULT_DIGIT_ITEM), newToken(DEFAULT_DIGIT_ITEM));
    }

    @Test
    public void digitTokensShouldBeNonEqual() {
        assertNotEquals(newToken(DEFAULT_DIGIT_ITEM), newToken(FormulaItem.newDigitItem(2.0), 1, 1));
    }

    @Test
    public void variableTokensShouldBeEqual() {
        assertEquals(newToken(DEFAULT_VARIABLE_ITEM), newToken(DEFAULT_VARIABLE_ITEM));
    }

    @Test
    public void variableTokensHashCodeShouldBeEqual() {
        assertHashCodeEquals(newToken(DEFAULT_VARIABLE_ITEM), newToken(DEFAULT_VARIABLE_ITEM));
    }

    @Test
    public void variableTokensShouldBeNonEqual() {
        assertNotEquals(newToken(DEFAULT_VARIABLE_ITEM), newToken(FormulaItem.newVariableItem('y'), 1, 1));
    }

    @Test
    public void operationTokensShouldBeEqual() {
        assertEquals(newToken(DEFAULT_OPERATION_ITEM), newToken(DEFAULT_OPERATION_ITEM));
    }

    @Test
    public void operationTokensHashCodeShouldBeEqual() {
        assertHashCodeEquals(newToken(DEFAULT_OPERATION_ITEM), newToken(DEFAULT_OPERATION_ITEM));
    }

    @Test
    public void operationTokensShouldBeNonEqual() {
        assertNotEquals(newToken(DEFAULT_OPERATION_ITEM),
                newToken(FormulaItem.newOperationItem(UnaryOperations.SUBTRACTION), 1, 1));
    }
}
