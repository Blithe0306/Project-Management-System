/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action.aide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.csvreader.CsvWriter;
import com.ft.otp.base.entity.MegEntity;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.excel.ExcelCss;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户令牌绑定Action辅助类
 *
 * @Date in May 6, 2011,11:07:16 AM
 *
 * @author TBM
 */
public class UserBindActionAide {

    /**
     * 取得不同绑定关系下用户与令牌数据列表
     * @Date in May 6, 2011,7:19:18 PM
     * @param usrKey
     * @param tknKey
     * @param bindType 绑定方式
     * @param userTokenServ
     * @param isBindCheck 是否做绑定检查，检查用户或令牌是否已经达到绑定上限
     * @param usrMaxBindTkn 一个用户最多可以绑定5个令牌
     * @param tknMaxBindUsr 一个令牌最多可以被20个用户绑定
     * @return
     * @throws Exception
     */
    public Map<String, Object> getUTList(String[] usrArrKey, String tknArrKey[], int bindType,
            IUserTokenServ userTokenServ, int usrMaxBindTkn, int tknMaxBindUsr, ITokenServ tokenServ) throws BaseException {
        Map<String, Object> map = new HashMap<String, Object>();
        switch(bindType){
        	//1.一个用户绑定一支令牌；
        	case 1:
        		map = getUTList_1(bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, usrArrKey, tknArrKey, tokenServ);
        	break;
        	//2.一个用户绑定多支令牌；
        	case 2:
        		map = getUTList_2(bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, usrArrKey, tknArrKey, tokenServ);
        	break;
        	//3.每一个用户对应只绑定一支令牌，多选的用户或令牌放弃绑定；
        	case 3:
        		map = getUTList_3(bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, usrArrKey, tknArrKey, tokenServ);
        	break;
        	//4.每一个用户分别绑定多支令牌，绑定令牌上限不能超过n支；
        	case 4:
        		map = getUTList_4(bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, usrArrKey, tknArrKey, tokenServ);
        	break;
        	//5.多个用户共同绑定一支令牌；
        	case 5:
        		map = getUTList_5(bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, usrArrKey, tknArrKey, tokenServ);
        	break;
        	//6.一个用户绑定一支令牌，条件：每用户最大可绑定令牌数量与每令牌最大可绑定用户数量都"1"；
        	case 6:
        		map = getUTList_6(bindType, userTokenServ, usrMaxBindTkn, tknMaxBindUsr, usrArrKey, tknArrKey, tokenServ);
        	break;
        	
        }
        return map;
    }

