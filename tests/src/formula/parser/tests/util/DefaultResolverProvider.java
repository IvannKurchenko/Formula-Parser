package formula.parser.tests.util;

import formula.parser.constants.ConstantResolver;
import formula.parser.operation.OperationResolver;
import formula.parser.token.FormulaTokenChecker.ResolversProvider;

public class DefaultResolverProvider implements ResolversProvider {

    public static final DefaultResolverProvider PROVIDER = new DefaultResolverProvider();
    public static final ConstantResolver CONSTANT_RESOLVER = new ConstantResolver();
    public static final OperationResolver OPERATION_RESOLVER = new OperationResolver();

    @Override
    public ConstantResolver getConstantResolver() {
        return CONSTANT_RESOLVER;
    }

    @Override
    public OperationResolver getOperationResolver() {
        return OPERATION_RESOLVER;
    }
}
