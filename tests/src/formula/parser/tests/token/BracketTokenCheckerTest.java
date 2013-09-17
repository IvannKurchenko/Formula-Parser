package formula.parser.tests.token;

import formula.parser.tree.FormulaItem;
import formula.parser.token.FormulaTokenCheckers;
import org.junit.Test;

public class BracketTokenCheckerTest extends FormulaTokenCheckerTest {

    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";

    public BracketTokenCheckerTest() {
        super(FormulaTokenCheckers.BRACKET_CHECKER);
    }

    @Test
    public void shouldReturnOpenBracketItem() {
        assertShouldReturnItemWithType(OPEN_BRACKET, FormulaItem.Type.OPEN_BRACKET);
    }

    @Test
    public void shouldReturnCloseBracketItem() {
        assertShouldReturnItemWithType(CLOSE_BRACKET, FormulaItem.Type.CLOSE_BRACKET);
    }
}
