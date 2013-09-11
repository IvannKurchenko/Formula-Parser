package formula.parser.preprocessor;

import formula.parser.token.FormulaToken;

import java.util.List;

/**
 * Rule that make formula pre-processing for some specific situation.
 */
public interface FormulaPreprocessorRule {

    /**
     * Make formula pre-processing for some specific situation.
     *
     * @param formulaTokenList list of {@link FormulaToken}, that represents split formula.
     */
    public void prepossess(List<FormulaToken> formulaTokenList);
}
