package com.ft.otp.util.tool;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * 创建一个公用的方法来产生一个TXT文件
 * 
 * @author FG
 * @Date 2011-12-12下午05:35:46
 */
public class FileUtils {
    private static Logger logger = Logger.getLogger(FileUtils.class);

    private String filepath;
    private String filename;
    private OutputStream output;
    private String encoding = "UTF-8";

    public FileUtils() {
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileUtils(String path, String filename) {
        this.filename = filename;
        this.filepath = path;
        createFile();
    }

    public FileUtils(String path, String filename, String encoding) {
        this.filename = filename;
        this.filepath = path;
        this.encoding = encoding;
        createFile();
    }

    /**
     * 写入数据到输出流
     * 
     * @Date 2011-12-12下午05:51:32
     * @Author FG
     * @param data
     *            需要写入的数据
     */
    public void append(String data) {
        try {

            IOUtils.write(data, output, encoding);
        } catch (IOException e) {
        }
    }

    /**
     * 创建文件
     * 
     * @Date 2011-12-13下午02:51:01
     * @Author FG
     */
    private void createFile() {
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            output = new FileOutputStream(new File(filepath, filename));
        } catch (IOException e) {
        }
    }

    /**
     * 写入数据到输出流
     * 
     * @Date 2011-12-12下午05:51:32
     * @Author FG
     * @param data
     *            需要写入的数据
     */
    public void append(byte[] data) {
        try {
            IOUtils.write(data, output);
        } catch (IOException e) {
        }
    }

    /**
     * 写入数据到输出流
     * 
     * @Date 2011-12-12下午05:51:32
     * @Author FG
     * @param data
     *            需要写入的数据
     */
    public void append(char[] data) {
        try {
            IOUtils.write(data, output, encoding);
        } catch (IOException e) {
        }
    }

    public void close() {
        try {
            output.flush();
            output.close();
        } catch (IOException e) {
        }

    }

    /**
     * 清空一下目录下的所有文件
     * 
     * @Date 2011-12-14下午02:07:19
     * @Author FG
     */
    public static void clearPath(String path) {
        File file = new File(path);
        clear(file);
        file.delete();

    }

    public static void clear(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                clear(f);
            } else {
                f.delete();
            }
        }
    }

    public static void close(OutputStream out) {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
        }

    }

    public static void main(String[] args) {
        clearPath("D:\\work\\delete");
    }

    public static String getFileString(File inputFile) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                sb.append(new String(buff, 0, bytesRead));
            }
            if (bis != null) {
                bis.close();
            }
        } catch (Exception e) {
            logger.error("Read file error!", e);
        }

        return sb.toString();
    }
    
    /**   
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true   
     *    
     * @param fileName   
     * @param content   
     */    
    public static void writeTxtContent(String file, String conent) {   
        BufferedWriter out = null;   
        try {   
            out = new BufferedWriter(new  OutputStreamWriter(new FileOutputStream(file, true)));   
            out.write(conent);   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally{   
            try{   
                out.close();   
            } catch(IOException e) {   
                e.printStackTrace();   
            }   
        }   
    }   
    
}
