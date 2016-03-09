package cn.jbricks.toolkit.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created with IntelliJ IDEA.
 * User: ziliang
 * Date: 13-6-1
 * Time: 下午4:28
 * To change this template use File | Settings | File Templates.
 */
public class StringEscapeUtil {

    public static void main(String[] args) throws Exception {
        System.out.println(escapeSmsContent("牛逼的短信<woshi ziliang'book\"你是猪\">")) ;
    }


    public static String escapeSmsContent(String str) throws Exception {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter ((int)(str.length() * 1.5));
            escapeHtml(writer, str);
            return writer.toString();
        } catch (IOException ioe) {
            //should be impossible
            throw new Exception(ioe);
        }
    }

    public static String escapeHtml(Writer writer, String string) throws IOException {
        if (writer == null ) {
            throw new IllegalArgumentException ("The Writer must not be null.");
        }
        if (string == null) {
            return "";
        }
        escape(writer, string);
        return writer.toString();
    }

    public static void escape(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            switch (ch) {
                case '&' :
                    out.write("&amp;");
                    break;
                case '<' :
                    out.write("&lt;");
                    break;
                case '>' :
                    out.write("&gt;");
                    break;
                case '\'' :
                    out.write("&apos;");
                    break;
                case '"' :
                    out.write("&quot;");
                    break;
                default :
                    out.write(ch);
                    break;
            }
        }
    }
}
