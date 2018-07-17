/**
 *Administrator
 */
package com.ft.otp.manager.report.service.aide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.ft.otp.common.Constant;
import com.ft.otp.common.language.Language;
import com.ft.otp.util.excel.ExcelCss;
import com.ft.otp.util.tool.CreateFileUtil;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 预定义导出Excel帮助类  (预定义报表的统计数据导出)
 *
 * @Date in Aug 10, 2013,1:25:10 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ExportServiceAide {

    private Logger logger = Logger.getLogger(ExportServiceAide.class);

    /**
     * 导出统计报表
     * 
     * @Date in Aug 10, 2013,1:46:24 PM
     * @param arrData
     *        传入统计图上的csv数据 "总量","0" "成功","0"
     * @param exportType
     *        导出类型
     * @param filePath
     *        统计图图片路径
     * @return 成功返回文件名 失败返回null
     */
    public String exportReport(String[] arrData, String exportType, String excelPath, String pngPath,
            HttpServletRequest request) {
        try {

            List<String[]> allRowData = new ArrayList<String[]>();

            String[] firstRowTitle = new String[arrData[0].split(",").length];// 每一行的头 title  即 第一列

            String totalTitle = "";
            String typeStr = "";// 各种统计类型的一名称
            if (StrTool.strEquals(exportType, "authcount")) {// 认证服务器认证、同步量统计
                totalTitle = Language.getLangStr(request, "report_operation");
                typeStr = Language.getLangStr(request, "report_export_oper_type");
            } else if (StrTool.strEquals(exportType, "timeauth")) {// 认证服务器时段认证量统计
                totalTitle = Language.getLangStr(request, "report_operation_auth");
                typeStr = Language.getLangStr(request, "report_export_time");
            } else if (StrTool.strEquals(exportType, "otpportal")) {// 用户门户访问量导出
                totalTitle = Language.getLangStr(request, "report_operation_access");
                typeStr = Language.getLangStr(request, "report_export_time");
            } else if (StrTool.strEquals(exportType, "userCount")) {// 用户数量
                totalTitle = Language.getLangStr(request, "report_user_caption_count");
                typeStr = Language.getLangStr(request, "report_export_report_type");
            } else if (StrTool.strEquals(exportType, "authType")) { // 用户认证方式
                totalTitle = Language.getLangStr(request, "report_user_authtype_count");
                typeStr = Language.getLangStr(request, "report_export_report_type");
            } else if (StrTool.strEquals(exportType, "state")) {// 令牌状态统计
                totalTitle = Language.getLangStr(request, "report_token_caption_state");
                typeStr = Language.getLangStr(request, "report_export_report_type");
            } else if (StrTool.strEquals(exportType, "expired")) {// 令牌过期时间统计
                totalTitle = Language.getLangStr(request, "report_token_caption_expire");
                typeStr = Language.getLangStr(request, "report_export_report_type");
            }
            firstRowTitle[0] = totalTitle;
            allRowData.add(firstRowTitle);

            for (int i = 0; i < arrData.length; i++) {
                String oneData = arrData[i];// one data 格式为 "lable","Value" 或"认证","0" 或"lable","总量","成功","失败"
                String[] arrOneData = oneData.split(",");
                if (i == 0) {// 第一行
                    arrOneData[0] = typeStr;
                    if (arrOneData.length == 2) {// 单系列图
                        arrOneData[1] = Language.getLangStr(request, "report_export_num");
                    }
                }
                allRowData.add(arrOneData);
            }

            String fileName = getExcelFileName(totalTitle);
            excelPath = excelPath.replace("test" + Constant.FILE_XLS, fileName);

            // 如果已存在file则删除
            if (CreateFileUtil.isFileExists(excelPath)) {
                CreateFileUtil.deleteFile(excelPath);
            }

            if (CreateFileUtil.createFile(excelPath)) {
                // Excel数据插入
                createExcel(allRowData, totalTitle, excelPath, pngPath);
            }

            return fileName;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 生成Excel数据文件
     * @Date in May 14, 2011,3:00:22 PM
     * @param dataList  excel 数据
     * @param title 标题
     * @param excelPath
     */
    private void createExcel(List<String[]> dataList, String title, String excelPath, String pngPath) throws Exception {
        //新建立一个jxl文件
        OutputStream oStream = new FileOutputStream(excelPath);
        //创建Excel工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(oStream);

        //创建Excel工作表，创建Sheet
        WritableSheet sheet = workbook.createSheet(title, 0);

        //合并单元格，放置标题
        sheet.mergeCells(0, 0, dataList.get(0).length - 1, 0);
        Label header = new Label(0, 0, dataList.get(0)[0], ExcelCss.getHeader());
        //写入头
        sheet.addCell(header);

        //循环各行 各列
        Label label = null;
        for (int i = 1; i < dataList.size(); i++) {// 行
            String[] arrData = dataList.get(i);
            for (int j = 0; j < arrData.length; j++) {//列
                label = new Label(j, i, arrData[j].replace("\"", ""), ExcelCss.getTitle());
                sheet.addCell(label);
                if (i == 1) {// 第一行 是标题
                    //设置列宽
                    sheet.setColumnView(j, 25);
                }
            }
        }

        // 插入图片
        try {
            if (CreateFileUtil.isFileExists(pngPath)) {// 图片存在
                int colNum = 0;
                if (dataList.get(1).length >= 4) {
                    colNum = 4;
                } else if (dataList.get(1).length == 3) {
                    colNum = 6;
                } else if (dataList.get(1).length == 2) {
                    colNum = 8;
                } else {
                    colNum = 10;
                }
                WritableImage image = new WritableImage(0, dataList.size() + 2, colNum, 20, new File(pngPath));////前两位是起始格，后两位是图片占多少个
                //格，并非是位置
                sheet.addImage(image);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        //设置行高
        //sheet.setRowView(2, 400);

        //写入数据
        workbook.write();
        //写入之后才能删除图片
        CreateFileUtil.deleteFile(pngPath);
        //关闭文件
        workbook.close();

        oStream.flush();
        oStream.close();
        System.gc();
    }

    /**
     * 替换路径
     * 方法说明
     * @Date in Aug 13, 2013,10:38:21 AM
     * @param oldPath
     * @param replaceStr
     * @return
     */
    public String getReplacePath(String oldPath, String replaceFileName) {
        String path = oldPath.replace("test" + Constant.FILE_XLS, replaceFileName);
        return path;
    }

    /**
     * 获取文件名称
     * 方法说明
     * @Date in Aug 14, 2013,2:55:26 PM
     * @param fileName
     * @return
     */
    public String getExcelFileName(String fileName) {
        return fileName + DateTool.getCurDate("yyyyMMdd") + Constant.FILE_XLS;
    }

    /**
     * 导出文件下载
     * 
     * @Date in May 11, 2011,4:57:32 PM
     * @param fileName
     * @param filePath
     * @param response
     * @throws Exception
     * @author TBM
     */
    public void downLoadFile(String fileName, String filePath, HttpServletResponse response) throws Exception {
        // filePath是指欲下载的文件的路径。
        File file = new File(filePath);

        // 以流的形式下载文件。
        InputStream iStream = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer = new byte[iStream.available()];
        iStream.read(buffer);
        iStream.close();

        // 清空response
        response.reset();
        // 设置response的Header
        fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", String.valueOf(file.length()));

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    //public void downloadImg(String fileName,String){

    //}

    /**
     * 替换生成图表时的handler
     * 方法说明
     * @Date in Aug 15, 2013,8:10:36 PM
     * @param xmlData
     * @param replaceStr
     * @return
     */
    public String replaceExportHandler(String xmlData, HttpServletRequest request) {
        String replaceStr = request.getParameter("handler");
        if (StrTool.strNotNull(replaceStr)) {
            return xmlData.replace("http://localhost:8080/sdotpcenter/FCExporter", replaceStr);
        }

        return xmlData;
    }
}
