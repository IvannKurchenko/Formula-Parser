package formula.parser;

import formula.parser.operation.BinaryOperation;
import formula.parser.operation.UnaryOperation;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;

import java.util.*;

import static formula.parser.FormulaItem.Type.*;

class FormulaTree implements Formula {

    private Node rootNode;
    private Set<Character> variables;

    FormulaTree(FormulaTokenizer formulaTokenizer) {
        variables = new HashSet<Character>();
        buildTree(formulaTokenizer);
    }

    @Override
    public Set<Character> variables() {
        return variables;
    }

    @Override
    public double calculate(Map<Character, Double> argument) {
        return calculate(rootNode, argument);
    }

    private void buildTree(FormulaTokenizer formulaTokenizer) {
        List<FormulaToken> tokenList = formulaTokenizer.getTokenList();
        Node nextNode = addRoot(tokenList);
        for (int i = 1; i < tokenList.size(); i++) {
            addVariable(tokenList.get(i));
            nextNode = addNode(tokenList.get(i).getItem(), nextNode);
        }
    }

    private Node addRoot(List<FormulaToken> tokenList) {
        rootNode = new Node(tokenList.get(0).getItem(), null);
        addVariable(tokenList.get(0));
        return rootNode;
    }

    private void addVariable(FormulaToken token) {
        if (token.getItem().getType() == FormulaItem.Type.VARIABLE) {
            variables.add(token.getItem().getVariableName());
        }
    }

