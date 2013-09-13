package formula.parser.tests.preprocessor;

import formula.parser.FormulaParseException;
import formula.parser.preprocessor.FormulaPreprocessorRules;
import org.junit.Test;

public class MinusPreprocessorRuleTest extends PreprocessorRuleTest{

    public MinusPreprocessorRuleTest() {
        super(FormulaPreprocessorRules.MINUS_OPERATION_RULE);
    }

    @Test
    public void shouldCreatePlusOperationBeforeVariable() throws FormulaParseException {
        assertPrepossessResult("x - 3","x + -3");
    }

    @Test
    public void shouldCreatePlusOperationBeforeDigit() throws FormulaParseException {
        assertPrepossessResult("3 - 3","3 + -3");
    }

    @Test
    public void shouldCreatePlusOperationBeforeCloseBracket() throws FormulaParseException {
        assertPrepossessResult("(x - 3) - 3","(x - 3 ) + -3");
    }
}
