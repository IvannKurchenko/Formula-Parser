package formula.parser.operation;

/**
 * Enum of specific {@link UnaryOperation} implementations.
 */
public enum UnaryOperations implements UnaryOperation {

    FACTORIAL("!") {
        @Override
        public double operate(double argument) {
            return argument > 0 ? fact(argument) : 0;
        }

        private double fact(double num) {
            return (num == 0) ? 1 : num * fact(num - 1);
        }
    },

    LN("ln") {
        @Override
        public double operate(double argument) {
            return Math.log(argument);
        }
    },

    SIN("sin") {
        @Override
        public double operate(double argument) {
            return Math.sin(argument);
        }
    },

    COS("cos") {
        @Override
        public double operate(double argument) {
            return Math.cos(argument);
        }
    },

    TAN("tan") {
        @Override
        public double operate(double argument) {
            return Math.tan(argument);
        }
    },

    ARC_SIN("asin", "arcsin") {
        @Override
        public double operate(double argument) {
            return Math.asin(argument);
        }
    },

    ARC_COS("acos", "arccos") {
        @Override
        public double operate(double argument) {
            return Math.acos(argument);
        }
    },

    ARC_TAN("atan", "arctan") {
        @Override
        public double operate(double argument) {
            return Math.atan(argument);
        }
    };

    private static final int UNARY_OPERATION_MINIMUM_PRIORITY = 2;

    private int priority;
    private String[] sings;

    UnaryOperations(String... signs){
        this(UNARY_OPERATION_MINIMUM_PRIORITY, signs);
    }

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
