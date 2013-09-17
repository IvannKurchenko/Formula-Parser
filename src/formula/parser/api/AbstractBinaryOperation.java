package formula.parser.api;

import formula.parser.operation.BinaryOperation;

/**
 * TODO : describe and implement!
 */
public abstract class AbstractBinaryOperation extends AbstractOperation implements BinaryOperation {

    /**
     *
     * @param priority
     * @param signs
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
