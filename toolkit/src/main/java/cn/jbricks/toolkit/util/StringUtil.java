package cn.jbricks.toolkit.util;

public class StringUtil {

	public static Long[] parseIds(String ids) {
		if (ids == null)
			return null;
		String[] idsStr = ids.split(",");
		if (idsStr == null)
			return null;
		Long[] result = new Long[idsStr.length];
		for (int i = 0; i < idsStr.length; i++) {
			try {
				result[i] = Long.parseLong(idsStr[i]);
			} catch (Exception e) {
				continue;
			}
		}
		return result;
	}

    public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
