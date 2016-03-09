package cn.jbricks.toolkit.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: WSW
 * Date: 13-5-24
 * Time: 下午7:41
 * To change this template use File | Settings | File Templates.
 */
public class RandomUtil {

    public static int getInt(int min, int max) {
        return min + new Double(Math.random() * (max - min)).intValue();
    }

    public static String getRandomString(int length) {
        SecureRandom ran = new SecureRandom();
        String rt = "";
        String all = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        int rint = 0;
        for (int i = 0; i < length; i++) {
            rint = ran.nextInt();
            if (rint < 0) {
                rint = -rint;
            }
            rint = rint % all.length();
            rt += all.substring(rint, rint + 1);
        }
        return rt;
    }

    public static int getRandomInt(int max) {
        return new Random().nextInt(max);
    }

//    public static final void main(String[] arg) {
//        System.out.println(RandomUtil.getRandomInt(99999999));
//        System.out.println(RandomUtil.getRandomInt(99999999));
//        System.out.println(RandomUtil.getRandomInt(99999999));
//        System.out.println(RandomUtil.getRandomInt(99999999));
//    }
}
