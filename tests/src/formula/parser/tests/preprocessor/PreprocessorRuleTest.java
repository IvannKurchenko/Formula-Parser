package formula.parser.tests.preprocessor;

import formula.parser.FormulaItem;
import formula.parser.FormulaParseException;
import formula.parser.preprocessor.FormulaPreprocessorRule;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;

import java.util.ArrayList;
import java.util.List;

import static formula.parser.tests.util.AssetUtil.assertListEquals;

public class PreprocessorRuleTest {

    private FormulaPreprocessorRule testFormulaPreprocessorRule;

    protected PreprocessorRuleTest(FormulaPreprocessorRule testFormulaPreprocessorRule){
        this.testFormulaPreprocessorRule = testFormulaPreprocessorRule;
    }

    protected void assertPrepossessResult(String incomingFormula, String expectedFormula) throws FormulaParseException {
        List<FormulaToken> incomingTokenList = splitOnTokens(incomingFormula);
        testFormulaPreprocessorRule.prepossess(incomingTokenList);
        List<FormulaToken> expectedTokenList = splitOnTokens(expectedFormula);
        assertListEquals(toItemList(incomingTokenList), toItemList(expectedTokenList));

    }

    private List<FormulaToken> splitOnTokens(String formulaString) throws FormulaParseException {
        return new FormulaTokenizer(formulaString).getTokenList();
    }

    private List<FormulaItem> toItemList(List<FormulaToken> tokenList){
        List<FormulaItem> itemList = new ArrayList<FormulaItem>();
        for(FormulaToken token : tokenList){
            itemList.add(token.getItem());
        }
        return itemList;
    }
}
