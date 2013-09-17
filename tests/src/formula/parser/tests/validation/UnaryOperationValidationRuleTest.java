package formula.parser.tests.validation;

import formula.parser.api.FormulaParseException;
import formula.parser.validation.FormulaValidationException;
import formula.parser.validation.FormulaValidationRules;
import org.junit.Test;

public class UnaryOperationValidationRuleTest extends FormulaValidationRuleTest{

    public UnaryOperationValidationRuleTest() {
        super(FormulaValidationRules.UNARY_OPERATION_RULE);
    }

    @Test
    public void shouldPassValidationForPrefixUnaryOperation() throws FormulaParseException {
        checkValidation("sin(x) - 1");
    }

    @Test
    public void shouldPassValidationForPostfixUnaryOperation() throws FormulaParseException {
        checkValidation("x! - 1");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationForPrefixUnaryOperation() throws FormulaParseException {
        checkValidation("(x)sin + 1");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationForPrefixUnaryOperationWithoutArgument() throws FormulaParseException {
        checkValidation("1 - sin");
    }


    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationForPostfixUnaryOperation() throws FormulaParseException {
        checkValidation("!x - 1");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationForPostfixUnaryOperationWithoutArgument() throws FormulaParseException {
        checkValidation("!x - 1");
    }
}
