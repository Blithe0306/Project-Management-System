/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.LicenseConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.TokenXmlConstant;
import com.ft.otp.common.config.LicenseConfig;
import com.ft.otp.common.config.VendorConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.token.distmanager.service.IDistManagerServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.token.tokenimport.entity.VendorInfo;
import com.ft.otp.manager.token.tokenimport.service.ITokenImport;
import com.ft.otp.manager.token.tokenimport.service.TokenImportFactory;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.manager.tokenspec.service.ITokenSpecServ;
import com.ft.otp.util.tool.CreateFileUtil;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;
import com.ft.otp.util.zip.ZipUntie;

/**
 * 令牌导入业务处理控制
 *
 * @Date in Mar 26, 2013,8:28:26 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class TokenImportAction extends BaseAction {

    private Logger logger = Logger.getLogger(TokenImportAction.class);
    private static final long serialVersionUID = 1749099543464611523L;

    private IDistManagerServ distManagerServ = (IDistManagerServ) AppContextMgr.getObject("distManagerServ");
    private ITokenSpecServ tokenSpecServ = (ITokenSpecServ) AppContextMgr.getObject("tokenSpecServ");
    private ITokenServ importTokenServ;

    private File seedFile;//令牌文件
    private File keyFile;//密钥文件
    private String keyPass;//密码
    private int enabled;//是否启用
    private String orgunitIds;//组织机构
    private String vendorId = "1001";//厂商编号

    private String seedFileFileName;//令牌文件名称
    private String keyFileFileName;//密钥文件名称
    //日志记录
    public String importTknInfo;

    /**
     * @return the importTokenServ
     */
    public ITokenServ getImportTokenServ() {
        return importTokenServ;
    }

    /**
     * @param importTokenServ the importTokenServ to set
     */
    public void setImportTokenServ(ITokenServ importTokenServ) {
        this.importTokenServ = importTokenServ;
    }

    public String getImportTknInfo() {
        return importTknInfo;
    }

    public void setImportTknInfo(String importTknInfo) {
        this.importTknInfo = importTknInfo;
    }

    /**
     * 导入令牌业务操作处理
     * 
     * @Date in Mar 27, 2013,11:27:44 AM
     * @return
     */
    public String importToken() {
        int result = 0;
        try {
            VendorInfo vendorInfo = VendorConfig.getValue(vendorId);
            if (null == vendorInfo) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_file_failed"));
                return null;
            }

            //加载jar包，得到令牌导入实现服务
            ITokenImport tokenImport = TokenImportFactory.geTokenImportServ(vendorInfo);
            if (null == tokenImport) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_file_failed"));
                return null;
            }

            //文件上传
            uploadFile();
            if (null == seedFile) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_warn_tknnum_file_failed"));
                return null;
            }
            
            //验证是否需要上传bin文件
            boolean isUpPin = vdIsUploadPinFile(seedFile);
            if(isUpPin && keyFile == null) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_vd_bin_error_2"));
                return null;
            }
            
            //令牌导入初始化
            result = tokenImport.initTokenImport(seedFile, keyFile, keyPass);
            if (result != NumConstant.common_number_0) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_file_init_failed"));
                return null;
            }

            //导入的令牌总数
            int imTokenCount = tokenImport.getTokenCount();
            if (imTokenCount <= NumConstant.common_number_0) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_failed_please_check"));
                return null;
            }

            boolean normal = vendorInfo.getNormal();//是否是集成方厂商
            String specId = ""; //令牌规格，其它厂商使用
            //手机令牌数据，如果没有手机令牌数据，则在getTokenData接口实现中直接返回null
            List<Object> mobileList = null;
            try {
                Object objMobile = tokenImport.getTokenData(TokenXmlConstant.MAP_MOBILE);
                if (null != objMobile) {
                    mobileList = (List<Object>) objMobile;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //授权检查
            int impTokenNum = licenseVerify(imTokenCount);
            if (impTokenNum <= 0) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_file_too_big"));
                return null;
            }

            if (normal) {
                //添加令牌规格
                insertSpec(tokenImport);
            } else {
                List<?> specs = tokenSpecServ.query(new TokenSpec(), new PageArgument());
                if (StrTool.listNotNull(specs)) {
                    TokenSpec tokenSpec = (TokenSpec) specs.get(0);
                    specId = tokenSpec.getSpecid();
                } else {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_spec_invalid"));
                    return null;
                }
            }

            //添加令牌数据
            int[] retArr = insertToken(tokenImport, impTokenNum, mobileList, specId);
            //反初始化
            tokenImport.unInit();

            //组织导入完成后的提示信息
            String[] resultArr = getResponse(retArr, impTokenNum, imTokenCount);

            // 日志记录
            this.setImportTknInfo(resultArr[1]);
            // 日志记录
            outPutOperResult(resultArr[0], resultArr[1]);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_import_failed_please_check"));
        }

        return null;
    }
    
    /**
     * 组织导入结果提示信息
     * 
     * @Date in Mar 30, 2013,5:50:03 PM
     * @param retArr
     * @param tokenCount
     * @param mobileTokenNum
     * @return
     */
    private String[] getResponse(int[] retArr, int impTokenNum, int imTokenCount) {
        String[] result = new String[2];

        int importFail = retArr[0];
        int repeatCount = retArr[1];
        int importSucc = 0;
        importSucc = impTokenNum - importFail - repeatCount;

        //导入结果
        if (importSucc == importFail || importSucc == repeatCount) {
            result[0] = Constant.alert_error;
        } else {
            result[0] = Constant.alert_succ;
        }

        String signStr = Language.getLangStr(request, "colon");// :号
        String signStrD = Language.getLangStr(request, "comma");// ，号
        String signStrJ = Language.getLangStr(request, "stop");// 。号
        String unitStr = Language.getLangStr(request, "tkn_comm_one");// 支

        String succLang = Language.getLangStr(request, "tkn_import_succ_tip") + signStr;
        String failLang = Language.getLangStr(request, "tkn_import_error_tip") + signStr;
        String repeatLang = Language.getLangStr(request, "tkn_import_re_tip") + signStr;
        String exceedLang = Language.getLangStr(request, "tkn_import_exceed_tip") + signStr;

        StringBuilder str = new StringBuilder();
        //令牌总数统计提示
        str.append(Language.getLangStr(request, "tkn_import_success")).append(imTokenCount).append(unitStr).append(
                signStrD);
        str.append(succLang).append(importSucc).append(unitStr);
        if (importFail > 0) {
            str.append(signStrD);
            str.append(failLang).append(importFail).append(unitStr);
        }
        if (repeatCount > 0) {
            str.append(signStrD);
            str.append(repeatLang).append(repeatCount).append(unitStr);
        }

        int exceedTkn = imTokenCount - impTokenNum;
        if (exceedTkn > 0) {
            str.append(signStrD);
            str.append(exceedLang).append(exceedTkn).append(unitStr);
        }
        str.append(signStrJ);

        result[1] = str.toString();
        return result;
    }

    /**
     * 令牌导入授权数检查
     * 
     * @Date in Mar 30, 2013,12:38:41 PM
     * @param tokenCount
     * @param mobileTokenNum
     * @return
     */
    private int licenseVerify(int impTokenCount) throws Exception {
    	// 重新加载授权
    	LicenseConfig.reloadLicenceInfo();
        int licTokenType = (Integer) LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_LICTYPE);
        int tokenCount = (Integer) LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_TOKENCOUNT);
        if (licTokenType == LicenseConstant.LICENSE_TYPE_PREMIUM) {
            return impTokenCount;
        } else {
            TokenInfo tokenInfo = new TokenInfo();
            int dbTokenCount = importTokenServ.count(tokenInfo);
            int licTokenCnt = tokenCount - dbTokenCount;
            if (licTokenCnt <= impTokenCount) {
                return licTokenCnt;
            } else {
                return impTokenCount;
            }
        }
    }

    /**
     * 添加令牌规格
     * 
     * @Date in Mar 29, 2013,3:21:59 PM
     * @param tokenImport
     * @throws Exception
     */
    private void insertSpec(ITokenImport tokenImport) throws Exception {
        List<Object> specList = new ArrayList<Object>();
        //令牌规格数据，其它厂商如果没有规格，则在getTokenData接口实现中直接返回null
        List<Object> objList = null;
        try {
            Object object = tokenImport.getTokenData(TokenXmlConstant.MAP_SPEC);
            if (null != object) {
                objList = (List<Object>) object;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (!StrTool.listNotNull(objList)) {
            return;
        }
        Iterator<?> iter = objList.iterator();
        TokenSpec tokenSpec = null;
        while (iter.hasNext()) {
            tokenSpec = (TokenSpec) iter.next();
            tokenSpec.getSpecid();

            Object object = tokenSpecServ.find(tokenSpec);
            if (null == object) {
                specList.add(tokenSpec);
            }
        }

        if (StrTool.listNotNull(specList)) {
            tokenSpecServ.importTokenSpec(specList);

            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10008);
        }
    }

    /**
     * 添加令牌数据
     * 
     * @Date in Mar 29, 2013,2:31:54 PM
     * @param tokenImport
     * @param tokenCount
     */
    private int[] insertToken(ITokenImport tokenImport, int tokenCount, List<Object> objList, String specId) {
        int[] arr = new int[2];

        TokenInfo tokenInfo = null;
        List<Object> tokenList = new ArrayList<Object>();
        List<Object> mobileList = new ArrayList<Object>();

        int importFail = 0;//令牌导入失败数
        int repeatCount = 0; //重复、已经存在的令牌数
        int count = 0; //令牌导入计数

        int perCount = 0;
        int perIndex = 0; //进度条
        LinkUser linkUser = null;
        HttpSession session = getSession();
        if (null != session) {
            String sessionId = session.getId();
            linkUser = OnlineUsers.getUser(sessionId);
        } else {
            linkUser = new LinkUser();
        }
        perIndex = linkUser.getPercent();

        for (int i = 0, j = 0; i < tokenCount; i++) {
            //获取下一个令牌
            tokenInfo = tokenImport.getNextToken();
            tokenInfo = setTokenInfo(tokenInfo, specId);
            Object object = null;
            int physicaltype = tokenInfo.getPhysicaltype(); //令牌物理类型
            try {
                if (!StrTool.strNotNull(tokenInfo.getToken()) || !StrTool.strNotNull(tokenInfo.getPubkey())
                        || !StrTool.strNotNull(tokenInfo.getSpecid())) {
                    importFail++;
                    continue;
                }

                object = importTokenServ.find(tokenInfo);
            } catch (Exception ex) {
                importFail++;
                continue;
            }
            if (null == object) {
                if (physicaltype == NumConstant.MOBILE_TOKEN) {
                    Object mbObject = objList.get(j);
                    mobileList.add(mbObject);
                    j++;
                }

                tokenList.add(tokenInfo);
                count++;
            } else {
                repeatCount++;
                if (physicaltype == NumConstant.MOBILE_TOKEN) {
                    j++;
                }
            }
            if (count >= NumConstant.batchCount) {
                //按批量导入令牌数据
                if (count >= NumConstant.batchCount) {
                    try {
                        importTokenServ.importToken(tokenList);
                        if (StrTool.listNotNull(mobileList)) {
                            distManagerServ.importExtToken(mobileList);
                            mobileList.clear();
                        }
                    } catch (Exception e) {
                        importFail += count;
                        logger.error(e.getMessage(), e);
                    }
                    count = 0;
                    tokenList.clear();
                }
            }

            perCount++;
            int temp = (perCount * 60) / tokenCount;
            linkUser.setPercent(perIndex + temp);
        }

        //不够批量或批量剩余硬件令牌的导入
        if ((tokenCount < NumConstant.batchCount)
                || (tokenCount > NumConstant.batchCount && count < NumConstant.batchCount)) {
            try {
                if (count > 0) {
                    importTokenServ.importToken(tokenList);
                    if (StrTool.listNotNull(mobileList)) {
                        distManagerServ.importExtToken(mobileList);
                        mobileList.clear();
                    }
                    tokenList.clear();
                }
            } catch (Exception e) {
                importFail += count;
                logger.error(e.getMessage(), e);
            }
        }

        arr[0] = importFail;
        arr[1] = repeatCount;

        return arr;
    }

    /**
     * 令牌数据补充完善
     * 
     * @Date in Mar 28, 2013,4:38:16 PM
     * @param tokenInfo
     * @return
     */
    private TokenInfo setTokenInfo(TokenInfo tokenInfo, String specId) {
        String tokenSN = tokenInfo.getTokenSN();
        String privData = tokenInfo.getPrivData();
        int expired = tokenInfo.getExpired();
        if (StrTool.strNotNull(tokenSN) && StrTool.strNotNull(privData) && expired > 0) {
            tokenInfo.setToken(tokenSN);
            tokenInfo.setPubkey(privData);
            tokenInfo.setExpiretime(expired);
            tokenInfo.setSpecid(specId);
        }

        tokenInfo.setVendorid(vendorId);
        tokenInfo.setAuthnum("0");
        tokenInfo.setLocked(0);
        tokenInfo.setEnabled(enabled);
        tokenInfo.setLost(0);
        tokenInfo.setLogout(0);
        tokenInfo.setAuthmethod(0);
        tokenInfo.setEmpindeath(0);
        tokenInfo.setTemploginerrcnt(0);
        tokenInfo.setLongloginerrcnt(0);
        tokenInfo.setLoginlocktime(0);
        tokenInfo.setDriftcount(0);
        tokenInfo.setGensmstime(0);
        tokenInfo.setImporttime(StrTool.timeSecond());
        tokenInfo.setDistributetime(0);
        tokenInfo.setCrauthnum("0");
        tokenInfo.setAuthtime(0);
        tokenInfo.setCrauthtime(0);
        tokenInfo.setAuthotp("");
        tokenInfo.setCrauthotp("");
        tokenInfo.setCrauthdata("");
        tokenInfo.setCractivecode("");
        //        tokenInfo.setPubkeystate(0); 已在加载导入文件时赋值
        tokenInfo.setNewpubkey("");
        tokenInfo.setCractivetime(0);
        tokenInfo.setCractivecount(0);

        if (StrTool.strNotNull(orgunitIds)) {
            String idStr = orgunitIds.substring(0, orgunitIds.length() - 1);
            String orgArr[] = idStr.split(":");
            tokenInfo.setDomainid(StrTool.parseInt(orgArr[0]));
            int orgId = StrTool.parseInt(orgArr[1]);
            if (orgId > 0) {
                tokenInfo.setOrgunitid(orgId);
            }
        }

        return tokenInfo;
    }

    /**
     * 令牌文件、密钥文件上传
     * 
     * @Date in Mar 28, 2013,11:03:54 AM
     * @throws Exception
     */
    private void uploadFile() throws Exception {
        String filePath = null;
        //得到令牌文件、密钥文件
        if (seedFileFileName.endsWith(Constant.FILE_ZIP)) {
            filePath = getFilePath(Constant.WEB_TEMP_FILE_TOKEN_IMPORT, seedFileFileName);
            File afterDecFileDir = decompressionZip(seedFile, filePath);
            if (null != afterDecFileDir) {
                seedFile = getXmlFile(afterDecFileDir);
                keyFile = getKeyFile(afterDecFileDir);
            }
        } else {
            filePath = getFilePath(Constant.WEB_TEMP_FILE_TOKEN_IMPORT, null);
            if (null != seedFile && StrTool.strNotNull(seedFileFileName)) {
                File seedFile2 = new File(filePath, seedFileFileName);
                FileUtils.copyFile(seedFile, seedFile2);

                seedFile = seedFile2;
            }
            if (null != keyFile && StrTool.strNotNull(keyFileFileName)) {
                File keyFile2 = new File(filePath, keyFileFileName);
                FileUtils.copyFile(keyFile, keyFile2);

                keyFile = keyFile2;
            }
        }
    }

    /**
     * .zip文件解压，获得解压后的文件对象
     * 
     * @Date in Mar 26, 2013,2:27:15 PM
     * @param file
     * @param filePath
     * @return
     */
    public File decompressionZip(File file, String filePath) {
        File afterDecFile = null;
        try {
            File fileTarget = new File(filePath);
            if (!fileTarget.exists()) {
                CreateFileUtil.createFile(filePath);
            }
            FileUtils.copyFile(file, fileTarget);

            //解压.zip文件
            ZipUntie.decompressionZip(fileTarget.getParentFile());
            //解压之后的文件
            afterDecFile = new File(filePath.substring(0, filePath.length() - 4));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return afterDecFile;
    }

    /**
     * 取得XML令牌文件
     * 
     * @Date in Mar 28, 2013,10:20:38 AM
     * @param fileDir
     * @return
     */
    public File getXmlFile(File fileDir) {
        File[] files = fileDir.listFiles();
        File xmlFile = null;
        //获取xml 路径 和 bin 文件路径
        for (int i = 0; i < files.length; i++) {
            String fName = files[i].getName();
            if (fName.toLowerCase().endsWith(Constant.FILE_XML)) {
                xmlFile = files[i];
                break;
            }
        }

        return xmlFile;
    }

    /**
     * 获取密钥文件
     * 
     * @Date in Mar 28, 2013,10:25:15 AM
     * @param fileDir
     * @return
     */
    public File getKeyFile(File fileDir) {
        File[] files = fileDir.listFiles();
        File keyFile = null;
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            keyFile = files[i];
            //密钥为.bin文件
            if (fileName.toLowerCase().endsWith(Constant.FILE_BIN)) {
                break;
            }
        }

        return keyFile;
    }
    
    /**
     * 验证是否上传bin文件
     * @Date in Nov 4, 2013,6:17:45 PM
     * @param sfile
     * @return
     */
    public boolean vdIsUploadPinFile(File sfile) {
        boolean result = false;
        Element rootElement = null;//XML目录根节点
        XmlUtil xmlUtil = new XmlUtil();

        //取得XML令牌文件根目录
        rootElement = xmlUtil.getElement(sfile);
        
        Element encryptionNode = rootElement.element("EncryptionKey");
        if (null != encryptionNode) {
            result = true;
        }
        return result;
    }

    /**
     * @return the seedFile
     */
    public File getSeedFile() {
        return seedFile;
    }

    /**
     * @param seedFile the seedFile to set
     */
    public void setSeedFile(File seedFile) {
        this.seedFile = seedFile;
    }

    /**
     * @return the keyFile
     */
    public File getKeyFile() {
        return keyFile;
    }

    /**
     * @param keyFile the keyFile to set
     */
    public void setKeyFile(File keyFile) {
        this.keyFile = keyFile;
    }

    /**
     * @return the keyPass
     */
    public String getKeyPass() {
        return keyPass;
    }

    /**
     * @param keyPass the keyPass to set
     */
    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    /**
     * @return the enabled
     */
    public int getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the orgunitIds
     */
    public String getOrgunitIds() {
        return orgunitIds;
    }

    /**
     * @param orgunitIds the orgunitIds to set
     */
    public void setOrgunitIds(String orgunitIds) {
        this.orgunitIds = orgunitIds;
    }

    /**
     * @return the vendorId
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * @param vendorId the vendorId to set
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * @return the seedFileFileName
     */
    public String getSeedFileFileName() {
        return seedFileFileName;
    }

    /**
     * @param seedFileFileName the seedFileFileName to set
     */
    public void setSeedFileFileName(String seedFileFileName) {
        this.seedFileFileName = seedFileFileName;
    }

    /**
     * @return the keyFileFileName
     */
    public String getKeyFileFileName() {
        return keyFileFileName;
    }

    /**
     * @param keyFileFileName the keyFileFileName to set
     */
    public void setKeyFileFileName(String keyFileFileName) {
        this.keyFileFileName = keyFileFileName;
    }

}
