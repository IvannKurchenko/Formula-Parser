package formula.parser.tests.validation;

import formula.parser.FormulaParseException;
import formula.parser.token.FormulaTokenizer;
import formula.parser.validation.FormulaValidationRule;

public class FormulaValidationRuleTest {

    private FormulaValidationRule testRule;

    public FormulaValidationRuleTest(FormulaValidationRule testRule) {
        this.testRule = testRule;
    }

    protected void checkValidation(String testString) throws FormulaParseException {
        FormulaTokenizer tokenizer = new FormulaTokenizer(testString);
        testRule.validate(tokenizer.getTokenList());
    }
}
