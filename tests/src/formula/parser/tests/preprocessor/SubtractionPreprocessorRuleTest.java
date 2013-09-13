package formula.parser.tests.preprocessor;

import formula.parser.FormulaParseException;
import formula.parser.preprocessor.FormulaPreprocessorRules;
import org.junit.Test;

public class SubtractionPreprocessorRuleTest extends PreprocessorRuleTest{

    public SubtractionPreprocessorRuleTest() {
        super(FormulaPreprocessorRules.SUBTRACTION_OPERATION_RULE);
    }

    @Test
    public void shouldCreateAdditionOperationBeforeVariable() throws FormulaParseException {
        assertPrepossessResult("x - 3","x + -3");
    }

    @Test
    public void shouldCreateAdditionOperationBeforeDigit() throws FormulaParseException {
        assertPrepossessResult("3 - 3","3 + -3");
    }

    @Test
    public void shouldCreateAdditionOperationBeforeCloseBracket() throws FormulaParseException {
        assertPrepossessResult("(x - 3) - 3","(x + - 3) + - 3");
    }

    @Test
    public void shouldCreateAdditionOperationBeforePostfixUnaryOperation() throws FormulaParseException {
        assertPrepossessResult("3! - 3","3! + -3");
    }
}
