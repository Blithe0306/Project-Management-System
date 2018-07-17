/**
 *Administrator
 */
package com.ft.otp.manager.report.service.aide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import com.ft.otp.util.excel.ExcelCss;
import com.ft.otp.util.tool.CreateFileUtil;

/**
 * 预定义导出Excel帮助类  (预定义报表的明细导出)
 *
 * @Date in Aug 12, 2013,8:47:16 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ExportReportAide {

    private Logger logger = Logger.getLogger(ExportReportAide.class);
    protected String excelFilePath = "";// 生成excel路径
    protected OutputStream oStream = null;
    protected WritableWorkbook workbook = null;// 工作薄
    protected WritableSheet sheet = null;// sheet
    protected int currRow = 0;// 当前插入行  直接就能使用的开始行

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.IBaseExportReportServ#initExport(java.lang.String)
     */
    public boolean initExport(String excelPath) {
        try {
            excelPath = CreateFileUtil.correctPath(excelPath);
            File file = new File(excelPath);

            //判断目标文件所在的目录是否存在
            if (!file.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                file.getParentFile().mkdirs();
            }

            // 如果已存在file则删除
            if (CreateFileUtil.isFileExists(excelPath)) {
                CreateFileUtil.deleteFile(excelPath);
            } else {
                CreateFileUtil.createFile(excelPath);
            }

            // 新建立一个jxl文件
            oStream = new FileOutputStream(excelPath);
            // 创建Excel工作薄
            workbook = Workbook.createWorkbook(oStream);

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.IBaseExportReportServ#addHeader(java.util.List)
     */
    public boolean addHeader(List<String[]> headList) {
        try {
            // 创建Excel工作表，创建Sheet
            sheet = workbook.createSheet(headList.get(0)[0], 0);
            for (int i = 0; i < headList.size() - 1; i++) {// 最后一行 为 title
                //合并单元格，放置标题
                sheet.mergeCells(0, i, headList.get(headList.size() - 1).length - 1, i);
                Label header = null;
                if (i == 0) {
                    header = new Label(0, i, headList.get(i)[0], ExcelCss.getHeader());// 一级标题
                } else {
                    header = new Label(0, i, headList.get(i)[0], ExcelCss.getTitle());// 二级标题
                }
                //写入头
                sheet.addCell(header);
            }

            // title
            String[] arrTitle = headList.get(headList.size() - 1);
            Label label = null;
            for (int i = 0; i < arrTitle.length; i++) {
                label = new Label(i, headList.size() - 1, arrTitle[i], ExcelCss.getTitle());
                sheet.addCell(label);
                //设置列宽
                sheet.setColumnView(i, 25);
            }

            this.currRow = headList.size();

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.IBaseExportReportServ#addBody(java.util.List)
     */
    public boolean addBody(List<String[]> bodyList) {
        try {
            String[] arrRow = null;
            Label label = null;
            for (int i = 0; i < bodyList.size(); i++) {// 行
                arrRow = bodyList.get(i);
                for (int j = 0; j < arrRow.length; j++) {// 列
                    label = new Label(j, this.currRow + i, arrRow[j], ExcelCss.getNormolCell());
                    sheet.addCell(label);
                }
            }

            this.currRow = currRow + bodyList.size();

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.report.service.IBaseExportReportServ#writeExportTail()
     */
    public boolean writeExportTail() {
        try {
            if (workbook != null) {
                //写入数据
                workbook.write();
                //关闭文件
                workbook.close();
            }

            if (oStream != null) {
                oStream.flush();
                oStream.close();
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    /**
     * 重新写一个sheet
     * 
     * @Date in Aug 30, 2013,3:58:46 PM
     * @return
     */
    public boolean reWriteSheet() {
        try {
            this.currRow = 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;

    }

    /**
     * 重置 该导出Excel帮助对象
     * 
     * @Date in Aug 30, 2013,4:28:28 PM
     * @return
     */
    public boolean reset() {
        try {
            excelFilePath = "";// 生成excel路径
            oStream = null;
            workbook = null;// 工作薄
            sheet = null;// sheet
            currRow = 0;// 当前插入行  直接就能使用的开始行
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
