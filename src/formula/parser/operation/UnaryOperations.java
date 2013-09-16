package formula.parser.operation;

/**
 * Enum of specific {@link UnaryOperation} implementations.
 */
public enum UnaryOperations implements UnaryOperation {

    FACTORIAL(Notation.POSTFIX, "!") {
        @Override
        public double operate(double argument) {
            return argument > 0 ? fact(argument) : 0;
        }

        private double fact(double num) {
            return (num == 0) ? 1 : num * fact(num - 1);
        }
    },

    SUBTRACTION("-") {
        @Override
        public double operate(double firstArgument) {
            return -firstArgument;
        }
    },

    LN("ln") {
        @Override
        public double operate(double argument) {
            return Math.log(argument);
        }
    },

    LG("lg") {
        @Override
        public double operate(double argument) {
            return Math.log10(argument);
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

    private static final int UNARY_OPERATION_MINIMUM_PRIORITY = 3;

    private int priority;
    private Notation notation;
    private String[] sings;

    UnaryOperations(String... signs){
        this(UNARY_OPERATION_MINIMUM_PRIORITY, Notation.PREFIX, signs);
    }

    UnaryOperations(Notation notation, String... signs){
        this(UNARY_OPERATION_MINIMUM_PRIORITY, notation, signs);
    }

    UnaryOperations(int priority, Notation notation, String... sings) {
        this.priority = priority;
        this.notation = notation;
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

    @Override
    public Notation getNotation(){
        return notation;
    }
}
