package formula.parser.tests.preprocessor;

import formula.parser.FormulaParseException;
import formula.parser.preprocessor.FormulaPreprocessorRules;
import formula.parser.tests.util.AssetUtil;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;
import org.junit.Test;

import java.util.List;

public class PostfixOperationPreprocessorRuleTest extends PreprocessorRuleTest {

    public PostfixOperationPreprocessorRuleTest() {
        super(FormulaPreprocessorRules.POSTFIX_UNARY_OPERATION_PREPROCESSOR_RULE);
    }

    @Test
    public void shouldReplaceUnaryOperationBeforeVariable() throws FormulaParseException {
        assertPrepossessResult("3 + x!/5", "3 + !x/5");
    }

    @Test
    public void shouldReplaceUnaryOperationBeforeDigit() throws FormulaParseException {
        assertPrepossessResult("3 + 5!/x", "3 + !5/x");
    }

    @Test
    public void shouldReplaceUnaryOperationBeforeBrackets() throws FormulaParseException {
        assertPrepossessResult("3 + (5 - x)!/10", "3 + !(5 -x )/10");
    }

    private void assertPrepossessResult(String testFormula, String expectedPreprocess) throws FormulaParseException {
        List<FormulaToken> testTokenList = new FormulaTokenizer(testFormula).getTokenList();
        List<FormulaToken> expectedTokenList = new FormulaTokenizer(expectedPreprocess).getTokenList();
        prepossess(testTokenList);
        AssetUtil.assertListEquals(testTokenList, expectedTokenList);
    }

}
