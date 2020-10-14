package com.lzz.tools.helper;

import org.springframework.util.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.List;

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

}
