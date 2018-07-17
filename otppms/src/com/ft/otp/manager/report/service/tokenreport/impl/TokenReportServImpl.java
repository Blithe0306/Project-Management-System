/**
 *Administrator
 */
package com.ft.otp.manager.report.service.tokenreport.impl;

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
import com.ft.otp.manager.report.service.tokenreport.ITokenReportServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌报表类功能说明
 *
 * @Date in Jan 30, 2013,1:58:36 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class TokenReportServImpl implements ITokenReportServ {

    private Logger logger = Logger.getLogger(TokenReportServImpl.class);
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
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getReportTknCountDataXml(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportTknCountDataXmlByEmpin(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        ReportInfo reportInfo = (ReportInfo) reportDao.getReportTknCountDataByEmpin(reportQueryForm);
        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption(Language.getLangStr(request, "report_token_subcaption_pin"));
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_token_pay"));
        // 赋值二级组织机构标题
        //elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 1));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_total"), StrTool.longToString(reportInfo
                .getTokenSumCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_enabled_count"), StrTool
                .longToString(reportInfo.getEnableTknCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_enabled_pin_count"), StrTool
                .longToString(reportInfo.getEnableEmpin()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getReportTknCountDataXmlByExpired(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportTknCountDataXmlByExpired(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        ReportInfo reportInfo = (ReportInfo) reportDao.getReportTknCountDataByExpired(reportQueryForm);
        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption(Language.getLangStr(request, "report_token_caption_expire"));
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_token_pay"));

        // 赋值二级组织机构标题
        //elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 1));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_total"), StrTool.longToString(reportInfo
                .getTokenSumCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_week_expire"), StrTool
                .longToString(reportInfo.getWeekExpiredTnkCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_month_expire"), StrTool
                .longToString(reportInfo.getMonthExpiredTnkCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_quarter_expire"), StrTool
                .longToString(reportInfo.getQuarterExpiredTnkCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_more_expire"), StrTool
                .longToString(reportInfo.getMoreExpiredTnkCount()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getReportTknCountDataXmlByState(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportTknCountDataXmlByState(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        ReportInfo reportInfo = (ReportInfo) reportDao.getReportTknCountDataByState(reportQueryForm);
        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption(Language.getLangStr(request, "report_token_caption_state"));
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_token_pay"));

        // 赋值二级组织机构标题
        //elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 1));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_operation_total"), StrTool
                .longToString(reportInfo.getTokenSumCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_bind"), StrTool.longToString(reportInfo
                .getBindCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_enable"), StrTool.longToString(reportInfo
                .getEnableTknCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_enabled_pin_count"), StrTool
                .longToString(reportInfo.getCommonLong1()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_lock"), StrTool.longToString(reportInfo
                .getLockTknCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_lose"), StrTool.longToString(reportInfo
                .getLoseTknCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_invalid"), StrTool.longToString(reportInfo
                .getInvalidTknCount()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getReportTknByExpired(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public List<?> getReportTknByExpired(ReportQueryForm reportQueryForm) throws BaseException {
        return reportDao.getReportTknByExpired(reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getReportBindTknCount(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public Object getReportBindTknCount(ReportQueryForm reportQueryForm) throws BaseException {
        return reportDao.getReportBindTknCount(reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getReportSynTknCountByTime(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public int getReportSynTknCountByTime(ReportQueryForm reportQueryForm) throws BaseException {
        return reportDao.getReportSynTknCountByTime(reportQueryForm);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getTokenReportXmlAtHome(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getTokenReportXmlAtHome(ReportQueryForm reportQueryForm, HttpServletRequest request,
            ReportInfo reportInfo) throws BaseException {
        if (!StrTool.objNotNull(reportInfo)) {
            reportInfo = (ReportInfo) reportDao.getReportTknCountDataByState(reportQueryForm);
        }
        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption("");
        elementRootInfo.setNumberSuffix("");

        // 赋值二级组织机构标题
        elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 3, request));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置查询值
        List<ElementSetInfo> sets = new ArrayList<ElementSetInfo>();
        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_operation_total"), StrTool
                .longToString(reportInfo.getTokenSumCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_bind"), StrTool.longToString(reportInfo
                .getBindCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_enable"), StrTool.longToString(reportInfo
                .getEnableTknCount()), ""));
        //        sets.add(new ElementSetInfo(Language.getLangStr(request, "report_token_enabled_pin_count"), StrTool
        //                .longToString(reportInfo.getCommonLong1()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_lock"), StrTool.longToString(reportInfo
                .getLockTknCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_lose"), StrTool.longToString(reportInfo
                .getLoseTknCount()), ""));
        sets.add(new ElementSetInfo(Language.getLangStr(request, "tkn_comm_invalid"), StrTool.longToString(reportInfo
                .getInvalidTknCount()), ""));
        elementInfo.setElementSets(sets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.IBaseReportServ#exportDetailReport(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String exportDetailReport(ReportQueryForm reportQueryForm, String exportType, String excelPath,
            HttpServletRequest request) {
        List<?> exportData = null;
        ExportReportAide writeAide = null;
        try {
            writeAide = new ExportReportAide();
            String fileName = "";

            if (StrTool.strEquals(exportType, "state")) {

                String title = Language.getLangStr(request, "report_export_state_detail_data");
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

                reportQueryForm.setOperType(1);// 绑定
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_4);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_comm_bind"), currNo,
                        writeAide, request);

                reportQueryForm.setOperType(2);// 启用
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_4);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_comm_enable"), currNo,
                        writeAide, request);

                reportQueryForm.setOperType(6);// 启用应急口令
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_4);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request,
                        "report_token_enabled_pin_count"), currNo, writeAide, request);

                reportQueryForm.setOperType(3);// 锁定
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_4);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_comm_lock"), currNo,
                        writeAide, request);

                reportQueryForm.setOperType(4);// 挂失
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_4);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_comm_lose"), currNo,
                        writeAide, request);

                reportQueryForm.setOperType(5);// 作废
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_4);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_comm_invalid"), currNo,
                        writeAide, request);

            } else if (StrTool.strEquals(exportType, "expired")) {// 令牌过期时间

                String title = Language.getLangStr(request, "report_export_expire_detail_data");
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

                reportQueryForm.setOperType(1);// 一周后过期
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_5);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_token_week_expire"),
                        currNo, writeAide, request);

                reportQueryForm.setOperType(2);// 一个月后过期
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_5);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_token_month_expire"),
                        currNo, writeAide, request);

                reportQueryForm.setOperType(3);// 一个季度后过期
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_5);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_token_quarter_expire"),
                        currNo, writeAide, request);

                reportQueryForm.setOperType(4);// 大于3个月后过期
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_5);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "report_token_more_expire"),
                        currNo, writeAide, request);
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
                TokenInfo tkInfo = (TokenInfo) exportData.get(i);

                // 判断是否已有此令牌
                for (int j = 0; j < rowDatas.size(); j++) {
                    String[] arrRow = rowDatas.get(j);
                    if (StrTool.strEquals(arrRow[2], tkInfo.getToken())) {// 比较令牌号是否一致 如一致只修改
                        isAdd = false;
                        if (StrTool.strNotNull(tkInfo.getUserId())) {
                            if (StrTool.objNotNull(tkInfo.getDomainid())) {// 如domainid 不为null 则是用户
                                arrRow[3] = arrRow[3] + "," + tkInfo.getUserId();
                            } else {// 如domainid 为null 则是管理员
                                arrRow[4] = arrRow[4] + "," + tkInfo.getUserId();
                            }
                            rowDatas.set(j, arrRow);
                        }
                        break;
                    }
                }

                if (isAdd) {// 新添加
                    rowDatas.add(getTitleOrValue(arrDef, currNo, operType, tkInfo, request));
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
     *        业务类型 ，绑定、启用、锁定、挂失、作废
     * @param type type 如果是 int型 则获取标题 否则获取该对象值
     * @return
     */
    private String[] getTitleOrValue(String[] arrDef, int currNo, String operType, Object type,
            HttpServletRequest request) {
        String[] resultArr = new String[arrDef.length];

        TokenInfo tkInfo = null;
        boolean isTitle = false;
        // 如果type 参数类型是 Integer型 则是获取标题
        if (type instanceof Integer) {
            isTitle = true;
        } else {
            tkInfo = (TokenInfo) type;
        }

        if (!isTitle && tkInfo == null) {
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
                        arrName = Language.getLangStr(request, "tkn_comm_tknum");
                    } else {
                        arrName = tkInfo.getToken();
                    }
                    break;
                case 4:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "user_username");
                    } else {
                        if (StrTool.objNotNull(tkInfo.getDomainid())) {
                            arrName = tkInfo.getUserId();
                        }
                    }
                    break;
                case 5:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "admin_info");
                    } else {
                        if (!StrTool.objNotNull(tkInfo.getDomainid())) {
                            arrName = tkInfo.getUserId();
                        }
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
     * @see com.ft.otp.manager.report.service.tokenreport.ITokenReportServ#getTokenStateReportInfo(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public ReportInfo getTokenStateReportInfo(ReportQueryForm reportQueryForm) throws BaseException {
        return (ReportInfo) reportDao.getReportTknCountDataByState(reportQueryForm);
    }
}
