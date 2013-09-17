package formula.parser.tests;

import java.util.Map;
import java.util.Set;

/**
 * General interface that represent expected result of calculation for testing implementation of
 * {@link formula.parser.api.Formula} interface.
 */
public interface FormulaCalculationTestChecker {

    /**
     * Return test string for parsing.
     * @return formula in string representation.
     */
    public String getFormulaString();

    /**
     * @return {@link Set} of variables related to {@link #getFormulaString}.
     * @see {@link formula.parser.api.Formula#getVariables}
     */
    public Set<Character> variables();

    /**
     * @return calculate value of formula related to {@link #getFormulaString()}
     * @see {@link formula.parser.api.Formula#calculate}
     */
    public double calculate(Map<Character, Double> arguments);
}
