package formula.parser.tree;

import formula.parser.api.Formula;
import formula.parser.operation.BinaryOperation;
import formula.parser.operation.UnaryOperation;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;

import java.util.*;

import static formula.parser.tree.FormulaItem.Type.*;

/**
 * <H1>General</H1>
 * Implementation of {@link formula.parser.api.Formula} interface based on presentation of formula as a tree structure.
 * Each node of tree contains as a value {@link FormulaItem} and from 0 to 2 children node's
 * that depends on type of {@link FormulaItem.Type}, it's next :
 *
 * <p> - {@link FormulaItem.Type#OPERATION} - could contain 1 or 2 children node's that depends on
 *      {@link formula.parser.operation.Operation.Type} of {@link formula.parser.operation.Operation} that item
 *      represents. {@link formula.parser.operation.Operation.Type#UNARY} operation contain one children node
 *      (it's left node by default) and {@link formula.parser.operation.Operation.Type#BINARY} - two children nodes.
 *      Each children node of {@link FormulaItem.Type#OPERATION} node represents argument of
 *      {@link formula.parser.operation.Operation}.
 *
 *
 * <p> - {@link FormulaItem.Type#VARIABLE} - not contains any children nodes and is a leaf of tree.
 *
 * <p> - {@link FormulaItem.Type#DIGIT} - not contains any children nodes and is a leaf of tree.
 *
 *
 * <p> - {@link FormulaItem.Type#OPEN_BRACKET} and {@link FormulaItem.Type#CLOSE_BRACKET} - contains one children node.
 *       {@link BracketsNode} is a special node implementation for bracket node. Each {@link BracketsNode} could be
 *       represented as a root node of formula subtree.
 *
 * <H1>Build of tree</H1>
 * Tree builds from {@link FormulaTokenizer} as a result of parsing formula from string presentation and read
 * items from left to right. Tree of formula build on next rules:
 * <p> - Priority of of parent node lower or equal than priority of children node. So, priority of nodes should increase
 *       from root node to leaf in tree. Priority of node defined as priority of {@link FormulaItem} that it contains.
 *
 * <p> - {@link BracketsNode} have no any priority and inside of brackets formula builds recursively by the same rules,
 *       as general tree.
 *
 * <p> - {@link FormulaItem.Type#VARIABLE} and {@link FormulaItem.Type#DIGIT} - nodes that
 *       represents arguments and should be leaves of tree, that's why they have {@link FormulaItem#MAXIMUM_PRIORITY}.
 *
 * <p> - If node could contain just one children node, by default, children node is left.
 *
 * <H1>Calculation</H1>
 * As a result of building tree rules, process of formula calculation is next : to calculate value of node need to
 * recursively calculate value of right subtree, left subtree and than value value of node. Leaf node
 * is node with arguments and contains simple value. Nodes with children node represents node with
 * {@link formula.parser.operation.Operation} and value calculate as a operation over children nodes - arguments
 * or {@link BracketsNode} - that simply skip during calculation.
 *
 * <H1>Example</H1>
 *  As example, for formula "( x - 2 ) * y - z / 4" result formula tree will be next :
 *
 *  <br>                       R
 *  <br>                      _|_
 *  <br>                     |_-_|
 *  <br>                    /     \
 *  <br>                ___/       \ ___
 *  <br>               |_*_|        |_/_|
 *  <br>            __/     \___  __/    \___
 *  <br>           |()|     |_y_||_z_|   |_4_|
 *  <br>       ___/
 *  <br>      |_-_|
 *  <br>  ___/     \___
 *  <br> |_x_|     |_2_|
 *
 * where R - mean 'root node'
 */
public class FormulaTree implements Formula {

    private Node rootNode;
    private Set<Character> variables;
    private Map<Character, Double> variablesValues;

    public FormulaTree(List<FormulaToken> tokenList) {
        Set<Character> variables = new HashSet<Character>();
        buildTree(tokenList, variables);
        this.variables = Collections.unmodifiableSet(variables);
        variablesValues = new HashMap<Character, Double>(variables.size());
    }

    @Override
    public Set<Character> getVariables() {
        return variables;
    }

    @Override
    public Formula setVariableValue(char variableName, double variableValue) {
        if(!variables.contains(variableName)){
            throw new IllegalArgumentException("Variable is absent in formula : " + variableName);
        }
        variablesValues.put(variableName, variableValue);
        return this;
    }

