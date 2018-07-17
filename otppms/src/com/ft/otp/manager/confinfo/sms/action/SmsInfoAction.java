/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.sms.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.SmsInfoConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.sms.SendSMS;
import com.ft.otp.manager.confinfo.sms.entity.SmsInfo;
import com.ft.otp.manager.confinfo.sms.service.ISmsInfoServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 短信网关配置业务action
 *
 * @Date in Nov 21, 2012,3:46:22 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class SmsInfoAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -6519931319922540715L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(SmsInfoAction.class);

    private ISmsInfoServ smsInfoServ;
    private SmsInfo smsInfo;
    private String smsNameArr = "";
    
    public String getSmsNameArr() {
		return smsNameArr;
	}

	public void setSmsNameArr(String smsNameArr) {
		this.smsNameArr = smsNameArr;
	}

	public ISmsInfoServ getSmsInfoServ() {
        return smsInfoServ;
    }

    public void setSmsInfoServ(ISmsInfoServ smsInfoServ) {
        this.smsInfoServ = smsInfoServ;
    }

    public SmsInfo getSmsInfo() {
        return smsInfo;
    }

    public void setSmsInfo(SmsInfo smsInfo) {
        this.smsInfo = smsInfo;
    }

    public SmsInfo getSmsInfoEntity() {
        if (!StrTool.objNotNull(smsInfo)) {
            smsInfo = new SmsInfo();
        }
        return smsInfo;
    }

    /**
     * 添加短信网关配置操作
     */
    public String add() {
        try {
            smsInfo.setEnabled(1); // 是否启用短信网关，0否，1是
            smsInfoServ.addObj(smsInfo);

            //重新加载短信网关配置
            SmsInfoConfig.reload();

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_add_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 删除短信网关配置操作
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delIds");
        try {
            if (StrTool.setNotNull(keySet)) {
            	
            	// 日志记录
            	String smsName = "";
            	Iterator<?> iter = keySet.iterator();
                while (iter.hasNext()) {
                    String keyId = (String) iter.next();
                    SmsInfo smsInfo = new SmsInfo();
                    smsInfo.setId(StrTool.parseInt(keyId));
                    smsInfo = (SmsInfo) smsInfoServ.find(smsInfo);
                    smsName += smsInfo.getSmsname() + ",";
                }
            	
            	this.setSmsNameArr(smsName);
            	// 日志记录
                smsInfoServ.delObj(keySet);

                //重新加载短信网关配置
                SmsInfoConfig.reload();

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public String find() {
        String smsid = request.getParameter("smsid");
        SmsInfo sms = getSmsInfoEntity();
        try {
            sms.setId(Integer.parseInt(smsid));
            sms = (SmsInfo) smsInfoServ.find(sms);
            if (!StrTool.objNotNull(sms)) {
                return init();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setSmsInfo(sms);
        return "edit";
    }

    /**
     * 短信网关配置初始化
     */
    public String init() {
        try {
            SmsInfo sms = getSmsInfoEntity();
            PageArgument pageArg = pageArgument(sms);
            pageArg.setPageSize(getPagesize());
            pageArg.setCurPage(getPage());

            List<?> resultList = query(pageArg);
            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询短信网关配置列表
     * @Date in Nov 21, 2012,4:31:13 PM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        List<?> smsList = null;
        try {
            SmsInfo sms = getSmsInfoEntity();
            smsList = smsInfoServ.query(sms, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return smsList;
    }

    /**
     * 获取封装分页信息
     * @Date in Nov 21, 2012,4:14:42 PM
     * @param sms
     * @return
     * @throws Exception
     */
    private PageArgument pageArgument(SmsInfo sms) throws Exception {
        int totalRow = 0;
        totalRow = smsInfoServ.count(sms);
        PageArgument pageArg = getArgument(totalRow);
        return pageArg;
    }

    /**
     * 短信网关配置编辑操作
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(smsInfo)) {
                smsInfoServ.updateObj(smsInfo);

                //重新加载短信网关配置
                SmsInfoConfig.reload();

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
                return init();
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#page()
     */
    public String page() {
        PageArgument pArgument = getArgument(request, 0);
        query(pArgument);

        return SUCCESS;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseActiotyn#view()
     */
    public String view() {
        return null;
    }

    /**
     * 验证是否有相同的配置名称
     * @Date in Nov 22, 2012,9:38:29 AM
     * @return
     */
    public String isExist() {
        SmsInfo sInfo = new SmsInfo();
        try {
            String smsname = MessyCodeCheck.iso885901ForUTF8(smsInfo.getSmsname());
            sInfo.setSmsname(smsname);
            sInfo = (SmsInfo) smsInfoServ.find(sInfo);
            if (StrTool.objNotNull(sInfo)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 启用，禁用短信网关配置
     * @Date in May 3, 2013,4:41:55 PM
     * @return
     */
    public String isEnableSms() {
        String smsid = request.getParameter("smsid");
        String enabled = request.getParameter("enabled");
        String smsname = request.getParameter("smsname");
        try {
            SmsInfo sInfo = new SmsInfo();
            sInfo.setId(StrTool.parseInt(smsid));
            sInfo.setEnabled(StrTool.parseInt(enabled));
            sInfo.setSmsname(smsname);
            smsInfoServ.updateEnabled(sInfo);

            //重新加载短信网关配置
            SmsInfoConfig.reload();

            if (StrTool.strEquals(enabled, StrConstant.common_number_0)) {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_disabled_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_enable_succ_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 测试短信网关
     * @Date in Nov 27, 2012,7:32:25 PM
     * @return
     */
    public String testSendSms() {
        String userName = smsInfo.getAccountattr();
        String passwd = smsInfo.getPasswdattr();
        String mobile = smsInfo.getPhoneattr();
        String message = smsInfo.getMessageattr();
        String paramannex = smsInfo.getParamannex();
        if (!StrTool.strNotNull(userName) || !StrTool.strNotNull(passwd) || !StrTool.strNotNull(mobile)
                || !StrTool.strNotNull(message)) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "sms_vd_send_param_is_not_null"));
            return null;
        }
        //短信网关参数初始化
        SendSMS.init(userName, passwd, mobile, message, paramannex);

        SendSMS.server = smsInfo.getHost();
        SendSMS.user = smsInfo.getUsername();
        SendSMS.password = smsInfo.getPwd();
        SendSMS.phonenumber = smsInfo.getCallPhone();
        SendSMS.message = smsInfo.getMessage();

        String result = null;
        try {
            result = SendSMS.send();
            if (StrTool.strNotNull(result)) {
                outPutOperResult(Constant.alert_succ, result);
            }else {
               // result = "303";
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "sms_vd_host_check"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_test_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

}
