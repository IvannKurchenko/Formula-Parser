package formula.parser.api;

import formula.parser.operation.UnaryOperation;

/**
 * Bas implementation of {@link UnaryOperation} interface.
 */
public abstract class AbstractUnaryOperation extends AbstractOperation implements UnaryOperation {

    private static final int MINIMUM_UNARY_OPERATION_PRIORITY = 5;
    private final Notation notation;

    /**
     * Create operation with given signs, notation and minimum priority.
     *
     * @param notation {@link Notation} type of operation.
     * @param signs signs related to this operation.
     */
    public AbstractUnaryOperation(Notation notation, String... signs) {
        this(notation, MINIMUM_UNARY_OPERATION_PRIORITY, signs);
    }

    /**
     * Creates operation.
     *
     * @param notation {@link Notation} type of operation.
     * @param priority priority of operation.
     * @param signs signs related to this operation.
     */
    public AbstractUnaryOperation(Notation notation, int priority, String... signs) {
        super(priority, signs);
        this.notation = notation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Notation getNotation() {
        return notation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return Type.UNARY;
    }
}