    /**
     * 1.一个用户绑定一支令牌
     * @Date in May 6, 2011,10:40:15 PM
     * @param bindType
     * @param userTokenServ
     * @param isBindCheck
     * @param usrMaxBindTkn
     * @param tknMaxBindUsr
     * @param usrArrKey
     * @param tknArrKey
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUTList_1(int bindType, IUserTokenServ userTokenServ, int usrMaxBindTkn,
            int tknMaxBindUsr, String[] usrArrKey, String[] tknArrKey, ITokenServ tokenServ) throws BaseException {
        List<Object> list = new ArrayList<Object>();
        String userId = usrArrKey[0];
        String errStr = "";
        String token = null;
        int usrTNum = 0;
        int usrNum = 0;
        int tknNum = 0;
        int flag = 0;
        int onBindTknNum = 0;

        // 判断用户是否超出最大绑定令牌数据限制
        int usrCount = utBindCount(userTokenServ, userId, null,list);
        onBindTknNum = usrMaxBindTkn - usrCount;
        if (onBindTknNum <= 0) {
            errStr = StrConstant.USR_TKN_ERR_01;
            return getUTMap(usrNum, tknNum, list, errStr);
        }
        List<String> orgList = new ArrayList<String>();
        for (int i = 0; i < tknArrKey.length; i++) {
        	UserToken userToken = new UserToken();
            if (usrTNum >= onBindTknNum) {
                break;
            }
            token = tknArrKey[i];
            
            // 判断当前令牌被绑定次数是否超出最大限制
            int tknCount = utBindCount(userTokenServ, null, token,list);
            if (tknCount >= tknMaxBindUsr) {
                continue;
            }
            if (oneUTBind(userTokenServ, userId, token)) {
            	userToken = getUsrTkn(userId, token, tokenServ);
            	if(userToken.getOneToken() == NumConstant.common_number_1){ // 用户与令牌机构ID相同
            		list.add(userToken);
            		usrNum++;
                    tknNum++;
                    break;
            	}else if(userToken.getOneToken() == NumConstant.common_number_2){ // 域令牌集合
            		orgList.add(token);
            	}else{
            		flag++;
            	}
            }
        }
        
        // 如果用户与令牌机构不同的个数等于令牌的总数，说明用户与所有令牌的机构都不同
        if(flag == tknArrKey.length){
        	errStr = StrConstant.USR_TKN_ERR_07;
        }
        
        // 循环域下的令牌
        // 目的：如果没有与用户组织机构相同的令牌，则用户与域下令牌直接绑定
        if(list.size() == NumConstant.common_number_0 && StrTool.listNotNull(orgList)){
        	for(int t=0; t<orgList.size(); t++){
    			token = orgList.get(t);
    			UserToken userToken = getUsrTkn(userId, token, tokenServ);
        		list.add(userToken);
        		usrNum++;
                tknNum++;
        		break;
    		}
        }
        return getUTMap(usrNum, tknNum, list, errStr);
    }

    /**
     * 2.一个用户绑定多支令牌
     * @Date in May 6, 2011,9:20:56 PM
     * @param bindType
     * @param userTokenServ
     * @param isBindCheck
     * @param usrMaxBindTkn
     * @param tknMaxBindUsr
     * @param usrArrKey
     * @param tknArrKey
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUTList_2(int bindType, IUserTokenServ userTokenServ, int usrMaxBindTkn,
            int tknMaxBindUsr, String[] usrArrKey, String[] tknArrKey, ITokenServ tokenServ) throws BaseException {
        int usrNum = 0;
        int tknNum = 0;
        List<Object> list = new ArrayList<Object>();
        String errStr = "";
        String userId = usrArrKey[0];
        String token = null;
        int usrTNum = 0;
        int onBindTknNum = 0;
        int flag = 0;

        // 判断用户是否超出最大绑定令牌数据限制
        int usrCount = utBindCount(userTokenServ, userId, null,list);
        onBindTknNum = usrMaxBindTkn - usrCount;
        if (onBindTknNum <= 0) {
            errStr = StrConstant.USR_TKN_ERR_01;
            return getUTMap(usrNum, tknNum, list, errStr);
        }
        usrNum = 1;
        List<String> orgList = new ArrayList<String>();
        for (int i = 0; i < tknArrKey.length; i++) {
        	UserToken userToken = new UserToken();
            if (usrTNum >= onBindTknNum) {
                break;
            }
            token = tknArrKey[i];
            
            // 判断当前令牌被绑定次数是否超出最大限制
            int tknCount = utBindCount(userTokenServ, null, token,list);
            if (tknCount >= tknMaxBindUsr) {
                continue;
            }
            if (oneUTBind(userTokenServ, userId, token)) {
            	userToken = getUsrTkn(userId, token, tokenServ);
                if(userToken.getOneToken() == NumConstant.common_number_1){ // 用户与令牌组织机构ID相同
                    tknNum++;
                    usrTNum++;
                    list.add(userToken);
            	}else if(userToken.getOneToken() == NumConstant.common_number_2){  // 域下令牌集合
            		orgList.add(token);
            	}else{
            		flag++;
            	}
            }
        }
        
        // 如果用户与令牌机构不同的个数等于令牌的总数，说明用户与所有令牌的机构都不同
        if(flag == tknArrKey.length){
        	errStr = StrConstant.USR_TKN_ERR_07;
        }
        
        // 循环域下的令牌
        // 目的：如果没有与用户组织机构相同的令牌，则用户与域下令牌直接绑定
    	if(StrTool.listNotNull(orgList)){
    		for(int t=0; t<orgList.size(); t++){
    			
    			// 判断用户是否超出最大绑定令牌数据限制
    			if (usrTNum >= onBindTknNum) {
                    break;
                }
    			token = orgList.get(t);
    			UserToken userToken = getUsrTkn(userId, token, tokenServ);
        		list.add(userToken);
                tknNum++;
                usrTNum++;
    		}
    	}
        return getUTMap(usrNum, tknNum, list, errStr);
    }

    /**
     * 3.每一个用户对应只绑定一支令牌，多选的用户或令牌放弃绑定
     * @Date in May 6, 2011,10:51:51 PM
     * @param bindType
     * @param userTokenServ
     * @param isBindCheck
     * @param usrMaxBindTkn
     * @param tknMaxBindUsr
     * @param usrArrKey
     * @param tknArrKey
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUTList_3(int bindType, IUserTokenServ userTokenServ, int usrMaxBindTkn,
            int tknMaxBindUsr, String[] usrArrKey, String[] tknArrKey, ITokenServ tokenServ) throws BaseException {
        List<Object> list = new ArrayList<Object>();
        String errStr = "";
        int usrNum = 0;
        int tknNum = 0;
        String userId = null;
        String token = null;
        boolean flag = true;
        int userMaxNum = 0;
        int tknMaxNum = 0;
        // 取符合条件的令牌号进行绑定
        int b = 0;
        for (int i = 0; i < usrArrKey.length; i++) {
            userId = usrArrKey[i];
            List<String> orgList = new ArrayList<String>();
            
            // 判断用户是否超出最大绑定令牌数据限制
            int usrCount = utBindCount(userTokenServ, userId, null,list);
            if (usrCount >= usrMaxBindTkn) {
            	userMaxNum++;
                continue;
            }

            for (int k = 0; k < tknArrKey.length; k++) {
                token = tknArrKey[k];
                UserToken userToken = new UserToken();
                
                // 判断当前令牌被绑定次数是否超出最大限制
                int tknCount = utBindCount(userTokenServ, null, token,list);
                if (tknCount >= tknMaxBindUsr) {
                	tknMaxNum++;
                    continue;
                }
                if (oneUTBind(userTokenServ, userId, token)) {
                    userToken = getUsrTkn(userId, token, tokenServ);
                    if(userToken.getOneToken() == NumConstant.common_number_1){ // 用户与令牌组织机构ID相同
                    	usrNum++;
                        tknNum++;
	                    list.add(userToken);
	                    flag = false;
	                    break;
                	}else if(userToken.getOneToken() == NumConstant.common_number_2){  // 域下令牌集合
                		flag = false;
                		orgList.add(token);
                	}
                }
            }
            
            // 如果域下集合不为空且用户与令牌的机构相同集合为空
            // 循环域下的令牌
            // 目的：如果没有与用户组织机构相同的令牌，则用户与域下令牌直接绑定
        	if(list.size()==b && StrTool.listNotNull(orgList)){
        		for(int t=0; t<orgList.size(); t++){
        			token = orgList.get(t);
        			UserToken userToken = getUsrTkn(userId, token, tokenServ);
            		list.add(userToken);
            		usrNum++;
                    tknNum++;
            		break;
        		}
        	}
        	b++;
        }
        
        // 如果用户与令牌机构ID没有相同且域下令牌集合也为空且令牌与用户绑定次数没有超
        // 可以认为用户与令牌的组织机构不同
        if(flag && userMaxNum == 0 && tknMaxNum == 0){
        	errStr = StrConstant.USR_TKN_ERR_07;
        }
        
        return getUTMap(usrNum, tknNum, list, errStr);
    }

    /**
     * 4.每一个用户分别绑定多支令牌，绑定令牌上限不能超过n支
     * @Date in May 6, 2011,10:56:01 PM
     * @param bindType
     * @param userTokenServ
     * @param isBindCheck
     * @param usrMaxBindTkn
     * @param tknMaxBindUsr
     * @param usrArrKey
     * @param tknArrKey
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUTList_4(int bindType, IUserTokenServ userTokenServ, int usrMaxBindTkn,
            int tknMaxBindUsr, String[] usrArrKey, String[] tknArrKey, ITokenServ tokenServ) throws BaseException {
        List<Object> list = new ArrayList<Object>();
        String errStr = "";
        int usrNum = 0;
        int tknNum = 0;
        String userId = null;
        String token = null;
        boolean flag = true;
        int userMaxNum = 0;
        int tknMaxNum = 0;
        int usrLen = usrArrKey.length;
        int tknLen = tknArrKey.length;
        for (int i = 0; i < usrLen; i++) {
            userId = usrArrKey[i];
            
            // 当前用户绑定令牌数
            int usrCount = utBindCount(userTokenServ, userId, null,list);
            int reCount = usrMaxBindTkn - usrCount;
            if (reCount <= 0) {
            	userMaxNum++;
                continue;
            }
            usrNum++;
            int num = 0;
            List<String> orgList = new ArrayList<String>();
            for (int k = 0; k < tknLen; k++) {
            	
            	// 判断用户是否超出最大绑定令牌数据限制
                if (num >= reCount) {
                	userMaxNum++;
                    break;
                }
                token = tknArrKey[k];
                UserToken userToken = new UserToken();
                
                // 判断当前令牌被绑定次数是否超出最大限制   
                int tknCount = utBindCount(userTokenServ, null, token,list);
                
                // 这里的绑定数量应这样计算 数据库中已绑定的数量+本次发生的绑定次数 才等于令牌的当前绑定次数 因为绑定关系并没有全部入库
                if (tknCount >= tknMaxBindUsr) {
                	tknMaxNum++;
                    continue;
                }
                if (oneUTBind(userTokenServ, userId, token)) {
                	userToken = getUsrTkn(userId, token, tokenServ);
                    if(userToken.getOneToken() == NumConstant.common_number_1){ // 用户与令牌组织机构ID相同
                        tknNum++;
                        num++;
                        list.add(userToken);
                        flag = false;
                	}else if(userToken.getOneToken() == NumConstant.common_number_2){  // 域下令牌集合
                		orgList.add(token);
                		flag = false;
                	}
                }
            }
            
            // 循环域下的令牌
            // 目的：如果没有与用户组织机构相同的令牌，则用户与域下令牌直接绑定
            if(StrTool.listNotNull(orgList)){
        		for(int t=0; t<orgList.size(); t++){
        			
        			// 判断用户是否超出最大绑定令牌数据限制
        			if (num >= reCount) {
                        break;
                    }
        			token = orgList.get(t);
        			UserToken userToken = getUsrTkn(userId, token, tokenServ);
            		list.add(userToken);
            		num++;
                    tknNum++;
        		}
        	}
        }
        
        // 如果用户与令牌机构ID没有相同且域下令牌集合也为空且令牌与用户绑定次数没有超
        // 可以认为用户与令牌的组织机构不同
        if(flag && userMaxNum == 0 && tknMaxNum == 0){
        	errStr = StrConstant.USR_TKN_ERR_07;
        }
        
        return getUTMap(usrNum, tknNum, list, errStr);
    }

    /**
     * 5.多个用户共同绑定一支令牌
     * @Date in May 6, 2011,10:58:47 PM
     * @param bindType
     * @param userTokenServ
     * @param isBindCheck
     * @param usrMaxBindTkn
     * @param tknMaxBindUsr
     * @param usrArrKey
     * @param tknArrKey
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUTList_5(int bindType, IUserTokenServ userTokenServ, int usrMaxBindTkn,
            int tknMaxBindUsr, String[] usrArrKey, String[] tknArrKey, ITokenServ tokenServ) throws BaseException {
        List<Object> list = new ArrayList<Object>();
        String errStr = "";
        int usrNum = 0;
        int tknNum = 0;
        String userId = null;
        String token = tknArrKey[0];
        int num = 0;
        int userCount = 0;
        // 令牌当前被绑定次数与最大被用户绑定次数的差值
        int onBindTknNum = 0;

        // 判断当前令牌被绑定次数是否超出最大限制
        int tknCount = utBindCount(userTokenServ, null, token,list);
        onBindTknNum = tknMaxBindUsr - tknCount;
        if (onBindTknNum <= 0) {
            errStr = StrConstant.USR_TKN_ERR_02;
            return getUTMap(usrNum, tknNum, list, errStr);
        }
        tknNum = 1;
        for (int i = 0; i < usrArrKey.length; i++) {
        	UserToken userToken = new UserToken();
            if (num >= onBindTknNum) {
                break;
            }
            userId = usrArrKey[i];
            
            // 判断用户是否超出最大绑定令牌数据限制
            int usrCount = utBindCount(userTokenServ, userId, null,list);
            if (usrCount >= usrMaxBindTkn) {
                continue;
            }
            if (oneUTBind(userTokenServ, userId, token)) {
            	userToken = getUsrTkn(userId, token, tokenServ);
            	
            	// 判断用户与令牌机构ID相同或为域下令牌
            	if(userToken.getOneToken() != NumConstant.common_number_3){
            		list.add(userToken);
                    usrNum++;
                    num++;
            	}else{
            		userCount++;
            	}
            }
        }
        
        // 如果用户与令牌的机构不同的个数等于用户的个数
        // 可以认为用户与令牌的组织机构不同
        if(userCount == usrArrKey.length){
    		errStr = StrConstant.USR_TKN_ERR_07;
    	}
        
        return getUTMap(usrNum, tknNum, list, errStr);
    }
    
    /**
     * 6.一对一，符合配置中
     * @param bindType
     * @param userTokenServ
     * @param isBindCheck
     * @param usrMaxBindTkn
     * @param tknMaxBindUsr
     * @param usrArrKey
     * @param tknArrKey
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUTList_6(int bindType, IUserTokenServ userTokenServ, int usrMaxBindTkn,
            int tknMaxBindUsr, String[] usrArrKey, String[] tknArrKey, ITokenServ tokenServ) throws BaseException {
        List<Object> list = new ArrayList<Object>();
        String errStr = "";

        int usrNum = 0;
        int tknNum = 0;
        String userId = null;
        String token = null;
        List<String> tempTknList = new ArrayList<String>();
        int b = 0;
        
        // 用户
        for (int i = 0; i < usrArrKey.length; i++) {
            userId = usrArrKey[i];
            
            //判断用户是否超出最大绑定令牌数据限制
            int usrCount = utBindCount(userTokenServ, userId, null,list);
            if (usrCount >= usrMaxBindTkn) {
                continue;
            }
            List<String> orgList = new ArrayList<String>(); // 同一域下的集合
        	
        	// 令牌
        	for (int j = 0; j<tknArrKey.length; j++){
        		token = tknArrKey[j];
                UserToken userToken = new UserToken();
                
                // 判断当前令牌被绑定次数是否超出最大限制
                int tknCount = utBindCount(userTokenServ, null, token,list);
                if (tknCount >= tknMaxBindUsr) {
                    continue;
                }
                if (oneUTBind(userTokenServ, userId, token)) {
                    userToken = getUsrTkn(userId, token, tokenServ);
                    if(userToken.getOneToken() == NumConstant.common_number_1){ // 用户与令牌机构ID相同
                    	usrNum++;
                        tknNum++;
	                    list.add(userToken);
	                    break;
                	}else if(userToken.getOneToken() == NumConstant.common_number_2){  //域令牌集合
                		orgList.add(token);
                	}
                } else {
                    tempTknList.add(token);
                }
        	}
        	
        	// 如果域下集合不为空且用户与令牌的机构相同集合为空
        	// 循环域下的令牌
            // 目的：如果没有与用户组织机构相同的令牌，则用户与域下令牌直接绑定
        	if(list.size()==b && StrTool.listNotNull(orgList)){
        		for(int t=0; t<orgList.size(); t++){
        			token = orgList.get(t);
        			UserToken userToken = getUsrTkn(userId, token, tokenServ);
            		list.add(userToken);
            		usrNum++;
                    tknNum++;
            		break;
        		}
        	}
        	b++;
        }

        return getUTMap(usrNum, tknNum, list, errStr);
    }

    /*
     * 解析userId:domainId
     * param userIdAndDomainId
     * retrun 
     */
     public Object[] udId(String userIdAndDomainId){
    	  Object[] results= new Object[NumConstant.common_number_3]; 
    	  String userId=userIdAndDomainId.split(":")[NumConstant.common_number_0];
          String domainId=userIdAndDomainId.split(":")[NumConstant.common_number_1];
          String orgunitId=userIdAndDomainId.split(":")[NumConstant.common_number_2];
          results[0]=userId;
          results[1]=domainId;
          results[2]=orgunitId;
          return  results;
     }
    
