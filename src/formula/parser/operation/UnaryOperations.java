package formula.parser.operation;

/**
 * Enum of specific {@link UnaryOperation} implementations.
 */
public enum UnaryOperations implements UnaryOperation {

    FACTORIAL(2, "!") {
        @Override
        public double operate(double argument) {
            return argument > 0 ? fact(argument) : 0;
        }

        private double fact(double num) {
            return (num == 0) ? 1 : num * fact(num - 1);
        }
    },

    LN(2, "ln") {
        @Override
        public double operate(double argument) {
            return Math.log(argument);
        }
    },

    SIN(2, "sin") {
        @Override
        public double operate(double argument) {
            return Math.sin(argument);
        }
    },

    COS(2, "cos") {
        @Override
        public double operate(double argument) {
            return Math.cos(argument);
        }
    },

    TAN(2, "tan") {
        @Override
        public double operate(double argument) {
            return Math.tan(argument);
        }
    },

    ARC_SIN(2, "asin", "arcsin") {
        @Override
        public double operate(double argument) {
            return Math.asin(argument);
        }
    },

    ARC_COS(2, "acos", "arccos") {
        @Override
        public double operate(double argument) {
            return Math.acos(argument);
        }
    },

    ARC_TAN(2, "atan", "arctan") {
        @Override
        public double operate(double argument) {
            return Math.atan(argument);
        }
    };

    private int priority;
    private String[] sings;

    UnaryOperations(int priority, String... sings) {
        this.priority = priority;
        this.sings = sings;
    }

    @Override
    public Type getType() {
        return Type.UNARY;
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
