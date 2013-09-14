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

import static org.junit.Assert.assertTrue;

public class OperationsTest {

    @Test
    public void priorityShouldBeInRange() {
        checkPriorityInRange(BinaryOperations.values());
        checkPriorityInRange(UnaryOperations.values());
    }

    @Test
    public void signShouldBeNotEmpty() {
        checkEmptySigns(UnaryOperations.values());
        checkEmptySigns(BinaryOperations.values());
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

    @Test
    public void unarOperationShouldHaveGreaterPriorityThenBinary(){
        for(Operation unaryOperation :  UnaryOperations.values()){
            for (Operation binaryOperation : BinaryOperations.values()){
                assertTrue( String.format("Operation %s have greater priority then %s", binaryOperation, unaryOperation),
                            unaryOperation.getPriority() > binaryOperation.getPriority() );
            }
        }
    }

    private void checkPriorityInRange(Operation[] operations) {
        for (Operation operation : operations) {

            Assert.assertTrue("Operation priority lower then possible minimum",
                    operation.getPriority() >= Operation.MIN_PRIORITY);

            Assert.assertTrue("Operation priority lower then possible minimum",
                    operation.getPriority() <= Operation.MAX_PRIORITY);

        }
    }

    private void checkEmptySigns(Operation... operations) {
        for (Operation operation : operations) {
            for (String sign : operation.getSigns()) {
                Assert.assertTrue(String.format("Operation %s have empty sign", operation),
                        sign != null && sign.length() > 0);
            }
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