    /**
     * 统计用户已绑定令牌、令牌已被用户绑定的次数
     * @Date in May 7, 2011,10:57:16 AM
     * @param userTokenServ
     * @param userId userid:domainId形式
     * @param token
     * @param list已发生的绑定记录 但是还未入库
     * 
     * @return
     * @throws BaseException
     */
    public int utBindCount(IUserTokenServ userTokenServ, String userId, String token,List<?> list) throws BaseException {
    	 int count=0;//绑定次数
        UserToken userToken = new UserToken();
        if(StrTool.strNotNull(userId)){
	        Object[] results=udId(userId);
	        String uId=(String)results[NumConstant.common_number_0];
	        int domainId=StrTool.parseInt((String)results[NumConstant.common_number_1]);
	        userToken.setUserId(uId);
	        userToken.setDomainId(domainId);
        }else{
        	userToken.setUserId(null);
        }
        userToken.setToken(token);
        
        
        //新添加逻辑
        count=userTokenServ.count(userToken);
        //如果统计令牌被绑定的最大次数
        if(StrTool.strNotNull(token)){ //该令牌被绑定了n个用户绑定了
        	Iterator<?> it=list.iterator();
        	while(it.hasNext()){
        		UserToken ut=(UserToken)it.next();
        		if(StrTool.strEquals(token, ut.getToken())){//当前令牌
        			count++;
        		}
        	}
        }
        //统计用户绑定令牌的最大次数  即该用户用已经绑定了n个令牌
        if(StrTool.strNotNull(userId)){
        	Object[] results=udId(userId);
	        String uId=(String)results[NumConstant.common_number_0];
        	Iterator<?> it=list.iterator();
        	while(it.hasNext()){
        		UserToken ut=(UserToken)it.next();
        		String newUserId=ut.getUserId();
        		if(StrTool.strEquals(uId, newUserId)){//当前用户
        			count++;
        		}
        	}
        }
        
        return count;
    }

