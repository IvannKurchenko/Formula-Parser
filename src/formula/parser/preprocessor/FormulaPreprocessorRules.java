package formula.parser.preprocessor;

import formula.parser.FormulaItem;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.UnaryOperations;
import formula.parser.token.FormulaToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static formula.parser.FormulaItem.newOperationItem;

/**
 * Enum of specific {@link FormulaPreprocessorRule} implementations.
 */
public enum FormulaPreprocessorRules implements FormulaPreprocessorRule {

    /**
     * This rule prepares subtraction operation for binary representation.
     * Because subtraction operation is unary operation in general, for binary presentation need to
     * add addition operation before.
     * Example : x - y       ----> x + -y
     *           x - (y + 3) ----> x + -(y + 3)
     */
    SUBTRACTION_OPERATION_RULE(){

        private final String[] SUBTRACTION_SIGNS = UnaryOperations.SUBTRACTION.getSigns();
        private final FormulaToken ADDITION_TOKEN = new FormulaToken(newOperationItem(BinaryOperations.ADDITION),-1,-1);

        @Override
        public void prepossess(List<FormulaToken> formulaTokenList) {
            List<Integer> additionInsertIndexList = new ArrayList<Integer>();

            for(int i=1; i<formulaTokenList.size(); i++){
                if(needInsertAdditionOperation(formulaTokenList, i)){
                    additionInsertIndexList.add(i);
                }
            }

            for(int i=0; i<additionInsertIndexList.size(); i++){
                formulaTokenList.add(additionInsertIndexList.get(i)+i, ADDITION_TOKEN);
            }
        }

        private boolean needInsertAdditionOperation(List<FormulaToken> tokenList, int checkPosition){
            FormulaToken currentToken = tokenList.get(checkPosition);
            FormulaToken previousToken = tokenList.get(checkPosition -1);

            return  isSubtractionOperation(currentToken) &&
                    (       previousToken.getItem().isArgument() ||
                            previousToken.getItem().isCloseBracket() ||
                            previousToken.getItem().isPostfixUnaryOperation() );
        }

        private boolean isSubtractionOperation(FormulaToken token){
            FormulaItem item = token.getItem();
            return  item.isUnaryOperation() &&
                    Arrays.equals(SUBTRACTION_SIGNS, item.getOperation().getSigns());
        }

    },

    /**
     * This rule insert additional multiply operation for arguments, where they missed and
     * can be added as default.
     * Example : 3x     ---> 3*x
     *           3(x+1) ---> 3*(x+1)
     *           x(x+1) ---> x*(x+1)
     */
    ARGUMENT_MULTIPLY_RULE(){

        private final FormulaToken MULTIPLY_TOKEN = new FormulaToken(newOperationItem(BinaryOperations.MULTIPLY),-1,-1);

        @Override
        public void prepossess(List<FormulaToken> formulaTokenList) {
            List<Integer> multiplyInsertIndexList = new ArrayList<Integer>();

            for(int i=0; i < (formulaTokenList.size()-1); i++){
                if(needInsertMultiplyOperation(formulaTokenList, i)){
                    multiplyInsertIndexList.add(i);
                }
            }

            for(int i=0; i<multiplyInsertIndexList.size(); i++){
                formulaTokenList.add(multiplyInsertIndexList.get(i) +i +1, MULTIPLY_TOKEN);
            }
        }

        private boolean needInsertMultiplyOperation(List<FormulaToken> tokenList, int checkPosition){
            FormulaItem currentItem = tokenList.get(checkPosition).getItem();
            FormulaItem nextItem = tokenList.get(checkPosition + 1).getItem();
            switch (currentItem.getType()){
                case DIGIT:
                    return  nextItem.getType() == FormulaItem.Type.VARIABLE ||
                            nextItem.getType() == FormulaItem.Type.OPEN_BRACKET;

                case VARIABLE:
                    return nextItem.getType() == FormulaItem.Type.OPEN_BRACKET;

                default:
                    return false;
            }
        }

    };
}
