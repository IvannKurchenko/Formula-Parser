package formula.parser.tests.util;


import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AssetUtil {

    public static void assertHashCodeEquals(Object expected, Object actual) {
        assertEquals(expected.hashCode(), actual.hashCode());
    }

    public static void assertNotEquals(Object expected, Object actual) {
        assertTrue(!expected.equals(actual));
    }

    public static void assertListEquals(List expected, List actual) {
        Iterator expectedListIterator = expected.listIterator();
        Iterator actualListIterator = actual.listIterator();
        while (expectedListIterator.hasNext() && actualListIterator.hasNext()) {
            assertEquals(expectedListIterator.next(), actualListIterator.next());
        }
    }
}
