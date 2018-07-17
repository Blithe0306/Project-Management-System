/**
 *Administrator
 */
package com.ft.otp.manager.lic.action;

import java.io.File;
import java.io.FileInputStream;
import org.apache.log4j.Logger;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.LicenseConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.LicenseConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.manager.lic.decode.LicenseDecode;
import com.ft.otp.manager.lic.decode.LicenseVerify;
import com.ft.otp.manager.lic.entity.LicInfo;
import com.ft.otp.manager.lic.entity.License;
import com.ft.otp.manager.lic.service.ILicInfoServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 授权上传、更新的业务处理Action
 *
 * @Date in Apr 22, 2013,3:09:43 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LicInfoAction extends BaseAction {

    private static final long serialVersionUID = -7897955086819169319L;
    private Logger logger = Logger.getLogger(LicInfoAction.class);

    //认证服务器接口
    private IServerServ serverServ = (IServerServ) AppContextMgr.getObject("serverServ");

    //令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");
    private ILicInfoServ licInfoServ;
    private File licFile;
    private LicInfo licInfo = null;
    //记录日志实体
    private LicInfo oldLicInfo;
    
    public LicInfo getOldLicInfo() {
		return oldLicInfo;
	}

	public void setOldLicInfo(LicInfo oldLicInfo) {
		this.oldLicInfo = oldLicInfo;
	}

	/**
     * @return the licInfoServ
     */
    public ILicInfoServ getLicInfoServ() {
        return licInfoServ;
    }

    /**
     * @param licInfoServ the licInfoServ to set
     */
    public void setLicInfoServ(ILicInfoServ licInfoServ) {
        this.licInfoServ = licInfoServ;
    }

    /**
     * @return the licFile
     */
    public File getLicFile() {
        return licFile;
    }

    /**
     * @param licFile the licFile to set
     */
    public void setLicFile(File licFile) {
        this.licFile = licFile;
    }

    /**
     * @return the licInfo
     */
    public LicInfo getLicInfo() {
        return licInfo;
    }

    /**
     * @param licInfo the licInfo to set
     */
    public void setLicInfo(LicInfo licInfo) {
        this.licInfo = licInfo;
    }

    /**
     * 上传/更新授权
     * 
     * @Date in Apr 27, 2013,3:59:21 PM
     * @return
     */
    public String upLic() {
        LicInfo licInfo = null;
        License license = getLicense();
        if (null == license) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "lic_invalid_file_err"));
            return null;
        }

        //授权时间有效性检查
        boolean result = LicenseVerify.verifyLicValidity(license);
        if (!result) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "lic_not_valid_err"));
            return null;
        }

        try {
            //获取数据库中当前有效授权实体信息
            LicInfo lic = new LicInfo();
            lic.setLicstate(1);
            lic = (LicInfo) licInfoServ.find(lic);
            if (null != lic) {
                int infoType = lic.getLictype();
                int licType = license.getLicType();

                //如果有效授权不为评估授权并且上传授权为评估授权，直接返回错误提示
                if (infoType != LicenseConstant.LICENSE_TYPE_EVALUATION
                        && licType == LicenseConstant.LICENSE_TYPE_EVALUATION) {
                    outPutOperResult(Constant.alert_error, Language
                            .getLangStr(request, "lic_not_allow_upload_eval_lic"));
                    return null;
                }

                String infoOwner = lic.getCustomerid();
                String licOwner = license.getOwner();

                //如果有效授权不为评估授权并且上传授权厂商不一致就直接返回错误提示
                if (infoType != LicenseConstant.LICENSE_TYPE_EVALUATION && !StrTool.strEquals(licOwner, infoOwner)) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "lic_username_not_same"));
                    return null;
                }

                //如果有效授权文件从高级更换为企业授权，需要校验令牌数，节点数
                if (infoType == LicenseConstant.LICENSE_TYPE_PREMIUM
                        && licType == LicenseConstant.LICENSE_TYPE_ENTERPRISE) {
                    //令牌总量检查
                    int tknum = tokenServ.count(new TokenInfo());
                    if (tknum > license.getTokenCount()) {
                        outPutOperResult(Constant.alert_error, Language.getLangStr(request, "lic_check_all_tkn_num"));
                        return null;
                    }

                    //服务器节点数和授权文件服务器节点数检查
                    int hostnum = serverServ.count(new ServerInfo());
                    if (hostnum > license.getServerNodes()) {
                        outPutOperResult(Constant.alert_error, Language.getLangStr(request, "lic_check_sernodes_num"));
                        return null;
                    }
                }
            }

            licInfo = new LicInfo();
            licInfo.setLicid(license.getLicId());
            licInfo = (LicInfo) licInfoServ.find(licInfo);
            if (null != licInfo) {
                licInfo.setLicstate(1);
                //更新为新授权
                licInfoServ.updateObj(licInfo);
                //更新最新授权文件到认证服务器
                updateServerLicId(licInfo.getLicid());
            } else {
                addNewLic(license);//上传新授权
            }

            //更新之前的授权状态为不启用
            if (null != lic) {
                String licId = lic.getLicid();
                String licenseId = license.getLicId();
                if (!StrTool.strEquals(licId, licenseId)) {
                    updateLic(lic);
                }
            }
            
            //日志记录
            if(!StrTool.objNotNull(lic)){
            	lic = new LicInfo();
            	lic.setLictype(-1); // 没有授权文件的标志
            }
            lic.setOldLicType(license.getLicType());
            this.setOldLicInfo(lic);
            //日志记录
            LicenseConfig.reLoad();//重新加载授权配置
            
            outPutOperResult(Constant.alert_succ, null);
            request.getSession().getServletContext().setAttribute(Constant.LICENCE_IF_EFFECTIVE, true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "lic_update_file_err"));
        }

        return null;
    }

    /**
     * 更新服务器的授权ID
     * 
     * @Date in Jul 17, 2013,2:35:05 PM
     * @param licId
     */
    private void updateServerLicId(String licId) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setLicid(licId);
        try {
            serverServ.updateNewLicId(serverInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 将上传授权文件信息替换已有授权信息
     * @Date in May 24, 2013,4:49:38 PM
     * @param newLic
     * @param license
     * @return
     */
    public LicInfo setLicInfoAttr(LicInfo newLic, License license) {
        newLic.setLicinfo(license.getLicInfo());
        newLic.setLictype(license.getLicType());
        newLic.setIssuer(license.getIssuer());
        newLic.setCustomerid(license.getOwner());
        newLic.setLicstate(NumConstant.common_number_1);
        newLic.setLicupdatetime(StrTool.timeSecond());

        return newLic;
    }

    /**
     * 查看授权
     * 
     * @Date in May 18, 2013,10:20:51 AM
     * @return
     */
    public String find() {
        licInfo = new LicInfo();
        licInfo.setLicstate(1);
        try {
            licInfo = (LicInfo) licInfoServ.find(licInfo);
            if (null == licInfo) {
                return null;
            }
            License license = new License();
            license = LicenseDecode.getLicenseInfo(licInfo.getLicinfo());
            if (null == license) { //授权解析失败
                return null;
            }

            licInfo.getLicInfo(licInfo, license);//授权详细属性的填充

            // 授权状态获取
            if (LicenseVerify.verifyLicValidity(license)) {
                if (LicenseVerify.verifyTypeAndState(licInfo)) {// 正常
                    licInfo.setLicstate(NumConstant.common_number_3);
                } else {// 不正常
                    licInfo.setLicstate(NumConstant.common_number_2);
                }
            } else {// 已失效
                licInfo.setLicstate(NumConstant.common_number_1);
            }
            outPutOperResult(Constant.alert_succ, licInfo);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        setLicInfo(licInfo);
        return null;
    }

    /**
     * 更新之前的授权状态为不启用
     * 
     * @Date in May 17, 2013,2:23:05 PM
     * @param licInfo
     */
    private void updateLic(LicInfo licInfo) {
        try {
            licInfo.setLicstate(0);
            licInfoServ.updateObj(licInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 上传/更新新授权
     * 
     * @Date in May 17, 2013,2:18:31 PM
     * @param license
     * @throws Exception
     */
    private void addNewLic(License license) throws Exception {
        LicInfo newLic = new LicInfo();
        newLic.setLicid(license.getLicId());
        newLic.setIssuer(license.getIssuer());
        newLic.setCustomerid(license.getOwner());
        newLic.setLicinfo(license.getLicInfo());
        newLic.setLictype(license.getLicType());
        newLic.setLicstate(NumConstant.common_number_1);
        newLic.setLicupdatetime(StrTool.timeSecond());

        licInfoServ.addObj(newLic);

        //更新授权文件到认证服务器
        updateServerLicId(newLic.getLicid());
    }

    /**
     * 解析授权文件
     * 
     * @Date in Apr 27, 2013,4:19:15 PM
     * @param file
     * @return
     */
    private License getLicense() {
        License license = null;
        FileInputStream inputStream = null;
        try {
            long length = licFile.length();
            byte[] bLicData = new byte[(int) length];
            inputStream = new FileInputStream(licFile);
            int len = inputStream.read(bLicData);
            inputStream.close();
            license = LicenseDecode.decodeLic(bLicData, len);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return license;
    }

}
