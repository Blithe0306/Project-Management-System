/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.action.aide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.sms.SendSMSTool;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
import com.ft.otp.manager.token.distmanager.service.IDistManagerServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.alg.AlgConvert;
import com.ft.otp.util.alg.dist.ActiveCodeUtil;
import com.ft.otp.util.alg.dist.RC4Util;
import com.ft.otp.util.tool.StrTool;

/**
 * 手机令牌分发辅助类功能说明
 *
 * @Date in May 13, 2011,2:58:43 PM
 *
 * @author ZJY
 */
public class DistManagerActionAide {

    private Logger logger = Logger.getLogger(DistManagerActionAide.class);
    //用户令牌服务接口
    private IUserTokenServ userTokenServ = (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
    //用户服务接口
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");

    /**
     * 获取手机令牌列表
     * @Date in May 11, 2011,6:17:08 PM
     * @param token
     * @param distManagerServ
     * @return
     * @throws BaseException 
     */
    @SuppressWarnings("unchecked")
    public List<DistManagerInfo> getdistList(String token, IDistManagerServ distManagerServ) throws BaseException {
        List<DistManagerInfo> distList = null;
        DistManagerInfo distManagerInfo = new DistManagerInfo();
        distManagerInfo.setToken(token);
        PageArgument apArgument = new PageArgument();
        distList = (List<DistManagerInfo>) distManagerServ.query(distManagerInfo, apArgument);
        if (!StrTool.listNotNull(distList)) {
            distList = new ArrayList<DistManagerInfo>();
        }

        return distList;
    }

    /**
     * 获取令牌列表
     * @Date in May 11, 2011,6:13:01 PM
     * @param token
     * @param tokenServ
     * @return
     * @throws BaseException 
     */
    @SuppressWarnings("unchecked")
    public List<TokenInfo> gettokenList(String token, ITokenServ tokenServ) throws BaseException {
        List<TokenInfo> tokenList = null;
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(token);
        PageArgument apArgument = new PageArgument();
        tokenList = (List<TokenInfo>) tokenServ.query(tokenInfo, apArgument);
        if (!StrTool.listNotNull(tokenList)) {
            tokenList = new ArrayList<TokenInfo>();
        }

        return tokenList;
    }

    public String genActiveCode(String strTokenNum, String strActivePass, String strUDID, ITokenServ tokenServ,
            IDistManagerServ distManagerServ) {
        // 判断令牌号是否为空
        if (!StrTool.strNotNull(strTokenNum) || !StrTool.strNotNull(strActivePass)) {
            return null;
        }
        List<TokenInfo> tokenList = null;
        List<DistManagerInfo> diList = null;
        try {
            tokenList = gettokenList(strTokenNum, tokenServ);
            diList = getdistList(strTokenNum, distManagerServ);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String strExtType = diList.get(0).getExttype();
        String strFactor = diList.get(0).getPubkeyfactor();
        long validDate = tokenList.get(0).getExpiretime();
        String authnum = tokenList.get(0).getAuthnum();
        String strAC = ActiveCodeUtil.genActiveCod(strTokenNum, strExtType, strFactor, validDate, authnum,
                strActivePass, strUDID);
        return ActiveCodeUtil.encodeActiveCode(strAC, strActivePass);
    }

    /**
     * 如果是随机生成、手动输入的密码，需要对密码进行加密
     */
    public String convertPass(String activepass) {
        if (StrTool.strNotNull(activepass)) {
            try {
                activepass = RC4Util.runRC4(activepass);
                activepass = AlgConvert.stringToHex(activepass);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return activepass;
    }

    /**
     * 获取配置表的URL为空时，取本地url地址
     * @return url
     * @param defaultUrl、
     * @protocol 请求协议类型 
     */
    public String getLocalUrl(String defaultUrl, String protocol, HttpServletRequest request) {
        String baseUrl = null;
        if (!StrTool.strNotNull(defaultUrl)) {
            String protocolStr = "";
            if ("2".equals(protocol)) {
                protocolStr = "https";
            } else {
                protocolStr = "http";
            }
            //获取本地IP
            String ip = request.getLocalAddr();
            //获取本地端口
            String port = Integer.toString(request.getLocalPort());
            //获取路径
            String path = request.getContextPath();
            if (path.startsWith("/")) {
                path = path.substring(1, path.length());
            }
            path = path + Constant.MOBILE_DIST;
            baseUrl = protocolStr + "://" + ip + ":" + port + "/" + path;
        } else {
            baseUrl = defaultUrl;
        }

        return baseUrl;
    }

    /**
     * 取得绑定指定令牌的所有用户
     * 
     * @Date in Aug 27, 2013,1:42:48 PM
     * @param token
     * @return
     */
    public String getUserIds(String token) {
        String userId = "";
        List<?> userTokens = getUserTokens(null, token);
        if (!StrTool.listNotNull(userTokens)) {
            return userId;
        }

        StringBuffer buffer = new StringBuffer();
        Iterator<?> iter = userTokens.iterator();
        while (iter.hasNext()) {
            UserToken userToken = (UserToken) iter.next();
            if (userTokens.size() > 1) {
                buffer.append("[").append(userToken.getUserId()).append("]");
            } else {
                buffer.append(userToken.getUserId());
            }
        }
        userId = buffer.toString();

        return userId;
    }

    /**
     * 取得用户数组(一个令牌可能被多个用户绑定)
     * 
     * @Date in Apr 26, 2011,3:20:17 PM
     * @param token
     * @param userTokenServ
     * @return username
     */
    public List<?> getUserTokens(String userId, String token) {
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        if (StrTool.strNotNull(userId)) {
            userToken.setUserId(userId);
        }
        List<?> userList = null;
        try {
            userList = userTokenServ.query(userToken, new PageArgument());
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return userList;
    }
    
    /**
     * 发送分发短信
     * @Date in Sep 3, 2013,4:39:41 PM
     * @param token
     * @param message
     * @return
     */
    public String sendDistMessage(String token, String message) {
        try {
            String userIds = "";
            UserToken userTkn = new UserToken();
            userTkn.setToken(token);
            List<?> userList = userTokenServ.query(userTkn, new PageArgument());
            if (StrTool.listNotNull(userList)) {
                for(int i=0; i<userList.size(); i++) {
                    UserToken uToken = (UserToken) userList.get(i);
                    userIds = userIds + uToken.getUserId()+",";
                }
                
                if (StrTool.strNotNull(userIds)) {
                    userIds = userIds.substring(0,userIds.length()- 1);
                    UserInfo uInfo = new UserInfo();
                    uInfo.setBatchIds(userIds.split(","));
                    //不按照用户的本地认证和后端认证方式查询
                    uInfo.setLocalAuth(-1);
                    uInfo.setBackEndAuth(-1);
                    
                    List<?> uList = userInfoServ.selectUserToSms(uInfo);
                    if (StrTool.listNotNull(uList)) {
                        for (int j=0; j<uList.size(); j++) {
                            UserInfo userInfo = (UserInfo) uList.get(j);
                            if (StrTool.strNotNull(userInfo.getCellPhone())) {
                                SendSMSTool.send(userInfo.getCellPhone(), message);
                            }
                        }
                       
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
