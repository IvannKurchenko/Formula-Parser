package formula.parser.operation;

/**
 * Enum of specific {@link UnaryOperation} implementations.
 */
public enum  UnaryOperations implements UnaryOperation {

    FACTORIAL(1,"!"){
        @Override
        public double operate(double argument) {
            return argument > 0 ? fact(argument) : 0 ;
        }

        private double fact(double num) {
            return (num == 0) ? 1 : num * fact(num - 1);
        }
    },

    LN(1,"ln"){
        @Override
        public double operate(double argument) {
            return Math.log(argument);
        }
    },

    SIN(1,"sin") {
        @Override
        public double operate(double argument) {
            return Math.sin(argument);
        }
    },

    COS(1,"cos") {
        @Override
        public double operate(double argument) {
            return Math.cos(argument);
        }
    },

    TAN (1,"tan") {
        @Override
        public double operate(double argument) {
            return Math.tan(argument);
        }
    },

    ARC_SIN (1,"asin","arcsin") {
        @Override
        public double operate(double argument) {
            return Math.asin(argument);
        }
    },

    ARC_COS(1,"acos","arccos"){
        @Override
        public double operate(double argument) {
            return Math.acos(argument);
        }
    },

    ARC_TAN (1,"atan","arctan") {
        @Override
        public double operate(double argument) {
            return Math.atan(argument);
        }
    };

    private int priority;
    private String[] sings;

    UnaryOperations(int priority, String... sings){
        this.priority = priority;
        this.sings = sings;
    }

    @Override
    public Type getType(){
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
