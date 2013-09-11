package formula.parser.validation;

import formula.parser.token.FormulaToken;

import java.util.List;

/**
 * Util class for checking formula correctness.
 */
public class FormulaValidator {

    /**
     * Checking formula correctness.
     *
     * @param formulaTokenList list of {@link FormulaToken}, that represents split formula.
     * @throws FormulaValidationException if formula incorrect for some rule.
     */
    public static void validate(List<FormulaToken> formulaTokenList) throws FormulaValidationException {
        for (FormulaValidationRule validationRule : FormulaValidationRules.values()) {
            validationRule.validate(formulaTokenList);
        }
    }
}
