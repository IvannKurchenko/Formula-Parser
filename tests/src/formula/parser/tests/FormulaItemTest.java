package formula.parser.tests;

import formula.parser.FormulaItem;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.Operation;
import org.junit.Test;

import static formula.parser.FormulaItem.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FormulaItemTest {

    private static final char DEFAULT_TEST_VARIABLE = 'x';
    private static final double DEFAULT_TEST_DIGIT = 1.0;
    private static final Operation DEFAULT_TEST_OPERATION = BinaryOperations.ADDITION;


    private static void assertHashCodeEquals(FormulaItem firstItem, FormulaItem secondItem){
        assertEquals(firstItem.hashCode(), secondItem.hashCode());
    }

    private static void assertNotEquals(FormulaItem firstItem, FormulaItem secondItem){
        assertTrue(!firstItem.equals(secondItem));
    }

    @Test
    public void variableItemsShouldBeEqual(){
        assertEquals(newVariableItem(DEFAULT_TEST_VARIABLE), newVariableItem(DEFAULT_TEST_VARIABLE));
    }

    @Test
    public void variableItemsHashCodeShouldBeEqual(){
        assertHashCodeEquals(newVariableItem(DEFAULT_TEST_VARIABLE), newVariableItem(DEFAULT_TEST_VARIABLE));
    }

    @Test
    public void variableItemsShouldBeNonEqual(){
        assertNotEquals(newVariableItem(DEFAULT_TEST_VARIABLE), newVariableItem('y'));
    }

    @Test
    public void digitItemsShouldBeEqual(){
        assertEquals(newDigitItem(DEFAULT_TEST_DIGIT), newDigitItem(DEFAULT_TEST_DIGIT));
    }

    @Test
    public void digitItemsHashCodeShouldBeEqual(){
        assertHashCodeEquals(newDigitItem(DEFAULT_TEST_DIGIT), newDigitItem(DEFAULT_TEST_DIGIT));
    }

    @Test
    public void digitItemsShouldBeNonEqual(){
        assertNotEquals(newDigitItem(DEFAULT_TEST_DIGIT), newDigitItem(2.0));
    }

    @Test
    public void operationItemsShouldBeEqual(){
        assertEquals(newOperationItem(DEFAULT_TEST_OPERATION), newOperationItem(DEFAULT_TEST_OPERATION));
    }

    @Test
    public void operationItemsHashCodeShouldBeEqual(){
        assertHashCodeEquals(newOperationItem(DEFAULT_TEST_OPERATION), newOperationItem(DEFAULT_TEST_OPERATION));
    }

    @Test
    public void operationItemsShouldBeNonEqual(){
        assertNotEquals(newOperationItem(DEFAULT_TEST_OPERATION), newOperationItem(BinaryOperations.SUBTRACTION));
    }

    @Test
    public void openBracketItemsShouldBeEqual(){
        assertEquals(newBracketItem(true), newBracketItem(true));
    }

    @Test
    public void openBracketItemsHashCodeShouldBeEqual(){
        assertHashCodeEquals(newBracketItem(true), newBracketItem(true));
    }

    @Test
    public void closeBracketItemsShouldBeEqual(){
        assertEquals(newBracketItem(false), newBracketItem(false));
    }

    @Test
    public void closeBracketItemsHashCodeShouldBeEqual(){
        assertHashCodeEquals(newBracketItem(false), newBracketItem(false));
    }

    @Test
    public void bracketsItemShouldBeNonEqual(){
        assertNotEquals(newBracketItem(true), newBracketItem(false));
    }
}
