package formula.parser.operation;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class for searching required {@link Operation} by it's sign.
 */
public class OperationResolver {

    private static final Map<String, BinaryOperation> BINARY_OPERATIONS = new HashMap<String, BinaryOperation>();
    private static final Map<String, UnaryOperation> UNARY_OPERATIONS = new HashMap<String, UnaryOperation>();

    private static int MAX_OPERATION_NAME_LENGTH;
    private static int MAX_OPERATION_PRIORITY;

    static {
        fillOperationMap(BINARY_OPERATIONS, BinaryOperations.values());
        fillOperationMap(UNARY_OPERATIONS, UnaryOperations.values());
    }

    private static <T extends Operation> void fillOperationMap(Map<String, T> operations, T[] values) {
        for (T operation : values) {

            for (String sign : operation.getSigns()) {
                operations.put(sign, operation);

                MAX_OPERATION_NAME_LENGTH = sign.length() > MAX_OPERATION_NAME_LENGTH ?
                        sign.length() : MAX_OPERATION_NAME_LENGTH;
            }

            MAX_OPERATION_PRIORITY = operation.getPriority() > MAX_OPERATION_PRIORITY ?
                    operation.getPriority() : MAX_OPERATION_PRIORITY;
        }
    }

    /**
     * Return maximum length of declared operations.
     *
     * @return maximum length of declared operations.
     */
    public static int getMaxOperationSignLength() {
        return MAX_OPERATION_NAME_LENGTH;
    }

    /**
     * Return maximum priority of declared operations.
     *
     * @return maximum priority of declared operations.
     */
    public static int getMaxOperationPriority() {
        return MAX_OPERATION_PRIORITY;
    }

    /**
     * Search {@link Operation} by it sign.
     *
     * @param sign of {@link Operation}.
     * @return found specific {@link Operation} implementation related to given sign
     *         or 'null' in case if no operation related to given sign.
     * @see OperationResolver#findBinaryOperationBySign(String)
     * @see OperationResolver#findUnaryOperationBySign(String)
     * @see Operation
     */
    public static Operation findOperationBySign(String sign) {
        Operation unaryOperation = findUnaryOperationBySign(sign);
        return unaryOperation != null ? unaryOperation : findBinaryOperationBySign(sign);
    }

    /**
     * Search {@link BinaryOperation} by it sign.
     *
     * @param sign of {@link BinaryOperation}.
     * @return found specific {@link BinaryOperation} implementation related to given sign
     *         or 'null' in case if no operation related to given sign.
     * @see OperationResolver#findOperationBySign(String)
     * @see OperationResolver#findUnaryOperationBySign(String)
     * @see BinaryOperation
     * @see BinaryOperations
     */
    public static BinaryOperation findBinaryOperationBySign(String sign) {
        return BINARY_OPERATIONS.get(sign);
    }

    /**
     * Search {@link UnaryOperation} by it sign.
     *
     * @param sign of {@link UnaryOperation}.
     * @return found specific {@link UnaryOperation} implementation related to given sign
     *         or 'null' in case if no operation related to given sign.
     * @see OperationResolver#findOperationBySign(String)
     * @see OperationResolver#findBinaryOperationBySign(String)
     * @see UnaryOperation
     * @see UnaryOperations
     */
    public static UnaryOperation findUnaryOperationBySign(String sign) {
        return UNARY_OPERATIONS.get(sign);
    }
}
