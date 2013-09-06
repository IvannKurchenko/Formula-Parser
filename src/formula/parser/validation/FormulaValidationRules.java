package formula.parser.validation;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;

import java.util.List;

/**
 * Enum of specific {@link FormulaValidationRule} implementations.
 */
public enum FormulaValidationRules implements FormulaValidationRule {

    /**
     * This rule checks that each open bracket should have it's appropriate close bracket
     * in correct order. For example : () - correct order, )( - incorrect order.
     */
    BRACKETS_RULE("Bracket '%s' in wrong place") {
        @Override
        public void validate(FormulaTokenizer formulaItems) throws FormulaValidationException {
            int bracketCount = 0;
            FormulaToken lastOpenBracket = null;

            for (FormulaToken formulaToken : formulaItems) {
                FormulaItem formulaItem = formulaToken.getItem();

                if (formulaItem.getType() == FormulaItem.Type.OPEN_BRACKET) {
                    lastOpenBracket = formulaToken;
                    bracketCount++;
                }

                if (formulaItem.getType() == FormulaItem.Type.CLOSE_BRACKET) {
                    bracketCount--;
                    ruleAssertTrue(bracketCount >= 0, formulaToken);
                }
            }

            ruleAssertTrue(bracketCount == 0, lastOpenBracket);
        }
    },

    /**
     * This rule checks that between arguments (literal digit value and variable) should be some operation.
     * For example : a * 3, a * (3... - correct expressions, a3 ,3a, 3( or )3 - incorrect expression.
     */
    ARGUMENTS_RULE("No operation for %s argument") {
        @Override
        public void validate(FormulaTokenizer formulaItems) throws FormulaValidationException {
            List<FormulaToken> tokenList = formulaItems.getTokenList();
            for (int i = 0; i < tokenList.size() - 1; i++) {

                FormulaToken firstToken = tokenList.get(i);
                FormulaToken secondToken = tokenList.get(i + 1);

                ruleAssertTrue( !(isArgument(firstToken) && isArgument(secondToken)) , secondToken);

                ruleAssertTrue( !(isArgument(firstToken) && isOpenBracket(secondToken)) , secondToken);

                ruleAssertTrue( !(isCloseBracket(firstToken) && isArgument(secondToken)) , secondToken);
            }
        }

        private boolean isOpenBracket(FormulaToken token){
            return token.getItem().getType() == FormulaItem.Type.OPEN_BRACKET;
        }

        private boolean isCloseBracket(FormulaToken token){
            return token.getItem().getType() == FormulaItem.Type.CLOSE_BRACKET;
        }
    },

    /**
     * This rule checks that each binary operation should have it's correct arguments or operations.
     * For example : a + b - correct arguments, a +* b - incorrect arguments.
     */
    BINARY_OPERATION_RULE("Binary operation '%s' have no valid arguments") {
        @Override
        public void validate(FormulaTokenizer formulaItems) throws FormulaValidationException {
            List<FormulaToken> tokenList = formulaItems.getTokenList();

            for (int i = 1; i < tokenList.size() - 1; i++) {
                FormulaItem item = tokenList.get(i).getItem();

                if (item.isBinaryOperation()) {

                    FormulaToken leftArgument = tokenList.get(i - 1);
                    ruleAssertTrue(isValidLeftArgument(leftArgument), leftArgument);

                    FormulaToken rightArgument = tokenList.get(i + 1);
                    ruleAssertTrue(isValidRightArgument(rightArgument), rightArgument);
                }

            }
        }

        private boolean isValidLeftArgument(FormulaToken token) {
            return  isArgument(token) ||
                    token.getItem().getType() == FormulaItem.Type.CLOSE_BRACKET;
        }

        private boolean isValidRightArgument(FormulaToken token) {
            return  isArgument(token) ||
                    token.getItem().getType() == FormulaItem.Type.OPEN_BRACKET ||
                    token.getItem().isUnaryOperation();
        }

    };

    private String errorMessage;

    private FormulaValidationRules(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected String formatErrorMessage(FormulaToken nonValidToken) {
        return String.format(errorMessage, nonValidToken.getItem());
    }

    protected void ruleAssertTrue(boolean condition, FormulaToken nonValidToken) throws FormulaValidationException {
        if (!condition) {
            String formattedErrorMessage = formatErrorMessage(nonValidToken);
            throw new FormulaValidationException(formattedErrorMessage, nonValidToken.getTokenPosition());
        }
    }

    protected boolean isArgument(FormulaToken token){
        return  token.getItem().getType() == FormulaItem.Type.VARIABLE |
                token.getItem().getType() == FormulaItem.Type.DIGIT_LITERAL;
    }
}
