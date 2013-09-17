package formula.parser.api;

import formula.parser.operation.UnaryOperation;

public abstract class AbstractUnaryOperation extends AbstractOperation implements UnaryOperation {

    private final Notation notation;

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
