/**
 *Administrator
 */
package com.ft.otp.util.excel;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 * 生成Excel数据表样式设置
 *
 * @Date in May 11, 2011,3:28:04 PM
 *
 * @author TBM
 */
public class ExcelCss {

    /**
     * 设置Excel表头(大标题)的样式
     * @Date in May 11, 2011,3:33:08 PM
     * @return
     * @throws Exception
     */
    public static WritableCellFormat getHeader() throws Exception {
        //定义字体大小
        WritableFont font = new WritableFont(WritableFont.TIMES, 18,
                WritableFont.BOLD);
        //字体颜色
        font.setColour(Colour.BLACK);

        WritableCellFormat format = new WritableCellFormat(font);
        //左右居中
        format.setAlignment(Alignment.CENTRE);
        //上下居中
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        //黑色边框
        format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        //灰色背景
        format.setBackground(Colour.GREY_25_PERCENT);

        return format;
    }

    /**
     * 设置标题栏样式
     * @Date in May 11, 2011,3:37:52 PM
     * @return
     */
    public static WritableCellFormat getTitle() throws Exception {
        //字体大小
        WritableFont font = new WritableFont(WritableFont.TIMES, 14,
                WritableFont.BOLD);
        //字体颜色
        font.setColour(Colour.WHITE);
        WritableCellFormat format = new WritableCellFormat(font);

        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

        //灰色背景
        format.setBackground(Colour.DARK_YELLOW);

        return format;
    }

    /**
     * 设置其他单元格样式
     * @Date in May 11, 2011,3:50:47 PM
     * @return
     */
    public static WritableCellFormat getNormolCell() throws Exception {
        //12号字体,上下左右居中,带黑色边框
        WritableFont font = new WritableFont(WritableFont.TIMES, 12);
        WritableCellFormat format = new WritableCellFormat(font);
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

        return format;
    }

}
