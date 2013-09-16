package formula.parser;

import java.util.Set;

/**
 * Common interface of parsed formula representation.
 * Main purpose is to calculate formula value. Formula can contain some set of variables, you can get
 * this set of variables using {@link #getVariables()} method.
 * In case if formula contains variables ({@link #getVariables()} returns non-empty set),
 * before calculation each variable should have it's value.
 * To set variable use {@link #setVariableValue(char, double)} method.
 * For calculation of formula value {@link #calculate()} method need invoke.
 */
public interface Formula {

    /**
     * Return {@link Set} of variables present in formula.
     * In case if variables absent in formula empty {@link Set} return.
     *
     * @return {@link Set} of variables.
     */
    public Set<Character> getVariables();

    /**
     * Set given variable value for given variable name.
     * In case if variable name is absent in variables {@link IllegalArgumentException} will thrown.
     *
     * @param variableName variable value
     * @param variableValue variable value.
     * @return current instance.
     */
    public Formula setVariableValue(char variableName, double variableValue);

    /**
     * Calculates value of formula. In case if formula contains variable
     * ({@link #getVariables()} returns non-empty set), each variable should have it's value.
     * To set variable use {@link #setVariableValue(char, double)} method.
     * In case if formula not contains variable ({@link #getVariables()} returns empty set), operation
     * could be invoked directly.
     *
     * @return calculated value of formula
     * @throws IllegalArgumentException if arguments not contains all of present variables in formula
     */
    public double calculate();
}
