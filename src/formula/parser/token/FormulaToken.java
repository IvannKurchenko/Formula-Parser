package formula.parser.token;

import formula.parser.tree.FormulaItem;

/**
 * Structure element of formula in string presentation.
 */
public class FormulaToken {

    private FormulaItem item;
    private int tokenSize;
    private int tokenPosition;

    public FormulaToken(FormulaItem item, int tokenSize, int tokenPosition) {
        this.item = item;
        this.tokenSize = tokenSize;
        this.tokenPosition = tokenPosition;
    }

    /**
     * Return {@link FormulaItem} associated to this token.
     *
     * @return {@link FormulaItem} associated to this token.
     */
    public FormulaItem getItem() {
        return item;
    }

    /**
     * Return size of token in string presentation.
     *
     * @return size of token.
     */
    public int getTokenSize() {
        return tokenSize;
    }

    /**
     * Return position of token in string presentation.
     *
     * @return position of token.
     */
    public int getTokenPosition() {
        return tokenPosition;
    }

    @Override
    public String toString() {
        return String.format("Formula formula.parser.tests.token : Item  = %s, Size = %d, Position = %d",
                item, tokenPosition, tokenPosition);
    }

    @Override
    public boolean equals(Object object){
        if(object == null){
            return false;
        }
        if(!(object instanceof FormulaToken)){
            return false;
        }

        FormulaToken another = (FormulaToken) object;
        return  item.equals( another.item ) &&
                tokenSize == another.tokenSize &&
                tokenPosition == another.tokenPosition ;
    }

    @Override
    public int hashCode(){
        int hashCode = 17;
        hashCode = 31 * hashCode + item.hashCode();
        hashCode = 31 * hashCode + tokenSize;
        hashCode = 31 * hashCode + tokenPosition;
        return hashCode;
    }
}

