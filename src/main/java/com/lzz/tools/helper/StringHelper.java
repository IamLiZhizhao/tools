package com.lzz.tools.helper;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * String类相关的帮助类
 *
 * @author lizhizhao
 * @since 2020-05-17 14:25
 */
public class StringHelper {

    /**
     * 根据前后缀获取中间的字符串
     * 存在则返回中间字符串，否则返回""
     * @param value
     * @param prefix
     * @param suffix
     * @return
     */
    private String substringByFix(String value, String prefix, String suffix) {
        int beginIndex = value.indexOf(prefix);
        int endIndex = value.indexOf(suffix);
        if (beginIndex < 0 || endIndex > value.length() || endIndex - beginIndex < 0) {
            return "";
        }
        return value.substring(beginIndex + prefix.length(), endIndex);
    }


    /**
     * 使用md5的算法进行加密
     */
    public static String md5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5=new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return md5.toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
        }
    }

}
