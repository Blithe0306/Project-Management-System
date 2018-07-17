/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action.aide;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.excel.ExcelCss;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * UserExportAction 辅助类
 *
 * @Date in May 14, 2011,11:20:06 AM
 *
 * @author TBM
 */
public class UserExportActionAide {

    /**
     * 获取需要导出的用户数据
     * @Date in May 14, 2011,2:58:36 PM
     * @param userExportServ
     * @param userSel
     * @param uGroupStr
     * @return
     * @throws BaseException
     */
    public List<?> getUsrTknGpInfo(IUserInfoServ userExportServ,
            String userSel, String udOrgunitIdsStr,
            String curLoginUserId ,String curLoginUserRoleMark, String usrAttr, String orgFlag) throws BaseException {
        List<?> list = null;
        UserInfo userInfo = new UserInfo();
        if (StrTool.strEquals(userSel, StrConstant.common_number_1)) {
            userInfo.setBind(NumConstant.common_number_1);//未绑定
        } else if (StrTool.strEquals(userSel, StrConstant.common_number_2)) {
            userInfo.setBind(NumConstant.common_number_2);//已绑定
        }
        if (StrTool.strNotNull(udOrgunitIdsStr) && udOrgunitIdsStr.indexOf(",")!=-1) {
    	 	String dOrgunitidStr=udOrgunitIdsStr.split(",")[0]; //udOrgunitIdsStr  domainId:orgunitId,
    	 //	userInfo.setDomainId(StrTool.parseInt(dOrgunitidStr.split(":")[0])); //设置域id
    	 //	userInfo.setOrgunitId(StrTool.parseInt(dOrgunitidStr.split(":")[1]));
    	 	
    	 	// 定义一个数组，目的：封装用户的机构ID，便于多个用户查询与机构相同的令牌
            int orgunitids[] = new int[udOrgunitIdsStr.split(",").length];
            for (int i = 0; i < udOrgunitIdsStr.split(",").length; i++) {

                // 取出传过来的用户机构ID号放到自定义的数组里
                orgunitids[i] = StrTool.parseInt(udOrgunitIdsStr.split(",")[i].split(":")[1]);
            }
            userInfo.setOrgunitIds(orgunitids);
            userInfo.setDomainId(StrTool.parseInt(dOrgunitidStr.split(":")[0])); //设置域id
        }else{ //空机构 意味着全部机构
        	userInfo.setDomainId(0);//不参与条件匹配
        	userInfo.setOrgunitId(0);//不参与条件匹配
        	
			if(!StrTool.strEquals(StrConstant.SUPER_ADMIN, curLoginUserRoleMark)){ //如果不是超级管理员 
				userInfo.setIsFliterTag(1);//设置当前管理员管理的所有的机构的ids值
	        	userInfo.setCurrentAdminId(curLoginUserId);
			}else{ //是超级管理员
				userInfo.setIsFliterTag(0);//不参与条件匹配
			}
        	
        }
        userInfo.setOrgFlag(StrTool.parseInt(orgFlag));
        // 判断导出用户的字段中是否有令牌
        String[] arr = usrAttr.split(",");
        boolean flag = false;
        List<String> tempList = Arrays.asList(arr);
        if(tempList.contains("20") || tempList.contains("25")){ // "20"指令牌，contains效率,"25"指令牌过期时间
        	// 包含
        	flag = true;
        }else{
        	// 不包含
        	flag = false;
        }
        
        // 包含令牌
        if(flag){
        	list = userExportServ.queryUIUTUG(userInfo); // 查询出带令牌的列表（相同用户名而令牌不同）
        }else{
        	list = userExportServ.queryUTUG(userInfo); // 查询出不带令牌的列表
        }
        return list;
    }

    /**
     * 生成Excel数据文件
     * @Date in May 14, 2011,3:00:22 PM
     * @param list
     */
    public void createExcel(String filePath, List<?> list, String usrAttr,
            UserImportActionAide importActionAide, HttpServletRequest request) throws Exception {
        //新建立一个jxl文件
        OutputStream oStream = new FileOutputStream(filePath);
        //创建Excel工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(oStream);

        String[] arrAttr = usrAttr.split(",");
        String[] usrAttrArr = importActionAide.getUserAttr(usrAttr, request);
        //属性个数
        int len = usrAttrArr.length;

        //创建Excel工作表，创建Sheet
        WritableSheet sheet = workbook.createSheet(Language.getLangStr(request, "user_detailed_information"), 0);

        //合并单元格，放置标题
        sheet.mergeCells(0, 0, len - 1, 1);
        Label header = new Label(0, 0, Language.getLangStr(request, "user_detailed_information"), ExcelCss.getHeader());
        //写入头
        sheet.addCell(header);

        //创建标题列
        Label label = null;
        UserInfo userInfo = null;
        for (int i = 0; i < len; i++) {
            String attrName = usrAttrArr[i];
            label = new Label(i, 2, attrName, ExcelCss.getTitle());
            sheet.addCell(label);
            //设置列宽
            sheet.setColumnView(i, 25);
        }

        //数据逐条写入
        for (int k = 0; k < list.size(); k++) {
            userInfo = (UserInfo) list.get(k);
            for (int i = 0; i < len; i++) {
                String arrtValue = "";
                int attrNum = StrTool.parseInt(arrAttr[i]);
                arrtValue = getUserValue(userInfo, attrNum, request);
                label = new Label(i, 3 + k, arrtValue, ExcelCss.getNormolCell());
                sheet.addCell(label);
            }
        }
         
        //设置行高
        //sheet.setRowView(2, 400);

        //写入数据
        workbook.write();
        //关闭文件
        workbook.close();
        oStream.close();
    }

    private String getUserValue(UserInfo userInfo, int attrNum, HttpServletRequest request) {
        String   value = "";
        //int mark = 0;
        if (attrNum == 1) {
            value = userInfo.getUserId();
        } else if (attrNum == 2) {
            value = userInfo.getPapersNumber();
        } else if (attrNum == 3) {
            value = userInfo.getRealName();
        } else if (attrNum == 4) {
            //对静态密码处理
            value = userInfo.getPwd();
        } else if (attrNum == 5) {
            value = userInfo.getCellPhone();
        } else if (attrNum == 6) {
            value = userInfo.getEmail();
        }else if (attrNum == 7) {
            int type = userInfo.getPapersType();
            if(type==NumConstant.common_number_0){
            	value = Language.getLangStr(request, "user_info_user_num");
            }else{
            	value = Language.getLangStr(request, "user_vd_other");
            }
        }else if (attrNum == 8) {
        	//value=userInfo.getDomainId()+":"+userInfo.getOrgunitId()+",";
        	//if(userInfo.getOrgunitId()==NumConstant.common_number_0){
        		//value="";
        	//}else{
        		//value=userInfo.getOrgunitId()+"";
        	//}
        	value=userInfo.getOrgunitNumber();
        }else if (attrNum == 9) {
            //value = userInfo.getDomainName();
            //if(userInfo.getOrgunitId()!=NumConstant.common_number_0){
            	//value=value+"-->"+userInfo.getOrgunitName();
            //}
        	value=userInfo.getOrgunitName();
        }else if (attrNum == 20) {
            value = userInfo.getToken();
        }else if (attrNum == 25) {
        	if(userInfo.getExpireTime()==NumConstant.common_number_0){
        		value="";
        	}else{
        		value =DateTool.dateToStr(userInfo.getExpireTime(), true);        		
        	}
        } else if (attrNum == 44) {
            value = userInfo.getTel();
        } else if (attrNum == 45) {
            value = userInfo.getAddress();
        } 
        
        return value;
    }
}
