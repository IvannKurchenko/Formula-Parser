package formula.parser.token;

import formula.parser.tree.FormulaItem;
import formula.parser.constants.ConstantResolver;
import formula.parser.operation.Operation;
import formula.parser.operation.OperationResolver;

/**
 * Enum of specific {@link FormulaTokenChecker} implementations.
 */
public enum FormulaTokenCheckers implements FormulaTokenChecker {

    /**
     * Checks is next {@link FormulaToken} is digit literal value in formula string.
     */
    DIGIT_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition, ResolversProvider resolversProvider) {
            if (!Character.isDigit(formula.charAt(startPosition))) {
                return null;
            }

            int endPosition = startPosition;
            int pointCount = 0;

            while (endPosition < formula.length() && validDigitLiteralChar(formula.charAt(endPosition))) {

                pointCount += isPoint(formula.charAt(endPosition)) ? 1 : 0;
                if (pointCount > 1) {
                    return null;
                }

                endPosition++;
            }

            double result = Double.parseDouble(formula.substring(startPosition, endPosition));
            FormulaItem digitLiteral = FormulaItem.newDigitItem(result);
            int digitLiteralSize = endPosition - startPosition;
            return new FormulaToken(digitLiteral, digitLiteralSize, startPosition);
        }

        private boolean validDigitLiteralChar(char character) {
            return Character.isDigit(character) || isPoint(character);
        }

        private boolean isPoint(char character) {
            return character == '.';
        }
    },

    /**
     * Check is next {@link FormulaItem} is constant in formula string.
     */
    CONSTANT_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition,ResolversProvider resolversProvider) {
            int endPosition = startPosition;
            ConstantResolver constantResolver = resolversProvider.getConstantResolver();
            while (isInBound(formula, startPosition, endPosition, constantResolver)) {

                String operationString = formula.substring(startPosition, endPosition + 1);
                Double value = constantResolver.findConstantBySign(operationString);
                if (value != null && !isLetter(formula, endPosition + 2) ) {
                    FormulaItem operationItem = FormulaItem.newDigitItem(value);
                    int tokenSize = endPosition - startPosition + 1;
                    return new FormulaToken(operationItem, tokenSize, startPosition);
                } else {
                    endPosition++;
                }

            }
            return null;
        }

        private boolean isInBound(String formula, int startPosition, int endPosition, ConstantResolver constantResolver) {
            return endPosition < formula.length() &&
                    (startPosition - endPosition) <= constantResolver.getConstantsMaxLength();
        }

        private boolean isLetter(String formula, int position){
            return position < formula.length() && Character.isLetter(formula.charAt(position));
        }
    },

    /**
     * Checks is next {@link FormulaToken} is operation in formula string.
     */
    OPERATION_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition, ResolversProvider resolversProvider) {
            int endPosition = startPosition;
            OperationResolver operationResolver = resolversProvider.getOperationResolver();
            while (isInBound(formula, startPosition, endPosition, operationResolver)) {

                String operationString = formula.substring(startPosition, endPosition + 1);
                Operation operation = operationResolver.findOperationBySign(operationString);
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

        private boolean isInBound(String formula, int startPosition, int endPosition, OperationResolver operationResolver) {
            return endPosition < formula.length() &&
                    (startPosition - endPosition) <= operationResolver.getOperationMaxLength();
        }
    },

    /**
     * Checks is next {@link FormulaToken} is variable in formula string.
     */
    VARIABLE_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition, ResolversProvider resolversProvider) {
            if (!Character.isLetter(formula.charAt(startPosition))) {
                return null;
            }

            if ((formula.length() - 1) == startPosition && Character.isLetter(formula.charAt(startPosition))) {
                return newVariableToken(formula, startPosition);
            }

            boolean nextCharNotLetter = !Character.isLetter(formula.charAt(startPosition + 1));
            boolean nextCharNotDigit = !Character.isDigit(formula.charAt(startPosition + 1));

            return nextCharNotLetter && nextCharNotDigit ? newVariableToken(formula, startPosition) : null;
        }

        private FormulaToken newVariableToken(String formulaString, int startPosition) {
            FormulaItem variableItem = FormulaItem.newVariableItem(formulaString.charAt(startPosition));
            return new FormulaToken(variableItem, 1, startPosition);
        }
    },

    /**
     * Checks is next {@link FormulaToken} is bracket in formula string.
     */
    BRACKET_CHECKER() {
        @Override
        public FormulaToken checkToken(String formula, int startPosition, ResolversProvider resolversProvider) {
            char character = formula.charAt(startPosition);
            return character == '(' || character == ')' ?
                    new FormulaToken(FormulaItem.newBracketItem(character == '('), 1, startPosition) :
                    null;
        }
    };
}
