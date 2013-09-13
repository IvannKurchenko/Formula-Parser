package formula.parser.tests.preprocessor;

import formula.parser.preprocessor.FormulaPreprocessorRule;
import formula.parser.token.FormulaToken;

import java.util.List;

public class PreprocessorRuleTest {

    private FormulaPreprocessorRule testFormulaPreprocessorRule;

    protected PreprocessorRuleTest(FormulaPreprocessorRule testFormulaPreprocessorRule){
        this.testFormulaPreprocessorRule = testFormulaPreprocessorRule;
    }


    protected void prepossess(List<FormulaToken> tokenList){
        testFormulaPreprocessorRule.prepossess(tokenList);
    }
}
