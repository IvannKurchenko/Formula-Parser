package formula.parser;

import formula.parser.operation.BinaryOperation;
import formula.parser.operation.UnaryOperation;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;

import java.util.*;

import static formula.parser.FormulaItem.Type.*;

/**
 * <H1>General</H1>
 * Implementation of {@link Formula} interface based on presentation of formula as a tree structure.
 * Each node of tree contains as a value {@link FormulaItem} and from 0 to 2 children node's
 * that depends on type of {@link FormulaItem.Type}, it's next :
 *
 * <p> - {@link FormulaItem.Type#OPERATION} - could contain 1 or 2 children node's that depends on
 *      {@link formula.parser.operation.Operation.Type} of {@link formula.parser.operation.Operation} that item
 *      represents. {@link formula.parser.operation.Operation.Type#UNARY} operation contain one children node
 *      (it's left node by default) and {@link formula.parser.operation.Operation.Type#BINARY}.
 *      Each children node of {@link FormulaItem.Type#OPERATION} node represents argument of
 *      {@link formula.parser.operation.Operation}.
 *
 *
 * <p> - {@link FormulaItem.Type#VARIABLE} - not contains any children nodes and is a leaf of tree.
 *
 * <p> - {@link FormulaItem.Type#DIGIT_LITERAL} - not contains any children nodes and is a leaf of tree.
 *
 *
 * <p> - {@link FormulaItem.Type#OPEN_BRACKET} and {@link FormulaItem.Type#CLOSE_BRACKET} - contains one children node.
 *       {@link BracketsNode} is a spacial node implementation for bracket node. Each {@link BracketsNode} could be
 *       represented as a root node of formula subtree.
 *
 * <H1>Build of tree</H1>
 * Tree builds from {@link FormulaTokenizer} as a result of parsing formula from string presentation and read
 * items from left to right. Tree of formula build on next rules:
 * <p> - Priority of of parent node lower or equal than priority of children node. So, priority of nodes should increase
 *       from root node to leaf in tree. Priority of node defined as priority of {@link FormulaItem} that it contains.
 *
 * <p> - {@link BracketsNode} have no any priority and inside of brackets formula build by the same rules, as
 *       general tree.
 *
 * <p> - {@link FormulaItem.Type#VARIABLE} and {@link FormulaItem.Type#DIGIT_LITERAL} - nodes that
 *       represents arguments and should be leaves of tree, that's why they have {@link FormulaItem#MAXIMUM_PRIORITY}.
 *
 * <p> - If node could contain just one children node, by default, children node is left.
 *
 * <H1>Calculation</H!>
 * As a result of building tree rules, process of formula calculation is next : to calculate value of node need to
 * recursively calculate value of right subtree, left subtree and than value value of node. Leaf node
 * is node with arguments and contains simple value. Nodes with children node represents node with
 * {@link formula.parser.operation.Operation} and value calculate as a operation over children nodes - arguments
 * or {@link BracketsNode} - that simply skip during calculation.
 *
 * <H1>Example</H>
 *  As example, for formula "( x - 2 ) * y - z / 4" result formula tree will be next :
 *
 *  <br>
 *  <br>                     ___
 *  <br>                    |_-_|
 *  <br>                ___/     \ ___
 *  <br>               |_*_|      |_/_|
 *  <br>            __/       ___/     \___
 *  <br>           |()|      |_z_|     |_4_|
 *  <br>       ___/
 *  <br>      |_-_|
 *  <br>  ___/     \___
 *  <br> |_x_|     |_2_|
 *
 */
