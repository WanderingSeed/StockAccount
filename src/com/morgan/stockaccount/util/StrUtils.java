package com.morgan.stockaccount.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 字符串与其他类型数据转换相关工具类
 * 
 * @author Morgan.Ji
 * @version 1.0
 * @date 2015年8月30日
 */
public class StrUtils {

    /**
     * email正则表达式
     */
    private final static Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /**
     * 把double类型的数字转换成精度为3的字符串,四舍五入
     * 
     * @param value
     * @return
     */
    public static String toString(double value) {
        return toString(value, 3);
    }

    /**
     * 把double类型的数字转换成指定精度的字符串,四舍五入
     * 
     * @param value
     * @param precision
     * @return
     */
    public static String toString(double value, int precision) {
        BigDecimal bigDecimal = new BigDecimal(value);
        String result = bigDecimal.toPlainString();// 防止转成科学计数法
        int index = result.indexOf(".");
        if ((result.length() - index) > (precision + 1)) {
            result = result.substring(0,
                    (result.length() > index + precision + 1) ? index + precision + 1 : result.length());
        } else {
            if (index < 0) {
                result += ".";
                index = result.length() - 1;
            }
            int oldLength = result.length();
            for (int i = 0; i < (precision - oldLength + index + 1); i++) {
                result += "0";
            }
        }
        return result;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     * 
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 判断中字符串内是否包含文汉字和符号
     * 
     * @param strName
     * @return
     */
    public static boolean containChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个字符是否为中文
     * 
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}
