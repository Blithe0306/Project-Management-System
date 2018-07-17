/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.radius.service.IRadProfileServ;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.form.TokenQueryForm;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.action.aide.UInfoActionAide;
import com.ft.otp.manager.user.userinfo.action.aide.UserBindActionAide;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.form.UInfoQueryForm;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.alg.AlgConvert;
import com.ft.otp.util.alg.dist.RC4Util;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户相关业务控制Action
 *
 * @Date in Apr 20, 2011,10:27:44 AM
 *
 * @author TBM
 */
public class UserInfoAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 4969280922327805021L;

    private Logger logger = Logger.getLogger(UserInfoAction.class);

    private IUserInfoServ userInfoServ = null;
    
    private IRadProfileServ radProfileServ = (IRadProfileServ) AppContextMgr.getObject("radProfileServ");

    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");

    //域服务接口
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");
    //组织机构服务接口
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");

    private int usrMaxBindTkn = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
            ConfConstant.CORE_MAX_BIND_TOKENS));
    private int tknMaxBindUsr = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
            ConfConstant.CORE_MAX_BIND_USERS));

    //UserInfoAction 辅助类
    private UInfoActionAide aide = new UInfoActionAide();
    private UserBindActionAide bindAide = new UserBindActionAide();
    //令牌的辅助类
    private TokenActionAide aideToken = new TokenActionAide();

    private UInfoQueryForm queryForm = null;
    private UserInfo userInfo = null;
    private UserInfo oldUserinfo = null;
    private TokenQueryForm tokenQueryForm;
    //令牌的辅助类
    private TokenActionAide tokenAide = new TokenActionAide();
    
    public UserInfo getOldUserinfo() {
		return oldUserinfo;
	}

	public void setOldUserinfo(UserInfo oldUserinfo) {
		this.oldUserinfo = oldUserinfo;
	}

	public TokenQueryForm getTokenQueryForm() {
		return tokenQueryForm;
	}

	public void setTokenQueryForm(TokenQueryForm tokenQueryForm) {
		this.tokenQueryForm = tokenQueryForm;
	}

	/**
     * @return the userInfoServ
     */
    public IUserInfoServ getUserInfoServ() {
        return userInfoServ;
    }

    /**
     * @param userInfoServ the userInfoServ to set
     */
    public void setUserInfoServ(IUserInfoServ userInfoServ) {
        this.userInfoServ = userInfoServ;
    }

    /**
     * @return the queryForm
     */
    public UInfoQueryForm getQueryForm() {
        return queryForm;
    }

    /**
     * @param queryForm the queryForm to set
     */
    public void setQueryForm(UInfoQueryForm queryForm) {
        this.queryForm = queryForm;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    public UserInfo getUserInfo(UInfoQueryForm queryForm) {
        UserInfo uInfo = new UserInfo();
        if (StrTool.objNotNull(queryForm)) {
            uInfo.setUserId(StrTool.trim(queryForm.getUserId()));
            uInfo.setRealName(StrTool.trim(queryForm.getRealName()));
            uInfo.setToken(StrTool.trim(queryForm.getToken()));
            uInfo.setLocked(queryForm.getLocked());
            uInfo.setEnabled(queryForm.getEnabled());
            uInfo.setLocalAuth(queryForm.getLocalAuth());
            uInfo.setBackEndAuth(queryForm.getBackEndAuth());
            uInfo.setUsbind(queryForm.getBindState());
            if (StrTool.strNotNull(queryForm.getDOrgunitId())) {
                if (queryForm.getDOrgunitId().indexOf(",") != -1) { //这个条件一定成立

                    // 定义一个数组，目的：封装用户的机构ID，便于多个用户查询与机构相同的令牌
                    int orgunitids[] = new int[queryForm.getDOrgunitId().split(",").length];
                    int domainids[] = new int[queryForm.getDOrgunitId().split(",").length];
                    for (int i = 0; i < queryForm.getDOrgunitId().split(",").length; i++) {

                        // 取出传过来的用户机构ID号放到自定义的数组里
                        domainids[i] = StrTool.parseInt(queryForm.getDOrgunitId().split(",")[i].split(":")[0]);
                        orgunitids[i] = StrTool.parseInt(queryForm.getDOrgunitId().split(",")[i].split(":")[1]);
                    }
                    uInfo.setOrgunitIds(orgunitids);
                    uInfo.setDomainIds(domainids);
                } else {
                    uInfo.setDomainId(NumConstant.common_number_0);
                }
            }
            uInfo.setOrgFlag(queryForm.getOrgFlag());
        } else {
            uInfo.setUserId(null);
            uInfo.setRealName(null);
            uInfo.setToken(null);
            uInfo.setOrgFlag(1);
            uInfo.setLocked(-1);
            uInfo.setEnabled(-1);
            uInfo.setLocalAuth(-1);
            uInfo.setBackEndAuth(-1);
            uInfo.setDomainId(NumConstant.common_number_0);
        }

        String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
        String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段
        if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {//如果是超级管理员
            uInfo.setIsFliterTag(null); //不根据组织机构顾虑
        } else {
            uInfo.setIsFliterTag(1); //根据组织机构顾虑
            uInfo.setCurrentAdminId(curLoginUserId);
        }

        return uInfo;
    }

    /**
     * @param userInfo the userInfo to set
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * 初始化用户列表
     */
    public String init() {
        if (isNeedClearForm()) {
            queryForm = null;
        }
        try {
            PageArgument pageArg = pageArgument();
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> resultList = query(pageArg);
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 添加用户
     * 重构与 2013-3-5
     */
    public String add() {
        try {
            if (StrTool.objNotNull(userInfo)) {
                boolean isExsitTag = aide.findUserIsExist(userInfoServ, userInfo);
                if (!isExsitTag) {//如果不存在
                    //整理用户信息
                    userInfo = aide.reUserId(userInfo);
                    userInfo = aide.reUserInfo(userInfo);
                    //1、添加用户信息
                    //配置默认密码
                    String defaultPass = ConfConfig.getConfValue(ConfConstant.CONF_TYPE_USER + "_"
                            + ConfConstant.DEFAULT_USER_PWD);
                    if (StrTool.strIsNotNull(userInfo.getPwd())) {
                        userInfo.setPwd(PwdEncTool.encPwd(userInfo.getPwd()));
                    } else {
                        userInfo.setPwd(PwdEncTool.encPwd(defaultPass));
                    }
                    userInfo.setCreateTime(StrTool.timeSecond());
                    
                    // Start,日志处理
                    userInfo = aide.setDOstr(userInfo);
                    // End,日志处理

                    userInfoServ.addObj(userInfo);

                    //3、新的用户令牌绑定
                    //aide.addUT(tokenServ,userInfo, userInfo.getTokens(), userTokenServ, bindAide, tknMaxBindUsr,usrMaxBindTkn);
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip") + ","
                            + userInfo.getUserId() + "," + userInfo.getDomainId() + "," + userInfo.getOrgunitId());
                    return null;
                } else {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "user_vd_userid_exist"));
                    return null;
                }
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
                return null;
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
        }
        return null;
    }

    /**
     * 保存用户，保存成功后直接转向下一个选项，而不跳转到列表页面
     */
    public String addUserNext() {
        try {
            if (!StrTool.objNotNull(userInfo)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
                return null;
            }
            //执行用户信息添加操作
            userInfoServ.addObj(userInfo);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
            return null;
        } catch (BaseException e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    //    /**
    //     * 对于用户已经保存过，修改对应的静态密码、绑定令牌
    //     */
    //    public String modifyUser() {
    //        try {
    //            //对用户的静态密码信息修改操作
    //            userInfoServ.updateObj(userInfo);
    //
    //            //先删除用户绑定的令牌
    //            UserToken userToken = new UserToken();
    //            userToken.setUserId(userInfo.getUserId());
    //            userTokenServ.delObj(userToken);
    //
    //            //执行用户令牌绑定操作
    //            aide.addUT(userInfo.getUserId(), userInfo.getTokens(), userTokenServ, bindAide, tknMaxBindUsr,
    //                    usrMaxBindTkn);
    //
    //            //执行用户与用户组关联操作，先删除用户对应的用户组
    //            UserAndGroup userAndGroup = new UserAndGroup();
    //            userAndGroup.setUserId(userInfo.getUserId());
    //            userAndGroupServ.delObj(userAndGroup);

    //然后执行用户与用户组关联操作
    //            try {
    //                addUG(userInfo.getUserId(), userInfo.getUgroups());
    //            } catch (BaseException ex) {
    //                log.error(ExceptionCode.USER_AND_GROUP_ADD, ex);
    //            }
    //            outPutOperResult(Constant.alert_succ, "保存信息成功");
    //            return null;
    //        } catch (BaseException e) {
    //
    //            outPutOperResult(Constant.alert_error, "保存信息失败");
    //        } finally {
    //            //userInfo.clearList();
    //        }
    //
    //        return null;
    //    }

    /**
     * 添加用户与用户组对应关系
     * @Date in Apr 21, 2011,10:12:29 PM
     * @param userId
     * @param groups
     */
  //  private void addUG(String userId, List<?> ugList) throws BaseException {
  //      if (!StrTool.listNotNull(ugList)) {
  //          return;
  //      }
  //      List<Object> gList = new ArrayList<Object>();
  //      UserAndGroup userGroup = null;
  //      for (int i = 0; i < ugList.size(); i++) {
  //          userGroup = new UserAndGroup();
  //          userGroup.setUserId(userId);
  //          userGroup.setGroupId(StrTool.parseInt((String) ugList.get(i)));
  //          gList.add(userGroup);
  //      }
  //      userAndGroupServ.addUsrGroup(gList);
  //  }

    /**
     * 删除用户操作
     */
    public String delete() {
        Set<?> keys = super.getDataKeys(request);
        try {
            if (StrTool.setNotNull(keys)) {
                userInfoServ.delObj(keys);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        delPage();
        return SUCCESS;
    }

    /**
     * 删除用户后保持当前页状态
     * @Date in Apr 15, 2011,10:06:40 AM
     */
    private void delPage() {
        try {
            PageArgument pageArg = pageArgument();
            query(pageArg);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 用户编辑初始化，查询一条用户信息
     */
    public String find() {
        UserInfo uInfo = null;
        String source = super.getSource(request);
        try {
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));//将用户名中存在的[]符号转换为&
            userInfo.setUserId(StrTool.strToUTF8(userInfo.getUserId()));
            uInfo = (UserInfo) userInfoServ.find(userInfo);
            //设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
            uInfo = aide.setDOstr(uInfo);

            if (!StrTool.objNotNull(uInfo)) {
                return init();
            }

            String empin = AlgConvert.hexToString(uInfo.getPwd());
            empin = RC4Util.runRC4(empin);
            uInfo.setPwd(empin);

            //变更机构，跳转到变更机构页面
            if (StrTool.strNotNull(source) && StrTool.strEquals("changeOrgunit", source)) {
                //设置 多用户绑定的令牌
                List<?> reTokens = queryMulUserToken();//找到多用户绑定的令牌
                uInfo.setReTokens(reTokens);
                //设置 非多用户绑定的令牌
                List<?> leftTokens = querySingleUserToken();
                uInfo.setLeftTokens(leftTokens);
            }

        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setUserInfo(uInfo);
        // 设置静态密码，跳转到静态密码页面
        if (StrTool.strNotNull(source) && StrTool.strEquals("setpass", source)) {
            return "pwd";
        }
        // 变更机构，跳转到变更机构页面
        if (StrTool.strNotNull(source) && StrTool.strEquals("changeOrgunit", source)) {
            return "changeOrgunit";
        }
        //跳转到编辑页面
        return SUCC_FIND;
    }

    /**
     * 编辑用户信息
     */
    public String modify() {

        try {
            if (StrTool.objNotNull(userInfo)) {//不是空的
                //整理用户信息
                userInfo = aide.reUserInfo(userInfo);
                //改变用户信息
                
                // Start,日志整理
                UserInfo userIn = new UserInfo();
                userIn.setUserId(userInfo.getUserId());
                userIn = (UserInfo) userInfoServ.find(userIn);
                this.setOldUserinfo(userIn);
                
                userInfo = aide.setDOstr(userInfo);
                // End,日志整理
                
                userInfoServ.updateObj(userInfo);

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
                return null;
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "user_vd_edit_isnull"));
                return null;
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
            logger.error(e.getMessage(), e);
        } finally {
            //userInfo.clearList();
        }

        return null;
    }

    /**
     * 用户信息分页处理
     */
    public String page() {
        PageArgument pageArg = getArgument(request, 0);
        query(pageArg);

        return SUCCESS;
    }

    /**
     * 显示用户详细信息
     */
    public String view() {
        UserInfo uInfo = null;
        try {
            // 1 取得用户信息
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));//将用户名中存在的[]符号转换为&
            userInfo.setUserId(StrTool.strToUTF8(userInfo.getUserId()));
            uInfo = (UserInfo) userInfoServ.find(userInfo);
            if (!StrTool.objNotNull(uInfo)) {
                //return init();
                setResponseWrite(Language.getLangStr(request, "tkn_view_user_not_exist"));
                return null;
            }

            /**
             * 注：后续做了策略后重新处理这块，如果策略中一个用户只可以对应一个令牌，
             * 则不需要执行多次查询，一次连接查询即可获得所需要数据。
             */
            // 2 取得令牌列表(一个用户对应多个令牌)
            try {
                uInfo.setTokens(aide.getJoinTokens(userTokenServ, userInfo.getUserId(), userInfo.getDomainId()));
            } catch (BaseException e) {
                logger.error(e.getMessage(), e);
            }

            //3设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
            uInfo = aide.setDOstr(uInfo);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        this.setUserInfo(uInfo);
        return SUCC_VIEW;

    }

    /**
     * 新增用户之前判断用户是否已经存在
     */
    public String findUserIsExist() {
        boolean isExsitTag = aide.findUserIsExist(userInfoServ, userInfo);
        if (isExsitTag) {
            super.setResponseWrite("false");
        }
        return null;
    }

    /**
     * 处理数据查询
     * @Date in Apr 20, 2011,12:04:36 PM
     * @param pageArg
     */
    private List<?> query(PageArgument pageArg) {
        List<?> userList = null;
        try {
            //找到符合条件的用户列表  这里的uiList已经去掉了重复项(数据库 distinct)
            userList = userInfoServ.query(getUserInfo(queryForm), pageArg);

            //整理用户List，去掉重复用户 并设置用户绑定的令牌号 和对应的域、组织机构
            userList = getUInfoList(userList, domainInfoServ, orgunitInfoServ);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return userList;
    }

    private List<?> getUInfoList(List<?> uiList, IDomainInfoServ domainInfoServ, IOrgunitInfoServ orgunitInfoServ)
            throws BaseException {
        List<?> utList = null;
        List<?> uInfoList = new ArrayList<Object>();

        if (!StrTool.listNotNull(uiList)) {
            return uInfoList;
        }

        //取得列表中用户对应的令牌号
        try {
            utList = userTokenServ.batchQueryUT(uiList, null, NumConstant.common_number_0);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        //将用户列表整理成一个不重复用户的列表
        uInfoList = aide.getUInfoList(uiList, utList, domainInfoServ, orgunitInfoServ);

        return uInfoList;
    }
    
    /**
     * 令牌更换时数据初始化
     * @author LXH
     * @date Feb 28, 2014 9:38:31 AM
     * @return
     */
    public String bindChangeInit() {
        UserInfo uInfo = null;
        try {
            // 1 取得用户信息
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));//将用户名中存在的[]符号转换为&
            uInfo = (UserInfo) userInfoServ.find(userInfo);
            if (!StrTool.objNotNull(uInfo)) {
                return init();
            }
            //设置该用户对象中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
            uInfo = aide.setDOstr(uInfo);

            /**
             * 注：后续做了策略后重新处理这块，如果策略中一个用户只可以对应一个令牌，
             * 则不需要执行多次查询，一次连接查询即可获得所需要数据。
             */
            // 2 取得令牌数组(一个用户对应多个令牌)
            try {
                uInfo.setTokens(aide.getTokens(userTokenServ, userInfo.getUserId(), userInfo.getDomainId()));
            } catch (BaseException e) {
                logger.error(e.getMessage(), e);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        this.setUserInfo(uInfo);
        return "change_token_init";
    }

    /**
     * 绑定/更换令牌操作
     * @Date in Apr 27, 2011,5:26:20 PM
     * @return
     */
    public String bindChangeTkn() {
        String operid = request.getParameter("operid");
        try {
            if (StrTool.strEquals(operid, StrConstant.common_number_1)) {
                String length = request.getParameter("length");
                List<?> tokens = userInfo.getTokens(); // 新令牌
                List<?> hidTkns = userInfo.getHiddenTkns(); // 旧令牌
                if (StrTool.strEquals(length, StrConstant.common_number_0)) {
                    tokens.clear();
                }
                if (StrTool.isArrEquals(tokens, hidTkns)) {
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_replace_success_tip"));
                    return null;
                }
                String flag = "";
                String errToken = "";
                Map<String, Object> map = null;
                if (StrTool.listNotNull(hidTkns) && StrTool.listNotNull(tokens)) {
                    map = aide.upUTBind_2(tokenServ, hidTkns, tokens, userInfo, userTokenServ, bindAide, tknMaxBindUsr,
                            usrMaxBindTkn);

                    // 判断标志，判断更换是否成功
                    flag = (String) map.get(StrConstant.USR_TKN_SUCC);

                    // 交换失败令牌的一个集合
                    errToken = (String) map.get(StrConstant.USR_TKN_TOKENS);
                }
                if (StrTool.strEquals(flag, "0")) {
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_replace_success_tip"));
                } else if (StrTool.strEquals(flag, "1")) {

                    // 防制令牌号长度大于提示框设定值长度
                    errToken = errToken.replace(",", "\n");
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "user_replace_token_fail")
                            + errToken + Language.getLangStr(request, "user_bind_token_limit"));
                }
            } else {
                //System.out.println(tknMaxBindUsr + "" + usrMaxBindTkn);
                //执行用户令牌绑定操作
                aide.addUT(tokenServ, userInfo, userInfo.getTokens(), userTokenServ, bindAide, tknMaxBindUsr,
                        usrMaxBindTkn);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "user_bind_token_succ"));
            }
            return null;
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            if (StrTool.strEquals(operid, StrConstant.common_number_1)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_replace_error_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "user_bind_token_fail"));
            }
        }
        return null;
    }

    /**
     * 解除用户与令牌的绑定关系
     * @Date in May 3, 2011,9:41:15 AM
     * @return
     * @throws BaseException 
     */
    public String unBindUT() throws BaseException {
        String tokenIds = request.getParameter("tokenIds");
        UserToken userToken = new UserToken();
        //解绑单支令牌、多支令牌的标识
        String unbindNum = request.getParameter("unbindNum");

        try {
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));//将用户名中存在的[]符号转换为&
            if (StrTool.strNotNull(unbindNum) && StrTool.strEquals(unbindNum, "1")) { //单只解绑
                List<?> tokens = userInfo.getTokens();
                userToken.setTokenIds(tokens);
                userToken.setUserId(userInfo.getUserId());
                userToken.setDomainId(userInfo.getDomainId());

                // 令牌解绑后，判断将该令牌是否放回域中
                updateBind(tokens, 0);

                // 令牌解绑后， 被解绑令牌是否停用
                updateState(tokens, 0);

                userTokenServ.delObj(userToken);

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_unbind_success_tip"));
                return null;
            } else { //多只解绑
                if (StrTool.strNotNull(tokenIds)) {
                    String[] keys = tokenIds.split(",");
                    //一个用户绑定了多个令牌，选择令牌进行解绑
                    List<String> keyList = Arrays.asList(keys);
                    userToken.setTokenIds(keyList);
                    userToken.setUserId(userInfo.getUserId());
                    userToken.setDomainId(userInfo.getDomainId());

                    // 令牌解绑后，判断将该令牌是否放回域中
                    updateBind(keyList, 0);

                    // 令牌解绑后， 被解绑令牌是否停用
                    updateState(keyList, 0);

                    userTokenServ.delObj(userToken);

                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "tkn_unbind_success_tip"));
                    return null;

                }
            }
        } catch (Exception ex) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "tkn_unbind_error_tip"));
            return null;
        }
        return null;
    }

    /**
     * 判断解绑令牌，被解绑令牌是否停用
     * 
     * 
     */
    public void updateState(List<?> list, int flag) throws Exception {

        // 判断解绑令牌，被解绑令牌是否停用，1：是；0：否
        int rstate = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.CORE_UNBIND_STATE_SELECT));
        if (rstate == NumConstant.common_number_1) {

            if (flag == NumConstant.common_number_0) {
                for (int i = 0; i < list.size(); i++) {
                    String token = (String) list.get(i);
                    UserToken userToken = new UserToken();
                    userToken.setToken(token);

                    // 查看此令牌是否被其它用户绑定
                    int count = userTokenServ.count(userToken);

                    // 等于1代表只有一个用户绑定
                    if (count == NumConstant.common_number_1) {
                        TokenInfo tokenInfo = new TokenInfo();
                        tokenInfo.setToken(token);
                        tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 1); //停用
                        tokenServ.updateTokenState(tokenInfo);
                    }
                }
            } else {
                UserToken userTok = null;
                for (int j = 0; j < list.size(); j++) {
                    UserToken userToken = (UserToken) list.get(j);
                    // 取出解绑的令牌号
                    List<?> tokenList = userTokenServ.selTokens(userToken); // userToken中只有USERID有值
                    if (StrTool.listNotNull(tokenList)) {
                        for (int k = 0; k < tokenList.size(); k++) {
                            userTok = (UserToken) tokenList.get(k);
                            UserToken userTkn = new UserToken();
                            userTkn.setToken(userTok.getToken());

                            // 查看此令牌是否被其它用户绑定
                            int count = userTokenServ.count(userTkn);
                            if (count == NumConstant.common_number_1) {
                                TokenInfo tokenInfo = new TokenInfo();
                                tokenInfo.setToken(userTkn.getToken());
                                tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 1); //停用
                                tokenServ.updateTokenState(tokenInfo);
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 令牌解绑后，将该令牌放回域中
     * @param userInfo 解绑，选中的信息
     * @param list 批量解绑，选中数据的LIST
     * @param flag 0:代表列表中"解绑"操作；1：代表操作中执行"批量解绑"
     * @throws Exception
     */
    public void updateBind(List<?> list, int flag) throws Exception {
        try {
            // 令牌解绑后是否回到所属的域中。0，否，1是
            if (StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                    ConfConstant.TK_UNBIND_IS_CHANGE_ORG), StrConstant.common_number_1)) {
                UserToken userIn = null;
                UserToken userTok = null;
                List<?> domainList = null;
                if (StrTool.listNotNull(list)) {
                    switch (flag) {
                        // 列表中"解绑"操作
                        case 0:
                            for (int t = 0; t < list.size(); t++) {
                                UserToken userTo = new UserToken();
                                String token = (String) list.get(t);

                                // 查出该令牌下所有绑定用户的信息
                                userTo.setToken(token);
                                domainList = userTokenServ.selObjs(userTo);
                                if (StrTool.listNotNull(domainList)) {
                                    for (int d = 0; d < domainList.size(); d++) {
                                        userIn = (UserToken) domainList.get(d);

                                        // 判断令牌是否是管理员绑定，管理员绑定时isNullDomain：0代表无管理员绑定；1代表有管理员绑定；
                                        if (userIn.getIsNullDomain() == 0) {
                                            TokenInfo ti = new TokenInfo();
                                            ti.setToken(userIn.getToken());
                                            ti.setDomainid(userIn.getDomainId());

                                            // 令牌放回域中，机构ID置为NULL
                                            ti.setOrgunitid(null);
                                            tokenServ.updateTokenOrg(ti);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            break;

                        // 操作中执行"批量解绑"
                        case 1:
                            for (int j = 0; j < list.size(); j++) {
                                UserToken userToken = (UserToken) list.get(j);

                                // 取出解绑的令牌号
                                List<?> tokenList = userTokenServ.selTokens(userToken); // userToken中只有USERID有值
                                if (StrTool.listNotNull(tokenList)) {
                                    UserToken userTo = new UserToken();
                                    for (int h = 0; h < tokenList.size(); h++) {
                                        userTok = (UserToken) tokenList.get(h);
                                        userTo.setToken(userTok.getToken());

                                        // 查出该令牌下所有绑定用户的信息
                                        domainList = userTokenServ.selObjs(userTo);
                                        if (StrTool.listNotNull(domainList)) {
                                            for (int i = 0; i < domainList.size(); i++) {
                                                userIn = (UserToken) domainList.get(i);

                                                // 判断令牌是否是管理员绑定，管理员绑定时isNullDomain：0代表无管理员绑定；1代表有管理员绑定
                                                if (userIn.getIsNullDomain() == 0) {
                                                    TokenInfo ti = new TokenInfo();
                                                    ti.setToken(userIn.getToken());
                                                    ti.setDomainid(userIn.getDomainId());

                                                    // 令牌放回域中，机构ID置为NULL
                                                    ti.setOrgunitid(null);
                                                    tokenServ.updateTokenOrg(ti);
                                                } else {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 行数统计
     * 分页处理
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = userInfoServ.count(getUserInfo(queryForm));
        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 锁定/解锁用户
     * @Date in Jun 24, 2011,3:53:51 PM
     * @return
     * @throws BaseException
     */
    public String editUserLost() {
        try {
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));//将用户名中存在的[]符号转换为&
            if (userInfo.getLocked() == NumConstant.common_number_0) { //如果是没有锁定 那么永久锁定
                userInfo.setLoginLockTime(StrTool.timeSecond());
                userInfoServ.updateUserLost(userInfo, NumConstant.common_number_2);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_lock_succ_tip"));
            } else if (userInfo.getLocked() == NumConstant.common_number_1
                    || userInfo.getLocked() == NumConstant.common_number_2) { //如果是临时锁定或者是永久锁定都可以解锁
                userInfo.setLoginLockTime(0);//解锁的话 锁定时间设为0
                userInfo.setTempLoginErrCnt(0);
                userInfo.setLongLoginErrCnt(0);
                userInfoServ.updateUserLost(userInfo, NumConstant.common_number_0);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_unlock_succ_tip"));
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
        }
        return null;
    }

    /*
     * 禁用/启用用户
     * @return
     * @throws BaseException
     */
    public String editUserEnabled() {
        try {
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));//将用户名中存在的[]符号转换为&
            if (userInfo.getEnabled() == NumConstant.common_number_0) { //如果已禁用
                userInfoServ.updateUserEnabled(userInfo, NumConstant.common_number_1);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_enable_succ_tip"));
            } else if (userInfo.getEnabled() == NumConstant.common_number_1) { //如果已启用 那么禁用
                userInfoServ.updateUserEnabled(userInfo, NumConstant.common_number_0);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_disabled_succ_tip"));
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
        }

        return null;
    }

    /**
     * 用户批量操作（批量删除、批量解绑）
     * @throws Exception 
     */
    public String batchOper() throws Exception {
        Set<?> keys = super.getDelIds("userIds");
        String operobj = request.getParameter("operObj");
        String oper = request.getParameter("oper");
        String userIdStr = request.getParameter("userId");
        userIdStr = aide.replaceUserId(userIdStr);
        String realNameStr = request.getParameter("realName");
        String dOrgunitId = request.getParameter("dOrgunitId");
        String tokenStr = request.getParameter("token");
        String radprofileid = request.getParameter("radprofileid");
        String backEndAuth = request.getParameter("backEndAuth");
        String localAuth = request.getParameter("localAuth");
        String backEndAuthStr = request.getParameter("backEndAuthStr");
        String localAuthStr = request.getParameter("localAuthStr");
        String pwd = request.getParameter("pwd");
        String locked = request.getParameter("locked");
        String enabled = request.getParameter("enabled");
        String bindState = request.getParameter("bindState");

        boolean flag = false;
        if (StrTool.strEquals("1", oper)) { // 解绑操作特殊处理
            Map<String, Object> map = null;
            if (StrTool.strEquals(operobj, StrConstant.common_number_0)) {//本页
                map = modifyData(keys);
            } else if (StrTool.strEquals(operobj, StrConstant.common_number_1)) {//本次查询
                queryForm.setUserId(userIdStr);
                queryForm.setRealName(realNameStr);
                queryForm.setDOrgunitId(dOrgunitId);
                queryForm.setToken(tokenStr);
                queryForm.setLocked(StrTool.parseInt(locked));
                queryForm.setEnabled(StrTool.parseInt(enabled));
                queryForm.setBackEndAuth(StrTool.parseInt(backEndAuthStr));
                queryForm.setLocalAuth(StrTool.parseInt(localAuthStr));
                queryForm.setBindState(StrTool.parseInt(bindState));
                keys = convertLitToSets(queryForm);
                map = modifyData(keys);
            }
            if (!StrTool.mapNotNull(map)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
                return null;
            }
            int bindTotal = ((Integer) map.get("bindTotal")).intValue();
            int noBindTotal = ((Integer) map.get("noBindTotal")).intValue();
            int allTotal = ((Integer) map.get("allTotal")).intValue();
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "user_vd_unbind_1") + allTotal
                    + Language.getLangStr(request, "page_records") + Language.getLangStr(request, "user_vd_user_data")
                    + Language.getLangStr(request, "user_vd_unbind_2") + bindTotal
                    + Language.getLangStr(request, "page_records") + Language.getLangStr(request, "user_vd_user_total")
                    + noBindTotal + Language.getLangStr(request, "page_records")
                    + Language.getLangStr(request, "user_vd_user_data")
                    + Language.getLangStr(request, "user_vd_unbind_3") + Language.getLangStr(request, "plaint"));
        } else {
            if (StrTool.strEquals(operobj, StrConstant.common_number_0)) {//本页
                flag = modifyData(oper, keys, radprofileid, backEndAuth, localAuth, pwd);
            } else if (StrTool.strEquals(operobj, StrConstant.common_number_1)) {//本次查询
                queryForm.setUserId(userIdStr);
                queryForm.setRealName(realNameStr);
                queryForm.setDOrgunitId(dOrgunitId);
                queryForm.setToken(tokenStr);
                queryForm.setLocked(StrTool.parseInt(locked));
                queryForm.setEnabled(StrTool.parseInt(enabled));
                queryForm.setBackEndAuth(StrTool.parseInt(backEndAuthStr));
                queryForm.setLocalAuth(StrTool.parseInt(localAuthStr));
                queryForm.setBindState(StrTool.parseInt(bindState));
                keys = convertLitToSets(queryForm);
                flag = modifyData(oper, keys, radprofileid, backEndAuth, localAuth, pwd);
            }
            if (flag) {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_opera_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
            }
        }
        return null;
    }

    /**
     * 操作方法,针对解绑操作，特殊处理
     * @param keys
     * @return
     * @throws Exception
     */
    public Map<String, Object> modifyData(Set<?> keys) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Iterator<?> iter = keys.iterator();
            List<Object> list = new ArrayList<Object>();
            List<Object> bindTokenList = new ArrayList<Object>();
            List<Object> bindTokList = new ArrayList<Object>();
            List<UserToken> usTokenList = new ArrayList<UserToken>();
            List<Object> allList = new ArrayList<Object>();
            while (iter.hasNext()) {
                UserToken userToken = new UserToken();
                UserToken userTo = new UserToken();
                String userIdandDomainId = (String) iter.next();
                String userId = userIdandDomainId.split(":")[0];
                userId = aide.replaceUserId(userId);
                int domainId = StrTool.parseInt(userIdandDomainId.split(":")[1]);
                userToken.setUserId(userId);
                userToken.setDomainId(domainId);
                allList.add(userToken);
                // 根据用户ID与域ID查出中间表是否有数据（证明此用户绑定令牌）
                usTokenList = (List<UserToken>) userTokenServ.selUserTokens(userToken);
                if (StrTool.listNotNull(usTokenList)) {
                    bindTokenList.add(userToken);
                    for (int i = 0; i < usTokenList.size(); i++) {
                        userTo = (UserToken) usTokenList.get(i);
                        bindTokList.add(userTo);
                    }
                } else {
                    list.add(userToken);
                }
            }

            int bindTotal = 0; // 绑定令牌用户数（等于解绑的用户数）
            int noBindTotal = 0; // 没有绑定令牌的用户数
            int allTotal = allList.size(); // 选择的用户数（allList不可能为空）
            if (StrTool.listNotNull(bindTokenList)) {

                // 用户与令牌解绑是否需要迁移
                updateBind(bindTokenList, 1);

                //被解绑令牌是否停用
                updateState(bindTokenList, 1);

                //批量解除用户与令牌的对应关系
                userTokenServ.batchUnBindUT(bindTokList);

                bindTotal = bindTokenList.size();
            }

            if (StrTool.listNotNull(list)) {
                noBindTotal = list.size();
            }

            map.put("bindTotal", bindTotal);
            map.put("noBindTotal", noBindTotal);
            map.put("allTotal", allTotal);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 操作公用方法
     * 方法说明
     * @Date in Feb 8, 2012,10:23:57 AM
     * @param oper
     * @param keys
     * @return
     * @throws Exception 
     */
    public boolean modifyData(String oper, Set<?> keys, String radprofileid, String backendAuth, String localAuth,
            String pwd) throws Exception {
        boolean flag = false;
        try {
            if (StrTool.strEquals(oper, StrConstant.common_number_0)) { //批量删除 这里需要这些用户所在的domianId
                userInfoServ.delObj(keys);
                flag = true;
            } else if (StrTool.strEquals(oper, StrConstant.common_number_2)) { //批量添加Radius配置
                Iterator<?> itera = keys.iterator();
                List<Object> ulist = new ArrayList<Object>();
                while (itera.hasNext()) {
                    UserInfo uInfo = new UserInfo();
                    String userIdandDomainId = (String) itera.next();
                    String userId = userIdandDomainId.split(":")[0];
                    userId = aide.replaceUserId(userId);
                    int domainId = StrTool.parseInt(userIdandDomainId.split(":")[1]);
                    uInfo.setUserId(userId);
                    uInfo.setDomainId(domainId);
                    if (StrTool.objNotNull(radprofileid)) {
                        uInfo.setRadProfileId(StrTool.parseInt(radprofileid));
                    }
                    ulist.add(uInfo);
                }
                userInfoServ.batchSetRadId(ulist);
                flag = true;
            } else if (StrTool.strEquals(oper, StrConstant.common_number_3)) { // 锁定
                flag = userInfoServ.updateUserLocked(keys, NumConstant.common_number_2);
            } else if (StrTool.strEquals(oper, StrConstant.common_number_4)) { // 解锁
                flag = userInfoServ.updateUserLocked(keys, NumConstant.common_number_0);
            } else if (StrTool.strEquals(oper, StrConstant.common_number_5)) { // 禁用
                flag = userInfoServ.updateUserAbled(keys, NumConstant.common_number_0);
            } else if (StrTool.strEquals(oper, StrConstant.common_number_6)) { // 启用
                flag = userInfoServ.updateUserAbled(keys, NumConstant.common_number_1);
            } else if (StrTool.strEquals(oper, StrConstant.common_number_7)) { //批量设置后端认证
                Iterator<?> itera = keys.iterator();
                List<Object> ulist = new ArrayList<Object>();
                while (itera.hasNext()) {
                    UserInfo uInfo = new UserInfo();
                    String userIdandDomainId = (String) itera.next();
                    String userId = userIdandDomainId.split(":")[0];
                    userId = aide.replaceUserId(userId);
                    int domainId = StrTool.parseInt(userIdandDomainId.split(":")[1]);
                    uInfo.setUserId(userId);
                    uInfo.setDomainId(domainId);
                    if (StrTool.objNotNull(backendAuth)) {
                        uInfo.setBackEndAuth(StrTool.parseInt(backendAuth));
                    }
                    ulist.add(uInfo);
                }
                userInfoServ.batchSetBackendId(ulist);
                flag = true;
            } else if (StrTool.strEquals(oper, StrConstant.common_number_8)) { //批量设置本地认证模式
                Iterator<?> itera = keys.iterator();
                List<Object> ulist = new ArrayList<Object>();

                //配置默认密码
                String defaultPass = ConfConfig.getConfValue(ConfConstant.CONF_TYPE_USER + "_"
                        + ConfConstant.DEFAULT_USER_PWD);
                while (itera.hasNext()) {
                    UserInfo uInfo = new UserInfo();
                    String userIdandDomainId = (String) itera.next();
                    String userId = userIdandDomainId.split(":")[0];
                    userId = aide.replaceUserId(userId);
                    int domainId = StrTool.parseInt(userIdandDomainId.split(":")[1]);
                    uInfo.setUserId(userId);
                    uInfo.setDomainId(domainId);

                    // 静态密码
                    if (StrTool.strNotNull(pwd)) {
                        uInfo.setPwd(PwdEncTool.encPwd(pwd));
                    } else {
                        uInfo.setPwd(PwdEncTool.encPwd(defaultPass));
                    }
                    if (StrTool.objNotNull(localAuth)) {
                        uInfo.setLocalAuth(StrTool.parseInt(localAuth));
                    }
                    ulist.add(uInfo);
                }
                userInfoServ.batchSetLocalauth(ulist);
                flag = true;
            }
        } catch (BaseException e) {
            flag = false;
            logger.error(e.getMessage(), e);
        }

        return flag;
    }

    /**
     * 将LIST数据封装到set中
     * 方法说明
     * @Date in Feb 8, 2012,10:23:37 AM
     * @param queryForm
     * @param total
     * @return
     */
    public Set<?> convertLitToSets(UInfoQueryForm queryForm) {
        Set<String> keys = new HashSet<String>();
        try {
            PageArgument pageArg = new PageArgument();
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            UserInfo uiQuery = getUserInfo(queryForm);
            uiQuery.setUpPageTag(NumConstant.common_number_0);//不分页
            List<?> queryList = userInfoServ.query(uiQuery, pageArg);
            int size = queryList.size();
            if (StrTool.listNotNull(queryList)) {
                for (int i = 0; i < size; i++) {
                    UserInfo userInfo = (UserInfo) queryList.get(i);
                    keys.add(userInfo.getUserId() + ":" + userInfo.getDomainId());
                }
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return keys;
    }

    /**
     * 设置用户静态密码
     * 过期时间怎么处理
     */
    public String staticUserPass() {
        try {
            userInfo.setPwd(PwdEncTool.encPwd(userInfo.getPwd()));
            userInfoServ.updateStaticPass(userInfo);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_set_succ_tip"));
            return null;
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_set_error_tip"));
        }
        return null;
    }

    /* 
     * 找到该用户绑定的令牌 “仅仅”是该用户绑定的令牌
     */
    public List<?> querySingleUserToken() {
        List<?> tokens = new ArrayList<Object>();
        try {
            //String json="";
            UserToken utQuery = new UserToken();
            utQuery.setUserId(userInfo.getUserId());
            utQuery.setBindTag(NumConstant.common_number_0);//单用户绑定的令牌
            utQuery.setDomainId(userInfo.getDomainId());
            tokens = userTokenServ.queryMulUserToken(utQuery);//找到多用户绑定的令牌
            //if(StrTool.objNotNull(tokens)){
            //json=JsonTool.getJsonFromList(tokens.size(),tokens,null);
            //}
            //setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return tokens;
    }

    /* 
     * 找到该用户绑定的令牌同时该令牌被其他用户绑定
     */
    public List<?> queryMulUserToken() {
        List<?> tokens = new ArrayList<Object>();
        try {
            //String json="";
            UserToken utQuery = new UserToken();
            utQuery.setUserId(userInfo.getUserId());
            utQuery.setBindTag(NumConstant.common_number_1);//多用户绑定的令牌
            utQuery.setDomainId(userInfo.getDomainId());
            tokens = userTokenServ.queryMulUserToken(utQuery);//找到多用户绑定的令牌
            //if(StrTool.objNotNull(tokens)){
            //json=JsonTool.getJsonFromList(tokens.size(),tokens,null);
            //}
            //setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return tokens;
    }

    /**
     * 首页快速查询 获取令牌 和 用户的查询数量
     * 方法说明
     * @Date in May 24, 2013,11:04:54 AM
     * @return
     */
    public String countTknOrUserAtHome() {
        String result = "0,0";
        try {
            String queryText = request.getParameter("queryText");

            //查询用户数量
            UserInfo uInfo = getUserInfo(queryForm);
            uInfo.setUserId(queryText);
            int totalRow = userInfoServ.count(uInfo);

            if (totalRow != 0) {
                result = totalRow + ",0";
            } else {
                //查询令牌数量
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setToken(queryText);
                tokenInfo = aideToken
                        .setAdminDomainIds(tokenInfo, super.getCurLoginUserRole(), super.getCurLoginUser());
                totalRow = tokenServ.count(tokenInfo);
                result = "0," + totalRow;
            }
            outPutOperResultString(Constant.alert_succ, result);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResultString(Constant.alert_error, result);
        }

        return null;
    }

    /**
     * 刷新页面，检查配置
     * @return
     * @throws Exception
     */
    public String checkConfi() throws Exception {
        String confType = request.getParameter("confType");
        String confMark = request.getParameter("confMark");
        String bindToneks = ConfDataFormat.getConfValue(confType, confMark);
        outPutOperResultString(Constant.alert_succ, bindToneks);
        return null;
    }
    
    /*
     * 初始化用户更换令牌列表 
     * 根据条件筛选令牌列表
     * 1前提非作废的令牌
     * 2根据当前分配用户所在的机构(域或组织机构) 筛选出本机构的所有令牌 ；《但是在确定时要判断绑定上限 给出提示》
     * 3如果不是当前分配用户所在机构（必选） 1）如果是域 那么筛选该域下的 未绑定的令牌
     * 							2）如果是组织机构 那么筛选该机构下的 未绑定的令牌
     * 
     * @return
     */
    public String initNewToken() {
        if (isNeedClearForm()) {
            tokenQueryForm = null;
        }
        try {
            String domainId = request.getParameter("domainId"); //当前用户所在域
            String orgunitId = request.getParameter("orgunitId"); //当前用户所在机构
            String bindState = request.getParameter("bindState");
            //设置查询条件
            TokenInfo tiQuery = new TokenInfo();

            // 交换令牌时如果组织机构为0，拼接相关SQL
            if (StrTool.strEquals(orgunitId, StrConstant.common_number_0)) {
                tiQuery.setTflag(1);
            }
            tiQuery.setLogout(0);//未禁用的令牌
            if (!StrTool.objNotNull(tokenQueryForm)) { //空条件
                tiQuery.setToken("");//令牌号
                tiQuery.setDomainid(StrTool.parseInt(domainId)); //
                tiQuery.setOrgunitid(StrTool.parseInt(orgunitId) == NumConstant.common_number_0 ? null : StrTool
                        .parseInt(orgunitId));//
                if (StrTool.strNotNull(bindState)) {
                    tiQuery.setBind(Integer.parseInt(bindState));
                }
            } else {

                tiQuery.setToken(tokenQueryForm.getToken());//令牌号
                if (tokenQueryForm.getOrgunitIds().indexOf(",") == -1) { //如果要查询的机构是 空的
                    tiQuery.setDomainid(null); //不参与条件匹配
                    tiQuery.setOrgunitid(0);//不参与条件匹配
                } else { //有组织机构
                    int queryDomainId = StrTool.parseInt(tokenQueryForm.getOrgunitIds().split(",")[0].split(":")[0]);
                    int queryOrgunitId = StrTool.parseInt(tokenQueryForm.getOrgunitIds().split(",")[0].split(":")[1]);
                    tiQuery.setDomainid(queryDomainId);
                    if (queryDomainId != NumConstant.common_number_0 && queryOrgunitId == NumConstant.common_number_0) { //如果是域
                        tiQuery.setOrgunitid(null);
                    } else {
                        tiQuery.setOrgunitid(queryOrgunitId);
                    }
                    tiQuery.setBind(tokenQueryForm.getBindState());
                    tiQuery.setUserId(tokenQueryForm.getUserId());
                }
            }

            String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
            String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段
            if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {//如果是超级管理员
                tiQuery.setIsFliterTag(null); //不根据组织机构顾虑
            } else {
                tiQuery.setIsFliterTag(1); //根据组织机构顾虑
                tiQuery.setCurrentAdminId(curLoginUserId);
            }

            int total = tokenServ.countCT(tiQuery); //统计数量
            PageArgument pageArg = getArgument(total);
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<TokenInfo> resultList = (List<TokenInfo>) tokenServ.queryCT(tiQuery, pageArg);
            for (TokenInfo tokenInfo : resultList) {
                tokenInfo = tokenAide.setDomainAndOrgunitName(tokenInfo);
            }
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    
    /**
     * 获得用户绑定的列表
     * @author LXH
     * @date Feb 28, 2014 11:06:40 AM
     * @return
     */
    public String initOldToken() {
        //一对多的解绑时提供对令牌的选择
        try {
            userInfo.setUserId(aide.replaceUserId(userInfo.getUserId()));
            List<?> resultList = aide.getJoinTokens(userTokenServ, userInfo.getUserId(), userInfo.getDomainId());
            String jsonStr = JsonTool.getJsonFromList(resultList.size(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    
    /**
     * 获取用户首次登录用户门户密码验证方式
     */
    public String getPortalInitPwdVType() {
        String vtype = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_PORTAL,
                ConfConstant.INIT_PWD_LOGIN_VERIFY_TYPE);
        if (StrTool.strNotNull(vtype)) {
            outPutOperResult(Constant.alert_succ, vtype);
        } else {
            outPutOperResult(Constant.alert_error, "error");
        }
        return null;
    }

}
