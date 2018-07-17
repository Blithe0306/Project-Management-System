/**
 *Administrator
 */
package com.ft.otp.manager.report.service.operationreport.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.logs.userlog.entity.UserLogInfo;
import com.ft.otp.manager.report.dao.IReportDao;
import com.ft.otp.manager.report.entity.ReportInfo;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.form.fusioncharts.ElementDataSetInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementRootInfo;
import com.ft.otp.manager.report.service.aide.ExportReportAide;
import com.ft.otp.manager.report.service.aide.ExportServiceAide;
import com.ft.otp.manager.report.service.aide.ReportServiceAide;
import com.ft.otp.manager.report.service.operationreport.IOperationReportServ;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 业务报表类功能说明
 *
 * @Date in Jan 30, 2013,2:01:02 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class OperationReportServImpl implements IOperationReportServ {

    private Logger logger = Logger.getLogger(OperationReportServImpl.class);
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
     * @see com.ft.otp.manager.report.service.operationreport.IOperationReportServ#getReportAuthCountDataXml(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportAuthCountDataXml(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        ReportInfo reportInfo = (ReportInfo) reportDao.getReportAuthCountData(reportQueryForm);
        ElementInfo elementInfo = new ElementInfo();

        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        elementRootInfo.setCaption(Language.getLangStr(request, "report_operation"));
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_operation_time"));

        // 赋值二级组织机构标题
        elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, 2, request));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置多系列名称
        List<String> categories = new ArrayList<String>(); // 种类名称 即 x轴上的名称
        categories.add(Language.getLangStr(request, "tkn_auth"));
        categories.add(Language.getLangStr(request, "tkn_synch"));

        // 设置多系列值
        List<ElementDataSetInfo> dataSets = new ArrayList<ElementDataSetInfo>();
        ElementDataSetInfo elementDataSetInfo1 = new ElementDataSetInfo();
        elementDataSetInfo1.setSeriesName(Language.getLangStr(request, "report_operation_total"));
        List<String> dataset1 = new ArrayList<String>();
        dataset1.add(String.valueOf(reportInfo.getAuthSumCount()));
        dataset1.add(String.valueOf(reportInfo.getSyncSumCount()));
        elementDataSetInfo1.setSetValues(dataset1);
        dataSets.add(elementDataSetInfo1);

        ElementDataSetInfo elementDataSetInfo2 = new ElementDataSetInfo();
        elementDataSetInfo2.setSeriesName(Language.getLangStr(request, "report_operation_success"));
        List<String> dataset2 = new ArrayList<String>();
        dataset2.add(String.valueOf(reportInfo.getAuthSuccessCount()));
        dataset2.add(String.valueOf(reportInfo.getSyncSuccessCount()));
        elementDataSetInfo2.setSetValues(dataset2);
        dataSets.add(elementDataSetInfo2);

        ElementDataSetInfo elementDataSetInfo3 = new ElementDataSetInfo();
        elementDataSetInfo3.setSeriesName(Language.getLangStr(request, "report_operation_failed"));
        List<String> dataset3 = new ArrayList<String>();
        dataset3.add(String.valueOf(reportInfo.getAuthFaildCount()));
        dataset3.add(String.valueOf(reportInfo.getSyncFaildCount()));
        elementDataSetInfo3.setSetValues(dataset3);
        dataSets.add(elementDataSetInfo3);

        elementInfo.setCategories(categories);
        elementInfo.setDataSets(dataSets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.operationreport.IOperationReportServ#getReportAuthCountDataXmlByTime(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportAuthCountDataXmlByTime(ReportQueryForm reportQueryForm, int mark, HttpServletRequest request)
            throws BaseException {
        ElementInfo elementInfo = new ElementInfo();
        // 设置根节点各属性值
        ElementRootInfo elementRootInfo = new ElementRootInfo();
        if (mark == NumConstant.common_number_0) {
            elementRootInfo.setCaption(Language.getLangStr(request, "report_operation_auth"));
        } else {
            elementRootInfo.setCaption(Language.getLangStr(request, "report_operation_access"));
        }
        elementRootInfo.setNumberSuffix(Language.getLangStr(request, "report_operation_time"));

        // 赋值二级时间标题
        elementRootInfo.setSubCaption(aide.setSubcation(reportQueryForm, NumConstant.common_number_0, request));
        elementInfo.setElementRootInfo(elementRootInfo);

        // 设置多系列名称
        List<String> categories = new ArrayList<String>(); // 种类名称 即 x轴上的名称

        // 设置多系列值
        List<ElementDataSetInfo> dataSets = new ArrayList<ElementDataSetInfo>();
        ElementDataSetInfo elementDataSetInfo1 = new ElementDataSetInfo();
        elementDataSetInfo1.setSeriesName(Language.getLangStr(request, "report_operation_total"));
        List<String> dataset1 = new ArrayList<String>();
        ElementDataSetInfo elementDataSetInfo2 = new ElementDataSetInfo();
        elementDataSetInfo2.setSeriesName(Language.getLangStr(request, "report_operation_success"));
        List<String> dataset2 = new ArrayList<String>();
        ElementDataSetInfo elementDataSetInfo3 = new ElementDataSetInfo();
        elementDataSetInfo3.setSeriesName(Language.getLangStr(request, "report_operation_failed"));
        List<String> dataset3 = new ArrayList<String>();

        long beginDate = reportQueryForm.getBeginDateLong();
        long endDate = reportQueryForm.getEndDateLong();
        long misTime = endDate - beginDate;

        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(DateTool.strToDateFull(reportQueryForm.getBeginDateStr()));
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(DateTool.strToDateFull(reportQueryForm.getEndDateStr()));
        if (misTime / (60 * 60) >= 0 && misTime / (60 * 60) < 24) {
            // 时间段小于24小时
            for (int i = 0; i < 24; i++) {
                // 大于结束时间并且小时不相等时跳出循环
                if (DateTool.dateToInt(calBegin.getTime()) > endDate
                        && calBegin.get(Calendar.HOUR_OF_DAY) != calEnd.get(Calendar.HOUR_OF_DAY)) {
                    break;
                } else {
                    categories.add(calBegin.get(Calendar.DAY_OF_MONTH)
                            + Language.getLangStr(request, "report_operation_day") + calBegin.get(Calendar.HOUR_OF_DAY)
                            + Language.getLangStr(request, "report_operation_hour"));

                    // 临时查询条件
                    ReportQueryForm tempForm = aide.getQueryFormByHour(i, reportQueryForm, calBegin, calEnd);

                    if (mark == NumConstant.common_number_0) {
                        ReportInfo reportInfo = (ReportInfo) reportDao.getReportAuthCountDataByTime(tempForm);
                        dataset1.add(String.valueOf(reportInfo.getAuthSumCount()));
                        dataset2.add(String.valueOf(reportInfo.getAuthSuccessCount()));
                        dataset3.add(String.valueOf(reportInfo.getAuthFaildCount()));
                    } else {
                        ReportInfo reportInfo = (ReportInfo) reportDao.getReportPortalCountData(tempForm);
                        dataset1.add(String.valueOf(reportInfo.getAccessSumCount()));
                        dataset2.add(String.valueOf(reportInfo.getAccessSuccessCount()));
                        dataset3.add(String.valueOf(reportInfo.getAccessFaildCount()));
                    }

                    calBegin.add(Calendar.HOUR_OF_DAY, 1);
                }
            }
        } else if (misTime / (24 * 60 * 60) >= 0 && misTime / (24 * 60 * 60) < 31) {
            // 大于1天小于31天
            for (int i = 0; i < 31; i++) {
                if (DateTool.dateToInt(calBegin.getTime()) > endDate
                        && calBegin.get(Calendar.DAY_OF_MONTH) != calEnd.get(Calendar.DAY_OF_MONTH)) {
                    break;
                } else {
                    categories.add((calBegin.get(Calendar.MONTH) + 1)
                            + Language.getLangStr(request, "report_operation_month")
                            + calBegin.get(Calendar.DAY_OF_MONTH)
                            + Language.getLangStr(request, "report_operation_day"));

                    // 临时查询条件
                    ReportQueryForm tempForm = aide.getQueryFormByDay(i, reportQueryForm, calBegin, calEnd);

                    if (mark == NumConstant.common_number_0) {
                        ReportInfo reportInfo = (ReportInfo) reportDao.getReportAuthCountDataByTime(tempForm);
                        dataset1.add(String.valueOf(reportInfo.getAuthSumCount()));
                        dataset2.add(String.valueOf(reportInfo.getAuthSuccessCount()));
                        dataset3.add(String.valueOf(reportInfo.getAuthFaildCount()));
                    } else {
                        ReportInfo reportInfo = (ReportInfo) reportDao.getReportPortalCountData(tempForm);
                        dataset1.add(String.valueOf(reportInfo.getAccessSumCount()));
                        dataset2.add(String.valueOf(reportInfo.getAccessSuccessCount()));
                        dataset3.add(String.valueOf(reportInfo.getAccessFaildCount()));
                    }

                    calBegin.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        } else if (misTime / (24 * 60 * 60) >= 31 && misTime / (24 * 60 * 60) < 365) {
            // 大于1个月小于12个月
            for (int i = 0; i < 12; i++) {
                if (DateTool.dateToInt(calBegin.getTime()) > endDate
                        && calBegin.get(Calendar.MONTH) != calEnd.get(Calendar.MONTH)) {
                    break;
                } else {
                    categories.add(calBegin.get(Calendar.YEAR) + Language.getLangStr(request, "report_operation_year")
                            + (calBegin.get(Calendar.MONTH) + 1)
                            + Language.getLangStr(request, "report_operation_month"));

                    // 临时查询条件
                    ReportQueryForm tempForm = aide.getQueryFormByMonth(i, reportQueryForm, calBegin, calEnd);

                    if (mark == NumConstant.common_number_0) {
                        ReportInfo reportInfo = (ReportInfo) reportDao.getReportAuthCountDataByTime(tempForm);
                        dataset1.add(String.valueOf(reportInfo.getAuthSumCount()));
                        dataset2.add(String.valueOf(reportInfo.getAuthSuccessCount()));
                        dataset3.add(String.valueOf(reportInfo.getAuthFaildCount()));
                    } else {
                        ReportInfo reportInfo = (ReportInfo) reportDao.getReportPortalCountData(tempForm);
                        dataset1.add(String.valueOf(reportInfo.getAccessSumCount()));
                        dataset2.add(String.valueOf(reportInfo.getAccessSuccessCount()));
                        dataset3.add(String.valueOf(reportInfo.getAccessFaildCount()));
                    }

                    calBegin.add(Calendar.MONTH, 1);
                }
            }
        } else {
            // 大于等于12个月的  暂时不统计
        }

        elementInfo.setCategories(categories);
        
        // 用户门户只装入成功的值 认证同步装所有的值
        if(mark != NumConstant.common_number_0){
            elementDataSetInfo2.setSetValues(dataset2);
            dataSets.add(elementDataSetInfo2);
        }else{
            elementDataSetInfo1.setSetValues(dataset1);
            dataSets.add(elementDataSetInfo1);
            elementDataSetInfo2.setSetValues(dataset2);
            dataSets.add(elementDataSetInfo2);
            elementDataSetInfo3.setSetValues(dataset3);
            dataSets.add(elementDataSetInfo3);
        }
        
        elementInfo.setDataSets(dataSets);

        return aide.createBarDocument(elementInfo, request);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.operationreport.IOperationReportServ#getReportPortalCountDataXml(com.ft.otp.manager.report.form.ReportQueryForm)
     */
    public String getReportPortalCountDataXml(ReportQueryForm reportQueryForm, HttpServletRequest request)
            throws BaseException {
        return null;
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
            if (StrTool.strEquals(exportType, "authcount")) {

                String title = Language.getLangStr(request, "report_auth_sync_detail_data");
                String fileName = exportAide.getExcelFileName(title);
                excelPath = exportAide.getReplacePath(excelPath, fileName);
                writeAide.initExport(excelPath);

                String[] colTitle = new String[] { "1", "2", "3", "4", "5", "6", "7" };
                int currNo = 1;// 当前序号

                List<String[]> headList = new ArrayList<String[]>();
                headList.add(new String[] { title });
                headList.add(new String[] { aide.setSubcation(reportQueryForm, 2, request) });
                headList.add(getTitleOrValue(colTitle, 1, "", 0, request));
                writeAide.addHeader(headList);

                reportQueryForm.setOperType(1001);// 认证
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_1);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_auth"), currNo, writeAide,
                        request);

                reportQueryForm.setOperType(1002);// 同步
                exportData = reportDao.getExportData(reportQueryForm, NumConstant.common_number_1);
                currNo = addBodyData(exportData, colTitle, Language.getLangStr(request, "tkn_synch"), currNo,
                        writeAide, request);
                writeAide.writeExportTail();

                return fileName;
            }

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
                UserLogInfo tkInfo = (UserLogInfo) exportData.get(i);
                rowDatas.add(getTitleOrValue(arrDef, currNo, operType, tkInfo, request));
                currNo++;
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
     *        业务类型 ，认证 or 同步
     * @param type type 如果是 int型 则获取标题 否则获取该对象值
     * @return
     */
    private String[] getTitleOrValue(String[] arrDef, int currNo, String operType, Object type,
            HttpServletRequest request) {
        String[] resultArr = new String[arrDef.length];

        UserLogInfo uLog = null;
        boolean isTitle = false;
        // 如果type 参数类型是 Integer型 则是获取标题
        if (type instanceof Integer) {
            isTitle = true;
        } else {
            uLog = (UserLogInfo) type;
        }

        if (!isTitle && uLog == null) {
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
                        arrName = Language.getLangStr(request, "user_username");
                    } else {
                        arrName = uLog.getUserid();
                    }
                    break;
                case 4:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "tkn_comm_tknum");
                    } else {
                        arrName = uLog.getToken();
                    }
                    break;
                case 5:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "report_export_time");
                    } else {
                        arrName = DateTool.dateToStr(uLog.getLogtime(), true);
                    }
                    break;
                case 6:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "log_info_auth_ip");
                    } else {
                        arrName = uLog.getClientip();
                    }
                    break;
                case 7:
                    if (isTitle) {
                        arrName = Language.getLangStr(request, "log_info_operate_result");
                    } else {
                        arrName = uLog.getActionresult() == 0 ? Language.getLangStr(request, "common_syntax_failure") : Language
                                .getLangStr(request, "common_syntax_success");
                    }
                    break;
                default:
                    break;
            }

            resultArr[i] = arrName;
        }

        return resultArr;
    }

}
