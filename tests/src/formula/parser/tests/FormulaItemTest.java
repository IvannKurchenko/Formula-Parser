package formula.parser.tests;

import formula.parser.operation.BinaryOperations;
import formula.parser.operation.Operation;
import formula.parser.operation.UnaryOperations;
import org.junit.Test;

import static formula.parser.tree.FormulaItem.*;
import static formula.parser.tests.util.AssetUtil.assertHashCodeEquals;
import static formula.parser.tests.util.AssetUtil.assertNotEquals;
import static junit.framework.Assert.assertEquals;

public class FormulaItemTest {

    private static final char DEFAULT_TEST_VARIABLE = 'x';
    private static final double DEFAULT_TEST_DIGIT = 1.0;
    private static final Operation DEFAULT_TEST_OPERATION = BinaryOperations.ADDITION;

    @Test
    public void variableItemsShouldBeEqual() {
        assertEquals(newVariableItem(DEFAULT_TEST_VARIABLE), newVariableItem(DEFAULT_TEST_VARIABLE));
    }

    @Test
    public void variableItemsHashCodeShouldBeEqual() {
        assertHashCodeEquals(newVariableItem(DEFAULT_TEST_VARIABLE), newVariableItem(DEFAULT_TEST_VARIABLE));
    }

    @Test
    public void variableItemsShouldBeNonEqual() {
        assertNotEquals(newVariableItem(DEFAULT_TEST_VARIABLE), newVariableItem('y'));
    }

    @Test
    public void digitItemsShouldBeEqual() {
        assertEquals(newDigitItem(DEFAULT_TEST_DIGIT), newDigitItem(DEFAULT_TEST_DIGIT));
    }

    @Test
    public void digitItemsHashCodeShouldBeEqual() {
        assertHashCodeEquals(newDigitItem(DEFAULT_TEST_DIGIT), newDigitItem(DEFAULT_TEST_DIGIT));
    }

    @Test
    public void digitItemsShouldBeNonEqual() {
        assertNotEquals(newDigitItem(DEFAULT_TEST_DIGIT), newDigitItem(2.0));
    }

    @Test
    public void operationItemsShouldBeEqual() {
        assertEquals(newOperationItem(DEFAULT_TEST_OPERATION), newOperationItem(DEFAULT_TEST_OPERATION));
    }

    @Test
    public void operationItemsHashCodeShouldBeEqual() {
        assertHashCodeEquals(newOperationItem(DEFAULT_TEST_OPERATION), newOperationItem(DEFAULT_TEST_OPERATION));
    }

    @Test
    public void operationItemsShouldBeNonEqual() {
        assertNotEquals(newOperationItem(DEFAULT_TEST_OPERATION), newOperationItem(UnaryOperations.SUBTRACTION));
    }

    @Test
    public void openBracketItemsShouldBeEqual() {
        assertEquals(newBracketItem(true), newBracketItem(true));
    }

    @Test
    public void openBracketItemsHashCodeShouldBeEqual() {
        assertHashCodeEquals(newBracketItem(true), newBracketItem(true));
    }

    @Test
    public void closeBracketItemsShouldBeEqual() {
        assertEquals(newBracketItem(false), newBracketItem(false));
    }

    @Test
    public void closeBracketItemsHashCodeShouldBeEqual() {
        assertHashCodeEquals(newBracketItem(false), newBracketItem(false));
    }

    @Test
    public void bracketsItemShouldBeNonEqual() {
        assertNotEquals(newBracketItem(true), newBracketItem(false));
    }
}
