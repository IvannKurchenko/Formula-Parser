package formula.parser.preprocessor;

import formula.parser.token.FormulaToken;

import java.util.List;

/**
 * Enum of specific {@link FormulaPreprocessorRule} implementations.
 */
public enum FormulaPreprocessorRules implements FormulaPreprocessorRule {

    /**
     *
     */
    MINUS_OPERATION_RULE(){
        @Override
        public void prepossess(List<FormulaToken> formulaTokenList) {

        }
    };
}
