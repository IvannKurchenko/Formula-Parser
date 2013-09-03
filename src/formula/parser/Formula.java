package formula.parser;

import java.util.Map;
import java.util.Set;

/**
 * Common interface of formula representation.
 */
public interface Formula {

    /**
     * Return {@link Set} of variables present in formula.
     * In case if variables absent in formula empty {@link Set} return.
     * @return {@link Set} of variables.
     */
    public Set<Character> variables();

    /**
     * Calculates value of formula.
     * @param arguments mapped variables to it's arguments.
     * @return calculated value of formula
     * @throws NullPointerException if arguments is null
     * @throws IllegalArgumentException if arguments not contains all of present variables in formula
     */
    public double calculate(Map<Character,Double> arguments);
}
