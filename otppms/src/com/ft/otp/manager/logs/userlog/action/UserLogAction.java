/**
 *Administrator
 */
package com.ft.otp.manager.logs.userlog.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.logs.userlog.entity.UserLogInfo;
import com.ft.otp.manager.logs.userlog.form.UserLogQueryForm;
import com.ft.otp.manager.logs.userlog.service.IUserLogServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户日志实现
 *
 * @Date in May 4, 2011,4:46:39 PM
 *
 * @author ZJY
 */
public class UserLogAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 4982141211254063073L;
    
    //管理员和组织机构关系业务操作
    private IAdminAndOrgunitServ adminAndOrgunitServ = (IAdminAndOrgunitServ) AppContextMgr
            .getObject("adminAndOrgunitServ");
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");
    private Logger logger = Logger.getLogger(UserLogAction.class);
    private IUserLogServ userLogServ;
    private UserLogQueryForm userLogQueryForm;

    /**
     * @return the userLogServ
     */
    public IUserLogServ getUserLogServ() {
        return userLogServ;
    }

    /**
     * @param userLogServ the userLogServ to set
     */
    public void setUserLogServ(IUserLogServ userLogServ) {
        this.userLogServ = userLogServ;
    }

    /**
     * @return the userLogQueryForm
     */
    public UserLogQueryForm getUserLogQueryForm() {
        return userLogQueryForm;
    }

    /**
     * @param userLogQueryForm the userLogQueryForm to set
     */
    public void setUserLogQueryForm(UserLogQueryForm userLogQueryForm) {
        this.userLogQueryForm = userLogQueryForm;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     * @throws BaseException 
     */
    public UserLogInfo getUserLogInfo(UserLogQueryForm userLogQueryForm) throws BaseException {
        UserLogInfo userLogInfo = new UserLogInfo();
        if (StrTool.objNotNull(userLogQueryForm)) {
            userLogInfo = userLogQueryForm.getUserLogInfo();
        }
        
        if(!StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())){	
	    	LinkUser linkUser = getLinkUser();
	    	Map<String, Object> permMap = linkUser.getPermMap();
	    	Object userObject = permMap.get(StrConstant.LOG_USER_ALL_LOG); //查看所有用户日志
	    	
	    	if(userObject == null){
	            String loginUser = getCurLoginUser();
	            AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(loginUser, NumConstant.common_number_0,
	                    NumConstant.common_number_0);
	            
	            // 当前管理员所管理的域
	            List<?> aaoList = adminAndOrgunitServ.queryAdminAndOrgunitByAdminId(adminAndOrgunit);
	            String [] userArr = null;
	            boolean flag = true;
	            
	            // 判断当前管理员是否有管理组织机构
	            if(StrTool.listNotNull(aaoList)){
	            	int [] orgunitArr = new int[aaoList.size()];
	            	
	            	// 封装组织机构ID
	    	        for (int i=0; i<aaoList.size(); i++){
	    	        	AdminAndOrgunit adm = (AdminAndOrgunit)aaoList.get(i);
	    	        	if(adm.getOrgunitId() == 0){
	    	        		flag = false;
	    	        		break;
	    	        	}else{
	    	        		orgunitArr[i] = adm.getOrgunitId();
	    	        	}
	    	        }
	    	        if(flag){
		    	        UserInfo userinfo = new UserInfo();
		    	        userinfo.setOrgunitIds(orgunitArr);
		    	        
		    	        // 查出当前管理员管理的组织机构下的用户
		    	        List<?> userList = userInfoServ.queryUser(userinfo);
		    	        userArr = new String[userList.size()];
		    	        
		    	        // 如果用户为空，特殊处理，赋特殊值，“----”无法创建，传此值目的是列表为空；
		    	        if(StrTool.listNotNull(userList)){
		    	        	for (int j=0; j<userList.size(); j++){
		    	        		UserInfo userI = (UserInfo)userList.get(j);
		    	        		userArr[j] = userI.getUserId();
		    		        }
		    	        }else{
		    	        	flag = false;
		    	        	userLogInfo.setUserid("----");
		    	        }
	    	        }
	            }else{
	            	
	            	// 特殊处理，赋特殊值，“----”无法创建，传此值目的是列表为空；
	            	flag = false;
	            	userLogInfo.setUserid("----");
	            }
	            if(flag){
	            	
	            	// 判断查询条件“用户”是否为空；
	            	if(!"".equals(userLogInfo.getUserid()) && userLogInfo.getUserid() != null){
	            		List<String> tempList = Arrays.asList(userArr);
	            		if(!tempList.contains(userLogInfo.getUserid())){
	            			
	            			// 输入的用户，当前管理员没有权限查看日志，特殊处理，赋特殊值，“----”无法创建，传此值目的是列表为空；
	            			userLogInfo.setUserid("----");
		            	}
	            	}else{
	            		userLogInfo.setBatchIds(userArr);
	            	}
	            }
	    	}
        }
        
        return userLogInfo;
    }

    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    public String find() {
        return null;
    }

    /**
     * 行数统计
     * 分页处理
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = userLogServ.count(getUserLogInfo(userLogQueryForm));
        PageArgument pageArg = getArgument(totalRow);
        return pageArg;
    }

    /***
     * 初始化用户日志
     */
    public String init() {
        if (isNeedClearForm()) {
            userLogQueryForm = null;
        }
        try {
            PageArgument pageArgument = pageArgument();
            pageArgument.setCurPage(getPage());
            pageArgument.setPageSize(getPagesize());
            List<?> resultList = query(pageArgument);
            String jsonStr = JsonTool.getJsonFromList(pageArgument.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询用户日志
     */
    public List<?> query(PageArgument pageArgument) {
        List<?> userlogList = null;
        try {
            userlogList = userLogServ.query(getUserLogInfo(userLogQueryForm), pageArgument);
            if (!StrTool.listNotNull(userlogList)) {
                userlogList = new ArrayList<Object>();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return userlogList;
    }

    public String modify() {
        return null;
    }

    /**
     * 用户日志分页处理
     */
    public String page() {
        PageArgument pArgument = getArgument(request, 0);
        query(pArgument);
        return SUCCESS;
    }

    public String view() {
        String id = request.getParameter("id");
        UserLogInfo logInfo = new UserLogInfo();
        logInfo.setId(Integer.parseInt(id));
        try {
            logInfo = (UserLogInfo) userLogServ.find(logInfo);
            logInfo.setActionidOper(Language.getLangStr(request, AdmLogConstant.lang_user_action_id + logInfo.getActionid()));
            logInfo.setLogcontent(Language.getLangStr(request, logInfo.getLogcontent()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        request.setAttribute("userLog", logInfo);
        return "view";
    }

}
