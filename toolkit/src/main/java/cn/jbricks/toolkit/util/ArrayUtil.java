package cn.jbricks.toolkit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ziliang
 * Date: 13-6-1
 * Time: 下午3:01
 * To change this template use File | Settings | File Templates.
 */
public class ArrayUtil {


    public static Map toMap(Object[] array, Map map) {
        if (array == null) {
            return map;
        }

        if (map == null) {
            map = new HashMap((int) (array.length * 1.5));
        }

        for (int i = 0; i < array.length; i++) {
            Object object = array[i];

            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) object;

                map.put(entry.getKey(), entry.getValue());
            } else if (object instanceof Object[]) {
                Object[] entry = (Object[]) object;

                if (entry.length < 2) {
                    throw new IllegalArgumentException("Array element " + i + ", '" + object
                            + "', has a length less than 2");
                }

                map.put(entry[0], entry[1]);
            } else {
                throw new IllegalArgumentException("Array element " + i + ", '" + object
                        + "', is neither of type Map.Entry nor an Array");
            }
        }

        return map;
    }
}
