package formula.parser.tests.operation;

import formula.parser.operation.BinaryOperations;
import formula.parser.operation.Operation;
import formula.parser.operation.UnaryOperations;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationsTest {

    @Test
    public void priorityShouldBeInRange() {
        checkPriorityInRange(BinaryOperations.values());
        checkPriorityInRange(UnaryOperations.values());
    }

    private void checkPriorityInRange(Operation[] operations) {
        for (Operation operation : operations) {

            Assert.assertTrue("Operation priority lower then possible minimum",
                    operation.getPriority() >= Operation.MIN_PRIORITY);

            Assert.assertTrue("Operation priority lower then possible minimum",
                    operation.getPriority() <= Operation.MAX_PRIORITY);

        }
    }

    @Test
    public void signShouldBeNotEmpty() {
        checkEmptySigns(UnaryOperations.values());
        checkEmptySigns(BinaryOperations.values());
    }

    private void checkEmptySigns(Operation... operations) {
        for (Operation operation : operations) {
            for (String sign : operation.getSigns()) {
                Assert.assertTrue(String.format("Operation %s have empty sign", operation),
                        sign != null && sign.length() > 0);
            }
        }
    }

    @Test
    public void signShouldDefineOneOperation() {
        Map<String, List<Operation>> signOperationMap = new HashMap<String, List<Operation>>();

        appendSignOperationMap(signOperationMap, UnaryOperations.values());
        appendSignOperationMap(signOperationMap, BinaryOperations.values());

        for (Map.Entry<String, List<Operation>> signOperation : signOperationMap.entrySet()) {
            Assert.assertTrue(String.format("Sign %s defines more than on operation", signOperation.getKey()),
                    signOperation.getValue().size() == 1);
        }
    }

    private void appendSignOperationMap(Map<String, List<Operation>> signOperationMap, Operation... operations) {
        for (Operation operation : operations) {
            for (String operationSign : operation.getSigns()) {
                List<Operation> operationsList = signOperationMap.get(operationSign);
                if (operationsList == null) {
                    operationsList = new ArrayList<Operation>();
                    operationsList.add(operation);
                    signOperationMap.put(operationSign, operationsList);
                } else {
                    operationsList.add(operation);
                }
            }
        }
    }
}
