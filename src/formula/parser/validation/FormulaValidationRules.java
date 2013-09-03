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
    BRACKETS_RULE("Bracket '%s' in wrong place"){
        @Override
        public void validate(FormulaTokenizer formulaItems) throws FormulaValidationException {
            int bracketCount = 0;
            FormulaToken lastOpenBracket = null;

            for(FormulaToken formulaToken : formulaItems) {
                FormulaItem formulaItem = formulaToken.getItem();

                if( formulaItem.getType()== FormulaItem.Type.OPEN_BRACKET ){
                    lastOpenBracket = formulaToken;
                    bracketCount++;
                }

                if( formulaItem.getType()==FormulaItem.Type.CLOSE_BRACKET ){
                    bracketCount--;
                    ruleAssertTrue(bracketCount>=0, formulaToken);
                }
            }

            ruleAssertTrue(bracketCount==0, lastOpenBracket);
        }
    },

    /**
     * This rule checks that between literal digit value and variable should be some operation.
     * For example : a * 3 - correct expression, a3 or a( - incorrect expression.
     */
    LITERAL_VARIABLE_RULE("No operation for %s argument"){
        @Override
        public void validate(FormulaTokenizer formulaItems) throws FormulaValidationException {
            List<FormulaToken> tokenList = formulaItems.getTokenList();
            for(int i=0; i<tokenList.size()-1; i++){
                FormulaItem firstItem = tokenList.get(i).getItem();
                FormulaItem secondItem = tokenList.get(i+1).getItem();
                //TODO : write rule!
            }
        }
    },

    /**
     * This rule checks that each binary operation should have it's correct arguments or operations.
     * For example : a + b - correct arguments, a +* b - incorrect arguments.
     */
    BINARY_OPERATION_RULE("Binary operation '%s' have no valid arguments"){
        @Override
        public void validate(FormulaTokenizer formulaItems) throws FormulaValidationException {
            List<FormulaToken> tokenList = formulaItems.getTokenList();

            for(int i=1; i<tokenList.size()-1;i++){
                FormulaItem item = tokenList.get(i).getItem();

                if ( item.isBinaryOperation() ){

                    FormulaToken leftArgument = tokenList.get(i-1);
                    ruleAssertTrue(isValidLeftArgument(leftArgument.getItem()), leftArgument);

                    FormulaToken rightArgument = tokenList.get(i+1);
                    ruleAssertTrue(isValidRightArgument(rightArgument.getItem()),rightArgument);
                }

            }
        }

        private boolean isValidLeftArgument(FormulaItem item){
            return  isLiteralOrVariableArgument(item) ||
                    item.getType() == FormulaItem.Type.CLOSE_BRACKET;
        }

        private boolean isValidRightArgument(FormulaItem item){
            return  isLiteralOrVariableArgument(item) ||
                    item.getType() == FormulaItem.Type.OPEN_BRACKET ||
                    item.isUnaryOperation();
        }

        private boolean isLiteralOrVariableArgument(FormulaItem item){
            return  item.getType() == FormulaItem.Type.VARIABLE ||
                    item.getType() == FormulaItem.Type.DIGIT_LITERAL;
        }
    };

    private String errorMessage;

    private FormulaValidationRules(String errorMessage){
        this.errorMessage = errorMessage;
    }

    protected String formatErrorMessage(FormulaToken nonValidToken){
        return String.format(errorMessage, nonValidToken.getItem().getType().name());
    }

    protected void ruleAssertTrue(boolean condition, FormulaToken nonValidToken) throws FormulaValidationException {
        if(!condition)  {
            String formattedErrorMessage = formatErrorMessage(nonValidToken);
            throw new FormulaValidationException(formattedErrorMessage, nonValidToken.getTokenPosition());
        }
    }
}
