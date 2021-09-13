package com.lzz.tools.helper;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
    public static void copy(String srcPath, String destPath) throws IOException {
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
    public static void fileChannelCopy(String srcPath, String destPath) throws IOException {
        FileChannel in = FileChannel.open(Paths.get(srcPath), StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get(destPath), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        in.transferTo(0, in.size(), out);
    }

    /**
     * 读取文件内容
     * @param fileName 文件名（含路径）
     * @return
     */
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    /**
     * 读取文件内容
     * @param inputStream 输入流
     * @return
     */
    public static String readFileContentByStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(inputStreamReader);
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

}
