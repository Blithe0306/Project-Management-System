/**
 *Administrator
 */
package com.ft.otp.manager.report.service.aide;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.report.form.ReportQueryForm;
import com.ft.otp.manager.report.form.fusioncharts.ElementDataSetInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementRootInfo;
import com.ft.otp.manager.report.form.fusioncharts.ElementSetInfo;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 报表管理服务帮助类功能说明
 *
 * @Date in Aug 29, 2012,5:36:45 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ReportServiceAide {

    /**
     * 饼图的头部和尾部属性定义
     * 方法说明
     * @Date in Jan 26, 2013,11:26:05 AM
     * @param caption
     * @param document
     * @return
     */
    private Element createPieElement(Document document, ElementRootInfo elementRootInfo) {
        Element rootElem = document.addElement("chart");
        rootElem.addAttribute("caption", elementRootInfo.getCaption());//标题
        rootElem.addAttribute("subCaption", elementRootInfo.getSubCaption());
        rootElem.addAttribute("pieYScale", "45"); //倾斜角度
        rootElem.addAttribute("pieSliceDepth", "10"); //饼图的深度
        rootElem.addAttribute("pieRadius", "130");
        rootElem.addAttribute("smartLineColor", "2E4A89");
        rootElem.addAttribute("animation", "1"); //是否动画显示数据，默认为1(True)
        rootElem.addAttribute("baseFont", "Arial, Helvetica, sans-serif");//Canvas里面的字体
        rootElem.addAttribute("baseFontSize", "12");//Canvas里面的字体大小
        rootElem.addAttribute("baseFontColor", "000000");//Canvas里面的字体颜色
        rootElem.addAttribute("showLegend", "1"); //是否显示系列名，默认为1(True)
        rootElem.addAttribute("numberSuffix", elementRootInfo.getNumberSuffix());//数字显示的后缀
        rootElem.addAttribute("captionPadding", "0"); //间隔标题的距离
        rootElem.addAttribute("showAboutMenuItem", "0"); //右键时不显示About FusionChart
        rootElem.addAttribute("formatNumber", "0");//千位是否需要逗号隔开
        rootElem.addAttribute("formatNumberScale", "0");//数据是否自动转换为k/m格式
        //图标离左、右、上、下的间距
        rootElem.addAttribute("chartLeftMargin", "15");
        rootElem.addAttribute("chartRightMargin", "15");
        rootElem.addAttribute("chartTopMargin", "0");
        rootElem.addAttribute("chartBottomMargin", "30");

        Element stylesElem = rootElem.addElement("styles");//样式集
        Element definitionElem = stylesElem.addElement("definition");//定义样式

        Element style1 = definitionElem.addElement("style");//创建样式
        Element style2 = definitionElem.addElement("style");
        Element style3 = definitionElem.addElement("style");

        style1.addAttribute("name", "CaptionFont");//样式名称CaptionFont
        style1.addAttribute("type", "FONT");
        style1.addAttribute("color", "000000");
        style1.addAttribute("size", "12");

        style2.addAttribute("name", "LabelFont");
        style2.addAttribute("type", "FONT");
        style2.addAttribute("color", "000000");
        style2.addAttribute("bgColor", "FFFFFF");

        style3.addAttribute("name", "ToolTipFont");
        style3.addAttribute("type", "FONT");
        style3.addAttribute("bgColor", "FFFFFF");
        style3.addAttribute("borderColor", "2E4A89");

        Element applicationElem = stylesElem.addElement("application");
        Element apply1 = applicationElem.addElement("apply");
        Element apply2 = applicationElem.addElement("apply");
        Element apply3 = applicationElem.addElement("apply");

        apply1.addAttribute("toObject", "CAPTION");//应用对象CAPTION
        apply1.addAttribute("styles", "CaptionFont");//给应用对象加样式

        apply2.addAttribute("toObject", "DATALABELS");
        apply2.addAttribute("styles", "LabelFont");

        apply3.addAttribute("toObject", "TOOLTIP");
        apply3.addAttribute("styles", "ToolTIpFont");

        return rootElem;
    }

    /**
     * 柱状图和条状图头部尾部定义
     * 方法说明
     * @Date in Jan 26, 2013,11:26:28 AM
     * @param document
     * @param caption
     * @param xAxisName
     * @param yAxisName
     * @return
     */
    private Element createColumnBarElement(Document document, ElementRootInfo elementRootInfo,
            HttpServletRequest request) {
        // 建立一个文档实例
        Element rootElem = document.addElement("chart");
        rootElem.addAttribute("palette", "2");//图表块框框的渐变程度(1-5可供选择)   paletteColors图表块里面的渐变颜色
        rootElem.addAttribute("caption", elementRootInfo.getCaption());//图表标题
        rootElem.addAttribute("subCaption", elementRootInfo.getSubCaption());
        rootElem.addAttribute("animation", "1");//动画
        rootElem.addAttribute("showvalues", "1");//是否显示图表表示的数据
        rootElem.addAttribute("xAxisName", elementRootInfo.getXAxisName());//X轴名称
        rootElem.addAttribute("yAxisName", elementRootInfo.getYAxisName());//Y轴名称
        rootElem.addAttribute("numberSuffix", elementRootInfo.getNumberSuffix());//数字显示的后缀
        rootElem.addAttribute("formatNumber", "0");//千位是否需要逗号隔开
        rootElem.addAttribute("formatNumberScale", "0");//数据是否自动转换为k/m格式
        rootElem.addAttribute("numDivLines", "4");//
        rootElem.addAttribute("useRoundEdges", "1");//对于柱状图是否使用圆角
        rootElem.addAttribute("legendPosition", "BOTTOM");//设置图例说明的位置
        rootElem.addAttribute("showAboutMenuItem", "0");//是否显示自定义菜单
        rootElem.addAttribute("baseFont", "Arial, Helvetica, sans-serif");//Canvas里面的字体
        rootElem.addAttribute("baseFontSize", "12");//Canvas里面的字体大小
        rootElem.addAttribute("baseFontColor", "000000");//Canvas里面的字体颜色
        rootElem.addAttribute("chartRightMargin", "50");//Canvas右边线离外边框的距离
        rootElem.addAttribute("decimalPrecision", "0");//坐标上是整数值
        rootElem.addAttribute("yAxisMaxValue", "5");//解决y轴如果都为0时出小数问题
        //rootElem.addAttribute("yAxisValuesStep", "5");//解决y轴（纵轴）标签隔几个显示

        /**
         * 添加导出相关 begin
         * xml文件注意：
         * jsp方式导出：graph加上如下属性： exportAtClient='0'  exportHandler='FCExporter.jsp'
         * flash方式：exportAtClient='1' exportHandler='fcExporter1'
         */
        rootElem.addAttribute("exportEnabled", "1");//启用
        rootElem.addAttribute("exportAtClient", "0");//1客户端导出 ,0 服务器端导出
        rootElem.addAttribute("exportDialogMessage", Language.getLangStr(request, "report_export_execution"));
        //rootElem.addAttribute("exportFormats", "JPG=导出JPG图片|PNG=导出为PNG图片|PDF=导出为PDF文件");//导出选项添加
        rootElem.addAttribute("exportFormats", "PNG="+Language.getLangStr(request, "report_export_png"));//导出选项添加
        rootElem.addAttribute("exportfilename", "report");//导出文件的名称
        //rootElem.addAttribute("exportHandler", "fcExporter1");////导出的处理器（jsp或者flash）
        // exportHandler 如果是服务器导出图片 http://localhost:8080/sdotpcenter/FCExporter 为web.xml中配置的servlet ip必须和浏览器中输入的ip一致
        // 此处先放一个默认值 js 中动态设置每一个图的此属性
        rootElem.addAttribute("exportHandler", "http://localhost:8080/sdotpcenter/FCExporter");////导出的处理器（jsp或者flash）
        rootElem.addAttribute("useRoundEdges", "1");//
        rootElem.addAttribute("exportAction", "save");//保存还是下载  save 将会生成图片并返回图片路径 download 将会生成并下载图片
        rootElem.addAttribute("showExportDataMenuItem", "0");//
        rootElem.addAttribute("exportDataMenuItemLabel", Language.getLangStr(request, "report_export_copy_data"));//
        /**
         * end
         */

        Element stylesElem = rootElem.addElement("styles");//样式集
        Element definitionElem = stylesElem.addElement("definition");//定义样式

        Element style1 = definitionElem.addElement("style");//创建样式
        Element style2 = definitionElem.addElement("style");

        style1.addAttribute("name", "CaptionFont");//样式名称CaptionFont
        style1.addAttribute("type", "FONT");
        style1.addAttribute("color", "000000");
        style1.addAttribute("size", "16");

        style2.addAttribute("type", "font");
        style2.addAttribute("name", "SubCaptionFont");

        Element applicationElem = stylesElem.addElement("application");//应用对象
        Element apply1 = applicationElem.addElement("apply");
        Element apply2 = applicationElem.addElement("apply");

        apply1.addAttribute("toObject", "CAPTION");//应用对象CAPTION
        apply1.addAttribute("styles", "CaptionFont");//给应用对象加样式

        apply2.addAttribute("toObject", "SubCaption");
        apply2.addAttribute("styles", "SubCaptionFont");

        return rootElem;
    }

    /**
     * 根据传入的数据创建柱状图、线型图document
     * 方法说明
     * @Date in Jan 26, 2013,4:23:48 PM
     * @param elementInfo
     * @return
     */
    public String createBarDocument(ElementInfo elementInfo, HttpServletRequest request) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = createColumnBarElement(document, elementInfo.getElementRootInfo(), request);
        addElement(rootElement, elementInfo);

        return createEncodeXML(document);
    }

    /**
     * 根据传入的数据创建饼状图document
     * 方法说明
     * @Date in Jan 26, 2013,4:23:16 PM
     * @param elementInfo
     * @return
     */
    public String createPieDocument(ElementInfo elementInfo) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = createPieElement(document, elementInfo.getElementRootInfo());
        addElement(rootElement, elementInfo);

        return createEncodeXML(document);
    }

    /**
     * 给rootElement添加数据节点
     * 方法说明
     * @Date in Jan 26, 2013,4:22:50 PM
     * @param rootElement
     * @param elementInfo
     */
    private void addElement(Element rootElement, ElementInfo elementInfo) {
        if (StrTool.objNotNull(elementInfo.getElementSets())) {
            // 单系列图
            for (ElementSetInfo elementSetInfo : elementInfo.getElementSets()) {
                // 添加set元素
                elementAddEle(rootElement, elementSetInfo);
            }
        } else {
            if (StrTool.objNotNull(elementInfo.getCategories()) && StrTool.objNotNull(elementInfo.getDataSets())) {
                // 多系列图
                // 添加categories 节点以及子节点
                Element categoryElement = rootElement.addElement("categories");
                for (String categories : elementInfo.getCategories()) {
                    Element categoryEle = categoryElement.addElement("category");
                    categoryEle.addAttribute("label", categories);
                }

                // 添加dataset节点及子节点
                for (ElementDataSetInfo elementDataSetInfo : elementInfo.getDataSets()) {
                    Element datasetEle = rootElement.addElement("dataset");
                    if (!StrTool.objNotNull(datasetEle.attribute("seriesName"))) {
                        datasetEle.addAttribute("seriesName", elementDataSetInfo.getSeriesName());
                    }
                    if (!StrTool.objNotNull(datasetEle.attribute("color"))) {
                        datasetEle.addAttribute("color", elementDataSetInfo.getColor());
                    }

                    // 添加set节点
                    for (String value : elementDataSetInfo.getSetValues()) {
                        Element setElem = datasetEle.addElement("set");//数据
                        setElem.addAttribute("value", value);//设置在图表中各个名字相对应的值
                    }
                }
            }
        }
    }

    /**
     * Element 添加 setElement
     * 方法说明
     * @Date in Aug 9, 2012,11:29:58 AM
     */
    private void elementAddEle(Element ele, ElementSetInfo elementSetInfo) {
        Element setElem = ele.addElement("set");//数据
        setElem.addAttribute("name", elementSetInfo.getName());//设置在图表中横轴标示的名字  也可是label
        setElem.addAttribute("value", elementSetInfo.getValue());//设置在图表中各个名字想对应的值

        if (StrTool.strNotNull(elementSetInfo.getColor())) {
            setElem.addAttribute("color", elementSetInfo.getColor());//设置在图表中相对应的柱行图的颜色
        }
    }

    /**
     * 根据document 获取报表的xml
     * 方法说明
     * @Date in Aug 8, 2012,4:52:15 PM
     * @param document
     * @throws Exception
     */
    private String createEncodeXML(Document document) {
        try {
            byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
            String utf8BomStr = new String(utf8Bom, "UTF-8");//定义BOM标记
            String xml = utf8BomStr + "<?xml version='1.0' encoding='UTF-8'?>\n" + document.getRootElement().asXML()
                    + "\n";
            return xml;
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * 根据报表管理的查询条件获取相应二级标题
     * 方法说明
     * @Date in Feb 1, 2013,10:46:09 AM
     * @param reportQueryForm
     * @param state 0：获取时间段标题；1：获取组织机构标题；2：获取时间段+组织机构标题
     * @return
     */
    public String setSubcation(ReportQueryForm reportQueryForm, int state, HttpServletRequest request) {
        StringBuilder subCaption = new StringBuilder();
        if (state == 1 || state == 2) {
            // 组织机构标题
            if (StrTool.strNotNull(reportQueryForm.getDomainAndOrgName())) {
                subCaption.append(Language.getLangStr(request, "report_subcation_org"));
                String colon = Language.getLangStr(request, "colon");
                // 英文的多语言有此 &nbsp;
                subCaption.append(colon.replaceAll("&nbsp;", " "));
                subCaption.append(reportQueryForm.getDomainAndOrgName());
            } else {
                subCaption.append(Language.getLangStr(request, "report_subcation_all_org"));
            }
        } else if (state == 0 || state == 2) {
            // 时间段多语言
            subCaption.append(Language.getLangStr(request, "report_time_period"));
            String colon = Language.getLangStr(request, "colon");
            // 英文的多语言有此 &nbsp;
            subCaption.append(colon.replaceAll("&nbsp;", " "));
            // 时间段标题
            if (StrTool.strNotNull(reportQueryForm.getBeginDateStr())
                    && StrTool.strNotNull(reportQueryForm.getEndDateStr())) {
                subCaption.append(reportQueryForm.getBeginDateStr());
                subCaption.append(Language.getLangStr(request, "report_subcation_end"));
                subCaption.append(reportQueryForm.getEndDateStr());
            } else if (StrTool.strNotNull(reportQueryForm.getBeginDateStr())
                    && !StrTool.strNotNull(reportQueryForm.getEndDateStr())) {
                subCaption.append(reportQueryForm.getBeginDateStr());
                subCaption.append(Language.getLangStr(request, "report_subcation_end_now"));
            } else if (!StrTool.strNotNull(reportQueryForm.getBeginDateStr())
                    && StrTool.strNotNull(reportQueryForm.getEndDateStr())) {
                subCaption.append(Language.getLangStr(request, "report_subcation_begin_end"));
                subCaption.append(reportQueryForm.getEndDateStr());
            } else {
                subCaption.append(Language.getLangStr(request, "report_subcation_all_time"));
            }
        } else if (state == 3) {// 首页统计图标题
            subCaption.append(Language.getLangStr(request, "report_subcation_end_time"));
            subCaption.append(reportQueryForm.getEndDateStr());
        }

        return subCaption.toString();
    }

    /**
     * 获取各小时段查询条件
     * 方法说明
     * @Date in Feb 19, 2013,1:34:53 PM
     * @param i
     * @param reportQueryForm
     * @param calBegin
     * @param calEnd
     * @return
     */
    public ReportQueryForm getQueryFormByHour(int i, ReportQueryForm reportQueryForm, Calendar calBegin, Calendar calEnd) {
        long beginDate = reportQueryForm.getBeginDateLong();
        long endDate = reportQueryForm.getEndDateLong();

        ReportQueryForm tempForm = new ReportQueryForm();
        tempForm.setDeptNames(reportQueryForm.getDeptNames());
        tempForm.setDomain(reportQueryForm.getDomain());

        if (calBegin.get(Calendar.DAY_OF_MONTH) == calEnd.get(Calendar.DAY_OF_MONTH)
                && calBegin.get(Calendar.HOUR_OF_DAY) == calEnd.get(Calendar.HOUR_OF_DAY)) {
            // 如果天、小时一致则结束时间确定  0分0秒-结束分秒
            calBegin.set(Calendar.MINUTE, 0);
            calBegin.set(Calendar.SECOND, 0);
            calBegin.set(Calendar.MILLISECOND, 0);
            tempForm.setBeginDateLong(DateTool.dateToInt(calBegin.getTime()));
            tempForm.setEndDateLong(endDate);
        } else if (i == NumConstant.common_number_0) {
            // 如果i等于0开始时间一定  开始分秒-59分59秒
            tempForm.setBeginDateLong(beginDate);
            calBegin.set(Calendar.MINUTE, 59);
            calBegin.set(Calendar.SECOND, 59);
            calBegin.set(Calendar.MILLISECOND, 999);
            tempForm.setEndDateLong(DateTool.dateToInt(calBegin.getTime()));
        } else {
            // 统计整个小时的 0分0秒-59分59秒
            calBegin.set(Calendar.MINUTE, 0);
            calBegin.set(Calendar.SECOND, 0);
            calBegin.set(Calendar.MILLISECOND, 0);
            tempForm.setBeginDateLong(DateTool.dateToInt(calBegin.getTime()));

            Calendar tempCal = calBegin;
            tempCal.set(Calendar.MINUTE, 59);
            tempCal.set(Calendar.SECOND, 59);
            tempCal.set(Calendar.MILLISECOND, 999);
            tempForm.setEndDateLong(DateTool.dateToInt(tempCal.getTime()));
        }

        return tempForm;
    }

    /**
     * 获取各天时段查询条件
     * 方法说明
     * @Date in Feb 19, 2013,1:34:53 PM
     * @param i
     * @param reportQueryForm
     * @param calBegin
     * @param calEnd
     * @return
     */
    public ReportQueryForm getQueryFormByDay(int i, ReportQueryForm reportQueryForm, Calendar calBegin, Calendar calEnd) {
        long beginDate = reportQueryForm.getBeginDateLong();
        long endDate = reportQueryForm.getEndDateLong();

        ReportQueryForm tempForm = new ReportQueryForm();
        tempForm.setDeptNames(reportQueryForm.getDeptNames());
        tempForm.setDomain(reportQueryForm.getDomain());

        if (calBegin.get(Calendar.MONTH) == calEnd.get(Calendar.MONTH)
                && calBegin.get(Calendar.DAY_OF_MONTH) == calEnd.get(Calendar.DAY_OF_MONTH)) {
            // 如果月、天一致则结束时间确定  0时0分0秒-结束分秒
            calBegin.set(Calendar.HOUR_OF_DAY, 0);
            calBegin.set(Calendar.MINUTE, 0);
            calBegin.set(Calendar.SECOND, 0);
            calBegin.set(Calendar.MILLISECOND, 0);
            tempForm.setBeginDateLong(DateTool.dateToInt(calBegin.getTime()));
            tempForm.setEndDateLong(endDate);
        } else if (i == NumConstant.common_number_0) {
            // 如果i等于0开始时间一定  开始分秒-23时59分59秒
            tempForm.setBeginDateLong(beginDate);
            calBegin.set(Calendar.HOUR_OF_DAY, 23);
            calBegin.set(Calendar.MINUTE, 59);
            calBegin.set(Calendar.SECOND, 59);
            calBegin.set(Calendar.MILLISECOND, 999);
            tempForm.setEndDateLong(DateTool.dateToInt(calBegin.getTime()));
        } else {
            // 统计整个小时的 0分0秒-59分59秒
            calBegin.set(Calendar.HOUR_OF_DAY, 0);
            calBegin.set(Calendar.MINUTE, 0);
            calBegin.set(Calendar.SECOND, 0);
            calBegin.set(Calendar.MILLISECOND, 0);
            tempForm.setBeginDateLong(DateTool.dateToInt(calBegin.getTime()));

            Calendar tempCal = calBegin;
            tempCal.set(Calendar.HOUR_OF_DAY, 23);
            tempCal.set(Calendar.MINUTE, 59);
            tempCal.set(Calendar.SECOND, 59);
            tempCal.set(Calendar.MILLISECOND, 999);
            tempForm.setEndDateLong(DateTool.dateToInt(tempCal.getTime()));
        }

        return tempForm;
    }

    /**
     * 获取各月时段查询条件
     * 方法说明
     * @Date in Feb 19, 2013,1:34:53 PM
     * @param i
     * @param reportQueryForm
     * @param calBegin
     * @param calEnd
     * @return
     */
    public ReportQueryForm getQueryFormByMonth(int i, ReportQueryForm reportQueryForm, Calendar calBegin,
            Calendar calEnd) {
        long beginDate = reportQueryForm.getBeginDateLong();
        long endDate = reportQueryForm.getEndDateLong();

        ReportQueryForm tempForm = new ReportQueryForm();
        tempForm.setDeptNames(reportQueryForm.getDeptNames());
        tempForm.setDomain(reportQueryForm.getDomain());

        if (calBegin.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR)
                && calBegin.get(Calendar.MONTH) == calEnd.get(Calendar.MONTH)) {
            // 如果年、月一致则结束时间确定  0时0分0秒-结束月日时分秒
            calBegin.set(Calendar.DAY_OF_MONTH, 1);
            calBegin.set(Calendar.HOUR_OF_DAY, 0);
            calBegin.set(Calendar.MINUTE, 0);
            calBegin.set(Calendar.SECOND, 0);
            calBegin.set(Calendar.MILLISECOND, 0);
            tempForm.setBeginDateLong(DateTool.dateToInt(calBegin.getTime()));
            tempForm.setEndDateLong(endDate);
        } else if (i == NumConstant.common_number_0) {
            // 如果i等于0开始时间一定  开始时间-此月最后一天23时59分59秒
            tempForm.setBeginDateLong(beginDate);
            calBegin.set(Calendar.DAY_OF_MONTH, calBegin.getActualMaximum(Calendar.DAY_OF_MONTH));
            calBegin.set(Calendar.HOUR_OF_DAY, 23);
            calBegin.set(Calendar.MINUTE, 59);
            calBegin.set(Calendar.SECOND, 59);
            calBegin.set(Calendar.MILLISECOND, 999);
            tempForm.setEndDateLong(DateTool.dateToInt(calBegin.getTime()));
        } else {
            // 统计整个月的 1日0时0分0秒-最后一天23点59分59秒
            calBegin.set(Calendar.DAY_OF_MONTH, 1);
            calBegin.set(Calendar.HOUR_OF_DAY, 0);
            calBegin.set(Calendar.MINUTE, 0);
            calBegin.set(Calendar.SECOND, 0);
            calBegin.set(Calendar.MILLISECOND, 0);
            tempForm.setBeginDateLong(DateTool.dateToInt(calBegin.getTime()));

            Calendar tempCal = calBegin;
            tempCal.set(Calendar.DAY_OF_MONTH, tempCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            tempCal.set(Calendar.HOUR_OF_DAY, 23);
            tempCal.set(Calendar.MINUTE, 59);
            tempCal.set(Calendar.SECOND, 59);
            tempCal.set(Calendar.MILLISECOND, 999);
            tempForm.setEndDateLong(DateTool.dateToInt(tempCal.getTime()));
        }

        return tempForm;
    }
}
