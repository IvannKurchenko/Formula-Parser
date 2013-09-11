package formula.parser.preprocessor;

import formula.parser.token.FormulaToken;

import java.util.List;

/**
 * Enum of specific {@link FormulaPreprocessorRule} implementations.
 */
public enum FormulaPreprocessorRules implements FormulaPreprocessorRule {

    /**
     * Preprocessor rule for replacing {@link formula.parser.operation.UnaryOperation} with
     * {@link formula.parser.operation.UnaryOperation.Notation#POSTFIX} before argument.
     * For example :
     * x! or (x - 3)! - incoming expression.
     * !x or !(x -3 ) - after pre-processing by this rule.
     * Needs for correct building of {@link formula.parser.FormulaTree}, because {@link formula.parser.operation.UnaryOperation}
     * should goes before argument.
     */
    POSTFIX_UNARY_OPERATION_PREPROCESSOR_RULE(){
        @Override
        public void prepossess(List<FormulaToken> formulaTokenList) {
        }
    };
}
