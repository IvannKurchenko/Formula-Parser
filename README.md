Formula-Parser
==============

This is a library for parsing and calculation of aritmetic expressions from string.
Main alghoritm based on representation of formula as a tree structure, where each node of formula tree is a structural 
element of formula (operation, digit, variable or bracket).

Example
=======
Exmpample of formula parsing and calculation:

<pre><code> try {
  String formulaString = "x + sin(2) + y / 2.5 - z * (10 * 5 - 2)";
  Formula formula = FormulaParser.parse(formulaString);
  System.out.println("Formula variables : " + formula.variables() );

  Map<Character, Double> arguments = new HashMap<Character, Double>();
  arguments.put('x', 1.0);
  arguments.put('y', 1.0);
  arguments.put('z', 1.0);
  double formulaValue = formula.calculate(arguments);
  System.out.println("Calculated value : " + formulaValue);

} catch(FormulaParseException e){
  //Something wrong with formula
}
</code></pre>

