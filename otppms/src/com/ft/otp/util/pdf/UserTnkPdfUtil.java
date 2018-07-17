/**
 *Administrator
 */
package com.ft.otp.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.util.tool.StrTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 用户、令牌批量绑定输出类、接口等说明
 *
 * @Date in Feb 9, 2012,10:08:39 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class UserTnkPdfUtil {

    public static boolean createUserTnkPdf(String filePath, String fileName, List<?> tknList, String usrAttr,
            String userNum, String tokenNum) {
        try {
            Document doc = new Document(PageSize.A4);
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            OutputStream outs = new FileOutputStream(filePath + fileName);
            PdfWriter.getInstance(doc, outs);
            doc.open();

            //标题字体
            String fonts = filePath.substring(0, filePath.indexOf("temp_file"));
            fonts = fonts + "manager/common/fonts/simfang.ttf";

            BaseFont bfTitle = BaseFont.createFont(fonts, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(bfTitle, 18, Font.NORMAL);

            //内容字体  
            BaseFont bfComic = BaseFont.createFont(fonts, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfComic, 12, Font.NORMAL);

            Paragraph titleP = new Paragraph("用户、令牌绑定" + "-" + "预览" + "\n\n", titleFont);
            titleP.setAlignment(titleP.ALIGN_CENTER);
            doc.add(titleP);

            Paragraph totalP = new Paragraph("此次共绑定用户：" + userNum + "个 " + " 令牌：" + tokenNum + "个" + "\n\n", font);
            totalP.setAlignment(totalP.ALIGN_CENTER);
            doc.add(totalP);

            if (StrTool.strNotNull(usrAttr)) {
                String[] arr = usrAttr.split(",");
                int num1 = 0;
                int num2 = 0;
                int colNum = arr.length;

                float[] widths = new float[colNum];
                for (int j = 0; j < colNum; j++) {
                    int attrNum = StrTool.parseInt(arr[j]);
                    if (attrNum == 1) {
                        num1 = 1;
                        widths[0] = 0.5f;
                    } else if (attrNum == 20) {
                        num2 = 2;
                        widths[1] = 0.5f;
                    }
                }

                //生成2列的表格
                PdfPTable table = new PdfPTable(colNum);
                table.setWidthPercentage(100);
                table.getDefaultCell().setPadding(3); //填充大小
                table.getDefaultCell().setBorderWidth(0); //边框宽度
                table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

                float[] liLen = new float[colNum];
                int t = 0;
                for (int k = 0; k < widths.length; k++) {
                    if (widths[k] != 0.0) {
                        liLen[t] = widths[k];
                        t++;
                    }
                }
                table.setWidths(liLen);
                //写入Title
                if (num1 == 1) {
                    table.addCell(new Paragraph("用户", font));
                }  
                if (num2 == 2) {
                    table.addCell(new Paragraph("令牌号", font));
                }

                //写入数据
                for (int i = 0; i < tknList.size(); i++) {
                    UserToken userToken = (UserToken) tknList.get(i);
                    if (num1 == 1) {
                        table.addCell(new Paragraph(userToken.getUserId(), font));
                    } 
                    if (num2 == 2) {
                        table.addCell(new Paragraph(userToken.getToken(), font));
                    }

                }
                doc.add(table);
            } else {
                return false;
            }
            doc.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
