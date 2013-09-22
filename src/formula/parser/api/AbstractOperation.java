package formula.parser.api;

import formula.parser.operation.Operation;

/*package*/ abstract class AbstractOperation implements Operation {

    private static void checkPriorityBound(int priority){
        if(priority < MIN_PRIORITY || priority > MAX_PRIORITY){
            throw  new IllegalArgumentException("Priority out of allowed bound!");
        }
    }

    private static void checkEmptySign(String... signs){
        for(String sign : signs){
            if(sign == null || sign.length() == 0 ){
                throw new IllegalArgumentException("Operation sign should be non null and non empty");
            }
        }
    }

    private final int priority;
    private final String[] signs;

    /**
     * Base constructor for operation implementation.
     *
     * @param priority priority of operation
     * @param signs signs related to given operation
     */
    /*package*/ AbstractOperation(int priority, String... signs){
        checkPriorityBound(priority);
        checkEmptySign(signs);
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
