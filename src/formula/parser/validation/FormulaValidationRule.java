package formula.parser.validation;

import formula.parser.token.FormulaTokenizer;

/**
 * Rule that checks formula correctness in some specific meaning.
 */
public interface FormulaValidationRule {

    /**
     * Check formula for some correctness rule.
     * @param formulaItems formula in split presentation.
     * @throws FormulaValidationException if formula not correspond to this rule.
     */
    void validate(FormulaTokenizer formulaItems) throws FormulaValidationException;
}
