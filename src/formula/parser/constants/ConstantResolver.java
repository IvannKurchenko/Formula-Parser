package formula.parser.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConstantResolver {

    private static final int CONSTANTS_MAX_LENGTH =  findConstantMaxLength(Constants.values());
    private static final Map<String, Double> CONSTANTS_SIGN_VALUE_MAP = createdConstantsSignValueMap(Constants.values());

    private static int findConstantMaxLength(Constant... constants) {
        int maxLength = 0;
        for(Constant constant : constants){
            for(String sign : constant.getSigns()){
                maxLength = maxLength < sign.length() ? sign.length() : maxLength;
            }
        }
        return maxLength;
    }

    private static final Map<String, Double> createdConstantsSignValueMap(Constant... constants){
        Map<String, Double> signValueMap = new HashMap<String, Double>();
        for(Constant constant : constants){
            for(String sign : constant.getSigns()){
                signValueMap.put(sign, constant.getValue());
            }
        }
        return Collections.unmodifiableMap(signValueMap);
    }

    public static int getConstantMaxLength(){
        return CONSTANTS_MAX_LENGTH;
    }

    public static Double findConstantBySign(String sign){
        return CONSTANTS_SIGN_VALUE_MAP.get(sign);
    }
}
