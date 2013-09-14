package formula.parser.operation;

/**
 * Enum of specific {@link BinaryOperation} implementations.
 */
public enum BinaryOperations implements BinaryOperation {

    ADDITION(0, "+") {
        @Override
        public double operate(double firstArgument, double secondArgument) {
            return firstArgument + secondArgument;
        }
    },

    DIVISION(1, "/") {
        @Override
        public double operate(double firstArgument, double secondArgument) {
            return firstArgument / secondArgument;
        }
    },

    MULTIPLY(1, "*") {
        @Override
        public double operate(double firstArgument, double secondArgument) {
            return firstArgument * secondArgument;
        }
    },

    POWER(2, "^") {
        @Override
        public double operate(double firstArgument, double secondArgument) {
            return Math.pow(firstArgument, secondArgument);
        }
    };

    private int priority;
    private String[] sings;

    BinaryOperations(int priority, String... sings) {
        this.priority = priority;
        this.sings = sings;
    }

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public final int getPriority() {
        return priority;
    }

    @Override
    public final String[] getSigns() {
        return sings;
    }
}
