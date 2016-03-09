package cn.jbricks.toolkit.util;

/**
 * Created by kuiyuexiang on 15/12/18.
 */
public class BitsUtil {

    public static boolean bitAnd(final int bit, final int constant) {
        if ((bit & constant) == bit)
            return true;
        return false;
    }

    public static boolean bitOr(final int bit, final int constant) {
        if ((bit | constant) == constant)
            return true;
        return false;
    }

    public static int setBit(final int bit, final int constant) {
        return bit | constant;
    }

    public static int clearBit(final int bit, final int constant) {
        return ~bit & constant;
    }



}
