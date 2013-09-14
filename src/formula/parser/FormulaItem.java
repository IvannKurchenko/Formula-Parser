package formula.parser;

import formula.parser.operation.Operation;
import formula.parser.operation.UnaryOperation;
import formula.parser.token.FormulaToken;

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
    private final double digitValue;
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
    public static FormulaItem newDigitItem(double digitValue) {
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

        digitValue = LITERAL_NO_VALUE;
        variableName = VARIABLE_NO_NAME;
    }

    private FormulaItem(double digitValue) {
        type = Type.DIGIT;
        priority = MAXIMUM_PRIORITY;
        this.digitValue = digitValue;

        operation = null;
        variableName = VARIABLE_NO_NAME;
    }

    private FormulaItem(char variableName) {
        type = Type.VARIABLE;
        priority = MAXIMUM_PRIORITY;
        this.variableName = variableName;

        operation = null;
        digitValue = LITERAL_NO_VALUE;
    }

    private FormulaItem(boolean openBracket) {
        type = openBracket ? Type.OPEN_BRACKET : Type.CLOSE_BRACKET;
        priority = NO_PRIORITY;
        operation = null;
        variableName = VARIABLE_NO_NAME;
        digitValue = LITERAL_NO_VALUE;
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
        return digitValue;
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
     * Checks is item type of {@link Type#OPEN_BRACKET}.
     * @return 'true' if item is type of {@link Type#OPEN_BRACKET}, 'false' otherwise.
     */
    public boolean isOpenBracket(){
        return getType() == FormulaItem.Type.OPEN_BRACKET;
    }

    /**
     * Checks is item type of {@link Type#CLOSE_BRACKET}.
     * @return 'true' if item is type of {@link Type#CLOSE_BRACKET}, 'false' otherwise.
     */
    public boolean isCloseBracket(){
        return getType() == FormulaItem.Type.CLOSE_BRACKET;
    }

    /**
     * Check is item type of {@link Type#DIGIT} or {@link Type#VARIABLE}
     * @return 'true' if item is type of {@link Type#DIGIT} or {@link Type#VARIABLE}, 'false' otherwise.
     */
    public boolean isArgument(){
        return  getType() == FormulaItem.Type.VARIABLE |
                getType() == FormulaItem.Type.DIGIT;
    }

    /**
     * Check is item is {@link UnaryOperation} with {@link UnaryOperation.Notation#POSTFIX}.
     * @return 'true' item is {@link UnaryOperation} with {@link UnaryOperation.Notation#POSTFIX}, 'false' otherwise.
     */
    public boolean isPostfixUnaryOperation(){
        return checkUnaryOperationNotation(UnaryOperation.Notation.POSTFIX);
    }

    /**
     * Check is item is {@link UnaryOperation} with {@link UnaryOperation.Notation#PREFIX}.
     * @return 'true' item is {@link UnaryOperation} with {@link UnaryOperation.Notation#PREFIX}, 'false' otherwise.
     */
    public boolean isPrefixUnaryOperation(){
        return checkUnaryOperationNotation(UnaryOperation.Notation.PREFIX);
    }

    private boolean checkUnaryOperationNotation(UnaryOperation.Notation notation){
        if(!isUnaryOperation()){
            return false;
        }

        UnaryOperation unaryOperation = (UnaryOperation) getOperation();
        return unaryOperation.getNotation() == notation;
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
                return Double.toString(digitValue);

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

    @Override
    public boolean equals(Object object) {
        if(object == null){
            return false;
        }

        if(!(object instanceof FormulaItem)){
            return false;
        }

        FormulaItem another = (FormulaItem) object;

        if(type != another.type)
            return false;

        switch (another.type){
            case DIGIT:
                return digitValue == another.digitValue;

            case VARIABLE:
                return variableName == another.variableName;

            case OPERATION:
                return operation.equals(another.operation);

            case OPEN_BRACKET:
                return type == Type.OPEN_BRACKET;

            case CLOSE_BRACKET:
                return type == Type.CLOSE_BRACKET;

            default:
                return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode = 31 * hashCode + type.hashCode();
        hashCode = 31 * hashCode + (operation != null ? operation.hashCode() : 0 );
        hashCode = 31 * hashCode + Double.valueOf(digitValue).hashCode();
        hashCode = 31 * hashCode + variableName;
        hashCode = 31 * hashCode + priority;
        return hashCode;
    }
}
