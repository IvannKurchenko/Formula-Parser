package formula.parser.api;

import formula.parser.operation.BinaryOperation;

/**
 * Base implementation of {@link BinaryOperation} interface.
 */
public abstract class AbstractBinaryOperation extends AbstractOperation implements BinaryOperation {

    private static final int MINIMUM_BINARY_OPERATION_PRIORITY = 0;

    /**
     * Creates operation with minimum operation priority.
     *
     * @param signs signs related to this operation.
     */
    public AbstractBinaryOperation(String... signs){
        this(MINIMUM_BINARY_OPERATION_PRIORITY, signs);
    }

    /**
     * Creates operation.
     *
     * @param priority priority of operation.
     * @param signs signs related to this operation.
     */
    public AbstractBinaryOperation(int priority, String... signs) {
        super(priority, signs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Type getType() {
        return Type.BINARY;
    }
}
