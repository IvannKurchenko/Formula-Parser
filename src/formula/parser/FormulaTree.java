package formula.parser;

import formula.parser.operation.BinaryOperation;
import formula.parser.operation.UnaryOperation;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;

import java.util.*;

import static formula.parser.FormulaItem.Type.*;

class FormulaTree implements Formula {

    private Node rootNode;
    protected Set<Character> variables;

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

    private Node addNode(FormulaItem formulaItem, Node node) {
        if (formulaItem.isArgument()) {
            return addArgument(formulaItem, node);
        }

        if (formulaItem.isOperation()) {
            return addOperation(formulaItem, node);
        }

        if (formulaItem.isBracket()) {
            return addBracket(formulaItem, node);
        }

        return rootNode;
    }

    private Node addArgument(FormulaItem item, Node node) {

        if (node.getFormulaItem().isUnaryOperation()) {
            Node unaryArgumentNode = new Node(item, node);
            node.setLeftNode(unaryArgumentNode);
            Node parentNode = node.getParentNode();
            if (parentNode != null) {
                return parentNode.getFormulaItem().isBracket() ? parentNode : rootNode;
            } else {
                return rootNode;
            }

        }

        if (node.getFormulaItem().isBinaryOperation()) {
            Node binaryRightArgument = new Node(item, node);
            node.setRightNode(binaryRightArgument);
            Node parentNode = node.getParentNode();
            if (parentNode != null) {
                return parentNode.getFormulaItem().isBracket() ? parentNode : rootNode;
            } else {
                return rootNode;
            }
        }

        if (node.getFormulaItem().isBracket()) {
            Node argumentNode = new Node(item, node);
            node.setLeftNode(argumentNode);
            return node;
        }

        return rootNode;
    }

    private Node addOperation(FormulaItem item, Node node) {
        if (node.getFormulaItem().isArgument()) {
            return addOperationToArgumentNode(item, node);
        }

        if (node.getFormulaItem().isOperation()) {
            return addOperationToOperationNode(item, node);
        }

        if (node.getFormulaItem().isBracket()) {
            return addOperationToBracketNode(item, node);
        }

        return rootNode;
    }

    private Node addOperationToArgumentNode(FormulaItem item, Node node) {
        Node parent = node.getParentNode();
        Node operationNode = new Node(item, parent);
        operationNode.setLeftNode(node);
        node.setParentNode(operationNode);

        if (parent != null) {
            if (parent.getFormulaItem().isBinaryOperation()) {
                parent.setRightNode(operationNode);
            } else if (parent.getFormulaItem().isBracket()) {
                parent.setLeftNode(operationNode);
            }
            return operationNode;
        } else {
            rootNode = operationNode;
            return rootNode;
        }
    }

    private Node addOperationToOperationNode(FormulaItem item, Node node) {
        int itemPriority = item.getPriority();
        int nodePriority = node.getFormulaItem().getPriority();

        if (itemPriority > nodePriority) {

            Node operationNode = new Node(item, node);
            Node nodeRightArgument = node.getRightNode();
            operationNode.setLeftNode(nodeRightArgument);
            node.setRightNode(operationNode);
            if (nodeRightArgument != null) {
                nodeRightArgument.setParentNode(operationNode);
            }
            return operationNode;

        } else {

            Node parentNode = node.getParentNode();
            Node operationNode = new Node(item, parentNode);
            operationNode.setLeftNode(node);
            node.setParentNode(operationNode);

            if (parentNode != null) {

                if (parentNode.getFormulaItem().isBinaryOperation()) {
                    parentNode.setRightNode(operationNode);
                } else if (parentNode.getFormulaItem().isBracket()) {
                    parentNode.setLeftNode(operationNode);
                }

                return operationNode;

            } else {
                rootNode = operationNode;
                return rootNode;
            }

        }
    }

    private Node addOperationToBracketNode(FormulaItem item, Node node) {
        Node leftNode = node.getLeftNode();
        if (leftNode != null) {
            Node nextNode = addNode(item, leftNode);
            return nextNode == rootNode ? node : nextNode;
        } else {
            Node bracketLeftNode = new Node(item, node);
            node.setLeftNode(bracketLeftNode);
            return node;
        }
    }

    private Node addBracket(FormulaItem item, Node node) {

        if (item.getType() == FormulaItem.Type.OPEN_BRACKET) {
            return addOpenBracket(item, node);
        }

        if (item.getType() == FormulaItem.Type.CLOSE_BRACKET) {
            return addCloseBracket(item, node);
        }

        return rootNode;

    }

    private Node addOpenBracket(FormulaItem item, Node node) {

        if (node.getFormulaItem().isOperation()) {
            return addOpenBracketToOperationNode(item, node);
        }

        if (node.getFormulaItem().isBracket()) {
            return addOpenBracketToBracketNode(item, node);
        }

        return node;
    }

    private Node addOpenBracketToOperationNode(FormulaItem item, Node node) {
        BracketsNode bracketsNode = new BracketsNode(item, node);

        if (node.getFormulaItem().isUnaryOperation()) {
            node.setLeftNode(bracketsNode);
        } else if (node.getFormulaItem().isBinaryOperation()) {
            node.setRightNode(bracketsNode);
        }

        return bracketsNode;
    }

    private Node addOpenBracketToBracketNode(FormulaItem item, Node node) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private Node addCloseBracket(FormulaItem item, Node node) {
        BracketsNode bracketsNode = (BracketsNode) node;
        bracketsNode.setCloseBracket(item);
        Node parentBracketNode = bracketsNode.getParentBracket();
        return parentBracketNode != null ? parentBracketNode : rootNode;
    }

    private double calculate(Node node, Map<Character, Double> arguments) {
        switch (node.getFormulaItem().getType()){

            case OPERATION:
                return calculateOperation(node, arguments);

            case DIGIT_LITERAL :
                return getLiteralValue(node);

            case VARIABLE:
                return getVariableValue(node, arguments);

            case OPEN_BRACKET:

            case CLOSE_BRACKET:

            default:
                return calculate(node.getLeftNode(), arguments);
        }
    }

    private double calculateOperation(Node node, Map<Character, Double> arguments){
        if (node.getFormulaItem().isBinaryOperation()) {

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
            return formulaItem.getType() == FormulaItem.Type.VARIABLE |
                    formulaItem.getType() == DIGIT_LITERAL;
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
