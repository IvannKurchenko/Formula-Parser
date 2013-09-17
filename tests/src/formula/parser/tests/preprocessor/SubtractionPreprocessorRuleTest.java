package formula.parser.tests.preprocessor;

import formula.parser.api.FormulaParseException;
import formula.parser.preprocessor.FormulaPreprocessorRules;
import org.junit.Test;

public class SubtractionPreprocessorRuleTest extends PreprocessorRuleTest{

    public SubtractionPreprocessorRuleTest() {
        super(FormulaPreprocessorRules.SUBTRACTION_OPERATION_RULE);
    }

    @Test
    public void shouldCreateAdditionOperationBeforeVariable() throws FormulaParseException {
        assertPrepossessResultEquals("x - 3", "x + -3");
    }

    @Test
    public void shouldCreateAdditionOperationBeforeDigit() throws FormulaParseException {
        assertPrepossessResultEquals("3 - 3", "3 + -3");
    }

    @Test
    public void shouldCreateAdditionOperationBeforeCloseBracket() throws FormulaParseException {
        assertPrepossessResultEquals("(x - 3) - 3", "(x + - 3) + - 3");
    }

    @Test
    public void shouldCreateAdditionOperationBeforePostfixUnaryOperation() throws FormulaParseException {
        assertPrepossessResultEquals("3! - 3", "3! + -3");
    }
}
