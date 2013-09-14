package formula.parser.validation;

import formula.parser.FormulaItem;
import formula.parser.token.FormulaToken;

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
        public void validate(List<FormulaToken> tokenList) throws FormulaValidationException {
            int bracketCount = 0;
            FormulaToken lastOpenBracket = null;

            for (FormulaToken formulaToken : tokenList) {

                if (formulaToken.getItem().isOpenBracket()) {
                    lastOpenBracket = formulaToken;
                    bracketCount++;
                }

                if (formulaToken.getItem().isCloseBracket()) {
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
        public void validate(List<FormulaToken> tokenList) throws FormulaValidationException {
            for (int i = 0; i < tokenList.size() - 1; i++) {

                FormulaToken firstToken = tokenList.get(i);
                FormulaToken secondToken = tokenList.get(i + 1);

                FormulaItem firstItem = firstToken.getItem();
                FormulaItem secondItem = secondToken.getItem();

                ruleAssertTrue( !(firstItem.isArgument() && secondItem.isArgument()) , secondToken);

                ruleAssertTrue( !(firstItem.isArgument() && secondItem.isPrefixUnaryOperation()) , secondToken);

                ruleAssertTrue( !(firstItem.isPostfixUnaryOperation() && secondItem.isPrefixUnaryOperation()) , secondToken);

                ruleAssertTrue( !(firstItem.isArgument() && secondItem.isOpenBracket()) , secondToken);

                ruleAssertTrue( !(firstItem.isCloseBracket() && secondItem.isArgument()) , secondToken);
            }
        }
    },

    /**
     * This rule checks that each binary operation should have it's correct arguments or operations.
     * For example : a + b - correct arguments, a +* b - incorrect arguments.
     */
    BINARY_OPERATION_RULE("Binary operation '%s' have no valid arguments") {
        @Override
        public void validate(List<FormulaToken> tokenList) throws FormulaValidationException {

            if (tokenList.size() < 3) {
                ruleAssertTrue(!isBinaryOperation(tokenList, 0), getToken(tokenList, 0));
                ruleAssertTrue(!isBinaryOperation(tokenList, 1), getToken(tokenList, 1));
            }

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
            return  token.getItem().isArgument() ||
                    token.getItem().isCloseBracket()||
                    token.getItem().isPostfixUnaryOperation();
        }

        private boolean isValidRightArgument(FormulaToken token) {
            return  token.getItem().isArgument() ||
                    token.getItem().isOpenBracket() ||
                    token.getItem().isPrefixUnaryOperation();
        }

        private boolean isBinaryOperation(List<FormulaToken> tokenList, int position){
            return tokenList.size() > position && tokenList.get(position).getItem().isBinaryOperation();
        }

        private FormulaToken getToken(List<FormulaToken> tokenList, int pos){
            return tokenList.size() > pos ? tokenList.get(pos) : null;
        }
    },

    /**
     * This rule checks that each unary operation should have it's correct argument or operation.
     * For example : sin(x) + 1 - correct , (x)sin + 1 - incorrect
     */
    UNARY_OPERATION_RULE("Binary operation '%s' have no valid arguments"){
        @Override
        public void validate(List<FormulaToken> formulaTokenList) throws FormulaValidationException {
            for(int i=0; i<formulaTokenList.size(); i++){

                FormulaToken token = formulaTokenList.get(i);

                if(token.getItem().isUnaryOperation()){

                    if(token.getItem().isPrefixUnaryOperation()){
                        ruleAssertTrue(isValidPrefixArgument(formulaTokenList,i), token);
                    }

                    if(token.getItem().isPostfixUnaryOperation()){
                        ruleAssertTrue(isValidPostfixArgument(formulaTokenList,i), token);
                    }
                }

            }
        }

        private boolean isValidPrefixArgument(List<FormulaToken> tokenList, int operationPosition){
            if(tokenList.size()-1 == operationPosition){
                return false;
            }

            FormulaToken nextToken = tokenList.get(operationPosition + 1);
            return  nextToken.getItem().isArgument() ||
                    nextToken.getItem().isOpenBracket() ||
                    nextToken.getItem().isPrefixUnaryOperation() ;
        }

        private boolean isValidPostfixArgument(List<FormulaToken> tokenList, int operationPosition){
            if(operationPosition == 0){
                return false;
            }
            FormulaToken previousToken = tokenList.get(operationPosition -1);
            return previousToken.getItem().isArgument() || previousToken.getItem().isCloseBracket();
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


}
