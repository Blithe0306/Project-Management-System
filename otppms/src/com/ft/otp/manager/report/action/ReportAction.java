/**
 *Administrator
 */
package com.ft.otp.manager.report.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.report.entity.ReportInfo;
import com.ft.otp.manager.report.entity.ReportMessage;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.service.aide.ExportServiceAide;
import com.ft.otp.manager.report.service.operationreport.IOperationReportServ;
import com.ft.otp.manager.report.service.tokenreport.ITokenReportServ;
import com.ft.otp.manager.report.service.userreport.IUserReportServ;
import com.ft.otp.util.tool.CreateFileUtil;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 报表管理业务处理Action
 *
 * @Date in Jan 26, 2013,10:58:02 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ReportAction extends BaseAction {

    private static final long serialVersionUID = -3604840885597783616L;

    private Logger logger = Logger.getLogger(ReportAction.class);

    // 业务报表接口
    private IOperationReportServ operationReportServ = null;
    // 用户报表接口
    private IUserReportServ userReportServ = null;
    // 令牌报表接口
    private ITokenReportServ tokenReportServ = null;

    //管理员——组织机构 关系服务接口
    private IAdminAndOrgunitServ adminAndOrgunitServ = (IAdminAndOrgunitServ) AppContextMgr
            .getObject("adminAndOrgunitServ");

    // 统计导出帮助类
    private ExportServiceAide exportAide = new ExportServiceAide();

    /**
     * @return the operationReportServ
     */
    public IOperationReportServ getOperationReportServ() {
        return operationReportServ;
    }

    /**
     * @param operationReportServ the operationReportServ to set
     */
    public void setOperationReportServ(IOperationReportServ operationReportServ) {
        this.operationReportServ = operationReportServ;
    }

    /**
     * @return the userReportServ
     */
    public IUserReportServ getUserReportServ() {
        return userReportServ;
    }

    /**
     * @param userReportServ the userReportServ to set
     */
    public void setUserReportServ(IUserReportServ userReportServ) {
        this.userReportServ = userReportServ;
    }

    /**
     * @return the tokenReportServ
     */
    public ITokenReportServ getTokenReportServ() {
        return tokenReportServ;
    }

    /**
     * @param tokenReportServ the tokenReportServ to set
     */
    public void setTokenReportServ(ITokenReportServ tokenReportServ) {
        this.tokenReportServ = tokenReportServ;
    }

    /**
     * 业务报表——认证服务器认证同步量统计处理方法
     * 方法说明
     * @Date in Feb 18, 2013,3:08:54 PM
     * @return
     */
    public String reportAuthCount() {
        try {
            String result = operationReportServ.getReportAuthCountDataXml(getQueryForm(NumConstant.common_number_0),
                    request);
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 业务报表——认证服务器时段认证量统计处理方法
     * 方法说明
     * @Date in Feb 18, 2013,3:10:24 PM
     * @return
     */
    public String reportTimeAuthCount() {
        try {
            String result = operationReportServ.getReportAuthCountDataXmlByTime(
                    getQueryForm(NumConstant.common_number_0), NumConstant.common_number_0, request);
            // 去掉图上显示值
            result = result.replaceAll("showvalues=\"1\"", "showvalues=\"0\"");
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 业务报表——用户门户访问量统计处理方法
     * 方法说明
     * @Date in Feb 18, 2013,3:10:29 PM
     * @return
     */
    public String reportPortalCount() {
        try {
            String result = operationReportServ.getReportAuthCountDataXmlByTime(
                    getQueryForm(NumConstant.common_number_0), NumConstant.common_number_1, request);
            // 去掉图上显示值
            result = result.replaceAll("showvalues=\"1\"", "showvalues=\"0\"");
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 用户报表——数量统计处理方法
     * 方法说明
     * @Date in Jan 29, 2013,6:08:32 PM
     * @return
     */
    public String reportUserCount() {
        try {
            String result = userReportServ.getReportUserCountXml(getQueryForm(NumConstant.common_number_0), request);
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 用户报表——各认证类型的用户数量统计方法
     * 方法说明
     * @Date in Jan 31, 2013,10:46:39 AM
     * @return
     */
    public String reportUserCountByAuthType() {
        try {
            String result = userReportServ.getReportUserCountXmlByAuthType(getQueryForm(NumConstant.common_number_0),
                    request);
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 令牌报表——按状态统计
     * 方法说明
     * @Date in Feb 1, 2013,11:09:12 AM
     * @return
     */
    public String reportTknCountByState() {
        try {
            String result = tokenReportServ.getReportTknCountDataXmlByState(getQueryForm(NumConstant.common_number_0),
                    request);
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 令牌报表——按启用应急口令统计
     * 方法说明
     * @Date in Feb 1, 2013,11:09:32 AM
     * @return
     */
    public String reportTknCountByEmpin() {
        try {
            String result = tokenReportServ.getReportTknCountDataXmlByEmpin(getQueryForm(NumConstant.common_number_0),
                    request);
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 令牌报表——按过期时间统计
     * 方法说明
     * @Date in Feb 1, 2013,11:09:36 AM
     * @return
     */
    public String reportTknCountByExpired() {
        try {
            String result = tokenReportServ.getReportTknCountDataXmlByExpired(
                    getQueryForm(NumConstant.common_number_0), request);
            result = exportAide.replaceExportHandler(result, request);

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 获取查询参数
     * 方法说明：最后获取的组织机构串中：
     *           如果组织机构串中有0则只属于域下的也会查出来 则sql格式为 and (org in('0') || org in('1') or (domain in('1') and org is null)) 
     *           如没有0则sql格式为 and(org in ('1') and (domain in ('') and org is not null ))
     * @Date in Jan 30, 2013,10:37:37 AM
     * @param 0 统计管理的，1首页获取的
     * @return
     */
    public ReportQueryForm getQueryForm(int source) {
        ReportQueryForm reportQueryForm = new ReportQueryForm();
        try {
            // 装入查询条件
            reportQueryForm.setBeginDateStr(request.getParameter("startdate"));
            if (source == NumConstant.common_number_0) { //统计管理相关统计
                reportQueryForm.setEndDateStr(request.getParameter("enddate"));
            } else {//首页相关统计
                reportQueryForm.setEndDateStr(DateTool.dateToStr(DateTool.currentUTC(), true));
            }

            // 获取域和组织机构查询条件
            String domainAndOrgunitIds = request.getParameter("domainAndOrgunitIds");
            reportQueryForm.setDomainAndOrgName(MessyCodeCheck.iso885901ForUTF8(request
                    .getParameter("domainAndOrgName")));

            StringBuilder sbDomains = new StringBuilder();// 域信息
            StringBuilder sbOrgunitids = new StringBuilder(); //组织机构信息

            if (!StrTool.strNotNull(domainAndOrgunitIds)) {
                // 如果查询域和组织机构值为空 超级管理员则赋值所有域和组织机构 普通管理员赋值自己管理的域和组织机构
                Set<String> domainsSet = new HashSet<String>();
                Set<String> orgunitidsSet = new HashSet<String>();

                String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
                String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段

                // 如果不是超级管理员并且不是首页的统计
                if (!"ADMIN".equals(curLoginUserRoleMark) && source != NumConstant.common_number_1) {
                    AdminAndOrgunit adminAndOrgunitQuery = new AdminAndOrgunit();
                    adminAndOrgunitQuery.setAdminId(curLoginUserId);
                    adminAndOrgunitQuery.setDomainId(0);
                    adminAndOrgunitQuery.setOrgunitId(0);

                    // 获取当前管理员的管理的域和组织机构
                    List<?> adminAndOrgunitsList = adminAndOrgunitServ
                            .queryAdminAndOrgunitByAdminId(adminAndOrgunitQuery);
                    for (int i = 0; i < adminAndOrgunitsList.size(); i++) {
                        AdminAndOrgunit adminAndOrgunit = (AdminAndOrgunit) adminAndOrgunitsList.get(i);
                        // 获取域和组织机构 用set装入没有重复
                        domainsSet.add(StrTool.intToString(adminAndOrgunit.getDomainId()));
                        if (StrTool.objNotNull(adminAndOrgunit.getOrgunitId())) {// 如果为0则此条只管理了域
                            orgunitidsSet.add(StrTool.intToString(adminAndOrgunit.getOrgunitId()));
                        } else {// 只管理的是域 组织机构字段是null
                            orgunitidsSet.add(StrConstant.common_number_0);// 会查询只属于域中的
                        }
                    }

                    // 组织域条件
                    if (StrTool.setNotNull(domainsSet)) {
                        String[] arrDomains = domainsSet.toArray(new String[domainsSet.size()]);
                        for (String str : arrDomains) {
                            sbDomains.append(str + ",");
                        }
                    } else {
                        sbDomains.append("0,");//为了不让查询出来
                    }

                    // 组织组织机构条件
                    if (StrTool.setNotNull(orgunitidsSet)) {
                        String[] arrOrgunitids = orgunitidsSet.toArray(new String[orgunitidsSet.size()]);
                        for (String str : arrOrgunitids) {
                            sbOrgunitids.append(str + ",");
                        }
                    } else {
                        sbOrgunitids.append("0,");//如果没有一个组织机构  赋值为0 说明会查询只属于域中的
                    }
                } else {// 是超级管理员
                    // 所有的域 和 组织机构  查询时不加入域和组织机构的过滤条件
                    sbDomains.append("");
                    sbOrgunitids.append("");
                }
            } else {
                // 查询值不为空
                String[] arrDomainAndOrgs = domainAndOrgunitIds.split(",");
                for (String str : arrDomainAndOrgs) {
                    if (str.indexOf(":") != -1) {
                        String[] arrOne = str.split(":");
                        sbDomains.append(arrOne[0] + ",");
                        sbOrgunitids.append(arrOne[1] + ",");
                    }
                }
            }

            // 赋值条件
            if (StrTool.strNotNull(sbDomains.toString())) {
                reportQueryForm.setDomain(sbDomains.substring(0, sbDomains.length() - 1));
            }

            if (StrTool.strNotNull(sbOrgunitids.toString())) {
                if (sbOrgunitids.indexOf("0,") == -1) {
                    // 说明：如果 查询组织机构的条件中有0 则只选择了域或者选择了域和机构 此时条件为 and (org in('0') || org in('1') or (domain in('1') and org is null)) 
                    // 如果组织机构中没有0 则条件为 and(org in ('1') and (domain in ('') and org is not null ))
                    reportQueryForm.setFilterType(NumConstant.common_number_1);
                }
                reportQueryForm.setDeptNames(sbOrgunitids.substring(0, sbOrgunitids.length() - 1));
            }
        } catch (BaseException e) {
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_get_query_error"));
            logger.error(e.getMessage(), e);
        }

        return reportQueryForm;
    }

    /**
     * 首页 用户统计图方法
     * @Date in May 11, 2013,12:13:00 PM
     * @return
     */
    public String userReportHome() {
        try {
            String result = "";
            ReportQueryForm reportQueryForm = getQueryForm(NumConstant.common_number_1);
            // 上一次的统计时间 +1小时 如果大于当前时间则不再统计直接获取 否则统计一次并修改统计数据
            if (ReportMessage.getUserReportTime() + 60 * 60 < DateTool.currentUTC()
                    || !StrTool.objNotNull(ReportMessage.getUserReportData())) {//重新统计
                ReportInfo reportInfo = userReportServ.getUserCountToHome(reportQueryForm);
                //修改统计时间和统计数据
                ReportMessage.setUserReportTime(reportQueryForm.getEndDateLong());
                ReportMessage.setUserReportData(reportInfo);
                result = userReportServ.getUserReportXmlAtHome(reportQueryForm, request, reportInfo);

            } else {
                result = userReportServ.getUserReportXmlAtHome(reportQueryForm, request, ReportMessage
                        .getUserReportData());
            }
            // 首页统计图去掉导出功能
            result = result.replaceAll("exportEnabled=\"1\"", "exportEnabled=\"0\"");

            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 首页 令牌统计图方法
     * @Date in May 11, 2013,12:13:49 PM
     * @return
     */
    public String tokenReportHome() {
        try {
            String result = "";
            ReportQueryForm reportQueryForm = getQueryForm(NumConstant.common_number_1);
            // 上一次的统计时间 +1小时 如果大于当前时间则不再统计直接获取 否则统计一次并修改统计数据
            if (ReportMessage.getTknReportTime() + 60 * 60 < DateTool.currentUTC()
                    || !StrTool.objNotNull(ReportMessage.getTokenReportData())) {//重新统计
                ReportInfo reportInfo = tokenReportServ.getTokenStateReportInfo(reportQueryForm);

                //修改统计时间和统计数据
                ReportMessage.setTknReportTime(reportQueryForm.getEndDateLong());
                ReportMessage.setTokenReportData(reportInfo);
                result = tokenReportServ.getTokenReportXmlAtHome(reportQueryForm, request, reportInfo);

            } else {
                result = tokenReportServ.getTokenReportXmlAtHome(reportQueryForm, request, ReportMessage
                        .getTokenReportData());
            }

            // 首页统计图去掉导出功能
            result = result.replaceAll("exportEnabled=\"1\"", "exportEnabled=\"0\"");
            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, Language.getLangStr(request, "report_statistics_error"));
        }

        return null;
    }

    /**
     * 导出 统计报表
     * 
     * @Date in Aug 9, 2013,5:00:42 PM
     * @return
     */
    public String exportReport() {
        try {
            String exportType = request.getParameter("exportType");
            String csvData = request.getParameter("csvData");
            csvData = MessyCodeCheck.iso885901ForUTF8(csvData);

            // 由于用户认证方式统计的静态密码+OTP 从jsp传过来后+丢失，此时判断并加上
            if (StrTool.strEquals(exportType, "authType")) {
                String pwdOtp = Language.getLangStr(request, "report_user_pwd_otp_count");
                csvData = csvData.replaceAll(pwdOtp.replace("+", " "), pwdOtp);
            }

            String[] arrData = csvData.split("\r\n");

            // 导出路径
            String fileName = "test" + Constant.FILE_XLS;
            String excelPath = getFilePath(Constant.WEB_TEMP_FILE_REPORT, fileName);

            String pngName = request.getParameter("fileName");
            pngName = pngName.substring(pngName.lastIndexOf("/") + 1, pngName.length());
            String pngPath = appPath(Constant.WEB_TEMP_FILE_REPORT, pngName);

            fileName = exportAide.exportReport(arrData, exportType, excelPath, pngPath, request);

            if (StrTool.strNotNull(fileName)) {
                outPutOperResult(Constant.alert_succ, fileName);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        outPutOperResult(Constant.alert_error, null);
        return null;
    }

    /**
     * 导出 生成详细报表
     * 
     * @Date in Aug 9, 2013,5:17:32 PM
     * @return
     */
    public String exportDetailReport() {
        try {
            String exportType = request.getParameter("exportType");
            ReportQueryForm queryForm = getQueryForm(NumConstant.common_number_0);
            // 导出路径
            String fileName = "test" + Constant.FILE_XLS;
            String excelPath = getFilePath(Constant.WEB_TEMP_FILE_REPORT, fileName);

            if (StrTool.strEquals(exportType, "authcount") || StrTool.strEquals(exportType, "timeauth")) {// 认证服务器认证同步量导出  认证服务器时段认证量导出
                fileName = operationReportServ.exportDetailReport(queryForm, exportType, excelPath, request);
            } else if (StrTool.strEquals(exportType, "userCount") || StrTool.strEquals(exportType, "authType")) {// 用户数量 用户认证方式
                fileName = userReportServ.exportDetailReport(queryForm, exportType, excelPath, request);
            } else if (StrTool.strEquals(exportType, "state") || StrTool.strEquals(exportType, "expired")) {// 令牌状态统计  令牌过期时间
                fileName = tokenReportServ.exportDetailReport(queryForm, exportType, excelPath, request);
            }

            if (StrTool.strNotNull(fileName)) {
                outPutOperResult(Constant.alert_succ, fileName);
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        outPutOperResult(Constant.alert_error, null);

        return null;
    }

    /**
     * 下载用户模板文件
     * 
     * @Date in May 11, 2011,4:26:18 PM
     * @return
     * @throws Exception
     */
    public String downLoad() throws Exception {
        String fileName = request.getParameter("fileName");
        if (StrTool.strNotNull(fileName)) {
            fileName = MessyCodeCheck.iso885901ForUTF8(fileName);
        }
        String excelPath = getFilePath(Constant.WEB_TEMP_FILE_REPORT, fileName);
        try {
            exportAide.downLoadFile(fileName, excelPath, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 导出直接下载图片
     * 方法说明
     * @Date in Aug 15, 2013,8:42:19 PM
     * @return
     * @throws Exception
     */
    public String downloadImg() throws Exception {
        String fileName = request.getParameter("fileName");
        String imgName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
        fileName = appPath(Constant.WEB_TEMP_FILE_REPORT, imgName);
        try {
            exportAide.downLoadFile(imgName, fileName, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // 下载完成后删除图片
        CreateFileUtil.deleteFile(fileName);
        return null;
    }

}
