package com.lzz.tools.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 文件流帮助类
 *
 * @author lizhizhao
 * @since 2020-10-14 17:10
 */
public class FileHelper {

    /**
     * 将网络文件转换成Byte数组
     * 例：pathStr:https://timgsa.baidu.com/1.jpg
     * @param pathStr
     * @return
     */
    public byte[] urlToBytes(String pathStr) {
        ByteArrayOutputStream bos = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(pathStr);
            inputStream = url.openStream();
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, n);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
