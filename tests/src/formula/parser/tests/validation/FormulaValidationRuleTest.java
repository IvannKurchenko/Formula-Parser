package formula.parser.tests.validation;

import formula.parser.api.FormulaParseException;
import formula.parser.token.FormulaTokenizer;
import formula.parser.validation.FormulaValidationRule;

import static formula.parser.tests.util.DefaultResolverProvider.CONSTANT_RESOLVER;
import static formula.parser.tests.util.DefaultResolverProvider.OPERATION_RESOLVER;

public class FormulaValidationRuleTest {

    private FormulaValidationRule testRule;

    public FormulaValidationRuleTest(FormulaValidationRule testRule) {
        this.testRule = testRule;
    }

    protected void checkValidation(String testString) throws FormulaParseException {
        FormulaTokenizer tokenizer = new FormulaTokenizer(testString, CONSTANT_RESOLVER, OPERATION_RESOLVER);
        testRule.validate(tokenizer.getTokenList());
    }
}
