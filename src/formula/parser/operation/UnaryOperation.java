package formula.parser.operation;

/**
 * General interface for mathematical operation over one argument.
 *
 * @see Type#UNARY
 */
public interface UnaryOperation extends Operation {

    /**
     * Enum represents type of notation supported for {@link UnaryOperation} arguments defining.
     * <br> Example :
     * <br> sin(x) - {@link Notation#PREFIX}, argument goes after argument.
     * <br> x! - {@link Notation#POSTFIX}, argument goes before operation.
     */
     public enum Notation{

         /**
          * Represent postfix notation.
          */
         POSTFIX,

         /**
          * Represent prefix notation.
          */
         PREFIX
     }

    /**
     * Calculates result of argument for specific implementation;
     *
     * @param argument operand value
     * @return result of operation
     */
    public double operate(double argument);


    /**
     * Return {@link Notation} type that describe argument defining for this operation.
     *
     * @return {@link Notation} for operation.
     */
    public Notation getNotation();
}
