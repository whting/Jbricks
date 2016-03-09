package cn.jbricks.toolkit.util;

/**
 * Created with IntelliJ IDEA.
 * User: ziliang
 * Date: 13-5-30
 * Time: 下午7:43
 * To change this template use File | Settings | File Templates.
 */
public class MobileUtil {
    private static final String PHONE_VAILD_CHECK_REGEX = "^[0-1]\\d{9,12}$";

    public static boolean isMobileNum(final String address)
    {
        if (null != address && address.matches(PHONE_VAILD_CHECK_REGEX))
        {
            try
            {
                final long phone = Long.parseLong(address);

                if (phone > 0)
                {
                    return true;
                }
            } catch (final Exception e)
            {
                return false;
            }

        }
        return false;
    }

    public static void main(final String[] args)
    {

        // should true
        System.out.println(("13632530792").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("02888071466").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("075588071466").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("15867190580").matches(PHONE_VAILD_CHECK_REGEX));

        // should fasle
        System.out.println(("skyfox@1..com").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("0123").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("a13632530792").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("13632530792b").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("-1").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("01231245412").matches(PHONE_VAILD_CHECK_REGEX));
        System.out.println(("91231245412").matches(PHONE_VAILD_CHECK_REGEX));

    }
}
