package formula.parser.validation;

import formula.parser.token.FormulaToken;

import java.util.List;

/**
 * Rule that checks formula correctness in some specific meaning.
 */
public interface FormulaValidationRule {

    /**
     * Check formula for some correctness rule.
     *
     * @param formulaTokenList formula in split presentation.
     * @throws FormulaValidationException if formula not correspond to this rule.
     */
    void validate(List<FormulaToken> formulaTokenList) throws FormulaValidationException;
}
