package formula.parser.preprocessor;

import formula.parser.token.FormulaToken;

import java.util.List;

/**
 * Class for prepossessing split formula for correct {@link formula.parser.validation.FormulaValidator} checking and
 * {@link formula.parser.FormulaTree} building.
 */
public class FormulaPrerprocessor {

    /**
     * Preprocessed incoming {@link List} of {@link FormulaToken} by different prepossess rules.
     *
     * @param formulaTokenList list of {@link FormulaToken}, that represents split formula.
     */
     public static void prepossess(List<FormulaToken> formulaTokenList){
         for(FormulaPreprocessorRule preprocessorRule : FormulaPreprocessorRules.values()){
             preprocessorRule.prepossess(formulaTokenList);
         }
     }
}
