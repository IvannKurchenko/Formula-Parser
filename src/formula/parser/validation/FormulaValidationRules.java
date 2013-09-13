package formula.parser.validation;

import formula.parser.FormulaItem;
import formula.parser.operation.UnaryOperation;
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
        public void validate(List<FormulaToken> tokenList) throws FormulaValidationException {
            int bracketCount = 0;
            FormulaToken lastOpenBracket = null;

            for (FormulaToken formulaToken : tokenList) {

                if (isOpenBracket(formulaToken)) {
                    lastOpenBracket = formulaToken;
                    bracketCount++;
                }

                if (isCloseBracket(formulaToken)) {
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

                ruleAssertTrue( !(isArgument(firstToken) && isArgument(secondToken)) , secondToken);

                ruleAssertTrue( !(isArgument(firstToken) && isPrefixUnaryOperation(secondToken)) , secondToken);

                ruleAssertTrue( !(isPostfixUnaryOperation(firstToken) && isPrefixUnaryOperation(secondToken)) , secondToken);

                ruleAssertTrue( !(isArgument(firstToken) && isOpenBracket(secondToken)) , secondToken);

                ruleAssertTrue( !(isCloseBracket(firstToken) && isArgument(secondToken)) , secondToken);
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
            return  isArgument(token) ||
                    isCloseBracket(token) ||
                    isPostfixUnaryOperation(token);
        }

        private boolean isValidRightArgument(FormulaToken token) {
            return  isArgument(token) ||
                    isOpenBracket(token) ||
                    isPrefixUnaryOperation(token);
        }

        private boolean isBinaryOperation(List<FormulaToken> tokenList, int position){
            return tokenList.size() > position && tokenList.get(position).getItem().isBinaryOperation();
        }

        private FormulaToken getToken(List<FormulaToken> tokenList, int pos){
            return tokenList.size() > pos ? tokenList.get(pos) : null;
        }
    },

    /**
     *
     */
    UNARY_OPERATION_RULE("Binary operation '%s' have no valid arguments"){
        @Override
        public void validate(List<FormulaToken> formulaTokenList) throws FormulaValidationException {
            for(int i=0; i<formulaTokenList.size(); i++){

                FormulaToken token = formulaTokenList.get(i);

                if(token.getItem().isUnaryOperation()){

                    if(isPrefixUnaryOperation(token)){
                        ruleAssertTrue(isValidPrefixArgument(formulaTokenList,i), token);
                    }

                    if(isPostfixUnaryOperation(token)){
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
            return isArgument(nextToken) || isOpenBracket(nextToken);
        }

        private boolean isValidPostfixArgument(List<FormulaToken> tokenList, int operationPosition){
            if(operationPosition == 0){
                return false;
            }
            FormulaToken previousToken = tokenList.get(operationPosition -1);
            return isArgument(previousToken) || isCloseBracket(previousToken);
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
                token.getItem().getType() == FormulaItem.Type.DIGIT;
    }

    protected boolean isOpenBracket(FormulaToken token){
        return token.getItem().getType() == FormulaItem.Type.OPEN_BRACKET;
    }

    protected boolean isCloseBracket(FormulaToken token){
        return token.getItem().getType() == FormulaItem.Type.CLOSE_BRACKET;
    }

    protected boolean isPostfixUnaryOperation(FormulaToken token){
        return checkUnaryOperationNotation(token, UnaryOperation.Notation.POSTFIX);
    }

    protected boolean isPrefixUnaryOperation(FormulaToken token){
        return checkUnaryOperationNotation(token, UnaryOperation.Notation.PREFIX);
    }

    private boolean checkUnaryOperationNotation(FormulaToken token,  UnaryOperation.Notation notation){
        if(!token.getItem().isUnaryOperation()){
            return false;
        }

        UnaryOperation unaryOperation = (UnaryOperation) token.getItem().getOperation();
        return unaryOperation.getNotation() == notation;
    }
}