/*package*/ class FormulaTree implements Formula {

    private Node rootNode;
    private Set<Character> variables;

    /*package*/ FormulaTree(FormulaTokenizer formulaTokenizer) {
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

    /*
     * Add new argument item to existing operation node.
     * For binary operations it's added as right argument (node), because left argument already exist.
     * Example : x + y
     *          ___               ___
     *  x--->  |_+_|   --->      |_+_|
     *    ___ /             ___ /     \___
     *   |_x_|             |_x_|      |_y_|
     *
     * For unary operations it's added to left node.
     * Example : !x
     *          ___            ___
     *  x--->  |_x_|   --->   |_!_|
     *                   ___ /
     *                  |_x_|
     *
     */
    private Node addArgumentToOperation(FormulaItem item, Node operationNode) {
        Node newArgumentNode = new Node(item, operationNode);

        switch (operationNode.getFormulaItem().getOperation().getType()) {
            case BINARY:
                operationNode.setRightNode(newArgumentNode);
                break;

            case UNARY:
                operationNode.setLeftNode(newArgumentNode);
        }

        Node parentNode = operationNode.getParentNode();
        return  parentNode != null && parentNode.isBracket() ?
                parentNode :
                rootNode;
    }

    /*
     * Add new argument item to bracket node.
     * Because bracket not could have one children node new item adds as left children node.
     * Example : ...(x...
     *          ___            ___
     * x--->   |_(_|   --->   |_(_|
     *                   ___ /
     *                  |_x_|
     *
     */
    private Node addArgumentToBracket(FormulaItem item, Node bracketNode){
        Node newArgumentNode = new Node(item, bracketNode);
        bracketNode.setLeftNode(newArgumentNode);
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

    /*
     * Add new operation item to existing operation node.
     * This operation depends on priority of items.
     *
     */
    private Node addOperationToOperation(FormulaItem item, Node operationNode) {
        int itemPriority = item.getPriority();
        int nodePriority = operationNode.getFormulaItem().getPriority();

        if (itemPriority > nodePriority) {

            return increaseOperation(item, operationNode);

        } else {

            return reduceOperation(item, operationNode);
        }
    }

    /*
     * If new operation item priority lower or equal then existing operation node,
     * new operation node should be placed over existing node, according to tree build rules.
     * Example : ... x * y + ...
     *             ___                       ___
     *  + --->    |_P_|     ---->           |_P_|    ---> ...
     *        ___/                      ___/
     *       |_*_|                     |_+_|
     *   ___/     \___             ___/     \
     *  |_x_|     |_y_|           |_*_|  'next node'
     *                        ___/     \___
     *                       |_x_|     |_y_|
     *
     *  where P - means 'parent node'.
     *        'next node' - node where next item should be placed.
     *
     */
    private Node reduceOperation(FormulaItem item, Node operationNode){
        Node parentNode = operationNode.getParentNode();
        Node newOperationNode = new Node(item, parentNode);
        newOperationNode.setLeftNode(operationNode);
        operationNode.setParentNode(newOperationNode);

        if (parentNode != null) {

            if (parentNode.isBinaryOperation()) {
                parentNode.setRightNode(newOperationNode);
            } else if (parentNode.isBracket()) {
                parentNode.setLeftNode(newOperationNode);
            }

            return newOperationNode;

        } else {
            rootNode = newOperationNode;
            return rootNode;
        }
    }

    /*
     * If new operation item priority greater then existing operation node,
     * new operation node should be placed under existing node, according to tree build rules.
     * Example : ... x + y * ...
     *             ___                       ___
     *  * --->    |_P_|     ---->           |_P_|    --->  ...
     *        ___/                      ___/
     *       |_+_|                     |_+_|
     *   ___/     \___             ___/     \___
     *  |_x_|     |_y_|           |_x_|     |_*_|
     *                                  ___/     \
     *                                 |_y_|  'next node'
     *
     * where P - means 'parent node'.
     *       'next node' - node where next item should be placed.
     */
    private Node increaseOperation(FormulaItem item, Node operationNode){
        Node newOperationNode = new Node(item, operationNode);
        Node nodeRightArgument = operationNode.getRightNode();
        newOperationNode.setLeftNode(nodeRightArgument);
        operationNode.setRightNode(newOperationNode);
        if (nodeRightArgument != null) {
            nodeRightArgument.setParentNode(newOperationNode);
        }
        return newOperationNode;
    }

    /*
     * Add new operation item to existing bracket node.
     * In case if bracket node already have children (left) node
     * new operation item add to id recursively by calling 'addNode' method,
     * if not - add new children node to bracket.
     * Example : ... ( !x ...
     *          ___            ___
     *  !--->  |_(_|   --->   |_(_|   ---> ....
     *                   ___ /
     *                  |_!_|
     *
     */
    private Node addOperationToBracket(FormulaItem item, Node bracketNode) {
        Node leftNode = bracketNode.getLeftNode();
        if (leftNode != null) {
            Node nextNode = addNode(item, leftNode);
            return nextNode == rootNode ? bracketNode : nextNode;
        } else {
            Node bracketLeftNode = new Node(item, bracketNode);
            bracketNode.setLeftNode(bracketLeftNode);
            return bracketNode;
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

    /*
     * Add new open bracket item to existing operation node.
     * If existing operation node represents unary operation new bracket item added as right children,
     * if unary - as left children.
     * Example : ... x * ( ...
     *          ___               ___
     *  ( ---> |_*_|   --->      |_*_|       --->  ...
     *    ___ /             ___ /     \___
     *   |_x_|             |_x_|      |_(_|
     *                               /
     *                          'next node'
     *
     *  where 'next node' - node where next item should be placed.
     *
     */
    private Node addOpenBracketToOperation(FormulaItem item, Node operationNode) {
        BracketsNode newBracketsNode = new BracketsNode(item, operationNode);
        switch(operationNode.getFormulaItem().getOperation().getType()){
            case UNARY:
                operationNode.setLeftNode(newBracketsNode);
                break;
            case BINARY:
                operationNode.setRightNode(newBracketsNode);
        }
        return newBracketsNode;
    }

    /*
     * TODO  : implement and describe!
     */
    private Node addOpenBracketToBracket(FormulaItem item, Node bracketNode) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /*
     * Mark given open bracket node as closed.
     */
    private Node addCloseBracket(FormulaItem item, Node openBracketNode) {
        BracketsNode bracketsNode = (BracketsNode) openBracketNode;
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
