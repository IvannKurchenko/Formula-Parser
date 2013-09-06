package formula.parser;

import formula.parser.operation.Operation;

/**
 * Class represent general structural element of formula.
 */
public class FormulaItem {

    /**
     * Enum represent type of {@link FormulaItem}
     */
    public enum Type {
        /**
         * Type of {@link FormulaItem} that represents {@link Operation} item.
         */
        OPERATION,

        /**
         * Type of {@link FormulaItem} that represents digit literal value.
         */
        DIGIT,

        /**
         * Type of {@link FormulaItem} that represents variable.
         */
        VARIABLE,

        /**
         * Type of {@link FormulaItem} that represents open bracket.
         */
        OPEN_BRACKET,

        /**
         * Type of {@link FormulaItem} that represents close bracket.
         */
        CLOSE_BRACKET
    }

    public static final int MINIMAL_PRIORITY = 0;
    public static final int MAXIMUM_PRIORITY = Integer.MAX_VALUE;

    public static final char VARIABLE_NO_NAME = '\u0000';
    public static final double LITERAL_NO_VALUE = 0;
    public static final int NO_PRIORITY = -1;

    private final Type type;
    private final Operation operation;
    private final double literalValue;
    private final char variableName;
    private final int priority;

    /**
     * Constructs new {@link FormulaItem} that represents given {@link Operation}.
     *
     * @param operation formula operation.
     * @return new {@link FormulaItem} that represents given {@link Operation}.
     * @see Type#OPERATION
     */
    public static FormulaItem newOperationItem(Operation operation) {
        return new FormulaItem(operation);
    }

    /**
     * Constructs new {@link FormulaItem} that represents digit literal with given value.
     *
     * @param digitValue value of digit literal
     * @return new {@link FormulaItem} that represents digit literal
     * @see Type#DIGIT
     */
    public static FormulaItem newDigitLiteralItem(double digitValue) {
        return new FormulaItem(digitValue);
    }

    /**
     * Constructs new {@link FormulaItem} that represents variable with given name
     *
     * @param variableName name of variable.
     * @return new {@link FormulaItem} that represents variable
     * @see Type#VARIABLE
     */
    public static FormulaItem newVariableItem(char variableName) {
        return new FormulaItem(variableName);
    }

    /**
     * Constructs new {@link FormulaItem} that represents open or close bracket depend on given parameter.
     *
     * @param openBracket 'true' value represent open bracket - '(' and 'false' - close bracket - ')'
     * @return new {@link FormulaItem} that represents open or close bracket.
     * @see Type#OPEN_BRACKET
     * @see Type#CLOSE_BRACKET
     */
    public static FormulaItem newBracketItem(boolean openBracket) {
        return new FormulaItem(openBracket);
    }

    private FormulaItem(Operation operation) {
        type = Type.OPERATION;
        priority = operation.getPriority();
        this.operation = operation;

        literalValue = LITERAL_NO_VALUE;
        variableName = VARIABLE_NO_NAME;
    }

    private FormulaItem(double literalValue) {
        type = Type.DIGIT;
        priority = MAXIMUM_PRIORITY;
        this.literalValue = literalValue;

        operation = null;
        variableName = VARIABLE_NO_NAME;
    }

    private FormulaItem(char variableName) {
        type = Type.VARIABLE;
        priority = MAXIMUM_PRIORITY;
        this.variableName = variableName;

        operation = null;
        literalValue = LITERAL_NO_VALUE;
    }

    private FormulaItem(boolean openBracket) {
        type = openBracket ? Type.OPEN_BRACKET : Type.CLOSE_BRACKET;
        priority = NO_PRIORITY;
        operation = null;
        variableName = VARIABLE_NO_NAME;
        literalValue = LITERAL_NO_VALUE;
    }

    /**
     * Return operation that item represents or 'null' if item is not operation.
     * Supported for {@link Type#OPERATION}.
     *
     * @return operation that item represents.
     * @see FormulaItem#getType()
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Return value of literal digit that item represents or
     * {@link FormulaItem#LITERAL_NO_VALUE} if item is not operation.
     * Supported for {@link Type#DIGIT}
     *
     * @return value of digit literal.
     * @see FormulaItem#getType()
     */
    public double getDigitLiteralValue() {
        return literalValue;
    }

    /**
     * Return variable name that item represents or
     * {@link FormulaItem#VARIABLE_NO_NAME} if item is not operation.
     * Supported for {@link Type#VARIABLE_NO_NAME}.
     *
     * @return variable name that item represents
     * @see FormulaItem#getType()
     */
    public char getVariableName() {
        return variableName;
    }

    /**
     * Returns priority of item.
     * If item type is {@link Type#OPERATION} it returns {@link Operation#getPriority()}.
     * If item type is {@link Type#VARIABLE} or {@link Type#DIGIT} it returns {@link FormulaItem#MAXIMUM_PRIORITY}.
     * If item type is {@link Type#OPEN_BRACKET} or {@link Type#CLOSE_BRACKET} it returns {@link FormulaItem#NO_PRIORITY}.
     *
     * @return priority of item.
     * @see FormulaItem#MINIMAL_PRIORITY
     * @see FormulaItem#MAXIMUM_PRIORITY
     * @see Operation#getPriority()
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Check is item represents {@link formula.parser.operation.BinaryOperation}
     *
     * @return 'true' if item {@link Type} of {@link Type#OPERATION} and represent
     *         {@link formula.parser.operation.Operation.Type} is
     *         {@link formula.parser.operation.Operation.Type#BINARY},
     *         'false' otherwise.
     */
    public boolean isBinaryOperation() {
        return operation != null && operation.getType() == Operation.Type.BINARY;
    }

    /**
     * Check is item represents {@link formula.parser.operation.UnaryOperation}
     *
     * @return 'true' if item {@link Type} of {@link Type#OPERATION} and represent
     *         {@link formula.parser.operation.Operation.Type} is
     *         {@link formula.parser.operation.Operation.Type#UNARY},
     *         'false' otherwise.
     */
    public boolean isUnaryOperation() {
        return operation != null && operation.getType() == Operation.Type.UNARY;
    }

    /**
     * Checks is item type of {@link Type#OPEN_BRACKET} or {@link Type#CLOSE_BRACKET}.
     *
     * @return 'true' if item is item type of {@link Type#OPEN_BRACKET} or {@link Type#CLOSE_BRACKET},
     *         'false' otherwise.
     */
    public boolean isBracket() {
        return type == Type.OPEN_BRACKET || type == Type.CLOSE_BRACKET;
    }

    /**
     * Return {@link Type} of item.
     *
     * @return {@link Type} of item.
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {

            case OPERATION:
                return operation.toString();

            case DIGIT:
                return Double.toString(literalValue);

            case VARIABLE:
                return Character.toString(variableName);

            case OPEN_BRACKET:
                return "(";

            case CLOSE_BRACKET:
                return ")";

            default:
                return "unknown";
        }
    }
}
