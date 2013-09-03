package formula.parser.validation;

import formula.parser.FormulaParseException;

public class FormulaValidationException extends FormulaParseException{

    /**
     * Constructs a ParseException with the specified detail message and
     * offset.
     * A detail message is a String that describes this particular exception.
     *
     * @param s           the detail message
     * @param errorOffset the position where the error is found while parsing.
     */
    public FormulaValidationException(String s, int errorOffset) {
        super(s, errorOffset);
    }
}
