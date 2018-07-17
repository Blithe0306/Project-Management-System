/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.action.aide;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员令牌绑定Action辅助类
 *
 * @Date in Apr 28, 2013,1:42:05 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AdminBindActionAide {

    // 管理员和组织机构关系业务操作
    public IAdminAndOrgunitServ adminAndOrgunitServ = (IAdminAndOrgunitServ) AppContextMgr
            .getObject("adminAndOrgunitServ");

    //令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");
    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    // 管理员角色接口
    private IRoleInfoServ roleInfoServ = (IRoleInfoServ) AppContextMgr.getObject("roleInfoServ");

    /*
     * 解析userId:domainId
     * param userIdAndDomainId
     * retrun 
     */
    public Object[] udId(String userIdAndDomainId) {
        Object[] results = new Object[2];

        String[] arr = userIdAndDomainId.split(":");
        String userId = null;
        String domainId = null;
        if (StrTool.arrNotNull(arr)) {
            if (arr.length == 1) {//只有用户 没有域
                userId = arr[0];
            } else {
                userId = arr[0];
                domainId = arr[1];
            }
        }
        results[0] = userId;
        results[1] = domainId;

        return results;
    }

    /**
     * 统计管理员已绑定令牌、令牌已被管理员和用户绑定的次数
     * 注意：userid 如果传入null ，token 如果不为null 则查询的是令牌被绑定的次数 
     *      反之是用户绑定的令牌数
     * @Date in Jul 2, 2013,8:28:46 PM
     * @param userId userid:domainId形式
     * @param token
     * @return 绑定的次数
     * @throws BaseException
     */
    public int utBindCount(String userId, String token) throws BaseException {
        UserToken userToken = new UserToken();

        // 查询用户已绑定的令牌数
        if (StrTool.strNotNull(userId)) {
            Object[] uIdArr = udId(userId);

            String uId = null;
            Integer dId = 0;
            if (StrTool.objNotNull(uIdArr[0])) {
                uId = (String) uIdArr[0];
            }

            if (StrTool.objNotNull(uIdArr[1])) {
                dId = StrTool.parseInt((String) uIdArr[1]);
            }

            dId = dId == 0 ? -1 : dId;
            userToken.setDomainId(dId);// 如果域为0 则赋值域为null
            userToken.setUserId(uId);
        } else {
            userToken.setUserId(null);
        }

        // 查询令牌绑定已绑定的用户数包括用户和管理员
        if (StrTool.strNotNull(token)) {
            userToken.setToken(token);
        } else {
            userToken.setToken(null);
        }

        // 返回绑定次数
        return userTokenServ.count(userToken);
    }

    /**
     * 选择的用户与令牌虽然未都达到绑定上限，但需要检查是否已经绑定，重复进行绑定
     * 
     * @Date in Jul 2, 2013,8:28:04 PM
     * @param userId
     * @param token
     * @return
     */
    public boolean oneUTBind(String userId, String token) {
        boolean isBind = false;

        UserToken userToken = new UserToken();
        if (!StrTool.strNotNull(userId) || !StrTool.strNotNull(token)) {
            return isBind;
        }
        Object[] results = udId(userId);
        String uId = (String) results[NumConstant.common_number_0];
        int domainId = StrTool.parseInt((String) results[NumConstant.common_number_1]);

        userToken.setUserId(uId);
        userToken.setDomainId(domainId == 0 ? null : domainId);
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
     * 旧令牌列表不为空，新令牌列表不为空，既执行解绑又执行绑定操作
     * 
     * @Date in Jul 2, 2013,8:26:43 PM
     * @param hidTkns
     * @param tokens
     * @param adminInfo
     * @param tknMaxBindUsr
     * @param usrMaxBindTkn
     * @return 返回信息 返回null 成功，否则异常信息 
     * @throws BaseException
     */
    public String upUTBind_2(List<?> hidTkns, List<?> tokens, AdminUser adminInfo, int tknMaxBindUsr,
            int usrMaxBindTkn, HttpServletRequest request) throws BaseException {

        // 检索出旧令牌列表中比新列表中多出的令牌（即需要删除的令牌）  
        List<String> noeqList = new ArrayList<String>();
        boolean iseq = false;
        for (int i = 0; i < hidTkns.size(); i++) {
            iseq = false;
            String htoken = (String) hidTkns.get(i);
            for (int k = 0; k < tokens.size(); k++) {
                String token = (String) tokens.get(k);
                if (StrTool.strEquals(htoken, token)) {
                    iseq = true;
                    break;
                }
            }
            if (!iseq) {
                noeqList.add(htoken);
            }
        }

        if (StrTool.listNotNull(noeqList)) {
            UserToken userToken = new UserToken();
            userToken.setUserId(adminInfo.getAdminid());
            userToken.setDomainId(-1);
            userToken.setTokenIds(noeqList);

            // 更换令牌，被更换令牌状态改变 rstate：0：继续使用；1：停用，2：作废
            int rstate = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                    ConfConstant.CORE_REPLACE_STATE_SELECT));
            if (rstate != NumConstant.common_number_0) {
                for (int k = 0; k < noeqList.size(); k++) {
                    String token = noeqList.get(k);
                    UserToken userTkn = new UserToken();
                    userTkn.setToken(token);

                    // 查看此令牌是否被其它用户绑定
                    int count = userTokenServ.count(userTkn);

                    // 等于1代表只有一个用户绑定
                    if (count == NumConstant.common_number_1) {
                        TokenInfo tokenInfo = new TokenInfo();
                        tokenInfo.setToken(token);
                        if (rstate == NumConstant.common_number_1) {
                            tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 1);//停用
                            tokenServ.updateTokenState(tokenInfo);
                        } else if (rstate == NumConstant.common_number_2) {
                            tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 6);//作废
                            tokenServ.updateTokenState(tokenInfo);
                        }
                    }
                }
            }

            //更换令牌时先解除用户与旧令牌的绑定关系
            userTokenServ.delObj(userToken);
        }

        //检索出新令牌列表中比旧列表中多出的令牌（即需要添加的令牌）
        List<String> tokenList = new ArrayList<String>();
        for (int i = 0; i < tokens.size(); i++) {
            iseq = false;
            String token = (String) tokens.get(i);
            for (int k = 0; k < hidTkns.size(); k++) {
                String htoken = (String) hidTkns.get(k);
                if (StrTool.strEquals(token, htoken)) {
                    iseq = true;
                    break;
                }
            }

            if (!iseq) {
                tokenList.add(token);
            }
        }

        //添加管理员令牌绑定关系
        if (StrTool.listNotNull(tokenList)) {
            return addUT(adminInfo, tokenList, tknMaxBindUsr, usrMaxBindTkn, request);
        }

        return null;
    }

    /**
     * 添加管理员令牌对应绑定关系
     * 
     * @Date in Jul 2, 2013,4:10:37 PM
     * @param adminInfo
     * @param tknList
     * @param tknMaxBindUsr
     * @param usrMaxBindTkn
     * @return
     * @throws BaseException
     */
    public String addUT(AdminUser adminInfo, List<?> tknList, int tknMaxBindUsr, int usrMaxBindTkn,
            HttpServletRequest request) throws BaseException {
        String userId = adminInfo.getAdminid() + ":";

        //将要添加的绑定令牌集合
        List<Object> utList = new ArrayList<Object>();

        //判断用户是否超出最大绑定令牌数据限制
        int usrCount = utBindCount(userId, null);
        int num = usrMaxBindTkn - usrCount;
        if (num > 0) {
            UserToken userToken = null;
            for (int m = 0; m < tknList.size(); m++) {
                if (m >= num) {
                    break;
                }

                String tknStr = (String) tknList.get(m);
                //判断当前令牌被绑定次数是否超出最大限制
                int tknCount = utBindCount(null, tknStr);
                if (tknCount < tknMaxBindUsr) {
                    userToken = new UserToken();
                    userToken.setUserId(adminInfo.getAdminid());
                    userToken.setToken(tknStr);
                    userToken.setDomainId(null);
                    userToken.setBindTime(StrTool.timeSecond());
                    utList.add(userToken);
                } else {
                    // 令牌绑定用户数超出范围
                    return Language.getLangStr(request, "admin_bind_token_err");
                }
            }
        } else {
            return Language.getLangStr(request, "admin_bind_token_err_2");
        }

        if (StrTool.listNotNull(utList)) {
            //用户令牌对应关系添加
            userTokenServ.addUsrTkn(utList);
        } else {
            return Language.getLangStr(request, "admin_bind_token_err_3");
        }

        return null;
    }

    /*
     * 整理用户实例数据
     * 用户账号大小写转换
     */
    public UserInfo reUserId(UserInfo userInfo) {
        int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.USERID_FORMAT_TYPE));
        if (exchangeTag == NumConstant.common_number_0) { //不转换

        } else if (exchangeTag == NumConstant.common_number_1) { //大-小
            userInfo.setUserId(userInfo.getUserId().toLowerCase());
        } else if (exchangeTag == NumConstant.common_number_2) {//小-大
            userInfo.setUserId(userInfo.getUserId().toUpperCase());
        }
        return userInfo;
    }

    /*
     * 将用户名中的[]符号转换为&符号
     * @param userId
     * @return String
     */
    public String replaceUserId(String userId) {
        String newUserId = userId;
        if (StrTool.objNotNull(userId)) {
            newUserId = userId.replace(StrConstant.common_char2, StrConstant.common_char1);
        }
        return newUserId;
    }

    /**
     * 取得用户绑定的令牌(一个或多个)，同时关联获取令牌的主要属性
     * @Date in May 3, 2011,3:17:07 PM
     * @param userId
     * @return
     * @throws BaseException
     */
    public List<?> getJoinTokens(String userId) throws BaseException {
        UserToken uToken = new UserToken();
        uToken.setUserId(userId);
        uToken.setDomainId(null);
        List<?> utList = userTokenServ.queryJoinTkn(uToken, new PageArgument());

        return utList;
    }

    /**
     * 获取管理员 管理的域，如果没有则返回默认域
     * @Date in May 6, 2013,3:48:55 PM
     * @param adminId
     * @return
     */
    public int[] getAdminDomains(String adminId) throws Exception {
        AdminAndOrgunit admAndOrg = new AdminAndOrgunit();
        admAndOrg.setAdminId(adminId);
        admAndOrg.setOrgunitId(null);
        List<?> admAndOrgList = adminAndOrgunitServ.queryAdminAndOrgunitByAdminId(admAndOrg);
        int size = 0;
        if(!StrTool.listNotNull(admAndOrgList)){
        	size = 1;
        }else{
        	size = admAndOrgList.size();
        }
        int admDomains[] = new int[size];
        if (!StrTool.listNotNull(admAndOrgList)) {
            // 如果管理员没有管理员 则获取默认域
            admDomains[0] = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DEFAULT_DOMAIN_ID));
        } else {
            for (int i = 0; i < admAndOrgList.size(); i++) {
                AdminAndOrgunit adminAndOrgunit = (AdminAndOrgunit) admAndOrgList.get(i);
                if (adminAndOrgunit.getDomainId() != 0) {
                    admDomains[i] = adminAndOrgunit.getDomainId();
                }
            }
        }

        return admDomains;
    }

    /**
     * 获取管理员是否是有超级管理员角色
     * @Date in May 6, 2013,4:39:36 PM
     * @param adminId
     * @return
     */
    public boolean isAdminRoleByAdminId(String adminId) throws Exception {
        RoleInfo queryRole = new RoleInfo();
        //查询当前登录用户创建的角色和自己所属的角色（即上级分配的角色）
        queryRole.setLoginUser(adminId);
        List<?> roleList = roleInfoServ.query(queryRole, new PageArgument());

        if (!StrTool.listNotNull(roleList)) {
            return false;
        }

        RoleInfo[] roles = roleList.toArray(new RoleInfo[roleList.size()]);
        for (RoleInfo roleInfo : roles) {
            if (StrTool.strEquals(roleInfo.getRolemark(), "ADMIN")) {
                return true;
            }
        }

        return false;
    }

}
