package formula.parser.tests.util;

import formula.parser.api.AbstractBinaryOperation;
import formula.parser.api.AbstractUnaryOperation;
import formula.parser.operation.BinaryOperation;
import formula.parser.operation.UnaryOperation;

public class CustomTestOperations {

    public static final UnaryOperation EXP_UNARY_OPERATION = new AbstractUnaryOperation(UnaryOperation.Notation.PREFIX, "exp") {
        @Override
        public double operate(double argument) {
            return Math.pow(Math.E , argument);
        }
    };

    public static final BinaryOperation MOD_BINARY_OPERATION = new AbstractBinaryOperation("%", "mod") {
        @Override
        public double operate(double firstArgument, double secondArgument) {
            return firstArgument % secondArgument;
        }
    };
}
