/**
 *Administrator
 */
package com.ft.otp.common.soft;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.tokenimport.service.impl.ft.helper.TokenCryptoUtil;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.manager.tokenspec.service.ITokenSpecServ;
import com.ft.otp.util.alg.AESUtil;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.tool.CreateFileUtil;

/**
 * 软件令牌分发公用类
 *
 * @Date in Feb 27, 2013,6:18:25 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class SoftTknDistriHelp {

    /**
     * 生成软件令牌分发文件
     * 方法说明
     * @Date in Feb 27, 2013,6:19:40 PM
     * 
     * 数据的格式为=====>前缀:版本号:有效数据长度:有效数据:后缀【补零】
     * 有效数据====>令牌类型:令牌号:共享密钥:认证基数:起始时间:间隔时间:是否默认PIN:PIN:动态口令长度:令牌有效期:算法标识:"SUITE1":"SUITE2":"SUITE3"
     * 有效数据=的长度为4位，不够进行补“0”         
     * 需要注意的是，这里的补零是补字符“0”
     * 是否默认PIN:如果是默认PIN，这项值为1，否则为0
     * @param tokenInfo
     * @parm  isDefault 是否默认Pin
     */
    //令牌规格服务接口
    private static ITokenSpecServ tokenSpecServ = (ITokenSpecServ) AppContextMgr.getObject("tokenSpecServ");
    //加密密钥 key
    public static byte des_key[] = { (byte) 0x54, (byte) 0x7b, (byte) 0x5a, (byte) 0x9f, (byte) 0x27, (byte) 0x4e,
            (byte) 0xbd, (byte) 0x8c };
    //加密向量 iv
    public static byte des_iv[] = { (byte) 0xed, (byte) 0x30, (byte) 0x47, (byte) 0xbf, (byte) 0x3b, (byte) 0x86,
            (byte) 0x55, (byte) 0x1a };

    public static String generateDistFile(String path, TokenInfo tokenInfo, boolean isDefault) throws Exception {
        String inputStr = "";
        //前缀:版本号:
        String prevStr = "epassotp:3.0";
        //有效数据
        String validDataStr = "";
        if (isDefault) {
            validDataStr = getValidData(tokenInfo, 1);
        } else {
            validDataStr = getValidData(tokenInfo, 0);
        }
        //有效数据长度
        int validLen = validDataStr.length();
        String validDataLen = String.format("%04d", validLen);

        //后缀
        String lastStr = "epassotp000000";

        inputStr = prevStr + ":" + validDataLen + ":" + validDataStr + ":" + lastStr;

        //对inputStr进行加密 
        byte encData[] = AESUtil.DESEncrypt(des_key, des_iv, inputStr.getBytes());
        if (encData == null) {
            return null;
        }
        //数据写入文件
        String fileName = getFile(encData, tokenInfo.getToken(), path);

        return fileName;
    }

    /**
     * 文件的写入
     */
    public static String getFile(byte inputStr[], String fileName, String path) {
        File dir = new File(path);
        //如果临时文件所在目录不存在，首先创建
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 生成stf文件
        String fname = path + "/";
        fname += fileName + Constant.FILE_STF;

        CreateFileUtil.delAllFile(path, "");

        File licfile = new File(fname);

        FileOutputStream output = null;
        try {
            output = new FileOutputStream(licfile);
            output.write(inputStr, 0, inputStr.length);
        } catch (Exception e) {
            try {
                throw e;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return fname;
    }

    /**
     * 有效数据的拼接
     * @throws BaseException 
     */
    public static String getValidData(TokenInfo tokenInfo, int isDeault) throws Exception {
        TokenSpec tokenSpec = new TokenSpec();
        tokenSpec.setSpecid(tokenInfo.getSpecid());
        tokenSpec = (TokenSpec) tokenSpecServ.find(tokenSpec);
        String token = tokenInfo.getToken();
        String pubkey = tokenInfo.getPubkey();
        byte[] bPubkey = TokenCryptoUtil.decryptPubkey(token, pubkey);
        pubkey = AlgHelper.bytes2HexString(bPubkey);
        String validDataStr = tokenSpec.getTokenType() + ":" + token + ":" + pubkey + ":" + tokenInfo.getAuthnum()
                + ":" + tokenSpec.getBegintime() + ":" + tokenSpec.getIntervaltime() + ":" + isDeault + ":"
                + tokenInfo.getSofttoken_distribute_pwd() + ":" + tokenSpec.getOtplen() + ":"
                + tokenInfo.getExpiretime() + ":" + tokenSpec.getAlgid() + ":" + '"' + tokenSpec.getSignsuite() + '"'
                + ":" + '"' + tokenSpec.getCvssuite() + '"' + ":" + '"' + tokenSpec.getCrsuite() + '"';

        return validDataStr;
    }

    public static void downLoadFile(String fileName, String filePath, HttpServletResponse response) throws Exception {
        //filePath是指欲下载的文件的路径。
        File file = new File(filePath);

        //以流的形式下载文件。
        InputStream iStream = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer = new byte[iStream.available()];
        iStream.read(buffer);
        iStream.close();

        //清空response
        response.reset();
        //设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
        response.addHeader("Content-Length", String.valueOf(file.length()));

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }
}
