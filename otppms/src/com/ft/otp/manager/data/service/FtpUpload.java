package com.ft.otp.manager.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.util.tool.StrTool;

public class FtpUpload {

    FTPClient ftpClient = null;
    //单例对象
    private static FtpUpload instance = null;

    public static synchronized FtpUpload getInstance() {
        return StrTool.objNotNull(instance) ? instance : (new FtpUpload());
    }

    private Logger logger = Logger.getLogger(FtpUpload.class);

    /**连接远程服务器
     * @param serverIp FTP服务器的IP地址
     * @param user 登录FTP服务器的用户名
     * @param password 登录FTP服务器的用户名的口令
     * @param remotePath FTP服务器上的路径
     * @return int
     * 		   1 连接成功
     *         0 连接失败 或 进入目录失败 
     */
    public int connectServer(DBBakConfInfo conParam) {
        int result = 1;
        try {
            //不正确的参数 或 远程目录
            if (!StrTool.objNotNull(conParam) || !StrTool.strNotNull(conParam.getDir())) {
                result = 0;
                return result;
            }

            ftpClient = getFtpClient();
            ftpClient.connect(conParam.getServerip(), Integer.parseInt(conParam.getPort()));
            //登陆	
            boolean isLogin = ftpClient.login(conParam.getUser(), conParam.getPassword());
            if (!isLogin) {
                result = 0;
                return result;
            }
            ftpClient.enterLocalPassiveMode(); //被动传输
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                result = 0;//
                return result;
            }

            //进入相关目录
            boolean isin = ftpClient.changeWorkingDirectory(conParam.getDir());// 转到指定下载目录
            if (!isin) {//没有进入该目录
                result = 0;
                return result;
            }
        } catch (IOException e) {
            //closeConnect();
            result = 0;
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 退出登录
     * 关闭远程连接
     */
    public boolean closeConnect() {
        boolean result = false;
        try {
            result = ftpClient.logout();//
        } catch (IOException e) {
            result = false;
            logger.error(e.getMessage(), e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    result = false;
                    logger.error(e.getMessage(), e);
                }
            }
            //return result;
        }
        return result;
    }

    /**
     * 
     * 上传文件到远程服务器
     * @param filePath 当前文件所在目录<包括文件名>
     * @param remotefileName 写入远程服务器上的文件名
     * @param param 配置参数
     */
    public boolean upload(DBBakConfInfo conParam, String filePath, String remotefileName) {
        boolean isSuccess = false;
        try {
            int repaly = connectServer(conParam);//连接登陆
            if (repaly != 0) { //连接成功
                //isSuccess=storeFile(filePath,remotefileName);
                File file = new File(filePath);
                FileInputStream is = new FileInputStream(file);
                isSuccess = ftpClient.storeFile(remotefileName, is);
                is.close();
            }
        } catch (IOException e) { //传输如果有异常 那么再传输一次	 
            logger.error(e.getMessage(), e);
        } finally {
            closeConnect();
        }
        return isSuccess;
    }

    /**
     * @return the ftpClient
     */
    public FTPClient getFtpClient() {
        if (!StrTool.objNotNull(ftpClient)) {
            ftpClient = new FTPClient();
            ftpClient.setDataTimeout(60000); //设置传输超时时间为1分钟 
            ftpClient.setConnectTimeout(60000); //连接超时为1分钟
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return ftpClient;
    }

}
