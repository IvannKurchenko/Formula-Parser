package formula.parser.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConstantResolver {

    private static final Map<String, Double> CONSTANTS_SIGN_VALUE_MAP = createdConstantsSignValueMap(Constants.values());

    private static final Map<String, Double> createdConstantsSignValueMap(Constant... constants) {
        Map<String, Double> signValueMap = new HashMap<String, Double>();
        for (Constant constant : constants) {
            for (String sign : constant.getSigns()) {
                signValueMap.put(sign, constant.getValue());
            }
        }
        return Collections.unmodifiableMap(signValueMap);
    }

    public static Map<String, Double> getSupportedConstants() {
        return CONSTANTS_SIGN_VALUE_MAP;
    }

    private Map<String, Double> constantsSignValueMap;
    private int constantMaxLength;

    public ConstantResolver() {
        constantsSignValueMap = new HashMap<String, Double>(CONSTANTS_SIGN_VALUE_MAP);
    }

    public void addConstant(String constantName, Double constantValue) {
        constantsSignValueMap.put(constantName, constantValue);
        constantMaxLength = -1;
    }

    public int getConstantsMaxLength() {
        if(constantMaxLength < 0 ){
            findConstantMaxLength();
        }
        return constantMaxLength;
    }

    public Double findConstantBySign(String sign) {
        return constantsSignValueMap.get(sign);
    }

    private void findConstantMaxLength() {
        constantMaxLength = 0;
        for (String sign : constantsSignValueMap.keySet()) {
            constantMaxLength = constantMaxLength < sign.length() ? sign.length() : constantMaxLength;
        }
    }
}
