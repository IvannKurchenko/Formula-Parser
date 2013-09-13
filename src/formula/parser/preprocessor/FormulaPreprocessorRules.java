package formula.parser.preprocessor;

import formula.parser.FormulaItem;
import formula.parser.operation.BinaryOperations;
import formula.parser.operation.UnaryOperation;
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
     *
     */
    SUBTRACTION_OPERATION_RULE(){

        private final String[] SUBTRACTION_SIGNS = UnaryOperations.SUBTRACTION.getSigns();
        private final FormulaToken ADDITION_TOKEN = new FormulaToken(newOperationItem(BinaryOperations.ADDITION),-1,-1);

        @Override
        public void prepossess(List<FormulaToken> formulaTokenList) {
            List<Integer> additionIndexList = new ArrayList<Integer>();

            for(int i=1; i<formulaTokenList.size(); i++){
                if(needAddAdditionOperation(formulaTokenList, i)){
                    additionIndexList.add(i);
                }
            }

            for(int i=0; i<additionIndexList.size(); i++){
                formulaTokenList.add(additionIndexList.get(i)+i, ADDITION_TOKEN);
            }
        }

        private boolean needAddAdditionOperation(List<FormulaToken> tokenList, int checkPosition){
            FormulaToken currentToken = tokenList.get(checkPosition);
            FormulaToken previousToken = tokenList.get(checkPosition -1);

            return  isSubtractionOperation(currentToken) &&
                    (       isArgument(previousToken) ||
                            isCloseBracket(previousToken) ||
                            isPostfixUnaryOperation(previousToken) );
        }

        private boolean isSubtractionOperation(FormulaToken token){
            FormulaItem item = token.getItem();
            return  item.isUnaryOperation() &&
                    Arrays.equals(SUBTRACTION_SIGNS, item.getOperation().getSigns());
        }

        private boolean isArgument(FormulaToken token){
            return  token.getItem().getType() == FormulaItem.Type.VARIABLE |
                    token.getItem().getType() == FormulaItem.Type.DIGIT;
        }

        private boolean isCloseBracket(FormulaToken token){
            return token.getItem().getType() == FormulaItem.Type.CLOSE_BRACKET;
        }

        protected boolean isPostfixUnaryOperation(FormulaToken token){
            return checkUnaryOperationNotation(token, UnaryOperation.Notation.POSTFIX);
        }

        private boolean checkUnaryOperationNotation(FormulaToken token,  UnaryOperation.Notation notation){
            if(!token.getItem().isUnaryOperation()){
                return false;
            }

            UnaryOperation unaryOperation = (UnaryOperation) token.getItem().getOperation();
            return unaryOperation.getNotation() == notation;
        }
    };
}
