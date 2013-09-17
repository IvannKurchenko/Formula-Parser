package formula.parser.token;

import formula.parser.api.FormulaParseException;
import formula.parser.constants.ConstantResolver;
import formula.parser.operation.OperationResolver;
import formula.parser.token.FormulaTokenChecker.ResolversProvider;

import java.util.*;

/**
 * Parser class that split incoming formula in string presentation on list of {@link FormulaToken} elements.
 */
public class FormulaTokenizer {

    private final List<FormulaToken> tokenList;
    private final ResolversProvider resolversProvider;

    /**
     * Creates tokenizer related to incoming formula string.
     *
     * @param formula formula in string presentations.
     * @throws FormulaParseException in case if unknown operation or any other element will be found.
     */
    public FormulaTokenizer(String formula, ConstantResolver constantResolver, OperationResolver operationResolver) throws FormulaParseException {
        resolversProvider = new ResolverProvider(constantResolver, operationResolver);
        tokenList = splitOnTokens(formula);
    }

    /**
     * Returns list of {@link FormulaToken} split from incoming formula in string presentation
     *
     * @return list of split tokens.
     */
    public List<FormulaToken> getTokenList() {
        return tokenList;
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
        return items;
    }

    private FormulaToken nextToken(String formula, int startPosition) {
        for (FormulaTokenChecker checker : FormulaTokenCheckers.values()) {
            FormulaToken formulaToken = checker.checkToken(formula, startPosition, resolversProvider);
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
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (FormulaToken token : tokenList) {
            stringBuilder.append(token).append(',');
        }
        return stringBuilder.toString();
    }

    private static class ResolverProvider implements ResolversProvider {

        private final ConstantResolver constantResolver;
        private final OperationResolver operationResolver;

        ResolverProvider(ConstantResolver constantResolver, OperationResolver operationResolver) {
            this.constantResolver = constantResolver;
            this.operationResolver = operationResolver;
        }

        @Override
        public ConstantResolver getConstantResolver() {
            return constantResolver;
        }

        @Override
        public OperationResolver getOperationResolver() {
            return operationResolver;
        }
    }
}
