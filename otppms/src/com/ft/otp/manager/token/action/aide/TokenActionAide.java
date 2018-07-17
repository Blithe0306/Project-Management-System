/**
 *Administrator
 */
package com.ft.otp.manager.token.action.aide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.util.tool.StrTool;

import com.ft.otp.util.txt.UnicodeReader;

/**
 * 令牌Action类的辅助功能说明
 *
 * @Date in Jan 13, 2012,10:33:56 AM
 *
 * @author ZJY
 */
public class TokenActionAide {
    //域服务接口
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");
    //组织机构服务接口
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");
    //管理员-组织机构关系
    private IAdminAndOrgunitServ adminAndOrgunitServ = (IAdminAndOrgunitServ) AppContextMgr
            .getObject("adminAndOrgunitServ");

    /**
     * 
     * 将上传的批量令牌文件读取封装
     * @Date in Aug 7, 2012,8:55:35 AM
     * @param tokenQueryForm
     * @return
     */
    public TokenInfo getTknBatch(String tokenStr) {
        TokenInfo tokenInfo = new TokenInfo();
        if (StrTool.strNotNull(tokenStr)) {
            tokenInfo.setBatchIds(tokenStr.split(","));
        } else {
            String[] batchIds = new String[] { "NULL" };
            tokenInfo.setBatchIds(batchIds);
        }
        return tokenInfo;
    }

    /**
     * 读取批量令牌文件
     * @Date in Aug 3, 2012,2:39:17 PM
     * @param file
     * @return
     */
    public String getTknStrBuf(File tknFile) throws IOException {
        StringBuffer strBuf = new StringBuffer("");

        BufferedReader reader = new BufferedReader(new UnicodeReader(new FileInputStream(tknFile), null));//一行一行读
        String lineStr = reader.readLine();
        while (lineStr != null) {
            if (StrTool.strNotNull(lineStr)) {
                lineStr = lineStr.trim();
                // 如果符合令牌号规则 加入查询条件中
                if (lineStr.matches("^[a-z0-9A-Z]{0,32}$")) {
                    strBuf.append(lineStr);
                    strBuf.append(",");
                }
            }
            lineStr = reader.readLine();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }

        return strBuf.toString();
    }

    /**
     * 获取邮件发送实体信息
     * @throws BaseException 
     * 
     */
    public MailInfo getMailInfo(List<?> userList, String subject, String content, String filepath,
            IEmailInfoServ emInfoServ) throws BaseException {
        String[] toAddress = null;
        if (StrTool.listNotNull(userList)) {
            toAddress = new String[userList.size()];
            for (int i = 0; i < userList.size(); i++) {
                UserInfo userInfo = (UserInfo) userList.get(i);
                toAddress[i] = userInfo.getEmail();
            }
        }

        return SendMailUtil.getMailInfo(toAddress, subject, content, new String[] { filepath });
    }
    
    /**
     * 获取邮件发送实体信息
     * @throws BaseException 
     * 
     */
    public MailInfo admGetMailInfo(List<?> userList, String subject, String content, String filepath,
            IEmailInfoServ emInfoServ) throws BaseException {
        String[] toAddress = null;
        if (StrTool.listNotNull(userList)) {
            toAddress = new String[userList.size()];
            for (int i = 0; i < userList.size(); i++) {
                AdminUser adminUser = (AdminUser) userList.get(i);
                toAddress[i] = adminUser.getEmail();
            }
        }

        return SendMailUtil.getMailInfo(toAddress, subject, content, new String[] { filepath });
    }

    /**
     * 获取令牌绑定的用户邮箱账户
     * 方法说明
     * @Date in Feb 28, 2013,6:41:14 PM
     * @return
     * @throws BaseException 
     */
    public List<?> getUserEmails(String token, List<?> userList, IUserInfoServ userInfoServ, IAdminUserServ adminUserServ) throws BaseException {
        UserInfo uInfo = new UserInfo();
        AdminUser adminUser = new AdminUser();
        List<?> userinfoList = null;
        if (StrTool.listNotNull(userList)) {
            String userIdS[] = new String[1];
            
            // 邮件发送时该令牌只能被一个用户或管理员绑定，所以userList中只有一条数据
            UserToken userToken = (UserToken) userList.get(0);
            userIdS[0] = userToken.getUserId();
            if(StrTool.objNotNull(userToken.getDomainId())){ // 表示从用户中取邮箱地址
            	uInfo.setBatchIds(userIdS);
            	userinfoList = userInfoServ.selectUserEmail(uInfo);
            }else{ // 表示从管理员中取邮箱地址
            	adminUser.setBatchIds(userIdS);
            	userinfoList = adminUserServ.selectAmdEmail(adminUser);
            }
        }
        return userinfoList;
    }

    /**
     * 封装域和组织机构作为令牌查询条件的一部分
     * @Date in Apr 28, 2011,6:11:38 PM
     * @param deb
     * @return
     * @throws Exception
     */
    public TokenInfo setDomainAndOrgunit(TokenInfo tokenInfo, String orgunitStr) {
        if (StrTool.strNotNull(orgunitStr)) {
            String orgunits = orgunitStr.substring(0, orgunitStr.length() - 1);
            String orgunit[] = orgunits.split(":");
            //组织机构所在的域
            if (StrTool.strNotNull(orgunit[0])) {
                tokenInfo.setDomainid(StrTool.parseInt(orgunit[0]));
            }
            int orgunitids[] = new int[orgunitStr.split(",").length];
            for (int i = 0; i < orgunitStr.split(",").length; i++) {

                // 取出传过来的用户机构ID号放到自定义的数组里
                orgunitids[i] = StrTool.parseInt(orgunitStr.split(",")[i].split(":")[1]);
            }
            //组织机构
            tokenInfo.setOrgunitIds(orgunitids);
        } else {
            tokenInfo.setOrgFlag(1);
        }

        return tokenInfo;
    }

