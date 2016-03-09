package cn.jbricks.toolkit.top.util;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kuiyuexiang on 15/12/14.
 */
public class TopMap extends HashMap<String, String> {

    private static final Set<Class> simpleClasses = new HashSet();


    static {
        simpleClasses.add(Integer.TYPE);
        simpleClasses.add(Integer.class);
        simpleClasses.add(Long.TYPE);
        simpleClasses.add(Long.class);
        simpleClasses.add(Byte.class);
        simpleClasses.add(Byte.TYPE);
        simpleClasses.add(Double.TYPE);
        simpleClasses.add(Double.class);
        simpleClasses.add(Boolean.TYPE);
        simpleClasses.add(Boolean.class);
        simpleClasses.add(Character.TYPE);
        simpleClasses.add(Character.class);
        simpleClasses.add(Float.class);
        simpleClasses.add(Float.TYPE);
        simpleClasses.add(Void.TYPE);
        simpleClasses.add(String.class);
        simpleClasses.add(Short.class);
        simpleClasses.add(Short.TYPE);
    }


    public Object put(String s, Object o) {
        if (o != null && !isSimpleType(o.getClass())) {
            super.put(s, JSON.toJSONString(o));
        } else {
            if (o == null) {
                super.put(s, "");
            } else {
                super.put(s, String.valueOf(o));
            }
        }
        return o;
    }

    private static Map<String, String> objectToMap(final Object obj)
            throws IllegalArgumentException, IllegalAccessException {
        Class<? extends Object> c1 = obj.getClass();
        Map<String, String> map = new HashMap<String, String>();

        Field[] fields = c1.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            fields[i].setAccessible(true);
            Object value = fields[i].get(obj);
            if (value != null && !isStatic(fields[i].getModifiers())) {
                map.put(name, value.toString());
            }
        }
        return map;
    }

    private static boolean isStatic(int modifiers) {
        return (modifiers & 0x8) > 0;
    }

    public static boolean isSimpleType(Class clazz) {
        return simpleClasses.contains(clazz);
    }

}

