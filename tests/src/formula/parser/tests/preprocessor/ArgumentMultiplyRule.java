package formula.parser.tests.preprocessor;

import formula.parser.FormulaParseException;
import formula.parser.preprocessor.FormulaPreprocessorRules;
import org.junit.Test;

public class ArgumentMultiplyRule extends PreprocessorRuleTest {

    public ArgumentMultiplyRule() {
        super(FormulaPreprocessorRules.ARGUMENT_MULTIPLY_RULE);
    }

    @Test
    public void shouldInsertMultiplyBetweenDigitAndArgument() throws FormulaParseException {
        assertPrepossessResult("1 + 3 x - 50","1 + 3*x - 50");
    }

    @Test
    public void shouldInsertMultiplyBetweenDigitAndOpenBracket() throws FormulaParseException {
        assertPrepossessResult("1 + 3(x -2 ) - 50","1 + 3*(x -2 ) - 50");
    }

    @Test
    public void shouldInsertMultiplyBetweenVariableAndOpenBracket() throws FormulaParseException {
        assertPrepossessResult("1 + x(x -2 ) - 50","1 + x*(x -2 ) - 50");
    }
}
