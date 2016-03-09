package cn.jbricks.toolkit.util;

/**
 * User: kuiyuexiang
 * Date: 2013-05-04
 * Time: 下午9:52
 */
public class NumberUtil {

    public static boolean isNullOrZero(Long l) {
        if (l == null || l.longValue() == 0l) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNullOrZero(Integer integer) {
        if (integer == null || integer.intValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNullOrZero(Double d) {
        if (d == null || d.doubleValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEqual(Long v1, Long v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        if (v1.longValue() == v2.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEqual(Integer v1, Integer v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        if (v1.intValue() == v2.intValue()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEqual(Double v1, Double v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        if (v1.doubleValue() == v2.doubleValue()) {
            return true;
        } else {
            return false;
        }
    }

}
