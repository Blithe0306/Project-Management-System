/**
 *Administrator
 */
package com.ft.otp.manager.report.service.userreport.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.report.dao.IReportDao;
import com.ft.otp.manager.report.entity.ReportInfo;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.form.fusioncharts.ElementInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementRootInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementSetInfo;
import com.ft.otp.manager.report.service.aide.ExportReportAide;
import com.ft.otp.manager.report.service.aide.ExportServiceAide;
import com.ft.otp.manager.report.service.aide.ReportServiceAide;
import com.ft.otp.manager.report.service.userreport.IUserReportServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户统计业务实现类
 *
 * @Date in Jan 30, 2013,11:14:27 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class UserReportServImpl implements IUserReportServ {

    private Logger logger = Logger.getLogger(UserReportServImpl.class);
    // 报表业务辅助类
    private ReportServiceAide aide = new ReportServiceAide();
    // 统计导出帮助类
    private ExportServiceAide exportAide = new ExportServiceAide();
    // 报表DAO
    private IReportDao reportDao = null;

    /**
     * @return the reportDao
     */
    public IReportDao getReportDao() {
        return reportDao;
    }

    /**
     * @param reportDao the reportDao to set
     */
    public void setReportDao(IReportDao reportDao) {
        this.reportDao = reportDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.userreport.IUserReportServ#getReportCountXml(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportUserCountXml(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        ReportInfo reportInfo = (ReportInfo) reportDao.getReportUserCountData(reportQueryForm);

        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption(Language.getLangStr(request, "report_user_caption_count"));
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_user_caption_place"));

        // 赋值二级时间标题
        elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 0, request));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_add_count"), StrTool
                .longToString(reportInfo.getCommonLong1()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_bind_count"), StrTool
                .longToString(reportInfo.getCommonLong2()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_lock_count"), StrTool
                .longToString(reportInfo.getCommonLong3()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.userreport.IUserReportServ#getReportUserCountXmlByAuthType(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportUserCountXmlByAuthType(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        ReportInfo reportInfo = (ReportInfo) reportDao.getReportUserCountDataByAuthType(reportQueryForm);
        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption(Language.getLangStr(request, "report_user_authtype_count"));
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_user_caption_place"));

        // 赋值二级组织机构标题
        //elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 1));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_pwd_count"), StrTool
                .longToString(reportInfo.getCommonLong1()), ""));
        sets.add(new ElementSetInfo("OTP", StrTool.longToString(reportInfo.getCommonLong2()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_pwd_otp_count"), StrTool
                .longToString(reportInfo.getCommonLong3()), ""));
        //  sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_no_localauth"), StrTool
        //          .longToString(reportInfo.getCommonLong4()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.userreport.IUserReportServ#getUserReportXmlAtHome(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getUserReportXmlAtHome(ReportQueryForm reportQueryForm, HttpServletRequest request,
            ReportInfo reportInfo) throws BaseException {

        if (!StrTool.objNotNull(reportInfo)) {
            reportInfo = (ReportInfo) reportDao.getReportUserCountToMain(reportQueryForm);
        }

        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption("");
        elementRootInfo.setNumberSuffix("");

        // 赋值二级时间标题
        elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 3, request));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_total"), StrTool.longToString(reportInfo
                .getCommonLong1()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_bind_count"), StrTool
                .longToString(reportInfo.getCommonLong2()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_user_lock_count"), StrTool
                .longToString(reportInfo.getCommonLong3()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.IBaseExportReportServ#exportDetailReport(com.ft.otp.manager.report.form.ReportQueryForm, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    public String exportDetailReport(ReportQueryForm reportQueryForm, String exportType, String excelPath,
            HttpServletRequest request) {
        List<?> exportData = null;
        ExportReportAide writeAide = null;
        String fileName = null;
        try {
            writeAide = new ExportReportAide();
            if (StrTool.strEquals(exportType, "userCount")) { // 用户数量统计
                String title = Language.getLangStr(request, "report_export_user_count_data");
                fileName = exportAide.getExcelFileName(title);
                excelPath = exportAide.getReplacePath(excelPath, fileName);
                writeAide.initExport(excelPath);

                String[] colTitle = new String[] { "1", "2", "3", "4" };
                int currNo = 1;// 当前序号

                List<String[]> headList = new ArrayList<String[]>();
                headList.add(new String[] { title });
                headList.add(new String[] { aide.setSubcation(reportQueryForm, 2, request) });
                headList.add(getTitleOrValue(colTitle, 1, "", 0, request));
                writeAide.addHeader(headList);

                reportQueryForm.setOperType(0);
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_2);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_user_total"), currNo,
                        writeAide, request);

                reportQueryForm.setOperType(2);
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_2);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_export_already"),
                        currNo, writeAide, request);

                //                reportQueryForm.setOperType(3);
                //                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_2);
                //                currNo = addBodyData(exportData, colTitle, "未绑定用户"Language.getLangStr(request, "user_username"), currNo, writeAide);

                reportQueryForm.setOperType(1);
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_2);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_export_locked_user"),
                        currNo, writeAide, request);

            } else if (StrTool.strEquals(exportType, "authType")) {// 用户认证方式
                String title = Language.getLangStr(request, "report_export_user_authtype_data");
                fileName = exportAide.getExcelFileName(title);
                excelPath = exportAide.getReplacePath(excelPath, fileName);
                writeAide.initExport(excelPath);

                String[] colTitle = new String[] { "1", "2", "3", "4", "5" };
                int currNo = 1;// 当前序号

                List<String[]> headList = new ArrayList<String[]>();
                headList.add(new String[] { title });
                headList.add(new String[] { aide.setSubcation(reportQueryForm, 2, request) });
                headList.add(getTitleOrValue(colTitle, 1, "", 0, request));
                writeAide.addHeader(headList);

                reportQueryForm.setOperType(2);
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_3);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_user_pwd_count"),
                        currNo, writeAide, request);

                reportQueryForm.setOperType(0);
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_3);
                currNo = addBodyData(exportData, colTitle, "OTP", currNo, writeAide, request);

                reportQueryForm.setOperType(1);
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_3);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_user_pwd_otp_count"),
                        currNo, writeAide, request);

                //                reportQueryForm.setOperType(3);
                //                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_3);
                //                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_user_no_localauth"), currNo, writeAide);
                //
                //                reportQueryForm.setOperType(-1);
                //                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_3);
                //                currNo = addBodyData(exportData, colTitle, "应急口令", currNo, writeAide);
            }
            writeAide.writeExportTail();

            return fileName;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            writeAide.writeExportTail();
            writeAide = null;
            System.gc();
        }

        return null;
    }

    /**
     * 装入数据
     * 方法说明
     * @Date in Aug 13, 2013,1:21:03 PM
     * @param exportData
     * @param arrDef
     *        定义的导出属性
     * @param operType
     * @param currNo
     * @param writeAide
     * @return
     */
    private int addBodyData(List<?> exportData, String[] arrDef, String operType, int currNo,
            ExportReportAide writeAide, HttpServletRequest request) {
        List<String[]> rowDatas = new ArrayList<String[]>();
        if (StrTool.listNotNull(exportData)) {
            for (int i = 0; i < exportData.size(); i++) {
                boolean isAdd = true; // 将要新添加
                UserInfo uInfo = (UserInfo) exportData.get(i);

                // 判断是否已有此用户
                for (int j = 0; j < rowDatas.size(); j++) {
                    String[] arrRow = rowDatas.get(j);
                    if (StrTool.strEquals(arrRow[3], uInfo.getUserId())) {// 比较用户名是否一致 如一致只修改
                        isAdd = false;
                        if (StrTool.strNotNull(uInfo.getToken())) {
                            arrRow[4] = arrRow[4] + "," + uInfo.getToken();
                            rowDatas.set(j, arrRow);
                        }
                        break;
                    }
                }

                if (isAdd) {// 新添加
                    rowDatas.add(getTitleOrValue(arrDef, currNo, operType, uInfo, request));
                    currNo++;
                } else {
                    continue;
                }
            }

            writeAide.addBody(rowDatas);
            exportData = null;// 赋值为空
        }

        return currNo;
    }

    /**
     * 根据定义的arr 获取 相应的标题或值
     * 方法说明
     * @Date in Aug 13, 2013,10:59:49 AM
     * @param arrDef
     * @param currNo
     *        当前序号 传入的要能直接用
     * @param operType
     *        业务类型 很多
     * @param type type 如果是 int型 则获取标题 否则获取该对象值
     * @return
     */
    private String[] getTitleOrValue(String[] arrDef, int currNo, String operType, Object type,
            HttpServletRequest request) {
        String[] resultArr = new String[arrDef.length];

        UserInfo uInfo = null;
        boolean isTitle = false;
        // 如果type 参数类型是 Integer型 则是获取标题
        if (type instanceof Integer) {
            isTitle = true;
        } else {
            uInfo = (UserInfo) type;
        }

        if (!isTitle && uInfo == null) {
            return resultArr;
        }

        for (int i = 0; i < arrDef.length; i++) {
            String arrName = "";
            int num = StrTool.parseInt(arrDef[i]);
            switch (num) {
                case 1:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "report_export_serial");
                    } else {
                        arrName = String.valueOf(currNo + i);
                    }
                    break;
                case 2:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "report_export_oper_type");
                    } else {
                        arrName = operType;
                    }
                    break;
                case 3:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "org_name");
                    } else {
                        arrName = StrTool.strNotNull(uInfo.getOrgunitName()) ? uInfo.getOrgunitName() : uInfo
                                .getDomainName();
                    }
                    break;
                case 4:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "user_username");
                    } else {
                        arrName = uInfo.getUserId();
                    }
                    break;
                case 5:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "tkn_comm_tknum");
                    } else {
                        arrName = uInfo.getToken();
                    }
                    break;
                default:
                    break;
            }

            resultArr[i] = arrName;
        }

        return resultArr;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.userreport.IUserReportServ#getUserCountReportInfo(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public ReportInfo getUserCountReportInfo(ReportQueryForm reportQueryForm) throws BaseException {
        return (ReportInfo) reportDao.getReportUserCountData(reportQueryForm);
    }
    
    public ReportInfo getUserCountToHome(ReportQueryForm reportQueryForm) throws BaseException {
        return (ReportInfo) reportDao.getReportUserCountToMain(reportQueryForm);
    }
}
