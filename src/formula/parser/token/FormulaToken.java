package formula.parser.token;

import formula.parser.FormulaItem;

/**
 * Structure element of formula in string presentation.
 */
public class FormulaToken {

    private FormulaItem item;
    private int tokenSize;
    private int tokenPosition;

    public FormulaToken(FormulaItem item, int tokenSize, int tokenPosition){
        this.item = item;
        this.tokenSize = tokenSize;
        this.tokenPosition = tokenPosition;
    }

    /**
     * Return element associated to this token.
     * @return element associated to this token.
     */
    public FormulaItem getItem(){
        return item;
    }

    /**
     * Return size of token in string presentation.
     * @return size of token.
     */
    public int getTokenSize(){
        return tokenSize;
    }

    /**
     * Return position of token in string presentation.
     * @return position of token.
     */
    public int getTokenPosition(){
        return tokenPosition;
    }

    @Override
    public String toString(){
        return String.format("Formula formula.parser.tests.token : Item  = %s, Size = %d, Position = %d",
                item, tokenPosition, tokenPosition);
    }
}
