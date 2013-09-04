package formula.parser;

import formula.parser.token.FormulaTokenizer;
import formula.parser.validation.FormulaValidator;

/**
 * Parser for formula in string presentation
 */
public class FormulaParser {

    /**
     * Parse incoming formula in string presentation to {@link Formula} implementation.
     *
     * @param formula formula in string presentation.
     * @return parsed {@link Formula} implementation.
     * @throws FormulaParseException    in case if parse process failed.
     * @throws NullPointerException     in case if formula string is 'null'
     * @throws IllegalArgumentException in case if formula string is empty.
     */
    public static Formula parse(String formula) throws FormulaParseException {
        checkString(formula);
        FormulaTokenizer formulaTokenizer = new FormulaTokenizer(formula);
        FormulaValidator.validate(formulaTokenizer);
        return new FormulaTree(formulaTokenizer);
    }

    private static void checkString(String formula) {
        if (formula == null) throw new NullPointerException("Formula string is null");
        if (formula.length() == 0) throw new IllegalArgumentException("Formula string is empty");
    }
}
