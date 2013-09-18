package formula.parser.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for adding and searching of constants.
 */
public class ConstantResolver {

    private static final Map<String, Double> SUPPORTED_CONSTANTS = createdConstantsSignValueMap(Constants.values());

    private static Map<String, Double> createdConstantsSignValueMap(Constant... constants) {
        Map<String, Double> signValueMap = new HashMap<String, Double>();
        for (Constant constant : constants) {
            for (String sign : constant.getSigns()) {
                signValueMap.put(sign, constant.getValue());
            }
        }
        return Collections.unmodifiableMap(signValueMap);
    }

    /**
     * Return supported constants by default.
     *
     * @return map of supported constants by default, where key -sign of constant, value - constant value.
     */
    public static Map<String, Double> getSupportedConstants() {
        return SUPPORTED_CONSTANTS;
    }

    private Map<String, Double> constantsSignValueMap;
    private int constantMaxLength;

    public ConstantResolver() {
        constantsSignValueMap = new HashMap<String, Double>(SUPPORTED_CONSTANTS);
    }

    /**
     * Add custom constant to known constants.
     *
     * @param constantSign  constant sign, same as in formula string should be.
     * @param constantValue constant value.
     */
    public void addConstant(String constantSign, Double constantValue) {
        constantsSignValueMap.put(constantSign, constantValue);
        constantMaxLength = -1;
    }

    /**
     * Return maximum length of known constants.
     *
     * @return maximum length of known constants.
     */
    public int getConstantsMaxLength() {
        if (constantMaxLength < 0) {
            findConstantMaxLength();
        }
        return constantMaxLength;
    }

    /**
     * Search constant by given sign.
     *
     * @param sign sign of constant to search.
     * @return value of found constant.
     */
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
