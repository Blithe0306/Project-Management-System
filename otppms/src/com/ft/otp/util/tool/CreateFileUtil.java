package com.ft.otp.util.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.ft.otp.common.Constant;

public class CreateFileUtil {

    private static Logger logger = Logger.getLogger(CreateFileUtil.class);

    /**
     * 创建单个文件
     * 
     * @param destFileName    目标文件名
     * @return    创建成功，返回true，否则返回false
     */
    public static boolean createFile(String destFileName) {
        destFileName = correctPath(destFileName);
        File file = new File(destFileName);
        if (file.exists()) {
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            return false;
        }
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
      * 创建目录
      * @param destDirName    目标目录名
      * @return    目录创建成功返回true，否则返回false
      */
    public static boolean createDir(String destDirName) {
        destDirName = correctPath(destDirName);
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }

        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建一个新的内容为空的目录，如果目录已经存在，则删除后重新生成一个空的新目录。
     * @Date in 2011-12-19,下午04:13:04
     * @param destDirName
     * @return
     */
    public static boolean createNewDir(String destDirName) {
        destDirName = correctPath(destDirName);
        File dir = new File(destDirName);
        if (dir.exists()) {
            CreateFileUtil.delFolder(destDirName);
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    /**
      * 创建临时文件
      * @param prefix    临时文件名的前缀
      * @param suffix    临时文件名的后缀
      * @param dirName    临时文件所在的目录，如果输入null，则在用户的文档目录下创建临时文件
      * @return    临时文件创建成功返回临时文件路径及文件名，否则返回null
      */
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {
            try {
                //在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                //返回临时文件的路径
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        } else {
            File dir = new File(dirName);
            //如果临时文件所在目录不存在，首先创建
            if (!dir.exists()) {
                if (!CreateFileUtil.createDir(dirName)) {
                    return null;
                }
            }
            try {
                //在指定目录下创建临时文件
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

    /**
     * 删除目录
     * 
     * @Date in Apr 20, 2012,2:30:01 PM
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            folderPath = correctPath(folderPath);
            delAllFile(folderPath); // 
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
        }
    }

    private static boolean delAllFile(String path) {
        path = correctPath(path);
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }

        return flag;
    }

    /**
     * 创建文件并获取文件输出流
     * 
     * @Date in 2011-12-19,下午01:51:12
     * @param pathFileName 完整的路径和文件名
     * @return
     */
    public static FileOutputStream getFileOutStream(String pathFileName) throws IOException {
        pathFileName = correctPath(pathFileName);
        pathFileName = new String(pathFileName.getBytes("UTF-8"), "UTF-8");
        CreateFileUtil.createFile(pathFileName);
        File file = new File(pathFileName);
        FileOutputStream output = null;

        output = new FileOutputStream(file);
        return output;
    }

    /**
     * 通过文件输出流写
     * @Date in 2011-12-19,下午01:52:23
     * @param outputStream
     * @param outString
     * @param length
     */
    public static void writeByFileOutStream(FileOutputStream outputStream, byte[] outbytes, int length)
            throws IOException {
        outputStream.write(outbytes, 0, length);
        outputStream.flush();
    }

    /**
     * 关闭文件输出流
     * @Date  in 2011-12-19,下午01:52:41
     * @param outputStream
     */
    public static void closeFileOutStream(FileOutputStream outputStream) {
        try {
            if (null != outputStream) {
                outputStream.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 文件是否存在
     * 
     * @Date in Apr 20, 2012,5:43:26 PM
     * @param pathFileName
     * @return
     */
    public static boolean isFileExists(String pathFileName) {
        String path = correctPath(pathFileName);
        File file = new File(path);
        if (!file.exists()) {
            return false;
        } else {
            return true;
        }
    }

    public static void download2Client(HttpServletResponse response, String contentTypeStr, String contentStr,
            int contentOffset, int contentLength, String fileName) {
        String contentType = "text/html";
        if (StrTool.strNotNull(contentTypeStr)) {
            contentType = contentTypeStr;
        }

        try {
            response.reset();
            // 设置浏览器显示的内容类型
            // response.setContentType("text/html");
            response.setContentType(contentType);
            // 设置内容作为附件下载，并且名字为：fileName
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            ServletOutputStream osw = response.getOutputStream();
            osw.write(contentStr.getBytes(), contentOffset, contentLength);
            osw.flush();
            // osw.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void download2Client(HttpServletResponse response, String contentTypeStr, String contentStr,
            String fileName) {
        download2Client(response, contentTypeStr, contentStr, 0, contentStr.length(), fileName);
    }

    public static String correctPath(String path) {
        path = path.replace("\\\\", "/");
        path = path.replace("\\", "/");
        path = path.replace("//", "/");
        return path;
    }

    /**
     * 如果目录存在就删除，如果不存在就创建目录
     * 方法说明
     * @Date in Mar 30, 2012,3:32:48 PM
     * @param workDir
     * @return
     */
    public static File createOrDelDir(String workDir) {
        File file = new File(workDir);
        try {
            if (file.exists()) {
                CreateFileUtil.delFolder(workDir);
            }
            file.mkdir();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return file;
    }

    /**
     * 输出数据至TXT文件
     * 
     * @Date in Apr 12, 2012,7:59:31 PM
     * @param filePath
     * @param arr
     * @param list
     */
    public static synchronized boolean writeTxt(String filePath, String[] arr, boolean isContinuance) {
        if (!StrTool.strNotNull(filePath) || !StrTool.arrNotNull(arr)) {
            return false;
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                createFile(filePath);
            }

            fw = new FileWriter(file, isContinuance);
            bw = new BufferedWriter(fw);
            for (int i = 0; i < arr.length; i++) {
                bw.write(arr[i]);
                bw.write("\r\n");
                bw.flush();
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (null != bw) {
                    bw.close();
                }
                if (null != fw) {
                    fw.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    /**
     * 文件重命名
     * 
     * @Date in Apr 13, 2012,1:12:50 PM
     * @param resourceFile
     * @param newFilename
     * @return
     */
    public synchronized static boolean renameFile(String resourceFile, String newFilename) {
        if (!StrTool.strNotNull(resourceFile) || !StrTool.strNotNull(newFilename)) {
            return false;
        }
        try {
            File file = new File(newFilename);
            if (file.exists()) {
                return true;
            }

            File reFile = new File(resourceFile);
            //需要提供完整路径  
            reFile.renameTo(new File(newFilename));
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * 拷贝一个文件夹的所有内容至另外一个文件夹
     * 
     * @Date in Sep 27, 2012,1:31:20 PM
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        boolean copyOK = false;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }

            copyOK = true;
        } catch (Exception e) {
            copyOK = false;
        }

        return copyOK;
    }

    /**
     * 删除指定文件
     * 
     * @Date in Apr 13, 2012,1:23:29 PM
     * @param resourceFile
     * @return
     */
    public synchronized static boolean deleteFile(String resourceFile) {
        if (!StrTool.strNotNull(resourceFile)) {
            return false;
        }
        try {
            File file = new File(resourceFile);
            if (file.exists()) {
                file.delete();
            }
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * 删除目录下指定格式的所有文件
     * 
     * @Date in Jul 13, 2012,4:09:45 PM
     * @param path
     * @param format
     * @return
     */
    public static boolean delAllFile(String path, String format) {
        path = correctPath(path);
        boolean flag = false;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return flag;
            }
            if (!file.isDirectory()) {
                return flag;
            }
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                temp = new File(path + tempList[i]);
                String fileName = temp.getName();
                fileName = fileName.toLowerCase();
                if (fileName.endsWith(Constant.FILE_TXT)) {
                    deleteFile(path + "/" + tempList[i]);
                }
            }
            flag = true;
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 判断目录下是否已经存在某个文件
     * 是否已经上传该jar包
     * 
     * @Date in Nov 22, 2011,5:33:36 PM
     * @param jarName
     * @param path
     * @return
     */
    public static int isExistFile(String jarName, String path) {
        int state = 0;
        File file = new File(path);
        if (file.exists()) {
            if (StrTool.strEquals(jarName, file.getName())) {
                state = 1;
            }
        }

        return state;
    }

    /**
     * 导出文件到指定的目录
     * 种子导出统一使用些方法
     * 
     * @Date in Dec 13, 2011,9:57:16 AM
     * @param fileName
     * @param content
     * @param path
     */
    public static void exportFile(byte[] content, String path) throws Exception {
        File tnkfile = new File(path);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(tnkfile);
            output.write(content, 0, content.length);
            output.flush();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
