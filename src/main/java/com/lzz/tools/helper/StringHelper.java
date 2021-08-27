package com.lzz.tools.helper;

import org.springframework.util.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String帮助类
 *
 * @author lizhizhao
 * @since 2020-05-27 11:30
 */
public class StringHelper {

    public static boolean isNullOrEmpty(String source) {
        return source == null || source.trim().isEmpty();
    }

    /**
     * 根据前后缀截取中间字符串
     * 存在则返回中间字符串，否则返回""
     * @param value
     * @param prefix
     * @param suffix
     * @return
     */
    public static String substringByFix(String value, String prefix, String suffix) {
        int beginIndex = value.indexOf(prefix);
        int endIndex = value.indexOf(suffix);
        if (beginIndex < 0 || endIndex > value.length() || endIndex - beginIndex < 0) {
            return "";
        }
        return value.substring(beginIndex + prefix.length(), endIndex);
    }

    /**
     * 截取字符串前后缀中字符集合 - 包含前后缀
     * 
     * @param value 待截取字符串 例: ${userName};今天的日期为${date};捡到了${amount}元钱.
     * @param prefix 前缀 例: ${
     * @param suffix 后缀 例: }
     * @return 截取的字符串集合 例: [userName,date,amount]
     */
    public static List<String> splitByFix(String value, String prefix, String suffix) {
        List<String> result = new ArrayList<>();
        int indexStart = value.indexOf(prefix);
        int indexEnd = 0;
        while (indexStart >= 0) {
            indexEnd = value.indexOf(suffix);
            String substr = value.substring(indexStart + prefix.length(), indexEnd);
            result.add(substr);
            value = value.substring(indexEnd + suffix.length());
            indexStart = value.indexOf(prefix);
        }
        return result;
    }

    /**
     * 得到中文首字母,例如"单据"得到DJ返回
     *
     * @param str 中文字符串
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                sb.append(pinyinArray[0].charAt(0));
            } else {
                sb.append(word);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 拼接字符串
     * @param strArray
     * @return
     */
    private static String appendStr(String... strArray) {
        StringBuilder builder = new StringBuilder();
        if (strArray != null && strArray.length > 0) {
            for (String str : strArray) {
                if (!StringUtils.isEmpty(str)) {
                    builder.append(str);
                }
            }
        }
        return builder.toString();
    }

    public static final char UNDERLINE = '_';

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串2
     *
     * @param param
     * @return
     */
    public static String underlineToCamel2(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }

}
