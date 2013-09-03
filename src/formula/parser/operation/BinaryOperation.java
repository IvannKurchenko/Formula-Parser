package formula.parser.operation;

/**
 * General interface for mathematical operation over two argument.
 * @see Type#BINARY
 */
public interface BinaryOperation extends Operation {

    /**
     * Calculates result of two arguments for specific implementation;
     *
     * @param firstArgument left operand value
     * @param secondArgument right operand value
     * @return result of operation
     */
    public double operate(double firstArgument, double secondArgument);
}
