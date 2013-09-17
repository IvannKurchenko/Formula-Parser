package formula.parser.api;

import formula.parser.operation.Operation;

/*package*/ abstract class AbstractOperation implements Operation {

    private final int priority;
    private final String[] signs;

    /*package*/ AbstractOperation(int priority, String... signs){
        this.priority = priority;
        this.signs = signs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getPriority() {
        return priority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String[] getSigns() {
        return signs;
    }
}
