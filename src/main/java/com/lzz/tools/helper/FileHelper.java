package com.lzz.tools.helper;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件帮助类
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
    public static byte[] urlToBytes(String pathStr) {
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


    /**
     * 文件内容复制
     * 从srcPath到destPath
     * srcPath;"src.txt"    destPath;"dest.txt"
     * @throws IOException
     */
    private static void copy(String srcPath, String destPath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(srcPath);
             FileOutputStream fileOutputStream = new FileOutputStream(destPath)) {
            byte[] buffer = new byte[100];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
    }

    /**
     * 文件复制
     * @throws IOException
     */
    private static void fileChannelCopy(String srcPath, String destPath) throws IOException {
        FileChannel in = FileChannel.open(Paths.get(srcPath), StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get(destPath), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        in.transferTo(0, in.size(), out);
    }
}
