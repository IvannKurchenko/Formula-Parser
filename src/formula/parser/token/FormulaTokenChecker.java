package formula.parser.token;

/**
 * General interface for checking of specific kind of {@link FormulaToken} in formula in string presentation.
 */
interface FormulaTokenChecker {

    /**
     * Check next {@link FormulaToken} in incoming string from start position.
     * If specific {@link FormulaToken} presents return it's, 'null' otherwise.
     *
     * @param formula formula in string presentation.
     * @param startPosition start position of {@link FormulaToken} checking in incoming string.
     * @return specific {@link FormulaToken} implementation.
     */
    public FormulaToken checkToken(String formula, int startPosition);
}