    private Node addNode(FormulaItem formulaItem, Node startNode) {
        switch (formulaItem.getType()){

            case VARIABLE:
            case DIGIT_LITERAL:
                return addArgument(formulaItem, startNode);

            case OPERATION:
                return addOperation(formulaItem, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addBracket(formulaItem, startNode);

            default:
                return rootNode;
        }
    }

    private Node addArgument(FormulaItem item, Node startNode) {

        switch (startNode.getFormulaItem().getType()){

            case OPERATION:
                return addArgumentToOperation(item, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addArgumentToBracket(item, startNode);

            default:
                return rootNode;
        }
    }

    private Node addArgumentToOperation(FormulaItem item, Node operationNode) {
        Node argumentNode = new Node(item, operationNode);

        switch (operationNode.getFormulaItem().getOperation().getType()) {
            case BINARY:
                operationNode.setRightNode(argumentNode);
                break;

            case UNARY:
                operationNode.setLeftNode(argumentNode);
        }

        Node parentNode = operationNode.getParentNode();
        return  parentNode != null && parentNode.isBracket() ?
                parentNode :
                rootNode;
    }

    private Node addArgumentToBracket(FormulaItem item, Node bracketNode){
        Node argumentNode = new Node(item, bracketNode);
        bracketNode.setLeftNode(argumentNode);
        return bracketNode;
    }

    private Node addOperation(FormulaItem item, Node startNode) {
        switch (startNode.getFormulaItem().getType()){
            case VARIABLE:
            case DIGIT_LITERAL:
                return reduceOperation(item, startNode);

            case OPERATION:
                return addOperationToOperation(item, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addOperationToBracket(item, startNode);

            default:
                return rootNode;
        }
    }

    private Node addOperationToOperation(FormulaItem item, Node operationNode) {
        int itemPriority = item.getPriority();
        int nodePriority = operationNode.getFormulaItem().getPriority();

        if (itemPriority > nodePriority) {

            return increaseOperation(item, operationNode);

        } else {

            return reduceOperation(item, operationNode);
        }
    }

    private Node reduceOperation(FormulaItem item, Node startNode){
        Node parentNode = startNode.getParentNode();
        Node operationNode = new Node(item, parentNode);
        operationNode.setLeftNode(startNode);
        startNode.setParentNode(operationNode);

        if (parentNode != null) {

            if (parentNode.isBinaryOperation()) {
                parentNode.setRightNode(operationNode);
            } else if (parentNode.isBracket()) {
                parentNode.setLeftNode(operationNode);
            }

            return operationNode;

        } else {
            rootNode = operationNode;
            return rootNode;
        }
    }

    private Node increaseOperation(FormulaItem item, Node startNode){
        Node operationNode = new Node(item, startNode);
        Node nodeRightArgument = startNode.getRightNode();
        operationNode.setLeftNode(nodeRightArgument);
        startNode.setRightNode(operationNode);
        if (nodeRightArgument != null) {
            nodeRightArgument.setParentNode(operationNode);
        }
        return operationNode;
    }

    private Node addOperationToBracket(FormulaItem item, Node startNode) {
        Node leftNode = startNode.getLeftNode();
        if (leftNode != null) {
            Node nextNode = addNode(item, leftNode);
            return nextNode == rootNode ? startNode : nextNode;
        } else {
            Node bracketLeftNode = new Node(item, startNode);
            startNode.setLeftNode(bracketLeftNode);
            return startNode;
        }
    }

    private Node addBracket(FormulaItem item, Node startNode) {
        switch (item.getType()){
            case OPEN_BRACKET:
                return addOpenBracket(item, startNode);

            case CLOSE_BRACKET:
                return addCloseBracket(item, startNode);

            default:
                return rootNode;
        }
    }

    private Node addOpenBracket(FormulaItem item, Node startNode) {
        switch (startNode.getFormulaItem().getType()){
            case OPERATION:
                return addOpenBracketToOperation(item, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addOpenBracketToBracket(item, startNode);

            default:
                return startNode;
        }
    }

    private Node addOpenBracketToOperation(FormulaItem item, Node operationNode) {
        BracketsNode bracketsNode = new BracketsNode(item, operationNode);
        switch(operationNode.getFormulaItem().getOperation().getType()){
            case UNARY:
                operationNode.setLeftNode(bracketsNode);
                break;
            case BINARY:
                operationNode.setRightNode(bracketsNode);
        }
        return bracketsNode;
    }

    private Node addOpenBracketToBracket(FormulaItem item, Node bracketNode) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private Node addCloseBracket(FormulaItem item, Node startNode) {
        BracketsNode bracketsNode = (BracketsNode) startNode;
        bracketsNode.setCloseBracket(item);
        Node parentBracketNode = bracketsNode.getParentBracket();
        return parentBracketNode != null ? parentBracketNode : rootNode;
    }

    private double calculate(Node node, Map<Character, Double> arguments) {
        switch (node.getFormulaItem().getType()) {

            case OPERATION:
                return calculateOperation(node, arguments);

            case DIGIT_LITERAL:
                return getLiteralValue(node);

            case VARIABLE:
                return getVariableValue(node, arguments);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
            default:
                return calculate(node.getLeftNode(), arguments);
        }
    }

    private double calculateOperation(Node node, Map<Character, Double> arguments) {
        if (node.isBinaryOperation()) {

            BinaryOperation binaryOperation = (BinaryOperation) node.getFormulaItem().getOperation();
            double leftArgument = calculate(node.getLeftNode(), arguments);
            double rightArgument = calculate(node.getRightNode(), arguments);
            return binaryOperation.operate(leftArgument, rightArgument);

        } else {

            UnaryOperation unaryOperation = (UnaryOperation) node.getFormulaItem().getOperation();
            double argument = calculate(node.getLeftNode(), arguments);
            return unaryOperation.operate(argument);

        }
    }

    private double getLiteralValue(Node node) {
        return node.getFormulaItem().getDigitLiteralValue();
    }

    private double getVariableValue(Node node, Map<Character, Double> arguments) {
        char variableName = node.getFormulaItem().getVariableName();
        Double value = arguments.get(variableName);
        if (value == null) {
            throw new IllegalArgumentException(String.format("No mapped value for %s variable", variableName));
        }
        return value;
    }

    private static class Node {

        private Node parentNode;
        private Node rightNode, leftNode;
        private FormulaItem formulaItem;

        Node(FormulaItem item, Node parent) {
            formulaItem = item;
            parentNode = parent;
        }

        void setParentNode(Node node) {
            parentNode = node;
        }

        void setLeftNode(Node node) {
            if (!isLeaf()) {
                leftNode = node;
            } else {
                throw new IllegalArgumentException("Can't add node to leaf!");
            }
        }

        void setRightNode(Node node) {
            if (!isLeaf()) {
                rightNode = node;
            } else {
                throw new IllegalArgumentException("Can't add node to leaf!");
            }
        }

        FormulaItem getFormulaItem() {
            return formulaItem;
        }

        Node getParentNode() {
            return parentNode;
        }

        Node getLeftNode() {
            return leftNode;
        }

        Node getRightNode() {
            return rightNode;
        }

        boolean isLeaf() {
            return  formulaItem.getType() == FormulaItem.Type.VARIABLE |
                    formulaItem.getType() == DIGIT_LITERAL;
        }

        boolean isBracket(){
            return  formulaItem.isBracket();
        }

        boolean isBinaryOperation(){
            return  formulaItem.isBinaryOperation();
        }

        @Override
        public String toString() {
            return formulaItem.toString();
        }
    }

    private static class BracketsNode extends Node {

        private Node parentBracket;
        private FormulaItem closeBracket;

        BracketsNode(FormulaItem openBracket, Node parent) {
            super(openBracket, parent);
        }

        void setCloseBracket(FormulaItem closeBracket) {
            this.closeBracket = closeBracket;
        }

        boolean hasClosedBracket() {
            return closeBracket != null;
        }

        Node getParentBracket() {
            return parentBracket;
        }

        @Override
        Node getRightNode() {
            throw new UnsupportedOperationException(BracketsNode.class.getSimpleName() + "contains just one node");
        }

        @Override
        public String toString() {
            return closeBracket != null ? "()" : "(";
        }
    }
}
