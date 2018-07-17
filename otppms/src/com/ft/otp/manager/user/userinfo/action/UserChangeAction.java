/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.action.aide.UInfoActionAide;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.form.BindUTQForm;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户绑定令牌业务控制Action
 * 
 * 
 * @author TBM
 */
public class UserChangeAction extends BaseAction {

    private static final long serialVersionUID = -3724405181297496193L;

    private Logger logger = Logger.getLogger(UserChangeAction.class);

    private IUserInfoServ userChangeServ = null;
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");
    // 用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");
    private UInfoActionAide uInfoAide = new UInfoActionAide();
    private BindUTQForm queryForm;
    private UserInfo userInfo = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public IUserInfoServ getUserChangeServ() {
        return userChangeServ;
    }

    public void setUserChangeServ(IUserInfoServ userChangeServ) {
        this.userChangeServ = userChangeServ;
    }

    /**
     * @return the queryForm
     */
    public BindUTQForm getQueryForm() {
        return queryForm;
    }

    /**
     * @param queryForm
     *            the queryForm to set
     */
    public void setQueryForm(BindUTQForm queryForm) {
        this.queryForm = queryForm;
    }

    /**
     * 取出QueryForm中的实体
     * 
     * @param agentQueryForm
     * @return
     */
    private UserInfo getUserInfo(BindUTQForm queryForm) {
        UserInfo uInfo = new UserInfo();
        if (StrTool.objNotNull(queryForm)) {
            uInfo = queryForm.getUserInfo();
            if (queryForm.getDOrgunitId().indexOf(",") != -1) { // 这个条件一定成立
                uInfo.setDomainId(Integer.parseInt(queryForm.getDOrgunitId().split(",")[0].split(":")[0]));
                uInfo.setOrgunitId(Integer.parseInt(queryForm.getDOrgunitId().split(",")[0].split(":")[1]));
            } else {
                uInfo.setDomainId(0);
                uInfo.setOrgunitId(0); // 目的是不让其参加 查询条件判断
            }
            uInfo.setUsbind(queryForm.getUsbindState());
        } else {
            uInfo.setRealName(null);
            uInfo.setDomainId(0);
            uInfo.setOrgunitId(0);// 0是不参与条件匹配
        }
        String curLoginUserId = (String) super.getCurLoginUser();// 获得当前管理员id号
        String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); // 当前管理员所拥有的角色
        // 对应角色表中的rolemark字段
        if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {// 如果是超级管理员
            uInfo.setIsFliterTag(null); // 不根据组织机构顾虑
        } else {
            uInfo.setIsFliterTag(1); // 根据组织机构顾虑
            uInfo.setCurrentAdminId(curLoginUserId);
        }
        return uInfo;
    }

    /**
     * 用户查询
     * 
     * @return
     */
    public String usrQuery() {
        if (isNeedClearForm()) {
            queryForm = null;
        }
        try {
            String usbindState = request.getParameter("usbindState");
            if (StrTool.strNotNull(usbindState)) {
                queryForm = new BindUTQForm();
                queryForm.setUsbindState(Integer.parseInt(usbindState));
            }
            PageArgument pageArg = pageArgument();
            List<?> uiList = query(pageArg);
            // 返回JSON格式查询数据
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), uiList, pageArg);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理用户数据查询
     * 
     * @param pageArg
     */
    private List<?> query(PageArgument pageArg) {
        List<?> uiList = null;
        try {
            // 令牌分配======>初始化，返回空数据
            if (StrTool.strEquals(request.getParameter("assignInit"), StrConstant.common_number_1)) {
                return new ArrayList();
            }
            uiList = userChangeServ.queryUser(getUserInfo(queryForm), pageArg);

            // 设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串
            uiList = getUInfoList(uiList, domainInfoServ, orgunitInfoServ);

        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return uiList;
    }

    private List<?> getUInfoList(List<?> uiList, IDomainInfoServ domainInfoServ, IOrgunitInfoServ orgunitInfoServ)
            throws BaseException {
        List<?> utList = null;
        List<?> uInfoList = new ArrayList<Object>();

        if (!StrTool.listNotNull(uiList)) {
            return uInfoList;
        }

        // 取得列表中用户对应的令牌号
        try {
            utList = userTokenServ.batchQueryUT(uiList, null, NumConstant.common_number_0);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        // 将用户列表整理成一个不重复用户的列表
        uInfoList = uInfoAide.getUInfoList(uiList, utList, domainInfoServ, orgunitInfoServ);

        return uInfoList;
    }

    /**
     * 设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串
     * 
     * @param uiList
     * @param utList
     * @return
     */
    public List<?> setDOList(List<?> uiList, IDomainInfoServ domainInfoServ, IOrgunitInfoServ orgunitInfoServ) {
        List<UserInfo> uInfoList = new ArrayList<UserInfo>();
        Iterator<?> it = uiList.iterator();
        while (it.hasNext()) {
            UserInfo ui = (UserInfo) it.next();
            UserInfo userInfo = uInfoAide.setDOstr(ui);
            uInfoList.add(userInfo);
        }
        return uInfoList;
    }

    /**
     * 分页统计 分页处理
     * 
     * @return
     * @throws BaseException
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = userChangeServ.countUser(getUserInfo(queryForm));
        PageArgument pageArg = getArgument(totalRow);
        // 令牌分配初始化，返回空数据
        if (StrTool.strEquals(request.getParameter("assignInit"), StrConstant.common_number_1)) {
            pageArg.setTotalRow(0);
        }
        return pageArg;
    }
    
    /**
     * 组合用户集合
     * @param uInfo
     * @return
     * @throws BaseException
     */
    public String[] getUserArr(UserInfo uInfo) throws BaseException {
        String[] userArr = null;
        List<?> uiList = userChangeServ.selectUser(uInfo);
        if (!StrTool.listNotNull(uiList)) {
            return null;
        }

        userArr = new String[uiList.size()];
        Iterator<?> iter = uiList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            UserInfo userInfo = (UserInfo) iter.next();
            String userId = userInfo.getUserId();
            userArr[i] = userId;
            i++;
        }
        return userArr;
    }
    
    /**
     * 组装令牌集合
     * @param uInfo
     * @return
     * @throws BaseException
     */
    public String[] getTokenArr(UserToken userTkn) throws BaseException {
        String[] tokenArr = null;
        List<?> uiList = userTokenServ.queryToken(userTkn);
        if (!StrTool.listNotNull(uiList)) {
            return null;
        }
        tokenArr = new String[uiList.size()];
        Iterator<?> iter = uiList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            UserToken userToken = (UserToken) iter.next();
            String token = userToken.getToken();
            tokenArr[i] = token;
            i++;
        }
        return tokenArr;
    }

    /**
     * 用户机构迁移
     * 
     * @return
     * @throws Exception 
     */
    public String changeUser() throws Exception {
        int orginId = StrTool.parseInt(request.getParameter("orgunit")); // 变更到的机构ID
        int domainId = StrTool.parseInt(request.getParameter("domain")); // 变更到的域ID

        String userIdS = request.getParameter("assignUser"); // 选择用户集合
        String tokens = request.getParameter("assignToken"); // 绑定令牌的集合
        
        String usrOperSel = request.getParameter("usrOperSel");
        String dOrgunitId = request.getParameter("dOrgunitId");
        String realName = request.getParameter("realName");
        String usbindState = request.getParameter("usbindState");
        
        String changeTypeStr = request.getParameter("changeType"); // 变列方式
        int changeType = StrTool.parseInt(changeTypeStr);
        
        String[] userArr = null;
        String[] tokenArr = null;
        
        if (StrTool.strNotNull(usrOperSel) && StrTool.strEquals(usrOperSel, StrConstant.common_number_0)) { // 本页选中的记录
        	// 选择用户集合
            if (StrTool.strNotNull(userIdS)) {
                userArr = userIdS.split(",");
            }
        }else{ // 按条件查询
        	UserInfo uInfo = new UserInfo();
        	uInfo.setRealName(realName);
        	uInfo.setUsbind(StrTool.parseInt(usbindState));
        	uInfo.setOrgunitId(StrTool.parseInt(dOrgunitId.split(",")[0].split(":")[1]));
        	uInfo.setDomainId(StrTool.parseInt(dOrgunitId.split(",")[0].split(":")[0]));
        	userArr = getUserArr(uInfo);
        }
        
        if (StrTool.strNotNull(usrOperSel) && StrTool.strEquals(usrOperSel, StrConstant.common_number_0)) { // 本页选中的记录
            // 选择的用户绑定令牌集合，有重复令牌
            if (StrTool.strNotNull(tokens)) {
                tokenArr = tokens.split(",");
            }
        }else{ // 按条件查询
        	UserToken userToken = new UserToken();
        	List userlist = Arrays.asList(userArr);
        	userToken.setDomainId(StrTool.parseInt(dOrgunitId.split(",")[0].split(":")[0]));
        	userToken.setUserIds(userlist);
        	tokenArr = getTokenArr(userToken);
        }
        
        int[] arrNum = new int[3];
        int tknNum = 0; // 令牌数
        int unbindNum = 0; // 令牌解绑个数
        int changeNum = 0; // 令牌变更个数
        int userNum = 0; // 成功变更的用户数
        String successInfo = "";
        try {
            Map<String, Object> map = uInfoAide.getUCList(userArr, tokenArr, changeType, userTokenServ, userChangeServ,
                    tokenServ, orginId, domainId);
            arrNum = (int[]) map.get(StrConstant.USR_TKN_NUM);
            tknNum = arrNum[0];
            unbindNum = arrNum[1];
            changeNum = arrNum[2];
            userNum = arrNum[3];

            // Start,变更成功，页面提示信息整理
            String totalUnBind = Language.getLangStr(request, "user_unbind_total") + unbindNum;
            String totalChange = Language.getLangStr(request, "user_change_token_total") + changeNum;
            String totalUser = Language.getLangStr(request, "user_change_user_total") + userNum;
            // End,变更成功，页面提示信息整理
            successInfo = Language.getLangStr(request, "user_change_succ_tip") + "<br>" + totalUser + "<br>"
                     + totalUnBind + "<br>" + totalChange + "<br>";

            setActionResult(true);
            setResponseWrite(successInfo);
        } catch (BaseException ex) {
            setActionResult(false);
            setResponseWrite("false");
        }

        return null;
    }
    
    /**
     * 检查用户绑定的令牌是否被其它用户绑定
     * 用于用户变更方式先择项
     * @return
     * @throws Exception
     */
    public String checkToken() throws Exception {
    	String tokens = request.getParameter("tokenArr"); 
    	String userIdS = request.getParameter("assignUser");
    	String[] userIdArr = null;
    	String[] tokenArr = null;
    	List<?> usertList = null;
    	int onlySum = 0; //独立令牌个数
    	int pubSum = 0; //共有令牌个数
    	if (StrTool.strNotNull(tokens)) {
            tokenArr = tokens.split(",");
        }
    	if (StrTool.strNotNull(userIdS)) {
    		userIdArr = userIdS.split(",");
        }
    	try {
	    	List<?> userList = Arrays.asList(userIdArr);
	    	for (int i=0; i<tokenArr.length; i++){
	    		UserToken userToken = new UserToken();
	    		userToken.setToken(tokenArr[i]);
	    		userToken.setUserIds(userList);
	    		usertList = (List<?>) userTokenServ.queryTokenOth(userToken);
	    		if(StrTool.listNotNull(usertList)){
	    			pubSum++;
	    		}else{
	    			onlySum++;
	    		}
	    	}
	    	if(pubSum == tokenArr.length){ // 绑定的令牌都为公有
	    		outPutOperResultString(Constant.alert_succ, "1");
	    	}else if(onlySum == tokenArr.length){ // 绑定的令牌都为私有
	    		outPutOperResultString(Constant.alert_succ, "2");
	    	}else{
	    		outPutOperResultString(Constant.alert_succ, "0");
	    	}
    	}catch(BaseException e){
    	    logger.error(e.getMessage(), e);
    	}
    	return null;
    }
    
    
    
    
}
