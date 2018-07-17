/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action.aide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.form.UTokenQueryForm;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户Action业务处理辅助类
 *
 * @Date in Apr 26, 2011,6:15:07 PM
 *
 * @author TBM
 */
public class UInfoActionAide {

    private Logger logger = Logger.getLogger(UInfoActionAide.class);
    // 令牌服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ");
    //域服务接口
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");
    //组织机构服务接口
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");
    //用户服务接口
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");

    /**
     * 将用户列表、用户令牌列表、用户中的域信息、组织机构信息 等的数据整合为一个列表返回页面
     * @Date in Apr 28, 2011,8:23:44 AM
     * @param uiList
     * @param utList
     * @return
     * @throws BaseException 
     */
    public List<?> getUInfoList(List<?> uiList, List<?> utList, IDomainInfoServ domainInfoServ,
            IOrgunitInfoServ orgunitInfoServ) throws BaseException {
        List<UserInfo> uInfoList = new ArrayList<UserInfo>();
        if (!StrTool.listNotNull(utList)) {
            utList = new ArrayList<Object>();
        }

        //遍历用户列表
        UserInfo userInfo = null;
        String userId = "";
        for (int ui = 0; ui < uiList.size(); ui++) { //去掉重复项后再遍历 
            userInfo = (UserInfo) uiList.get(ui);
            userId = userInfo.getUserId();
            int uDomainId = userInfo.getDomainId();

            UserToken userToken = null;
            String utUserId = "";
            int utDomainId = 0;
            int utCount = 0;
            int orgunitId = 0;
            List<Object> tknArr = new ArrayList<Object>();
            //遍历用户令牌关系列表
            for (int ut = 0; ut < utList.size(); ut++) {
                userToken = (UserToken) utList.get(ut);
                utUserId = userToken.getUserId();
                utDomainId = userToken.getDomainId();
                if (utUserId.equals(userId) && utDomainId == uDomainId) {

                    // 存储令牌机构ID
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setToken(userToken.getToken());

                    // 根据令牌号查出令牌信息
                    tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);
                    orgunitId = tokenInfo.getOrgunitid();
                    userToken.setOrgunitId(orgunitId);

                    tknArr.add(userToken);
                    utCount++;
                }
            }
            if (utCount > 0) {
                userInfo.setTokens(tknArr);
            }
            //设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
            userInfo = setDOstr(userInfo);

            uInfoList.add(userInfo);
        }

