package formula.parser.operation;

/**
 * General interface for mathematical operation over one argument.
 *
 * @see Type#UNARY
 */
public interface UnaryOperation extends Operation {

    /**
     * Calculates result of argument for specific implementation;
     *
     * @param argument operand value
     * @return result of operation
     */
    public double operate(double argument);
}
