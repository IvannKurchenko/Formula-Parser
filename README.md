Formula-Parser
==============

Library for parsing and calculation of aritmetic expressions from string.
Main alghoritm based on building semantic formula tree from it string representation. Alghoritm is next.
- **General**.
  Each node of tree contains as a value item that represent structural formula element (digit, variable, operation or bracket)  
  and from 0 to 2 children node's that depends on type of item, it's next :
 
  * Operation - could contain 1 or 2 children node's that depends on
    operation type of represents. Unary operation contain one children node
    (it's left node by default) and binary - two children nodes.
    Each children node ofoperation node represents it's argument.
 
 
  * Variable - not contains any children nodes and is a leaf of tree.
 
  * Digit - not contains any children nodes and is a leaf of tree.
 
 
  * Bracket - contains one children node. Each bracket could be  represented as a root node of formula subtree.
 
- **Build of tree**
  Before building tree it's split on tokens, preprocess and validate for different rules. 
  Formula tree builds on next rules:
  * Priority of of parent node lower or equal than priority of children node. So, priority of nodes should increase
    from root node to leaf in tree. Priority of node defined as priority of formula item that it contains.
 
  * Brackets have no any priority and inside of brackets formula builds recursively by the same rules, as general tree.
 
  * Variables and digits - nodes that represents arguments and should be leaves of tree, that's why they have max priority.
 
  * If node could contain just one children node, by default, children node is left.
 
- **Calculation**. 
  As a result of building tree rules, process of formula calculation is next : to calculate value of node need to
  recursively calculate value of right subtree, left subtree and than value value of node. Leaf node
  is node with arguments and contains simple value. Nodes with children node represents node with
  operation and value calculate as a operation over children nodes - arguments
  or brackets node - that simply skip during calculation.
 
- **Example** . As example, for formula "( x - 2 ) * y - z / 4" result formula tree will be next :
![Alt text](http://s9.postimg.org/wuadkt7fj/Drawing1.png)

Multipuly operation auto insertion
=====
Library by default add multipuly operation between arguments, like : 
<code>10sin(4(x-2))</code> ---> <code> 10 * sin(4* (x-2) )</code> 
 
Supported operation and constants
======
To get supported constants and operations appropriate methods provided in FormulaParser :
* <code>FormulaParser.getSupportedOperations()()</code>;
* <code>FormulaParser.getSupportedConstants()</code>;

Custom operation and constants
=======
Library provide posibility to use customs constant or operations. For this appropriate methods provided 
- **Custom constants**. To insert custom constant need to create formula parser and add constant name and value.
  <pre><code> FormulaParser formulaParser = new FormulaParser(); 
   formulaParser.addConstant("G", 9.8); </code></pre>

- **Custom operations**. To use custom operations recomended to extend <code>AbstractBinaryOperation</code>
  or <code>AbstractUnaryOperation</code>, where implement method with arguments operation.
  <pre><code> private static final Operation MOD = new AbstractBinaryOperation("%") {
        @Override
        public double operate(double firstArgument, double secondArgument) {
            return firstArgument % secondArgument;
        }
    };
    
    ...
        
    FormulaParser formulaParser = new FormulaParser();
    formulaParser.addOperation(MOD);

  </code></pre>

Example
=======
Exmpample of formula parsing and calculation:

<pre><code> FormulaParser formulaParser = new FormulaParser();
        try {
            
            Formula formula = formulaParser.parse("x + sin(2) + y / 2.5 - z * (10 * 5 - 2)");
            double result = formula.
                            setVariableValue('x', 1).
                            setVariableValue('y', 1).
                            setVariableValue('z', 1).
                            calculate();
            
        } catch (FormulaParseException e) {
            e.printStackTrace();
        }
</code></pre>