    /**
     * 选择的用户与令牌虽然未都达到绑定上限，但需要检查是否已经绑定，重复进行绑定。
     * @Date in May 6, 2011,8:44:37 PM
     * @param userTokenServ
     * @param userToken
     * @return
     */
    public boolean oneUTBind(IUserTokenServ userTokenServ, String userId, String token) {
        boolean isBind = false;

        UserToken userToken = new UserToken();
        if (!StrTool.strNotNull(userId) || !StrTool.strNotNull(token)) {
            return isBind;
        }
        Object[] results=udId(userId);
        String uId=(String)results[NumConstant.common_number_0];
        int domainId=StrTool.parseInt((String)results[NumConstant.common_number_1]);
        
        userToken.setUserId(uId);
        userToken.setDomainId(domainId);
        userToken.setToken(token);

        int utCount = 0;
        try {
            utCount = userTokenServ.count(userToken);
            if (utCount <= 0) {
                isBind = true;
            }
        } catch (BaseException ex) {
            isBind = false;
        }

        return isBind;
    }

    /**
     * 返回设置值的用户令牌实体对象
     * @Date in May 7, 2011,11:07:33 AM
     * @param userId
     * @param token
     * @return
     * @throws BaseException 
     */
    private UserToken getUsrTkn(String userId, String token, ITokenServ tokenServ) throws BaseException {
        UserToken userToken = null;
        if (!StrTool.strNotNull(userId) || !StrTool.strNotNull(token)) {
            return userToken;
        }
        Object[] results=udId(userId);
        String uId=(String)results[NumConstant.common_number_0];
        int domainId=StrTool.parseInt((String)results[NumConstant.common_number_1]);
        int orgunitId=StrTool.parseInt((String)results[NumConstant.common_number_2]);
        
        userToken = new UserToken();
        userToken.setUserId(uId);
        userToken.setDomainId(domainId);
        userToken.setOrguserId(orgunitId); // 用户机构ID
        
        // Start 2013-5-4
        
        // 令牌机构ID
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(token);
        tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);
        
