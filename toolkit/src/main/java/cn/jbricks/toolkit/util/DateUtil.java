package cn.jbricks.toolkit.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kuiyuexiang on 15/12/18.
 */
public class DateUtil {
    public static final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat YMD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat MDHM_DATE_FORMAT = new SimpleDateFormat("MM月dd日 HH:mm");

    public static final SimpleDateFormat HM_DATE_FORMAT = new SimpleDateFormat("HH:mm");

    public static String format(Date date) {
        if (date != null) {
            return DEFAULT_FORMAT.format(date);
        } else {
            return "";
        }
    }

    public static String formatYMD(Date date) {
        if (date != null) {
            return YMD_FORMAT.format(date);
        } else {
            return "";
        }
    }

    public static String formatMDHM(Date date) {
        if (date != null) {
            return MDHM_DATE_FORMAT.format(date);
        } else {
            return "";
        }
    }

    public static String formatHM(Date date) {
        if (date != null) {
            return HM_DATE_FORMAT.format(date);
        } else {
            return "";
        }
    }

    public static String format(Date date, DateFormat dateFormat) {
        if (dateFormat != null && date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    public static boolean isEqualDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (formatYMD(date1).equals(formatYMD(date2))) {
                return true;
            }
        }
        return false;
    }

    public static Date parse(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        Date date = null;
        try {
            if (dateStr.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                date = DEFAULT_FORMAT.parse(dateStr);
            } else if (dateStr.length() == "yyyy-MM-dd".length()) {
                date = YMD_FORMAT.parse(dateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
