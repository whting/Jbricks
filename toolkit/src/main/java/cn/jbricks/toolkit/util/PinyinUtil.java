package cn.jbricks.toolkit.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;

public class PinyinUtil {


    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        if (StringUtils.isEmpty(chines)) {
            return "";
        }
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (temp != null && temp.length > 0 && temp[0] != null && temp[0].length() > 0) {
                        pinyinName.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
        }
        return pinyinName.toString();
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        if (StringUtils.isEmpty(chines)) {
            return "";
        }
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (temp != null && temp.length > 0) {
                        pinyinName.append(temp[0]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
        }
        return pinyinName.toString();
    }

    public static void main(String[] args) {
        PerformUtil performUtil = new PerformUtil("pinyin");
        converterToSpell("欢迎来到最棒的Java中文社区");
        performUtil.testRecord("1");
        System.out.println(converterToSpell("汉字转换位汉语拼音,英文字符不变"));
        performUtil.testRecord("2");
        System.out.println(converterToSpell("以上一个是完全转换成汉语"));
        performUtil.testRecord("3");
        for (int i = 0; i < 10000; i++) {
            converterToSpell("欢迎来到最棒的Java中文社区");
            converterToSpell("欢迎来到最棒的Java中文社区");
        }
        performUtil.testRecord("fin");
    }
}
