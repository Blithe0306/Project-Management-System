/**
*Administrator
*/
package com.ft.otp.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


/**
 * 压缩ZIP
 *
 * @Date in May 12, 2010,3:17:44 PM
 *
 * @author TBM
 */
public class ZipCompress {

    public static void zip(String inputFilePath, OutputStream outputStream) throws Exception {
        ZipOutputStream out = new ZipOutputStream(outputStream);
        out.setEncoding("GBK");
        try {
            zip(new File(inputFilePath), out, "");
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /**
     * 压缩文件为zip格式
     * @Date in May 12, 2010,3:18:40 PM
     * @param inputFilename  需要压缩的文件路径
     * @param zipFilename    压缩后的文件路径
     * @throws IOException
     */
    public static void zip(String inputFilename, String zipFilename) throws Exception {
        zip(new File(inputFilename), zipFilename);
    }

    private static synchronized void zip(File inputFile, String zipFilename) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
        out.setEncoding("utf-8");
        try {
            zip(inputFile, out, "");
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    private static synchronized void zip(File inputFile, ZipOutputStream out, String base) throws Exception {
        if (inputFile.isDirectory()) {
            File[] inputFiles = inputFile.listFiles();
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                String fileNam = base + inputFiles[i].getName();
                if (fileNam.endsWith(".zip") || fileNam.endsWith(".ZIP")) {
                    continue;
                }
                zip(inputFiles[i], out, fileNam);
            }

        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }

            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[1024];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                    out.flush();
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (null != in) {
                    in.close();
                }
            }
        }
    }
}
