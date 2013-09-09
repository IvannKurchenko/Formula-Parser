package formula.parser;

import formula.parser.token.FormulaTokenizer;
import formula.parser.validation.FormulaValidator;

/**
 * Parser for formula in string presentation.
 */
public class FormulaParser {

    /**
     * Parse incoming formula in string presentation to {@link Formula} implementation.
     *
     * @param formula formula in string presentation.
     * @return parsed {@link Formula} implementation.
     * @throws FormulaParseException in case if parse process failed, formula string empty or 'null'.
     */
    public static Formula parse(String formula) throws FormulaParseException {
        checkString(formula);
        FormulaTokenizer formulaTokenizer = new FormulaTokenizer(formula);
        FormulaValidator.validate(formulaTokenizer);
        return new FormulaTree(formulaTokenizer);
    }

    private static void checkString(String formula) throws FormulaParseException {
        if (formula == null) throw new FormulaParseException("Formula string is null", 0);
        if (formula.length() == 0) throw new FormulaParseException("Formula string is empty", 0);
    }
}
