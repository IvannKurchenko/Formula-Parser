package formula.parser.tests.validation;

import formula.parser.FormulaParseException;
import formula.parser.token.FormulaTokenizer;
import formula.parser.validation.FormulaValidationException;
import formula.parser.validation.FormulaValidationRules;
import org.junit.Test;

public class BracketsValidationRuleTest {

    @Test
    public void shouldPassValidation() throws FormulaParseException {
        checkValidation("() () (( )) ");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughBracketWrongPosition() throws FormulaParseException {
        checkValidation("() )(");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughCloseBracketsCountLessThenOpen() throws FormulaParseException {
        checkValidation("() ((( ))");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughOpenBracketsCountLessThenClose() throws FormulaParseException {
        checkValidation("() (( )))");
    }

    private void checkValidation(String testString) throws FormulaParseException {
        FormulaTokenizer tokenizer = new FormulaTokenizer(testString);
        FormulaValidationRules.BRACKETS_RULE.validate(tokenizer);
    }
}