    /** 
     * 设置令牌所在的域和组织机构的名称字符串
     * setDomainAndOrgunitName
    * @throws BaseException 
     */
    public TokenInfo setDomainAndOrgunitName(TokenInfo tokenInfo) throws BaseException {
        //域组织机构串
        //获取域
        String dOrgunitName = DomainConfig.getValue(tokenInfo.getDomainid());
        if (StrTool.objNotNull(tokenInfo.getOrgunitid())) {
            if (tokenInfo.getOrgunitid() != 0 || !tokenInfo.getOrgunitid().equals(0)) { //如果该用户只属于域 不属于某个组织机构
                OrgunitInfo oiQuery = new OrgunitInfo();
                oiQuery.setOrgunitId(tokenInfo.getOrgunitid());
                oiQuery.setOrgunitNumber(null);
                oiQuery.setDomainId(0);
                oiQuery.setCreateTime(0);
                // 获取组织机构
                OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);
                //dOrgunitName = dOrgunitName + "-->" + orgunitInfo.getOrgunitName();
                dOrgunitName = orgunitInfo.getOrgunitName();
            }
        }

        tokenInfo.setDomainOrgunitName(dOrgunitName);//设置域组织机构串

        return tokenInfo;
    }

    /**
     * 根据管理员获取管理员管理的域
     * @Date in Apr 15, 2011,11:33:36 AM
     * @param curLoginUserRole
     *        当前登录角色
     * @param curLoginUser
     *        当前登录用户
     */
    public TokenInfo setAdminDomainIds(TokenInfo tknInfo, String curLoginUserRole, String curLoginUser)
            throws BaseException {
        int[] domainIds = null;

        //超级管理员,所有域
        if (StrTool.strEquals(StrConstant.SUPER_ADMIN, curLoginUserRole)) {
            List<?> domainList = domainInfoServ.query(new DomainInfo(), new PageArgument());
            if (StrTool.listNotNull(domainList)) {
                domainIds = new int[domainList.size()];
                for (int i = 0; i < domainList.size(); i++) {
                    DomainInfo domainInfo = (DomainInfo) domainList.get(i);
                    domainIds[i] = domainInfo.getDomainId();
                }
            }
        } else {
            //普通管理员，直接管理的域
            AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit();
            adminAndOrgunit.setAdminId(curLoginUser);
            List<?> adminAndOrginuitList = adminAndOrgunitServ.queryAdminAndOrgunitByAdminId(adminAndOrgunit);
            if (StrTool.listNotNull(adminAndOrginuitList)) {
                domainIds = new int[adminAndOrginuitList.size()];
                for (int j = 0; j < adminAndOrginuitList.size(); j++) {
                    AdminAndOrgunit admOrgunit = (AdminAndOrgunit) adminAndOrginuitList.get(j);
                    domainIds[j] = admOrgunit.getDomainId();
                }
            }
        }

        if (StrTool.arrNotNull(domainIds)) {
            tknInfo.setBatchIdsInt(domainIds);
        }

        return tknInfo;
    }

    /**
     * 填充令牌的状态
     * 
     * @Date in Jul 16, 2013,9:34:37 AM
     * @param tokenInfo
     * @param operType 0或1:启用/停用 2或3:锁定/解锁 4或5:挂失/解挂 6:作废
     * @return
     */
    public static TokenInfo getTokenInfo(TokenInfo tokenInfo, int operType) {
        switch (operType) {
            case 0:
                tokenInfo.setEnabled(1);

                tokenInfo.setLocked(-1);
                tokenInfo.setLost(-1);
                tokenInfo.setLogout(-1);
                break;
            case 1:
                tokenInfo.setEnabled(0);

                tokenInfo.setLocked(-1);
                tokenInfo.setLost(-1);
                tokenInfo.setLogout(-1);
                break;
            case 2:
                tokenInfo.setLocked(2);
                tokenInfo.setLoginlocktime(StrTool.timeSecond());

                tokenInfo.setEnabled(-1);
                tokenInfo.setLost(-1);
                tokenInfo.setLogout(-1);
                break;
            case 3:
                tokenInfo.setLocked(0);
                tokenInfo.setLoginlocktime(0);

                tokenInfo.setEnabled(-1);
                tokenInfo.setLost(-1);
                tokenInfo.setLogout(-1);
                break;
            case 4:
                tokenInfo.setLost(1);

                tokenInfo.setEnabled(-1);
                tokenInfo.setLocked(-1);
                tokenInfo.setLogout(-1);
                break;
            case 5:
                tokenInfo.setLost(0);

                tokenInfo.setEnabled(-1);
                tokenInfo.setLocked(-1);
                tokenInfo.setLogout(-1);
                break;
            case 6:
                tokenInfo.setLogout(1);

                tokenInfo.setEnabled(-1);
                tokenInfo.setLocked(-1);
                tokenInfo.setLost(-1);
                break;
            case 9:
                tokenInfo.setOrgunitid(null);

                break;

            default:
                break;
        }

        return tokenInfo;
    }

}
