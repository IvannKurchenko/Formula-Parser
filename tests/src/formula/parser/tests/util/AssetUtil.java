package formula.parser.tests.util;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AssetUtil {

    public static void assertHashCodeEquals(Object firstObject, Object secondObject) {
        assertEquals(firstObject.hashCode(), secondObject.hashCode());
    }

    public static void assertNotEquals(Object firstObject, Object secondObject) {
        assertTrue(!firstObject.equals(secondObject));
    }
}
