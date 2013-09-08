package formula.parser.tests.validation;

import formula.parser.FormulaParseException;
import formula.parser.validation.FormulaValidationException;
import formula.parser.validation.FormulaValidationRules;
import org.junit.Test;

public class BinaryOperationValidationRuleTest extends FormulaValidationRuleTest {

    public BinaryOperationValidationRuleTest() {
        super(FormulaValidationRules.BINARY_OPERATION_RULE);
    }

    @Test
    public void shouldPassValidation() throws FormulaParseException {
        checkValidation("2 + x");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughAbsentRightArgument() throws FormulaParseException {
        checkValidation("2 + ");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughAbsentLeftArgument() throws FormulaParseException {
        checkValidation(" + x");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughIllegalRightArgument() throws FormulaParseException {
        checkValidation("2 + )");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughIllegalOperationRightArgument() throws FormulaParseException {
        checkValidation("2 + *");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughIllegalLeftArgument() throws FormulaParseException {
        checkValidation("( + x");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughIllegalOperationLeftArgument() throws FormulaParseException {
        checkValidation("* + x");
    }

    @Test(expected = FormulaValidationException.class)
    public void shouldFailValidationThroughNoArguments() throws FormulaParseException {
        checkValidation("+");
    }
}
