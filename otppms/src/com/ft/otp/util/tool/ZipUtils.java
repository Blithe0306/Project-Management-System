package com.ft.otp.util.tool;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 * 目前压缩方式是，取出文件夹中所有的内容进行压缩
 * 
 * @author FG
 * @Date 2011-12-13下午03:21:43
 */
public class ZipUtils {

    private Logger logger = Logger.getLogger(ZipUtils.class);
    private ZipFile zipFile;
    private ZipOutputStream zipOut; // 压缩Zip
    private int bufSize; // size of bytes
    private byte[] buf;
    private int readedBytes;

    public ZipUtils() {
        this(512);
    }

    public ZipUtils(int bufSize) {
        this.bufSize = bufSize;
        this.buf = new byte[this.bufSize];
    }

    /**
     * 添加压缩文件，将取出所有文件进行压缩
     * 
     * @Date 2011-12-13下午03:18:40
     * @Author FG
     * @param zipDirectory
     *            需要压缩的文件
     * @param os
     */
    public void doZip(String zipDirectory, OutputStream os) {
        File zipDir = new File(zipDirectory);
        try {
            this.zipOut = new ZipOutputStream(os);
            handleDir(zipDir, this.zipOut);
            this.zipOut.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     *压缩文件夹内的文件
     * 
     * @Date 2011-12-13下午03:19:43
     * @Author FG
     * @param zipDirectory
     */
    public void doZip(String zipDirectory) {
        // zipDirectoryPath:需要压缩的文件夹名
        File zipDir = new File(zipDirectory);
        String zipFileName = zipDir.getName() + ".zip";// 压缩后生成的zip文件名
        try {
            doZip(zipDirectory, new BufferedOutputStream(new FileOutputStream(zipFileName)));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     *由doZip调用,递归完成目录文件读取
     * 
     * @Date 2011-12-13下午03:19:43
     * @Author FG
     * @param zipDirectory
     */
    private void handleDir(File dir, ZipOutputStream zipOut) throws IOException {
        FileInputStream fileIn;
        File[] files;

        files = dir.listFiles();

        if (files.length == 0) {// 如果目录为空,则单独创建之.
            // ZipEntry的isDirectory()方法中,目录以"/"结尾.
            this.zipOut.putNextEntry(new ZipEntry(dir.toString() + "/"));
            this.zipOut.closeEntry();
        } else {// 如果目录不为空,则分别处理目录和文件.
            for (File fileName : files) {

                if (fileName.isDirectory()) {
                    handleDir(fileName, this.zipOut);

                } else {
                    fileIn = new FileInputStream(fileName);
                    this.zipOut.putNextEntry(new ZipEntry(fileName.getName()));

                    while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
                        this.zipOut.write(this.buf, 0, this.readedBytes);
                    }

                    this.zipOut.closeEntry();
                }
            }
        }
    }

    /**
     *解压指定zip文件
     * 
     * @Author zxh
     * @param zipFile 即可以是路径，也可以是File对象
     */
    @SuppressWarnings("unchecked")
    public void unZip(Object unZipfile) {
        FileOutputStream fileOut;
        File file;
        InputStream inputStream;

        try {
            if (unZipfile instanceof String) {//判断解压的zip是File还是路径
                this.zipFile = new ZipFile(unZipfile.toString());
            } else if (unZipfile instanceof File) {
                this.zipFile = new ZipFile((File) unZipfile);
            }

            for (Enumeration entries = this.zipFile.getEntries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                file = new File(entry.getName());

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建之.
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    inputStream = zipFile.getInputStream(entry);

                    fileOut = new FileOutputStream(file);
                    while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
                        fileOut.write(this.buf, 0, this.readedBytes);
                    }
                    fileOut.close();

                    inputStream.close();
                }
            }
            this.zipFile.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 设置缓冲区大小
     * 
     * @Date 2011-12-13下午03:19:43
     * @Author FG
     * @param zipDirectory
     */
    public void setBufSize(int bufSize) {
        this.bufSize = bufSize;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            String name = args[1];
            ZipUtils zip = new ZipUtils();

            if (args[0].equals("-zip"))
                zip.doZip(name);
            else if (args[0].equals("-unzip"))
                zip.unZip(name);
        } else {
            System.out.println("Usage:");
            System.out.println("压缩:java AntZip -zip directoryName");
            System.out.println("解压:java AntZip -unzip fileName.zip");
            throw new Exception("Arguments error!");
        }
    }
}
