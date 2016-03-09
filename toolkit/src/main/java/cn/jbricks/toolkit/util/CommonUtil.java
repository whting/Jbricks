package cn.jbricks.toolkit.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Created with IntelliJ IDEA.
 * User: ziliang
 * Date: 13-6-15
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtil {
    public static String objToString(Object obj)
    {
        if(obj == null)
            return "";
        StringBuffer sb = new StringBuffer();
        PropertyDescriptor[] props = null;

        try {
            props = Introspector.getBeanInfo(obj.getClass(), Object.class)
                    .getPropertyDescriptors();
            if (props != null) {
                for (int i = 0; i < props.length; i++) {
                    try {
                        sb.append(props[i].getName());
                        sb.append(":");
                        sb.append(props[i].getReadMethod().invoke(obj));
                        sb.append(",");
                    } catch (Exception e) {
                        continue;
                    }
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "";
        }

        return "";
    }
}
