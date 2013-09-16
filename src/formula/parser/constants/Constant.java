package formula.parser.constants;

/**
 * General interface for mathematical constant representation.
 */
public interface Constant {

    /**
     * Return value of constant.
     * @return value of constant.
     */
    public double getValue();

    /**
     * Return signs related to this constant.
     * @return signs related to this constant.
     */
    public String[] getSigns();
}
