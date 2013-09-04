package formula.parser.token;

import formula.parser.FormulaItem;
import formula.parser.operation.Operation;
import formula.parser.operation.OperationResolver;

/**
 * Enum of specific {@link FormulaTokenChecker} implementations.
 */
enum FormulaTokenCheckers implements FormulaTokenChecker {

    /**
     * Checks is next {@link FormulaToken} is digit literal value in formula string.
     */
    DIGIT_LITERAL_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition) {
            if (!Character.isDigit(formula.charAt(startPosition))) {
                return null;
            }

            int endPosition = startPosition;
            while (endPosition < formula.length() && validDigitLiteralChar(formula.charAt(endPosition))) {
                endPosition++;
            }

            double result = Double.parseDouble(formula.substring(startPosition, endPosition));
            FormulaItem digitLiteral = FormulaItem.newDigitLiteralItem(result);
            int digitLiteralSize = endPosition - startPosition;
            return new FormulaToken(digitLiteral, digitLiteralSize, startPosition);
        }

        private boolean validDigitLiteralChar(char character) {
            return Character.isDigit(character) || character == '.';
        }
    },

    /**
     * Checks is next {@link FormulaToken} is operation in formula string.
     */
    OPERATION_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition) {
            int endPosition = startPosition;
            while (isInBound(formula, startPosition, endPosition)) {

                String operationString = formula.substring(startPosition, endPosition + 1);
                Operation operation = OperationResolver.findOperationBySign(operationString);
                if (operation != null) {
                    FormulaItem operationItem = FormulaItem.newOperationItem(operation);
                    int tokenSize = endPosition - startPosition + 1;
                    return new FormulaToken(operationItem, tokenSize, startPosition);
                } else {
                    endPosition++;
                }

            }
            return null;
        }

        private boolean isInBound(String formula, int startPosition, int endPosition) {
            return endPosition < formula.length() &&
                    (startPosition - endPosition) <= OperationResolver.getMaxOperationSignLength();
        }
    },

    /**
     * Checks is next {@link FormulaToken} is variable in formula string.
     */
    VARIABLE_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition) {
            if (!Character.isLetter(formula.charAt(startPosition))) {
                return null;
            }

            boolean nextCharNotLetter = !Character.isLetter(formula.charAt(startPosition + 1));
            boolean nextCharNotDigit = !Character.isDigit(formula.charAt(startPosition + 1));

            if (nextCharNotLetter && nextCharNotDigit) {
                FormulaItem variableItem = FormulaItem.newVariableItem(formula.charAt(startPosition));
                return new FormulaToken(variableItem, 1, startPosition);
            } else {
                return null;
            }
        }
    },

    /**
     * Checks is next {@link FormulaToken} is bracket in formula string.
     */
    BRACKET_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition) {
            char character = formula.charAt(startPosition);
            return character == '(' || character == ')' ?
                    new FormulaToken(FormulaItem.newBracketItem(character == '('), 1, startPosition) :
                    null;
        }
    };
}