    @Override
    public double calculate() {
        return checkNegativeZero(calculate(rootNode));
    }

    private void buildTree(List<FormulaToken> tokenList, Set<Character> variables) {
        Node nextNode = addRoot(tokenList, variables);
        for (int i = 1; i < tokenList.size(); i++) {
            addVariable(tokenList.get(i), variables);
            nextNode = addItem(tokenList.get(i).getItem(), nextNode);
        }
        removeBrackets(rootNode);
    }

    private Node addRoot(List<FormulaToken> tokenList, Set<Character> variables) {
        FormulaToken token = tokenList.get(0);
        rootNode = token.getItem().isBracket() ?    new BracketsNode(token.getItem(), null) :
                                                    new Node(token.getItem(), null);
        addVariable(token, variables);
        return rootNode;
    }

    private void addVariable(FormulaToken token, Set<Character> variables) {
        if (token.getItem().getType() == FormulaItem.Type.VARIABLE) {
            variables.add(token.getItem().getVariableName());
        }
    }

    private Node addItem(FormulaItem formulaItem, Node startNode) {
        switch (formulaItem.getType()){
            case VARIABLE:
            case DIGIT:
                return addArgumentItem(formulaItem, startNode);

            case OPERATION:
                return addOperationItem(formulaItem, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addBracketItem(formulaItem, startNode);

            default:
                return rootNode;
        }
    }

    private Node addArgumentItem(FormulaItem item, Node startNode) {
        switch (startNode.getFormulaItem().getType()){
            case OPERATION:
                return addArgumentItemToOperationNode(item, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addArgumentItemToBracketNode(item, startNode);

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
     * Example : -x
     *          ___            ___
     *  x--->  |_x_|   --->   |_-_|
     *                   ___ /
     *                  |_x_|
     *
     */
    private Node addArgumentItemToOperationNode(FormulaItem item, Node operationNode) {
        Node newArgumentNode = new Node(item, operationNode);

        switch (operationNode.getFormulaItem().getOperation().getType()) {
            case BINARY:
                operationNode.setRightNode(newArgumentNode);
                break;

            case UNARY:
                operationNode.setLeftNode(newArgumentNode);
        }

        Node parentNode = operationNode.getParentNode();
        Node nearestParentBracketNode = findNearestParentBracketNode(operationNode);
        return  parentNode != null && nearestParentBracketNode != null?
                findNearestParentBracketNode(operationNode) :
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
    private Node addArgumentItemToBracketNode(FormulaItem item, Node bracketNode){
        Node newArgumentNode = new Node(item, bracketNode);
        bracketNode.setLeftNode(newArgumentNode);
        return bracketNode;
    }

    private Node addOperationItem(FormulaItem item, Node startNode) {
        switch (startNode.getFormulaItem().getType()){
            case VARIABLE:
            case DIGIT:
                return reduceOperationNode(item, startNode);

            case OPERATION:
                return addOperationItemToOperationNode(item, startNode);

            case OPEN_BRACKET:
                return addOperationItemToOpenBracketNode(item, startNode);

            case CLOSE_BRACKET:
                return addOperationItemToCloseBracketNode(item, startNode);

            default:
                return rootNode;
        }
    }

    /*
     * Add new operation item to existing operation node.
     * This operation depends on priority of items.
     *
     */
    private Node addOperationItemToOperationNode(FormulaItem item, Node operationNode) {
        int itemPriority = item.getPriority();
        int nodePriority = operationNode.getFormulaItem().getPriority();

        if (itemPriority > nodePriority) {

            Node nearestPriorityNode = findNearestNodeByPriority(operationNode, itemPriority);
            return increaseOperationNode(item, nearestPriorityNode);

        } else {

            return reduceOperationNode(item, operationNode);
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
    private Node reduceOperationNode(FormulaItem item, Node operationNode){
        Node parentNode = operationNode.getParentNode();
        Node newOperationNode = new Node(item, parentNode);
        newOperationNode.setLeftNode(operationNode);
        operationNode.setParentNode(newOperationNode);

        if (parentNode != null) {

            if (parentNode.getFormulaItem().isBinaryOperation()) {
                parentNode.setRightNode(newOperationNode);
            } else if (parentNode.getFormulaItem().isBracket()) {
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
    private Node increaseOperationNode(FormulaItem item, Node operationNode){
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
     * Add new operation item to existing open bracket node.
     * In case if open bracket node already have children (left) node
     * new operation item add to id recursively by calling 'addItem' method,
     * if not - add new children node to open bracket.
     * Example : ... ( !x ...
     *          ___            ___
     *  !--->  |_(_|   --->   |_(_|   ---> ....
     *                   ___ /
     *                  |_!_|
     *
     */
    private Node addOperationItemToOpenBracketNode(FormulaItem item, Node openBracketNode) {
        Node leftNode = openBracketNode.getLeftNode();
        if (leftNode != null) {
            Node nextNode = addItem(item, leftNode);
            return nextNode == rootNode ? openBracketNode : nextNode;
        } else {
            Node bracketLeftNode = new Node(item, openBracketNode);
            openBracketNode.setLeftNode(bracketLeftNode);
            return openBracketNode;
        }
    }


    /*
     * Add new operation item to existing close bracket node.
     * Because subtree inside close bracket node has greater priority then any operation
     * new operation node should be placed over existing close bracket node, according to tree build rules.
     * Example : ... ( x + 2 ) * ...
     *                   ___                      ___
     *  * --->          |_P_|       --->         |_P_|    ---> ....
     *             ___ /                     ___/
     *            |(_)|                     |_*_|
     *        ___/                      ___/     \
     *       |_+_|                     |(_)|  'next node'
     *   ___/     \___             ___/
     *  |_x_|     |_2_|           |_+_|
     *                        ___/     \___
     *                       |_x_|     |_2_|
     *
     *  where P - means 'parent node'.
     *        'next node' - node where next item should be placed.
     *
     */
    private Node addOperationItemToCloseBracketNode(FormulaItem item, Node closeBracketNode){
        Node parentNode = closeBracketNode.getParentNode();
        Node newOperationNode = new Node(item, parentNode);

        newOperationNode.setLeftNode(closeBracketNode);
        closeBracketNode.setParentNode(newOperationNode);

        if(parentNode!=null && parentNode.getFormulaItem().isBinaryOperation()){
            parentNode.setRightNode(newOperationNode);
        } else if(parentNode!=null){
            parentNode.setLeftNode(newOperationNode);
        } else  {
            rootNode = newOperationNode;
        }

        return newOperationNode;
    }

    private Node addBracketItem(FormulaItem item, Node startNode) {
        switch (item.getType()){
            case OPEN_BRACKET:
                return addOpenBracketItem(item, startNode);

            case CLOSE_BRACKET:
                return addCloseBracketItemToBracketNode(item, startNode);

            default:
                return rootNode;
        }
    }

    private Node addOpenBracketItem(FormulaItem item, Node startNode) {
        switch (startNode.getFormulaItem().getType()){
            case OPERATION:
                return addOpenBracketItemToOperationNode(item, startNode);

            case OPEN_BRACKET:
            case CLOSE_BRACKET:
                return addOpenBracketItemToBracketNode(item, startNode);

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
    private Node addOpenBracketItemToOperationNode(FormulaItem item, Node operationNode) {
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
     * Add new open bracket item to existing open bracket node.
     * Example : ...( ( ...
     *          ___            ___
     * (--->   |_(_|   --->   |_(_|
     *                   ___ /
     *                  |_(_|
     */
    private Node addOpenBracketItemToBracketNode(FormulaItem item, Node openBracketNode) {
        BracketsNode newOpenBracketNode = new BracketsNode(item, openBracketNode);
        openBracketNode.setLeftNode(newOpenBracketNode);
        return newOpenBracketNode;
    }

    /*
     * Mark given open bracket node as closed.
     */
    private Node addCloseBracketItemToBracketNode(FormulaItem item, Node openBracketNode) {
        BracketsNode bracketsNode = (BracketsNode) openBracketNode;
        bracketsNode.setCloseBracket(item);
        Node parentBracketNode = bracketsNode.getParentBracket();
        return parentBracketNode != null ? parentBracketNode : rootNode;
    }


    /*
     * Return nearest parent bracket for given node.
     * Example : ...( x * 2 + y! - 5 ......
     *                       ___
     *                      |_(_| <--- nearest parent bracket
     *                  ___/
     *                 |_-_|
     *             ___/     \___
     *            |_+_|     |_5_|
     *        ___/     \___
     *       |_*_|     |_!_|
     *   ___/     \___      \___
     *  |_x_|     |_2_|     |_y_| <--- start node
     *
     */
    private BracketsNode findNearestParentBracketNode(Node node){
        Node parentNode = node.getParentNode();
        while (parentNode != null){
            if(parentNode.getFormulaItem().isBracket()){
                return (BracketsNode) parentNode;
            }
            parentNode = parentNode.getParentNode();
        }
        return null;
    }
    /*
     * Return nearest children node for given node with minimum difference with targetPriority.
     * In case if priority difference for two children nodes is same - right node returned.
     * Example : ... - y + x * 3 ^ 2 ^ ......  Need to find node with priority  for " ^ " operation :
     *
     *              ___
     *             |_+_|                 <--- start node
     *         ___/     \ ___
     *        |_-_|      |_*_|
     *    ___/       ___ /    \___
     *   |_y_|      |_x_|     |_^_|      <--- result node
     *                   ___ /     \___
     *                  |_3_|      |_2_|
     */
    private Node findNearestNodeByPriority(Node startNode, int targetPriority){
        if(startNode == null){
            return startNode;
        }

        int startNodePriority = startNode.getFormulaItem().getPriority();

        if(startNodePriority == targetPriority){
            return startNode;
        }

        if(startNodePriority > targetPriority){
            return null;
        }

        Node rightChildren = startNode.getRightNode();
        Node rightSubtreeResultNode = findNearestNodeByPriority(rightChildren, targetPriority);

        if(rightSubtreeResultNode != null){
            return rightSubtreeResultNode;
        }

        return startNode;
    }


    /*
     * Brackets using just for operation priority increasing inside of it , after tree building it could be removed,
     * because skip during calculation process.
     *
     * Example : ...(x - 2 ) * y ....
     *                 ___                             ___
     *                |_*_|                           |_*_|
     *             __/     \___                   ___/     \___
     *            |()|     |_y_|     --->        |_-_|     |_y_|
     *        ___/                           ___/     \___
     *       |_-_|                          |_x_|     |_2_|
     *   ___/     \___
     *  |_x_|     |_2_|
     *
     *
     */
    private void removeBrackets(Node startNode) {
        if (startNode == null || startNode.isLeaf()) {
            return;
        }

        if (startNode.getFormulaItem().isBracket()) {
            Node parentNode = startNode.getParentNode();

            if (parentNode != null) {

                if (parentNode.getLeftNode().equals(startNode)) {
                    parentNode.setLeftNode(startNode.getLeftNode());
                } else {
                    parentNode.setRightNode(startNode.getLeftNode());
                }

                startNode = parentNode;
            }
        }

        removeBrackets(startNode.getLeftNode());
        removeBrackets(startNode.getRightNode());
    }

    private double calculate(Node node) {
        switch (node.getFormulaItem().getType()) {

            case OPERATION:
                return calculateOperation(node);

            case DIGIT:
                return getLiteralValue(node);

            case VARIABLE:
                return getVariableValue(node);

            default:
                throw new IllegalStateException("Unknown item for calculation : " + node.getFormulaItem());
        }
    }

    private double calculateOperation(Node node) {
        if (node.getFormulaItem().isBinaryOperation()) {

            BinaryOperation binaryOperation = (BinaryOperation) node.getFormulaItem().getOperation();
            double leftArgument = calculate(node.getLeftNode());
            double rightArgument = calculate(node.getRightNode());
            return binaryOperation.operate(leftArgument, rightArgument);

        } else {

            UnaryOperation unaryOperation = (UnaryOperation) node.getFormulaItem().getOperation();
            double argument = calculate(node.getLeftNode());
            return unaryOperation.operate(argument);

        }
    }

    private double checkNegativeZero(double value){
        return value == -0.0 ? 0.0 : value;
    }

    private double getLiteralValue(Node node) {
        return node.getFormulaItem().getDigitLiteralValue();
    }

    private double getVariableValue(Node node) {
        char variableName = node.getFormulaItem().getVariableName();
        Double value = variablesValues.get(variableName);
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
                    formulaItem.getType() == DIGIT;
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
        FormulaItem getFormulaItem(){
            return !hasClosedBracket() ? super.getFormulaItem() : closeBracket ;
        }

        void setRightNode(Node node) {
            throw new UnsupportedOperationException(BracketsNode.class.getSimpleName() + " contains just one node");
        }

        @Override
        Node getRightNode() {
            throw new UnsupportedOperationException(BracketsNode.class.getSimpleName() + " contains just one node");
        }

        @Override
        public String toString() {
            return closeBracket != null ? "()" : "(";
        }
    }
}