        // End 2013-5-4
        if(tokenInfo.getOrgunitid()!=null && !"".equals(tokenInfo.getOrgunitid())){
        	userToken.setOrgunitId(tokenInfo.getOrgunitid()); //令牌机构ID
        }else{
        	userToken.setOrgunitId(0);
        }
        
        userToken.setToken(token);
        userToken.setBindTime(DateTool.dateToInt(new Date()));//设置绑定时间
        
        // 判断令牌机构ID与用户机构ID相同或者令牌为域下令牌且与用户域相同
        if(domainId == tokenInfo.getDomainid()){
	        if(userToken.getOrgunitId()==userToken.getOrguserId()){
	        	userToken.setOneToken(1);
	        }else{
	        	// 令牌机构ID与用户机构ID不同，且令牌机构ID为0
	        	if(userToken.getOrgunitId() == NumConstant.common_number_0){
	        		userToken.setOneToken(2);
	        	}else{
	        		// 令牌机构ID与用户机构ID不同,且令牌机构ID不为0
	        		userToken.setOneToken(3);
	        	}
	        	
	        }
        }
        return userToken;
    }

    /**
     * 返回容纳了用户令牌信息及操作失败信息的Map对象
     * @Date in May 7, 2011,12:27:48 PM
     * @param usrNum
     * @param tknNum
     * @param list
     * @param errList
     * @return
     */
    private Map<String, Object> getUTMap(int usrNum, int tknNum, List<Object> list, String errStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        //过滤出的要进行绑定的用户、令牌个数
        int[] arrNum = new int[2];
        if (StrTool.listNotNull(list)) {
            arrNum[0] = usrNum;
            arrNum[1] = tknNum;
        }

        map.put(StrConstant.USR_TKN_LIST, list);
        map.put(StrConstant.USR_TKN_NUM, arrNum);
        map.put(StrConstant.USR_TKN_ERR, errStr);

        return map;
    }

    /**
     * 返回消息实体
     * @Date in May 7, 2011,2:11:19 PM
     * @param result
     * @param failMsg
     * @param usrNum
     * @param tknNum
     * @return
     */
    public MegEntity getMegEntity(int result, String operMsg, int usrNum, int tknNum) {
        MegEntity megEntity = new MegEntity();
        megEntity.setResult(result);
        megEntity.setOperMsg(operMsg);
        megEntity.setUsrNumber(usrNum);
        megEntity.setTknNumber(tknNum);

        return megEntity;
    }

    /**
     * 根据条件取得用户名数组对象
     * @Date in May 9, 2011,9:42:19 AM
     * @param userBindServ
     * @param usrOperSel
     * @param uInfo
     * @return
     * @throws BaseException
     */
    public String[] getUserArr(IUserInfoServ userBindServ, UserInfo uInfo) throws BaseException {
        String[] userArr = null;
        PageArgument pageArg = new PageArgument();
        List<?> uiList = userBindServ.queryBind(uInfo, pageArg);
        if (!StrTool.listNotNull(uiList)) {
            return null;
        }

        userArr = new String[uiList.size()];
        Iterator<?> iter = uiList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            UserInfo userInfo = (UserInfo) iter.next();
            String userId = userInfo.getUserId();
            int orgunitId = userInfo.getOrgunitId();
            int domainId=userInfo.getDomainId();
            userArr[i] = userId+":"+domainId+":"+orgunitId;
            i++;
        }

        return userArr;
    }

    /**
     * 根据条件取得令牌号数组对象
     * @Date in May 9, 2011,10:13:31 AM
     * @param tokenServ
     * @param tknOperSel
     * @param tInfo
     * @return
     * @throws BaseException
     */
    public String[] getTokenArr(ITokenServ tokenServ, TokenInfo tInfo) throws BaseException {
        String[] tknArr = null;
        PageArgument pageArg = new PageArgument();
        List<?> tknList = tokenServ.queryBC(tInfo, pageArg);
        if (!StrTool.listNotNull(tknList)) {
            return null;
        }

        tknArr = new String[tknList.size()];
        for (int i = 0; i < tknList.size(); i++) {
            TokenInfo tokenInfo = (TokenInfo) tknList.get(i);
            String token = tokenInfo.getToken();
            tknArr[i] = token;
        }

        return tknArr;
    }

    /**
     * 输出绑定结果excel
     * 方法说明
     * @Date in Feb 7, 2012,2:20:06 PM
     * @param filePath
     * @param list
     * @throws Exception
     * @author ZJY
     */
    public void createUserTnkXls(String filePath, String fileName, String usrAttr, List<?> list, String bindType,
            UserImportActionAide aide, HttpServletRequest request) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        //新建立一个jxl文件
        OutputStream oStream = new FileOutputStream(filePath + fileName);
        //创建Excel工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(oStream);
        String[] arrAttr = usrAttr.split(",");
        String[] usrAttrArr = aide.getUserAttr(usrAttr, request);

        //属性个数
        int len = usrAttrArr.length;

        //创建Excel工作表，创建Sheet
        WritableSheet sheet = workbook.createSheet(Language.getLangStr(request, "user_vd_token_templet"), 0);

        //合并单元格，放置标题
        sheet.mergeCells(0, 0, len - 1, 1);
        Label header = new Label(0, 0, Language.getLangStr(request, "user_vd_token_templet"), ExcelCss.getHeader());
        //写入头
        sheet.addCell(header);

        //创建标题列
        Label label = null;
        for (int i = 0; i < len; i++) {
            String attrName = usrAttrArr[i];
            label = new Label(i, 2, attrName, ExcelCss.getTitle());
            sheet.addCell(label);
            //设置列宽
            sheet.setColumnView(i, 25);
        }

        //数据逐条写入, 用户与令牌的关系为一对一或多对一 
        if (StrTool.parseInt(bindType) != NumConstant.common_number_2
                && StrTool.parseInt(bindType) != NumConstant.common_number_4) {
            for (int k = 0; k < list.size(); k++) {
                UserToken userToken = (UserToken) list.get(k);
                for (int j = 0; j < len; j++) {
                    String arrtValue = "";
                    int attrNum = StrTool.parseInt(arrAttr[j]);
                    arrtValue = getUserTnkValue(userToken, attrNum);
                    label = new Label(j, 3 + k, arrtValue, ExcelCss.getNormolCell());
                    sheet.addCell(label);
                }
            }
        } else {
            // 一个用户绑定多个令牌，对用户进行单元格合并
            Map<String, List<String>> userTnkMap = getUserTnkGroupData(list);
            Set<?> keySet = userTnkMap.keySet();
            //初始化时单元格合并的起行号、结束行号
            int start = 3, count = 0, end = 0, loop = 0;
            for (Iterator<?> iter = keySet.iterator(); iter.hasNext();) {
                //用户
                String userId = (String) iter.next();
                //根据用户取出对应的令牌集合
                List<String> resultList = userTnkMap.get(userId);
                if (StrTool.listNotNull(resultList)) {
                    //相同的用户对应的令牌写入excel
                    for (int k = 0; k < resultList.size(); k++) {
                        String token = resultList.get(k);
                        for (int j = 0; j < len; j++) {
                            String arrtValue = "";
                            int attrNum = StrTool.parseInt(arrAttr[j]);
                            if (attrNum == 1) {
                                arrtValue = userId;
                            } if (attrNum == 20) {
                                arrtValue = token;
                            }

                            label = new Label(j, 3 + loop, arrtValue, ExcelCss.getNormolCell());
                            sheet.addCell(label);
                        }
                        loop++;
                    }
                }
                if (count != NumConstant.common_number_0) {
                    start = end + 1;
                }
                end = start + resultList.size() - 1;
                count++;
                sheet.mergeCells(0, start, 0, end);
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

    /**
     * 获取用户、令牌分组数据。用户与令牌为一对多的关系
     * 方法说明
     * @Date in Feb 8, 2012,4:26:59 PM
     * @return
     * @author ZJY
     */
    public Map<String, List<String>> getUserTnkGroupData(List<?> list) {
        //所有list中不重复的用户数据
        Map<String, Object> userNameMap = new LinkedHashMap<String, Object>();
        //存放每个用户对应的多个令牌集合 
        Map<String, List<String>> userTnkMap = new LinkedHashMap<String, List<String>>();
        for (int i = 0; i < list.size(); i++) {
            UserToken userToken = (UserToken) list.get(i);
            List<String> tokenList = new ArrayList<String>();
            if (userNameMap.containsKey(userToken.getUserId())) {
                tokenList = (ArrayList<String>) userTnkMap.get(userToken.getUserId());
                tokenList.add(userToken.getToken());
                userTnkMap.put(userToken.getUserId(), tokenList);
            } else {
                userNameMap.put(userToken.getUserId(), userToken);
                tokenList.add(userToken.getToken());
                userTnkMap.put(userToken.getUserId(), tokenList);
            }
        }

        return userTnkMap;
    }

    public String getUserTnkValue(UserToken userToken, int attrNum) {
        String value = "";
        if (attrNum == 1) {
            value = userToken.getUserId();
        } 
        if (attrNum == 20) {
            value = userToken.getToken();
        }
        return value;
    }

    /**  
     * 生成html网页文件
     * @throws IOException 
     */
    public boolean createUserTnkHtml(String filePath, String fileName, String usrAttr, List<?> list, HttpServletRequest request) throws IOException {
        String header = "<!DOCTYPE html PUBLIC" + " " + "'-//W3C//DTD HTML 4.01 Transitional//EN'" + " "
                + "'http://www.w3.org/TR/html4/loose.dtd'" + ">\n";
        String startHtml = "<html>\n";
        String startHead = "<head>\n";
        String meta = "<" + "meta http-equiv=" + "'Content-Type'" + "content=" + "'text/html; charset=gb2312'" + ">\n";
        String title = "<title>" + Language.getLangStr(request, "user_vd_token_bind") + "</title>\n";
        String endHead = "</head>\n";
        String startBody = "<body>\n";
        String starttable = "<table border=0 width='100%'>\n";
        String tr = "<tr><td>'"+Language.getLangStr(request, "user_username")+"'</td><td>'"+Language.getLangStr(request, "tkn_comm_tknum")+"'</td></tr>\n";

        for (int i = 0; i < list.size(); i++) {
            UserToken userToken = (UserToken) list.get(i);
            tr = tr + "<tr><td>" + userToken.getUserId() + "</td><td>" + userToken.getToken() + "</td></tr>\n";
        }
        String endTable = "</table>\n";
        String endBody = "</body>\n";
        String endHtml = "</html>";
        String content = header + startHtml + startHead + meta + title + endHead + startBody + starttable + tr
                + endTable + endBody + endHtml;
        File htmlFile = new File(filePath);
        if (!htmlFile.exists()) {
            htmlFile.mkdir();
        }
        FileOutputStream output = null;
        try {
            byte[] data = content.getBytes();
            output = new FileOutputStream(filePath + fileName);
            output.write(data, 0, data.length);
        } catch (Exception e) {
            return false;
        } finally {
            if (output != null) {
                output.close();
            }
        }

        return true;
    }

    /**
     * 写入csv，使用类库javacvs写入数据
     * 方法说明
     * @Date in Feb 9, 2012,4:47:40 PM
     * @return
     */
    public boolean createUserTnkCsv(String filepath, String fileName, List<?> list, HttpServletRequest request) {
        boolean flag = false;
        try {
            File htmlFile = new File(filepath);
            if (!htmlFile.exists()) {
                htmlFile.mkdir();
            }
            String csvFilePath = filepath + fileName;
            CsvWriter wr = new CsvWriter(csvFilePath, ',', Charset.forName("gb2312"));

            String[] header = { Language.getLangStr(request, "user_csv_token_bind") };
            wr.writeRecord(header);

            String[] title = { Language.getLangStr(request, "user_username"), Language.getLangStr(request, "tkn_comm_tknum") };
            wr.writeRecord(title);

            for (int i = 0; i < list.size(); i++) {
                UserToken userToken = (UserToken) list.get(i);
                String[] contents = { userToken.getUserId(), userToken.getToken() };
                wr.writeRecord(contents);
            }

            wr.close();
            flag = true;
        } catch (IOException e) {
            flag = false;
        }

        return flag;
    }

}
