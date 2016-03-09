package cn.jbricks.toolkit.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class EncryptUtil {
    /**
     * 生成有效签名
     *
     * @param params
     * @param secret
     * @return
     * @throws Exception
     */
    public static String signature2(Map<String, String> params, String secret,
                                    boolean appendSecret, boolean isHMac, String signName) throws Exception {
        params.remove(signName);
        String[] names = params.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        Arrays.sort(names);
        StringBuilder sb = new StringBuilder();
        // append if not hmac
        if (!isHMac) {
            sb.append(secret);
        }
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            sb.append(name);
            sb.append(params.get(name));
        }
        if (appendSecret && !isHMac) {
            sb.append(secret);
        }
        String sign = null;
        try {
            if(isHMac) {
                //hmac
                sign = byte2hex(encryptHMAC(sb.toString().getBytes("utf-8"), secret.getBytes("utf-8")));
            } else {
                //md5
                sign = DigestUtils.md5Hex(sb.toString().getBytes("utf-8"))
                        .toUpperCase();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return sign;
    }

    /**
     * HMAC加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, byte[] key) throws Exception  {
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);

    }

    /**
     * 生成有效签名
     *
     * @param params
     * @param secret
     * @return
     */
    public static String signature(Map<String, String> params, String secret,
                                   String signName) {
        String orgin = getSignatureOrgin(params, secret, signName);
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byte2hex(md.digest(orgin.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException("sign error !");
        }
        return result;
    }

    /**
     * 生成有效签名
     *
     * @param params
     * @param secret
     * @return
     */
    public static String getSignatureOrgin(Map<String, String> params,
                                           String secret, String signName) {
        String result = null;
        if (params == null)
            return result;
        // remove sign parameter
        params.remove(signName);
        Map<String, CharSequence> treeMap = new TreeMap<String, CharSequence>();
        treeMap.putAll(params);
        Iterator<String> iter = treeMap.keySet().iterator();
        StringBuffer orgin = new StringBuffer(secret);
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append(name).append(params.get(name));
        }
        return orgin.toString();
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
