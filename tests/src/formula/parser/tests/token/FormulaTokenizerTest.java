package formula.parser.tests.token;

import formula.parser.FormulaItem;
import formula.parser.FormulaParseException;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.UnaryOperations;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static formula.parser.FormulaItem.*;
import static formula.parser.tests.util.AssetUtil.assertListEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class FormulaTokenizerTest {

    private static String DEFAULT_TEST_FORMULA_STRING = "x + 3 - y * ( z / 2 ) + sin(2)";

    private static List<FormulaToken> DEFAULT_TEST_FORMULA_ITEM_LIST = new ArrayList<FormulaToken>();

    static {
        addToken(newVariableItem('x'), 1, 0);
        addToken(newOperationItem(BinaryOperations.ADDITION), 1, 2);
        addToken(newDigitItem(3), 1, 4);
        addToken(newOperationItem(UnaryOperations.SUBTRACTION), 1, 6);
        addToken(newVariableItem('y'), 1, 8);
        addToken(newOperationItem(BinaryOperations.MULTIPLY), 1, 10);
        addToken(newBracketItem(true), 1, 12);
        addToken(newVariableItem('z'), 1, 14);
        addToken(newOperationItem(BinaryOperations.DIVISION), 1, 16);
        addToken(newDigitItem(2), 1, 18);
        addToken(newBracketItem(false), 1, 20);
        addToken(newOperationItem(BinaryOperations.ADDITION), 1, 22);
        addToken(newOperationItem(UnaryOperations.SIN), 3, 24);
        addToken(newBracketItem(true), 1, 27);
        addToken(newDigitItem(2), 1, 28);
        addToken(newBracketItem(false), 1, 29);
    }

    private static void addToken(FormulaItem formulaItem, int tokenSize, int tokenStartPos) {
        DEFAULT_TEST_FORMULA_ITEM_LIST.add(new FormulaToken(formulaItem, tokenSize, tokenStartPos));
    }

    private static void assertListNotEmpty(List testList) {
        assertTrue(testList != null && testList.size() > 0);
    }

    private static void assertListNonContainNull(List testList) {
        for (Object element : testList) {
            assertNotNull(element);
        }
    }

    @Test
    public void splitShouldSucceed() throws FormulaParseException {
        FormulaTokenizer formulaTokens = new FormulaTokenizer(DEFAULT_TEST_FORMULA_STRING);
        assertListNotEmpty(formulaTokens.getTokenList());
    }

    @Test
    public void shouldSplitOnNonNullElements() throws FormulaParseException {
        FormulaTokenizer formulaTokens = new FormulaTokenizer(DEFAULT_TEST_FORMULA_STRING);
        assertListNonContainNull(formulaTokens.getTokenList());
    }

    @Test
    public void shouldSplitOnCorrectTokens() throws FormulaParseException {
        FormulaTokenizer formulaTokens = new FormulaTokenizer(DEFAULT_TEST_FORMULA_STRING);
        assertListEquals(DEFAULT_TEST_FORMULA_ITEM_LIST, formulaTokens.getTokenList());
    }

    @Test(expected = FormulaParseException.class)
    public void splitShouldFailThroughUnknownOperation() throws FormulaParseException {
        String testString = DEFAULT_TEST_FORMULA_STRING + " + operation";
        new FormulaTokenizer(testString);
    }

    @Test(expected = FormulaParseException.class)
    public void splitShouldFailThroughInvalidDigit() throws FormulaParseException {
        String testString = DEFAULT_TEST_FORMULA_STRING + " + 1.2.3";
        new FormulaTokenizer(testString);
    }
}
