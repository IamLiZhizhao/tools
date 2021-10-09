package com.lzz.tools.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件帮助类
 *
 * @author lizhizhao
 * @since 2020-10-14 17:10
 */
@Slf4j
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

    /**
     * 读取文件并拆分为各个小文件
     */
    public static void splitTxt() throws IOException {
        int count =1;
        FileInputStream inputFile = new FileInputStream("F:/20110930/20110930.txt");
        String line = ""; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));
        ArrayList<String> list=new ArrayList<String>();
        line = reader.readLine(); // 读取第一行
        while (line != null) {
            list.add(line); // 将读到的内容添加到 集合当中。

            if(count%5000==0){  // 每5000行创建一个txt文件
                FileOutputStream fos = new FileOutputStream("F:/20111001/"+count+".txt");
                PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("F:/20111001/"+count+".txt")));
                for(Iterator<String> it = list.iterator(); it.hasNext();) {
                    pw.println(it.next());
                }
                pw.flush();
                pw.close();
                list.clear();
            }
            line = reader.readLine();
            // 读取下一行
            count++;
        }
        // 将最后多的几行写完
        PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("F:/20111001/end.txt")));
        for(Iterator<String> it = list.iterator(); it.hasNext();){
            pw.println(it.next());
        }
        pw.flush();
        pw.close();
        list.clear();
        reader.close();
    }

    /**
     * 读取txt文件并拆分为各个小文件
     * @param inputFile
     * @return 各个小文件的InputStream列表
     * @throws IOException
     */
    private List<InputStream> splitTxt(InputStream inputFile) throws IOException {
        List<InputStream> fileInputList = new ArrayList<>();
        String line = ""; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));
        List<String> tmsOrderNoList=new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            tmsOrderNoList.add(line.trim());
        }
        reader.close();
        if (CollectionUtils.isEmpty(tmsOrderNoList)) {
            log.error("[splitTxt]导入文件中不存在内容");
            return fileInputList;
        }
        // 去重
        tmsOrderNoList = tmsOrderNoList.stream().distinct().collect(Collectors.toList());

        ArrayList<String> list=new ArrayList<String>();
        long count = 1;
        for (String tmsOrderNo : tmsOrderNoList) {
            // 将读到的内容添加到 集合当中。
            list.add(tmsOrderNo);
            // 每5000行创建一个txt文件
            if(count%5000 == 0){
                fileInputList.add(new ByteArrayInputStream(String.join("\r\n", list).getBytes(StandardCharsets.UTF_8)));
                list.clear();
            }
            // 读取下一行
            count++;
        }
        // 将最后多的几行写完
        fileInputList.add(new ByteArrayInputStream(String.join("\r\n", list).getBytes(StandardCharsets.UTF_8)));
        list.clear();
        return fileInputList;
    }

}