        return uInfoList;
    }

    /*
     *设置该用户列表中的 域对象、机构对象、域组织机构id串、域组织机构名字串 
     */
    public UserInfo setDOstr(UserInfo userInfo) {
        try {
            String dOrgunitId = "";
            String dOrgunitName = ""; //域组织机构串
            DomainInfo diQuery = new DomainInfo();
            diQuery.setDomainId(userInfo.getDomainId());
            diQuery.setDomainSn(null);
            diQuery.setDomainName(null);
            diQuery.setCreateTime(0);
            DomainInfo domainInfo = (DomainInfo) domainInfoServ.find(diQuery);
            userInfo.setDomainInfo(domainInfo);//设置域对象
            dOrgunitId = dOrgunitId + domainInfo.getDomainId();
            dOrgunitName = dOrgunitName + domainInfo.getDomainName();

            if (StrTool.objNotNull(userInfo.getOrgunitId()) && userInfo.getOrgunitId() != 0) { //如果该用户只属于域 不属于某个组织机构
                OrgunitInfo oiQuery = new OrgunitInfo();
                oiQuery.setOrgunitId(userInfo.getOrgunitId());
                oiQuery.setOrgunitNumber(null);
                oiQuery.setDomainId(0);
                oiQuery.setCreateTime(0);
                OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);
                userInfo.setOrgunitInfo(orgunitInfo);//设置组织机构对象
                dOrgunitId = dOrgunitId + ":" + orgunitInfo.getOrgunitId() + ",";
                //dOrgunitName = dOrgunitName + "-->" + orgunitInfo.getOrgunitName();
                //只显示机构
                dOrgunitName = orgunitInfo.getOrgunitName();
            } else {
                dOrgunitId = dOrgunitId + ":" + 0 + ",";
            }
            userInfo.setDOrgunitId(dOrgunitId);
            userInfo.setDOrgunitName(dOrgunitName);//设置域组织机构串

        } catch (BaseException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /**
     * 取得用户绑定的令牌(一个或多个)
     * @Date in Apr 26, 2011,3:20:17 PM
     * @return
     */
    public List<?> getTokens(IUserTokenServ userTokenServ, String userId, int domainId) throws BaseException {
        UTokenQueryForm uTokenQueryForm = new UTokenQueryForm();
        uTokenQueryForm.setUserId(userId);
        uTokenQueryForm.getUserToken().setDomainId(domainId);
        List<?> utList = userTokenServ.selTokens(uTokenQueryForm.getUserToken());

        return utList;
    }

    /**
     * 取得用户绑定的令牌(一个或多个)，同时关联获取令牌的主要属性
     * @Date in May 3, 2011,3:17:07 PM
     * @param userTokenServ
     * @param userId
     * @param domainId
     * @return
     * @throws BaseException
     */
    public List<?> getJoinTokens(IUserTokenServ userTokenServ, String userId, int domainId) throws BaseException {
        UserToken uToken = new UserToken();
        uToken.setUserId(userId);
        uToken.setDomainId(domainId == 0 ? null : domainId);
        List<?> utList = userTokenServ.queryJoinTkn(uToken, new PageArgument());

        return utList;
    }

    /**
     * 判断令牌列表是否为空
     * @Date in Apr 28, 2011,9:12:53 AM
     * @param length
     * @return
     */
    public boolean isSelectNo(String length) {
        if (StrTool.strEquals(length, "0")) {
            return true;
        }

        return false;
    }

    /**
     * 更换令牌时，取得新选择令牌列表与原有令牌列表中不相等的令牌
     * @Date in Apr 28, 2011,9:11:35 AM
     * @param tknArr
     * @param hidTknArr
     * @return
     */
    public String[] getTknArr(String[] tknArr, String[] hidTknArr) {
        if (!StrTool.arrNotNull(hidTknArr)) {
            return tknArr;
        }

        List<String> list = new ArrayList<String>();
        boolean unLike = false;
        //添加绑定
        if (tknArr.length > hidTknArr.length) {
            for (int i = 0; i < tknArr.length; i++) {
                unLike = false;
                String token = tknArr[i];
                for (int k = 0; k < hidTknArr.length; k++) {
                    if (token.equals(hidTknArr[k])) {
                        unLike = true;
                    }
                }
                if (!unLike) {
                    list.add(token);
                }
            }
        }
        //解除绑定
        else {
            for (int i = 0; i < hidTknArr.length; i++) {
                unLike = false;
                String hidTkn = hidTknArr[i];
                for (int k = 0; k < tknArr.length; k++) {
                    String token = tknArr[k];
                    if (hidTkn.equals(token)) {
                        unLike = true;
                    }
                }
                if (!unLike) {
                    list.add(hidTkn);
                }
            }
        }

        return list.toArray(new String[0]);
    }

    public boolean tknArrNoEqHidTkn(String[] tknArr, String[] hidTknArr) {
        if (!StrTool.arrNotNull(hidTknArr)) {
            return false;
        }

        int eqCount = 0;
        boolean unLike = false;
        if (tknArr.length >= hidTknArr.length) {
            for (int i = 0; i < tknArr.length; i++) {
                unLike = false;
                String token = tknArr[i];
                for (int k = 0; k < hidTknArr.length; k++) {
                    if (token.equals(hidTknArr[k])) {
                        unLike = true;
                    }
                }
                if (!unLike) {
                    eqCount += 1;
                }
            }
            if (eqCount == tknArr.length) {
                return false;
            }
        } else {
            for (int i = 0; i < hidTknArr.length; i++) {
                unLike = false;
                String hidTkn = hidTknArr[i];
                for (int k = 0; k < tknArr.length; k++) {
                    String token = tknArr[k];
                    if (hidTkn.equals(token)) {
                        unLike = true;
                    }
                }
                if (!unLike) {
                    eqCount += 1;
                }
            }
            if (eqCount == hidTknArr.length) {
                return false;
            }
        }

        return true;
    }

    /**
     * 旧令牌列表不为空，新令牌列表为空，只执行解绑操作
     * @Date in May 26, 2011,10:30:42 AM
     * @param hidTkns
     * @param userId
     * @param userTokenServ
     * @throws BaseException
     */
    public void upUTBind(List<?> hidTkns, String userId, IUserTokenServ userTokenServ) throws BaseException {
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setTokenIds(hidTkns);

        //更换令牌时解除用户与旧令牌的绑定关系
        userTokenServ.delObj(userToken);
    }

    /**
     * 旧令牌列表不为空，新令牌列表不为空，既执行解绑又执行绑定操作
     * @Date in May 10, 2011,11:19:16 AM
     * @param hidTkns
     * @param tokens
     * @param userId
     * @param userTokenServ
     * @param bindAide
     * @param tknMaxBindUsr
     * @param usrMaxBindTkn
     * @throws BaseException
     */
    public Map<String, Object> upUTBind_2(ITokenServ tokenServ, List<?> hidTkns, List<?> tokens, UserInfo userInfo,
            IUserTokenServ userTokenServ, UserBindActionAide bindAide, int tknMaxBindUsr, int usrMaxBindTkn)
            throws BaseException {
        //检索出旧令牌列表中与新列表中不相同的令牌
        List<String> noeqList = new ArrayList<String>();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean iseq = false;
        String flag = "0";
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

        //检索出新令牌列表中与旧列表中不相同的令牌
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

        // 检索出选中的令牌被绑定次数是否超出最大限制
        if (StrTool.listNotNull(tokenList)) {
            List<Object> utList = new ArrayList<Object>();
            List<String> newTknList = new ArrayList<String>();
            String tokenObject = "";
            boolean tkn = true;
            for (int i = 0; i < tokenList.size(); i++) {
                String newToken = (String) tokenList.get(i);

                // 判断当前令牌被绑定次数是否超出最大限制
                int tknCount = bindAide.utBindCount(userTokenServ, null, newToken, utList);
                if (tknCount >= tknMaxBindUsr) {
                    tkn = false;
                } else {
                    tkn = true;
                }

                // 超出最大限制的令牌放到一个集合里
                if (!tkn) {
                    newTknList.add(newToken);

                    // 把不符合的令牌拼成一个串
                    tokenObject = tokenObject + "," + newToken;
                }
            }
            if (newTknList.size() > 0) {
                flag = "1";
                map.put(StrConstant.USR_TKN_TOKENS, tokenObject.substring(1, tokenObject.length()));
            }
        }

        // 如果FLAG为0证明选中的令牌绑定次数有超出最大限制，不可以解除关系和添加绑定关系
        if (StrTool.strEquals(flag, "0")) {
            if (StrTool.listNotNull(noeqList)) {
                UserToken userToken = new UserToken();
                userToken.setUserId(userInfo.getUserId());
                userToken.setDomainId(userInfo.getDomainId() == 0 ? null : userInfo.getDomainId());
                userToken.setTokenIds(noeqList);

                // 更换令牌，被更换令牌状态改变
                // rstate：0：继续使用；1：停用，2：作废
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
                                tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, 6); // 作废
                                tokenServ.updateTokenState(tokenInfo);
                            }
                        }
                    }
                }

                //更换令牌时先解除用户与旧令牌的绑定关系
                userTokenServ.delObj(userToken);
            }

            //添加用户令牌绑定关系
            addUT(tokenServ, userInfo, tokenList, userTokenServ, bindAide, tknMaxBindUsr, usrMaxBindTkn);
        }
        map.put(StrConstant.USR_TKN_SUCC, flag);
        return map;
    }

    /**
     * 添加用户令牌对应绑定关系
     * 重构：因为要 迁移令牌的组织机构
     * 所以 @parm userId 换为 userInfo 
     */
    public String addUT(ITokenServ tokenServ, UserInfo userInfo, List<?> tknList, IUserTokenServ userTokenServ,
            UserBindActionAide bindAide, int tknMaxBindUsr, int usrMaxBindTkn) throws BaseException {
        String userId = userInfo.getUserId() + ":" + userInfo.getDomainId() + ":" + userInfo.getOrgunitId();
        if (!StrTool.listNotNull(tknList)) {
            return StrConstant.USR_TKN_ERR_06;
        }

        List<Object> utList = new ArrayList<Object>();
        UserToken userToken = null;
        //判断用户是否超出最大绑定令牌数据限制
        int usrCount = bindAide.utBindCount(userTokenServ, userId, null, utList);
        int num = usrMaxBindTkn - usrCount;
        if (num > 0) {
            for (int m = 0; m < tknList.size(); m++) {
                if (m >= num) {
                    break;
                }
                String tknStr = (String) tknList.get(m);
                //判断当前令牌被绑定次数是否超出最大限制
                int tknCount = bindAide.utBindCount(userTokenServ, null, tknStr, utList);
                if (tknCount < tknMaxBindUsr) {
                    userToken = new UserToken();
                    userToken.setUserId(userInfo.getUserId());
                    userToken.setToken(tknStr);
                    userToken.setDomainId(userInfo.getDomainId() == 0 ? null : userInfo.getDomainId());
                    userToken.setBindTime(DateTool.dateToInt(new Date()));
                    utList.add(userToken);
                }
            }
        } else {
            return StrConstant.USR_TKN_ERR_01;
        }

        if (StrTool.listNotNull(utList)) {
            //如果用户没有域信息 则不用迁移了  管理员没有域信息
            // 令牌绑定后是否迁移至用户所在的组织机构下 1为是；0为否；CORE_CONF_MARK
            if (StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                    ConfConstant.TK_BIND_IS_CHANGE_ORG), StrConstant.common_number_1)) {
                if (userInfo.getDomainId() != NumConstant.common_number_0) {
                    //令牌迁移机构
                    Iterator<?> it = utList.iterator();
                    while (it.hasNext()) {
                        UserToken utk = (UserToken) it.next();
                        UserToken utkQuery = new UserToken();
                        utkQuery.setToken(utk.getToken());
                        List<?> ukList = userTokenServ.query(utkQuery, new PageArgument());
                        if (ukList.size() == NumConstant.common_number_0) {//如果没被绑定过 那么迁移
                            TokenInfo tokInfo = new TokenInfo();
                            tokInfo.setToken(utk.getToken());
                            tokInfo.setDomainid(userInfo.getDomainId());
                            if (userInfo.getOrgunitId() == NumConstant.common_number_0) {
                                tokInfo.setOrgunitid(null);
                            } else {
                                tokInfo.setOrgunitid(userInfo.getOrgunitId());
                            }
                            tokenServ.updateTokenOrg(tokInfo);
                        }
                    }
                }
            }

            //用户令牌对应关系添加
            userTokenServ.addUsrTkn(utList);

        } else {
            return StrConstant.USR_TKN_ERR_05;
        }

        return StrConstant.USR_TKN_ERR_00;
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
        }
        return userInfo;
    }

    /*
     * 整理用户实例数据
     */
    public UserInfo reUserInfo(UserInfo userInfo) {
        userInfo.setUserId(replaceUserId(userInfo.getUserId()));
        //整理用户信息  域和组织机构
        if (StrTool.objNotNull(userInfo.getDOrgunitId()) && userInfo.getDOrgunitId().indexOf(",") != -1) { //这个条件一定成立
            int[] results = outIds(userInfo.getDOrgunitId()); //解析域id 和组织机构id
            int domainId = results[0];
            int orgunitId = results[1];
            userInfo.setDomainId(domainId); //设置域id
            if (orgunitId == NumConstant.common_number_0) {//如果组织机构是0 表示null的
                userInfo.setOrgunitId(null);
            } else {
                userInfo.setOrgunitId(orgunitId);
            }
        }
        //整理用户信息 返回给客户端Radius属性的配置ID
        if (userInfo.getRadProfileId() == NumConstant.common_number_0
                || userInfo.getRadProfileId().equals(NumConstant.common_number_0)) { //如果不返回
            userInfo.setRadProfileId(null);
        }
        return userInfo;
    }

    /*
     * 根据传入的domainId:orgunitId, 解析域domainId 和 orgunitId 
     * param dOrgunitidStr
     * return 
     */
    public int[] outIds(String doStr) {
        int[] results = new int[NumConstant.common_number_2];
        if (StrTool.objNotNull(doStr) && doStr.indexOf(",") != -1) { //这个条件一定成立
            String dOrgunitidStr = doStr.split(",")[NumConstant.common_number_0]; //userInfo.getDOrgunitId()  domainId:orgunitId,
            int domainId = StrTool.parseInt(dOrgunitidStr.split(":")[NumConstant.common_number_0]);
            int orgunitId = StrTool.parseInt(dOrgunitidStr.split(":")[NumConstant.common_number_1]);
            results[NumConstant.common_number_0] = domainId;
            results[NumConstant.common_number_1] = orgunitId;
            return results;
        } else {
            return null;
        }

    }

    /*
     * 判断指定域下的用户是否存在
     * param userInfoServ
     * param userInfo
     * retrun 
     */
    public boolean findUserIsExist(IUserInfoServ userInfoServ, UserInfo userInfo) {
        boolean isExsitTag = false;
        int exchangeTag = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.USERID_FORMAT_TYPE));
        try {
            if (StrTool.strNotNull(userInfo.getDOrgunitId())) { //如果有机构判断
                int[] resulets = outIds(userInfo.getDOrgunitId());
                userInfo.setDomainId(resulets[0]);
            }
            if (exchangeTag == NumConstant.common_number_1) {
                userInfo.setUserId(userInfo.getUserId().toLowerCase()); // 转换小写
            }
            UserInfo findInfo = (UserInfo) userInfoServ.find(userInfo);
            if (StrTool.objNotNull(findInfo)) {
                isExsitTag = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return isExsitTag;
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
     * 用户机构变更
     * @param userArr
     * @param tokenArr
     * @param changeType
     * @param userTokenServ
     * @param userChangeServ
     * @param tokenServ
     * @return
     * @throws Exception 
     */
    public Map<String, Object> getUCList(String[] userArr, String[] tokenArr, int changeType,
            IUserTokenServ userTokenServ, IUserInfoServ userChangeServ, ITokenServ tokenServ, int orginId, int domainId)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        switch (changeType) {
            // 该用户下没有绑定令牌或为域下用户，没有可变更的绑定令牌
            case 0:
                map = getUCList_1(changeType, userChangeServ, userArr, tokenArr, orginId, domainId);
                break;

            // 不变更绑定令牌的组织机构，但解除绑定关系
            case 1:
                map = getUCList_2(changeType, userChangeServ, userTokenServ, userArr, tokenArr, orginId, domainId);
                break;

            // 变更绑定的独自使用的组织机构令牌，解除共同使用的绑定令牌的绑定关系
            case 2:
                map = getUCList_3(changeType, userChangeServ, userTokenServ, userArr, tokenArr, orginId, domainId);
                break;

            // 变更绑定的所有组织机构令牌，解除公共绑定令牌中其它的绑定关系
            case 3:
                map = getUCList_4(changeType, userChangeServ, userTokenServ, userArr, tokenArr, orginId, domainId);
                break;
        }
        return map;
    }

    /**
     * 没有绑定令牌或为域下用户，没有可变更的绑定令牌
     * @param changeType
     * @param userChangeServ
     * @param userArr
     * @param orginId
     * @param domainId
     * @return
     * @throws BaseException
     */
    private Map<String, Object> getUCList_1(int changeType, IUserInfoServ userChangeServ, String[] userArr,
            String[] tokenArr, int orginId, int domainId) throws BaseException {

        String userId = null;
        String errStr = "";
        int tknNum = 0; // 令牌数
        int unbindNum = 0; // 令牌解绑个数
        int changeNum = 0; // 令牌变更个数
        int userNum = 0;

        // 去掉数组中，重复的令牌，且封装成LIST
        List<String> list = new ArrayList<String>();
        if (StrTool.arrNotNull(tokenArr)) {
            for (int i = 0; i < tokenArr.length; i++) {
                if (!list.contains(tokenArr[i])) {
                    list.add(tokenArr[i]);
                }
            }
        }

        // 计算用户绑定令牌的总数，去重复
        if (StrTool.listNotNull(list)) {
            tknNum = list.size();
        } else {
            tknNum = 0;
        }
        // 直接机构变更
        for (int i = 0; i < userArr.length; i++) {
            userId = userArr[i];
            changeOrg(userId, orginId, domainId, userChangeServ);
            userNum++;
        }

        return getUCMap(userNum, tknNum, unbindNum, changeNum, errStr);
    }

    /**
     * 不变更绑定令牌的组织机构，但解除绑定关系
     * @param changeType
     * @param userChangeServ
     * @param userArr
     * @param orginId
     * @param domainId
     * @return
     * @throws Exception 
     */
    private Map<String, Object> getUCList_2(int changeType, IUserInfoServ userChangeServ, IUserTokenServ userTokenServ,
            String[] userArr, String[] tokenArr, int orginId, int domainId) throws Exception {
        String userId = null;
        String errStr = "";
        int tknNum = 0; // 令牌数
        Integer unbindNum = 0; // 令牌解绑个数
        int changeNum = 0; // 令牌变更个数
        int userNum = 0;
        Map<String, Object> map = new HashMap<String, Object>();

        // 去掉数组中，重复的令牌，且封装成LIST
        List<String> list = new ArrayList<String>();
        if (StrTool.arrNotNull(tokenArr)) {
	        for (int i = 0; i < tokenArr.length; i++) {
	            if (!list.contains(tokenArr[i])) {
	                list.add(tokenArr[i]);
	            }
	        }
        }

        // 计算用户绑定令牌的总数，去重复
        if (StrTool.listNotNull(list)) {
            tknNum = list.size();
        } else {
            tknNum = 0;
        }
        // 先解绑，再变更用户组织机构
        for (int i = 0; i < userArr.length; i++) {
            userId = userArr[i];
            UserToken userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setDomainId(domainId);
            userToken.setBindTag(2); // 表示只查询域下该用户绑定的令牌
            List<?> tokens = userTokenServ.queryMulUserToken(userToken);

            // 令牌解绑，是否将该令牌放回域中
            updateBind(tokens, userTokenServ);

            // 令牌解绑
            map = tokenUnBind(unbindNum, userArr, tokens, userId, orginId, domainId, userTokenServ, 0, 0);
            unbindNum = (Integer) map.get("unbindNum");

            // 变更用户组织机构
            changeOrg(userId, orginId, domainId, userChangeServ);
            userNum++;
        }

        return getUCMap(userNum, tknNum, unbindNum, changeNum, errStr);
    }

    /**
     * 变更绑定的独自使用的组织机构令牌，解除共同使用的绑定令牌的绑定关系
     * @param changeType
     * @param userChangeServ
     * @param userTokenServ
     * @param userArr
     * @param orginId
     * @param domainId
     * @return
     * @throws Exception
     */
    private Map<String, Object> getUCList_3(int changeType, IUserInfoServ userChangeServ, IUserTokenServ userTokenServ,
            String[] userArr, String[] tokenArr, int orginId, int domainId) throws Exception {
        String userId = null;
        String errStr = "";
        int tknNum = 0; // 令牌数
        Integer unbindNum = 0; // 令牌解绑个数
        int changeNum = 0; // 令牌变更个数
        int userNum = 0;

        // 去掉数组中，重复的令牌，且封装成LIST
        List<String> list = new ArrayList<String>();
        if (StrTool.arrNotNull(tokenArr)) {
	        for (int i = 0; i < tokenArr.length; i++) {
	            if (!list.contains(tokenArr[i])) {
	                list.add(tokenArr[i]);
	            }
	        }
        }

        // 计算用户绑定令牌的总数，去重复
        if (StrTool.listNotNull(list)) {
            tknNum = list.size();
        } else {
            tknNum = 0;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        List<String> tokenSS = new ArrayList<String>(); // 变更的令牌的集合
        List<String> tokenAr = new ArrayList<String>(); // 解绑的令牌集
        List<String> tokenArt = new ArrayList<String>(); // 解绑的令牌集，再次封装
        // 先解绑，再变更用户组织机构
        for (int i = 0; i < userArr.length; i++) {
            userId = userArr[i];
            UserToken userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setDomainId(domainId);
            userToken.setBindTag(1); // 共同使用的令牌
            List<?> tokens = userTokenServ.queryMulUserToken(userToken);

            // 令牌解绑
            map = tokenUnBind(unbindNum, userArr, tokens, userId, orginId, domainId, userTokenServ, 1, 0);
            unbindNum = (Integer) map.get("unbindNum");
            tokenAr = (List<String>) map.get("tokenAr"); // 解绑的令牌集

            // 把解绑的令牌放到一个LIST
            if (StrTool.listNotNull(tokenAr)) {
                for (int h = 0; h < tokenAr.size(); h++) {
                    tokenArt.add(tokenAr.get(h));
                }
            }

            // 变更用户组织机构
            changeOrg(userId, orginId, domainId, userChangeServ);
            userNum++;
        }

        // 取出非解绑，意味着需要进行变更的令牌
        for (String str : list) {
            if (!tokenArt.contains(str)) {
                tokenSS.add(str);
            }
        }

        // 去掉tokenSS的重复项
        if (StrTool.listNotNull(tokenSS)) {
            for (int i = 0; i < tokenSS.size() - 1; i++) {
                for (int j = tokenSS.size() - 1; j > i; j--) {
                    if (tokenSS.get(j).equals(tokenSS.get(i))) {
                        tokenSS.remove(j);
                    }
                }
            }
        }

        // 变更单独使用令牌
        changeNum = changeTokenOrg(changeNum, tokenSS, orginId, domainId, 0);

        return getUCMap(userNum, tknNum, unbindNum, changeNum, errStr);
    }

    /**
     * 变更绑定的所有组织机构令牌，解除公共绑定令牌中其它的绑定关系
     * @param changeType
     * @param userChangeServ
     * @param userTokenServ
     * @param userArr
     * @param orginId
     * @param domainId
     * @return
     * @throws Exception
     */
    private Map<String, Object> getUCList_4(int changeType, IUserInfoServ userChangeServ, IUserTokenServ userTokenServ,
            String[] userArr, String[] tokenArr, int orginId, int domainId) throws Exception {
        String userId = null;
        String errStr = "";
        int tknNum = 0; // 令牌数
        Integer unbindNum = 0; // 令牌解绑个数
        int changeNum = 0; // 令牌变更个数
        int userNum = 0;

        // 去掉数组中，重复的令牌，且封装成LIST
        List<String> list = new ArrayList<String>();
        if (StrTool.arrNotNull(tokenArr)) {
	        for (int i = 0; i < tokenArr.length; i++) {
	            if (!list.contains(tokenArr[i])) {
	                list.add(tokenArr[i]);
	            }
	        }
        }

        // 计算用户绑定令牌的总数，去重复
        if (StrTool.listNotNull(list)) {
            tknNum = list.size();
        } else {
            tknNum = 0;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < userArr.length; i++) {
            userId = userArr[i];
            UserToken userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setDomainId(domainId);
            userToken.setBindTag(1);

            // 共同使用的令牌
            List<?> tokens = userTokenServ.queryMulUserToken(userToken);

            // 解除公共绑定令牌中其它的绑定关系
            map = tokenUnBind(unbindNum, userArr, tokens, userId, orginId, domainId, userTokenServ, 1, 1);
            unbindNum = (Integer) map.get("unbindNum");

            // 变更用户组织机构
            changeOrg(userId, orginId, domainId, userChangeServ);
            userNum++;
        }

        // 变更绑定的所有组织机构令牌
        changeNum = changeTokenOrg(changeNum, list, orginId, domainId, 1);

        return getUCMap(userNum, tknNum, unbindNum, changeNum, errStr);
    }

    /**
     * 变更用户组织机构
     * @param userId
     * @param orginId
     * @param domainId
     * @param userChangeServ
     * @throws BaseException
     */
    private void changeOrg(String userId, int orginId, int domainId, IUserInfoServ userChangeServ) throws BaseException {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setDomainId(domainId); //设置域id
        if (orginId == NumConstant.common_number_0) {
            userInfo.setOrgunitId(null);
        } else {
            userInfo.setOrgunitId(orginId);
        }

        /**以下为记录日志时需要用到机构名称**/
        // 查询出该用户 旧的组织机构域信息
        UserInfo oldUserInfo = new UserInfo();
        oldUserInfo.setUserId(userId);
        oldUserInfo = (UserInfo) userInfoServ.find(oldUserInfo);
        // 赋值域机构名称
        oldUserInfo = setDOstr(oldUserInfo);
        userInfo.setCurrentOrgunitName(oldUserInfo.getDOrgunitName());

        // 查询出新的组织机构信息
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setDomainId(domainId);
        newUserInfo.setOrgunitId(userInfo.getOrgunitId());
        newUserInfo = setDOstr(newUserInfo);
        userInfo.setTargetOrgunitName(newUserInfo.getDOrgunitName());

        // 变更用户组织机构
        userChangeServ.updateUserOrgunit(userInfo);
    }

    /**
     * 令牌解绑
     * @param userId
     * @param orginId
     * @param domainId
     * @param flag 
     * @param userChangeServ
     * @throws BaseException
     */
    private Map<String, Object> tokenUnBind(int unbindNum, String[] userArr, List<?> tokens, String userId,
            int orginId, int domainId, IUserTokenServ userTokenServ, int flag, int unbindFlag) throws BaseException {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> tokenAr = new ArrayList<String>();
        if (StrTool.objNotNull(tokens)) {
            Iterator<?> it = tokens.iterator();
            if (flag == NumConstant.common_number_0) {
                while (it.hasNext()) {
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setToken(((UserToken) it.next()).getToken());

                    // 根据令牌号查出令牌信息
                    tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);

                    // 如果令牌不为域下令牌进行解绑
                    if (tokenInfo.getOrgunitid() != NumConstant.common_number_0) {
                        UserToken ut = new UserToken();
                        ut.setUserId(userId);
                        ut.setDomainId(domainId);
                        ut.setToken(tokenInfo.getToken());
                        userTokenServ.delObjs(ut);
                        unbindNum++;
                    }
                }
            } else {
                List<?> userList = Arrays.asList(userArr);
                while (it.hasNext()) {
                    UserToken userToken = new UserToken();
                    userToken.setToken(((UserToken) it.next()).getToken());
                    userToken.setDomainId(domainId);
                    userToken.setUserIds(userList);

                    // 查询绑定此令牌，除了选中用户以外，其它哪些用户还同时绑定此令牌
                    List<?> usertList = (List<?>) userTokenServ.queryTokenOth(userToken);
                    if (StrTool.listNotNull(usertList)) {
                        TokenInfo tokenInfo = new TokenInfo();
                        tokenInfo.setToken(userToken.getToken());

                        // 根据令牌号查出令牌信息
                        tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);

                        // 如果令牌不为域下令牌进行解绑
                        if (tokenInfo.getOrgunitid() != NumConstant.common_number_0) {
                            UserToken ut = new UserToken();
                            ut.setDomainId(domainId);
                            ut.setToken(tokenInfo.getToken());
                            // 区分变更方式
                            if (unbindFlag == NumConstant.common_number_1) {

                                // 解除指定用户外，其它用户绑定同一令牌的令牌关系
                                ut.setUserIds(userList);
                                userTokenServ.delObjUs(ut);
                                unbindNum += usertList.size();
                            } else {

                                // 解除指定用户的绑定关系
                                ut.setUserId(userId);
                                userTokenServ.delObjs(ut);
                                unbindNum++;
                            }

                            // 封装解绑的令牌
                            tokenAr.add(tokenInfo.getToken());

                        }
                    }
                }
            }
        }
        map.put("tokenAr", tokenAr); // 封装解绑的令牌
        map.put("unbindNum", unbindNum); // 解绑令牌数
        return map;
    }

    /**
     * 变更令牌组织机构
     * @param userId
     * @param orginId
     * @param domainId
     * @param flag 
     * @param userChangeServ
     * @throws BaseException
     */
    private int changeTokenOrg(int changeNum, List<?> tokens, int orginId, int domainId, int flag) throws BaseException {
        Iterator<?> it = tokens.iterator();
        while (it.hasNext()) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setToken((String) it.next());

            // 根据令牌号查出令牌信息
            tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);

            // 如果令牌不为域下令牌进行变更
            if (tokenInfo.getOrgunitid() != NumConstant.common_number_0) {
                tokenInfo.setDomainid(domainId);
                if (orginId == NumConstant.common_number_0) {
                    tokenInfo.setOrgunitid(null);
                } else {
                    tokenInfo.setOrgunitid(orginId);
                }

                // 变更令牌组织机构
                tokenServ.updateTokenOrg(tokenInfo);
                changeNum++;
            }

        }
        return changeNum;
    }

    /**
     * 返回容纳了用户令牌信息及操作失败信息的Map对象
     * @param tknNum 令牌数
     * @param unbindNum 令牌解绑个数
     * @param changeNum 令牌变更个数
     * @param userNum 用户数
     * @param errStr 错误信息
     * @return
     */
    private Map<String, Object> getUCMap(int userNum, int tknNum, int unbindNum, int changeNum, String errStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        //过滤出的要进行变更的用户、令牌个数
        int[] arrNum = new int[4];
        arrNum[0] = tknNum;
        arrNum[1] = unbindNum;
        arrNum[2] = changeNum;
        arrNum[3] = userNum;
        map.put(StrConstant.USR_TKN_NUM, arrNum);
        map.put(StrConstant.USR_TKN_ERR, errStr);
        return map;
    }

    /**
     * 令牌解绑后，将该令牌放回域中
     * @param userInfo
     * @throws Exception
     */
    public void updateBind(List<?> utList, IUserTokenServ userTokenServ) throws Exception {

        // 令牌解绑后是否回到所属的域中。0，否，1是
        if (StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER,
                ConfConstant.TK_UNBIND_IS_CHANGE_ORG), StrConstant.common_number_1)) {
            UserToken userToken = null;
            UserToken userIn = null;
            // 取出LIST中用户与令牌的信息
            if (StrTool.listNotNull(utList)) {
                for (int k = 0; k < utList.size(); k++) {
                    userToken = (UserToken) utList.get(k);

                    // 查出所有令牌与用户之间关系数据
                    List<?> userList = userTokenServ.selObjs(userToken);
                    if (StrTool.listNotNull(userList)) {
                        for (int i = 0; i < userList.size(); i++) {
                            userIn = (UserToken) userList.get(i);

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
    }
}
