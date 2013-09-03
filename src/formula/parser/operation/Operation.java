package formula.parser.operation;

/**
 * General interface for mathematical operations.
 */
public interface Operation {

    /**
     * Enum represents type of operation.
     */
    public enum Type {

        /**
         * Type represents operation over two operands :
         * <code> a BINARY_OPERATION b</code>, where 'a' and 'b' - operands.
         * Example : a + b
         * @see BinaryOperation
         */
        BINARY,

        /**
         * Type represents operation over one operand :
         * <code> UNARY_OPERATION a </code>, where 'a' - operand.
         * Example : !a
         * @see UnaryOperation
         */
        UNARY
    }

    /**
     * Represents minimal value of operation priority.
     */
    public static final int MIN_PRIORITY = 0;

    /**
     * Represents maximum value of operation priority.
     */
    public static final int MAX_PRIORITY = 10;

    /**
     * Return type of operation.
     * @return type of operation.
     */
    public Type getType();

    /**
     * Returns priority of operation. Should be from {@link Operation#MIN_PRIORITY} to {@link Operation#MAX_PRIORITY}.
     * @return priority of operation
     */
    public int getPriority();

    /**
     * Returns signs related to this operation.
     * @return signs related to this operation
     */
    public String[] getSigns();
}
