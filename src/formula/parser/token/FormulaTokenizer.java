package formula.parser.token;

import formula.parser.FormulaParseException;

import java.util.*;

/**
 * Parser class that split incoming formula in string presentation on list of {@link FormulaToken} elements.
 */
public class FormulaTokenizer implements Iterable<FormulaToken> {

    private final List<FormulaToken> itemList;

    /**
     * Creates tokenizer related to incoming formula string.
     *
     * @param formula formula in string presentations.
     * @throws FormulaParseException in case if unknown operation or any other element will be found.
     */
    public FormulaTokenizer(String formula) throws FormulaParseException {
        itemList = splitOnTokens(formula);
    }

    /**
     * Returns list of {@link FormulaToken} split from incoming formula in string presentation
     *
     * @return list of split tokens.
     */
    public List<FormulaToken> getTokenList() {
        return itemList;
    }

    @Override
    public Iterator<FormulaToken> iterator() {
        return itemList.iterator();
    }

    private List<FormulaToken> splitOnTokens(String formula) throws FormulaParseException {
        List<FormulaToken> items = new ArrayList<FormulaToken>();
        int i = 0;
        while (i < formula.length()) {

            if (Character.isWhitespace(formula.charAt(i))) {
                i++;
                continue;
            }

            FormulaToken formulaToken = nextToken(formula, i);
            if (formulaToken != null) {
                items.add(formulaToken);
                i += formulaToken.getTokenSize();
            } else {
                throwException(formula, i);
            }
        }
        return Collections.unmodifiableList(items);
    }

    private FormulaToken nextToken(String formula, int startPosition) {
        for (FormulaTokenChecker checker : FormulaTokenCheckers.values()) {
            FormulaToken formulaToken = checker.checkToken(formula, startPosition);
            if (formulaToken != null) {
                return formulaToken;
            }
        }
        return null;
    }

    private void throwException(String formulaString, int offset) throws FormulaParseException {
        throw new FormulaParseException(formulaString, offset);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(FormulaToken token : this){
            stringBuilder.append(token).append(',');
        }
        return stringBuilder.toString();
    }
}
