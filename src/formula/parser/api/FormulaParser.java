package formula.parser.api;

import formula.parser.constants.ConstantResolver;
import formula.parser.operation.OperationResolver;
import formula.parser.tree.FormulaTree;
import formula.parser.operation.Operation;
import formula.parser.preprocessor.FormulaPrerprocessor;
import formula.parser.token.FormulaToken;
import formula.parser.token.FormulaTokenizer;
import formula.parser.validation.FormulaValidator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parser for formula in string presentation.
 */
public class FormulaParser {

    /**
     * Return {@link Map} of supported constants by default, where key - sign of constant, value - constant value;.
     * Returned map is unmodifiable. To add required constant use {@link #addConstant(String, double)} method.
     *
     * @return {@link Map} of supported constants, where key - sign of constant, value - constant value;
     */
    public static Map<String, Double> getSupportedConstants() {
        return ConstantResolver.getSupportedConstants();
    }

    /**
     * Return {@link Set} if supported operations by default. Returned set is unmodifiable.
     * To add required operation use {@link #addOperation(formula.parser.operation.Operation)} method.
     *
     * @return {@link Set} if supported operations.
     */
    public static Set<Operation> getSupportedOperations() {
        return null; //TODO : implement!
    }

    private ConstantResolver constantResolver;
    private OperationResolver operationResolver;

    public FormulaParser() {
        constantResolver = new ConstantResolver();
        operationResolver = new OperationResolver();
    }

    /**
     * Add custom constant that will be using during {@link #parse(String)} operation for given formula in string
     * presentation. In case if constant by given constant name exist in supported constant, it will replaced by new.
     *
     * @param constantName  name of constant.
     * @param constantValue value of constant.
     * @return current instance.
     */
    public FormulaParser addConstant(String constantName, double constantValue) {
        constantResolver.addConstant(constantName, constantValue);
        return this;
    }

    /**
     * Add custom {@link Operation} that will be using during {@link #parse(String)} operation for given formula in string
     * presentation. In case if {@link Operation} by given sign exist in supported operations, it will replaced by new.
     *
     * @param operation custom operation
     * @return current instance.
     */
    public FormulaParser addOperation(Operation operation) {
        //TODO : implement!
        return this;
    }

    /**
     * Parse incoming formula in string presentation to {@link Formula} implementation.
     *
     * @param formula formula in string presentation.
     * @return parsed {@link Formula} implementation.
     * @throws FormulaParseException in case if parse process failed, formula string empty or 'null'.
     */
    public Formula parse(String formula) throws FormulaParseException {
        checkString(formula);
        FormulaTokenizer formulaTokenizer = new FormulaTokenizer(formula, constantResolver, operationResolver);
        List<FormulaToken> tokenList = formulaTokenizer.getTokenList();
        FormulaPrerprocessor.prepossess(tokenList);
        FormulaValidator.validate(tokenList);
        return new FormulaTree(tokenList);
    }

    private void checkString(String formula) throws FormulaParseException {
        if (formula == null) throw new FormulaParseException("Formula string is null", 0);
        if (formula.length() == 0) throw new FormulaParseException("Formula string is empty", 0);
    }
}
