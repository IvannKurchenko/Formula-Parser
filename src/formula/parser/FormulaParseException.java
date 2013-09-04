package formula.parser;

import java.text.ParseException;

/**
 * Signal, that some error occurred during formula parse process.
 */
public class FormulaParseException extends ParseException {

    /**
     * Constructs a ParseException with the specified detail message and
     * offset.
     * A detail message is a String that describes this particular exception.
     *
     * @param s           the detail message
     * @param errorOffset the position where the error is found while parsing.
     */
    public FormulaParseException(String s, int errorOffset) {
        super(s, errorOffset);
    }
}
