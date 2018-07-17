package com.ft.otp.util.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZipUntie {

    private final static Log logger = LogFactory.getLog(ZipUntie.class);

    public static void decompressionZip(File baseDir) throws Exception {
        FileFilter zipFF = new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".zip");
            }
        };

        File[] zipFiles = baseDir.listFiles(zipFF);
        for (int i = 0; i < zipFiles.length; i++) {
            String zipFN = zipFiles[i].getName();
            File zipDir = new File(baseDir, zipFN.substring(0, zipFN.length() - 4));
            if (!zipDir.exists()) {
                try {
                    extractZip(zipFiles[i], zipDir);
                } catch (Exception e) {
                    logger.error("Extracting zip file failed", e);
                    throw e;
                }
            }
        }
    }

    private static void extractZip(File zipFile, File targetDir) throws Exception {
        ZipFile zfile = null;

        try {
            zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                String zeName = ze.getName();
                if (ze.isDirectory()) {
                    createDirs(targetDir, zeName);
                    continue;
                }

                OutputStream os = null;
                InputStream is = null;

                try {
                    os = new BufferedOutputStream(new FileOutputStream(getRealFileName(targetDir, zeName)));
                    is = new BufferedInputStream(zfile.getInputStream(ze));
                    int readLen = 0;
                    while ((readLen = is.read(buf, 0, 1024)) != -1) {
                        os.write(buf, 0, readLen);
                    }
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        } finally {
            if (zfile != null) {
                try {
                    zfile.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static File createDirs(File baseDir, String dirName) {
        String[] dirs = dirName.split("/");
        File ret = baseDir;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length; i++) {
                ret = new File(ret, dirs[i]);
            }
        }
        if (!ret.exists()) {
            ret.mkdirs();
        }
        return ret;
    }

    private static File getRealFileName(File baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = baseDir;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                ret = new File(ret, dirs[i]);
            }
        }
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(ret, dirs[dirs.length - 1]);
        return ret;
    }

}
