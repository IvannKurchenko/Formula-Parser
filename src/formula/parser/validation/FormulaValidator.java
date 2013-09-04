package formula.parser.validation;

import formula.parser.token.FormulaTokenizer;

/**
 * Util class for checking formula correctness.
 */
public class FormulaValidator {

    /**
     * Checking formula correctness.
     *
     * @param tokenizer formula in split presentation.
     * @throws FormulaValidationException if formula incorrect for some rule.
     */
    public static void validate(FormulaTokenizer tokenizer) throws FormulaValidationException {
        for (FormulaValidationRule validationRule : FormulaValidationRules.values()) {
            validationRule.validate(tokenizer);
        }
    }
}
