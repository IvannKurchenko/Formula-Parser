package formula.parser.operation;

import java.util.*;

/**
 * Util class for searching required {@link Operation} by it's sign.
 */
public class OperationResolver {

    private static final Set<Operation> SUPPORTED_OPERATIONS = createOperationSet(UnaryOperations.values(),
            BinaryOperations.values());

    private static final Map<String, BinaryOperation> BINARY_OPERATIONS = createOperationMap((BinaryOperation[])
            BinaryOperations.values());

    private static final Map<String, UnaryOperation> UNARY_OPERATIONS = createOperationMap((UnaryOperation[])
            UnaryOperations.values());

    private static Set<Operation> createOperationSet(Operation[]... operations) {
        Set<Operation> operationSet = new HashSet<Operation>();
        for (Operation[] operationArray : operations) {
            for (Operation operation : operationArray) {
                operationSet.add(operation);
            }
        }
        return Collections.unmodifiableSet(operationSet);
    }

    private static <T extends Operation> Map<String, T> createOperationMap(T[] values) {
        Map<String, T> operations = new HashMap<String, T>();
        for (T operation : values) {
            for (String sign : operation.getSigns()) {
                operations.put(sign, operation);
            }
        }
        return Collections.unmodifiableMap(operations);
    }

    public static Set<Operation> getSupportedOperations() {
        return SUPPORTED_OPERATIONS;
    }

    private Map<String, BinaryOperation> binaryOperations;
    private Map<String, UnaryOperation> unaryOperations;
    private int operationMaxLength;

    public OperationResolver() {
        binaryOperations = new HashMap<String, BinaryOperation>(BINARY_OPERATIONS);
        unaryOperations = new HashMap<String, UnaryOperation>(UNARY_OPERATIONS);
        operationMaxLength = -1;
    }


    /**
     * Add custom {@link Operation} to known operations.
     *
     * @param operation custom implementation of {@link Operation} interface.
     */
    public void addOperation(Operation operation){
        switch (operation.getType()){
            case UNARY:
                addOperation(unaryOperations, operation);
                break;

            case BINARY:
                addOperation(binaryOperations, operation);
                break;
        }
    }

    private <T extends Operation> void addOperation(Map<String, T> operations, Operation newOperation){
        T operation = (T) newOperation;
        for(String sign : operation.getSigns()) {
            operations.put(sign, operation);
        }
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
    public Operation findOperationBySign(String sign) {
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
    public BinaryOperation findBinaryOperationBySign(String sign) {
        return binaryOperations.get(sign);
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
    public UnaryOperation findUnaryOperationBySign(String sign) {
        return unaryOperations.get(sign);
    }

    /**
     * Return maximum length of declared operations.
     *
     * @return maximum length of declared operations.
     */
    public int getOperationMaxLength() {
        if (operationMaxLength < 0) {
            findOperationsMaxLength(unaryOperations);
            findOperationsMaxLength(binaryOperations);
        }
        return operationMaxLength;
    }

    private void findOperationsMaxLength(Map<String, ? extends Operation> operations) {
        for (Operation operation : operations.values()) {
            for (String sign : operation.getSigns()) {
                operationMaxLength = sign.length() > operationMaxLength ? sign.length() : operationMaxLength;
            }
        }
    }
}
