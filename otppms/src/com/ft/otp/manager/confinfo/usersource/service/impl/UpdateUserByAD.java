package com.ft.otp.manager.confinfo.usersource.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.confinfo.usersource.dao.LdapInfo;
import com.ft.otp.manager.confinfo.usersource.dao.OTPLdap;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * AD用户来源的更新业务
 *
 * @Date in 2013-6-18,上午11:05:35
 *
 * @version v1.0
 *
 * @author YYF
 */
public class UpdateUserByAD {

    private IOrgunitInfoServ orgunitInfoServ;
    private IAdminAndOrgunitServ adminAndOrgunitServ;
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");

    /**
     * 构造AD用户更新功能对象
     * @param userSourceDao 用户来源数据操作对象
     * @param orgunitInfoServ 组织机构操作对象
     * @param adminAndOrgunitServ 管理员-组织机构关系对象
     */
    public UpdateUserByAD(IOrgunitInfoServ orgunitInfoServ, IAdminAndOrgunitServ adminAndOrgunitServ) {
        super();
        this.orgunitInfoServ = orgunitInfoServ;
        this.adminAndOrgunitServ = adminAndOrgunitServ;
    }

    /**
     * 从AD中更新用户
     * 
     * @Date in 2013-6-13,下午03:03:36
     * @param userSourceInfo 用户来源配置属性集合类
     * flag 0:按条件查询 1：查询所有AD用户
     * @return 从AD中查徇得到的用户列表
     */
    public List<UserInfo> updateUserByAD(UserSourceInfo userSourceInfo,int flag) throws Exception {
        if (null == userSourceInfo) {
            return null;
        }

        List<UserInfo> usList = new ArrayList<UserInfo>();

        Integer select_domainId = userSourceInfo.getDomainid();//域ID
        Integer select_orgunitId = userSourceInfo.getOrgunitid();//组织构构ID

        //System.out.println("域ID/组织机构ID:" + select_domainId + "/" + select_orgunitId);

        boolean isUpdateOU = userSourceInfo.getIsupdateou() == 1;//是否更新AD
        List<String[]> adOUList = new ArrayList<String[]>();//保存AD中的OU列表
        List<String[]> dbFormatOUList = new ArrayList<String[]>();//保存OU路径与ID关系的列表[id][ou路径]

        //ldap数据来源，从系统配置表中得到要查询的ad中的属性名
        String[] returnedAtts = new String[8];
        returnedAtts[0] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_USERID);
        returnedAtts[1] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_REALNAME);
        returnedAtts[2] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_EMAIL);
        returnedAtts[3] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_ADDRESS);
        returnedAtts[4] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_TEL);
        returnedAtts[5] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_CELLPHONE);
        returnedAtts[6] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_ENABLED);
        returnedAtts[7] = ConfDataFormat.getConfValue(ConfConstant.AD_CONF, ConfConstant.LOCAL_ATTR_OU);

        //获取ad中OU数据及用户数据
        //获取ad中OU数据及用户数据
        LdapInfo ldapInfo = getLdapInfo(userSourceInfo);
        String baseDN = ldapInfo.getLdapDn();

        OTPLdap ldap = new OTPLdap();
        if (flag == 1) {
            if (StrTool.strNotNull(userSourceInfo.getRootdn())) {
                ldapInfo.setLdapDn(userSourceInfo.getRootdn());
            } else {
                if (baseDN.indexOf("DC") != -1) {
                    ldapInfo.setLdapDn(baseDN.substring(baseDN.indexOf("DC")));
                }
            }
        }
        List<String[]> adUserList = ldap.search(ldapInfo, returnedAtts);

        if (isUpdateOU) {
            ldapInfo.setLdapDn(baseDN);
            ldapInfo.setFilter("(objectclass=organizationalUnit)");
            adOUList = ldap.search(ldapInfo, new String[] { "distinguishedName" });
        }

        ldap.unInit();

        if (isUpdateOU) {
            //从AD中取得指定域和组织机构下面最完整OU路径列表
            List<String> adFormatOUList = queryADAndFormatOrgunit(adOUList, baseDN);

            //DB与AD中OU进行对比,如DB中不存在则添加OU,返回添加的组织机构ID列表
            List<Integer> insertOuList = addOU(select_domainId, select_orgunitId, adFormatOUList);

            //将新添加的组织机构ID列表,添加到"管理员-组织机构"关系表中,此时将列表数据添加到所有有权管理用户来源配置选定目录的管理员
            if (StrTool.listNotNull(insertOuList)) {
                addAdminAndOrgunit(select_domainId, select_orgunitId, insertOuList);
            }

            //得到最新OU对应的ID列表
            dbFormatOUList = queryDBAndFormatOrgunit(select_domainId, select_orgunitId);
        }

        if (null != adUserList) {
            for (String[] adUserData : adUserList) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(adUserData[0]);
                userInfo.setRealName(adUserData[1]);
                userInfo.setEmail(adUserData[2]);
                userInfo.setAddress(adUserData[3]);
                userInfo.setTel(adUserData[4]);
                userInfo.setCellPhone(adUserData[5]);

                if (isDisableADUser(adUserData[6])) {
                    userInfo.setEnabled(0);
                } else {
                    userInfo.setEnabled(1);
                }

                //不更新AD,将所有用户添加至所选组织机构
                userInfo.setOrgunitId(select_orgunitId);
                if (isUpdateOU) {
                    //更新AD
                    //获取包含OU的字符串，和db中获得的OU列表比较得到域id，设置域id到用户属性中
                    String distinguishedName = adUserData[7];
                    String ou = getOU(distinguishedName);
                    Integer orgunitId = getOrgunitIdByOrgunitPath(dbFormatOUList, ou);
                    if (null != orgunitId) {
                        userInfo.setOrgunitId(orgunitId);
                        //System.out.println(userInfo.getUserId() + "设置了OU-ID：" + userInfo.getOrgunitId());
                    }
                }

                //添加域id
                userInfo.setDomainId(select_domainId);
                //添加用户

                //是否更新无效用户
                if (userSourceInfo.getUpinvaliduser() == 1) {
                    //全部更新
                    usList.add(userInfo);
                } else {
                    if (userSourceInfo.getIssyncuserinfo() == 1 || userSourceInfo.getLocalusermark() != 0) {
                        usList.add(userInfo);
                    } else {
                        //只更新启用用户
                        if (userInfo.getEnabled() == 1) {
                            usList.add(userInfo);
                        }
                    }
                }
            }
        }

        return usList;
    }

    /**
     * 将组织机构ID列表数据添加到所有有权管理用户来源配置选定目录的管理员
     * @Date in 2013-6-20,下午04:28:34
     * @param select_domainId 域ID
     * @param select_orgunitId 组织机构ID
     * @param insertOuList 组织机构ID集合
     */
    private void addAdminAndOrgunit(Integer select_domainId, Integer select_orgunitId, List<Integer> insertOuList)
            throws Exception {
        //查询有选定目录权限的管理员
        List<?> queryAdminAndOrgunitList = new ArrayList();//根据所选组织机构查询的管理员-组织机构列表
        List<AdminAndOrgunit> needAddAdminAndOrgunitList = new ArrayList<AdminAndOrgunit>();//需要添加的对象列表
        AdminAndOrgunit adminAndOrgunit_queryParam = new AdminAndOrgunit();
        adminAndOrgunit_queryParam.setDomainId(select_domainId);
        adminAndOrgunit_queryParam.setOrgunitId(select_orgunitId);
        queryAdminAndOrgunitList = adminAndOrgunitServ.queryAdminAndOrgunitByDomainId(adminAndOrgunit_queryParam);
        //构造需要添加的对象列表
        for (Object object : queryAdminAndOrgunitList) {
            AdminAndOrgunit adminAndOrgunit = (AdminAndOrgunit) object;
            String adminId = adminAndOrgunit.getAdminId();
            if (null != adminAndOrgunit.getAdminId()) {
                for (Integer orgunitId : insertOuList) {
                    AdminAndOrgunit needAddAdminAndOrgunit = new AdminAndOrgunit();
                    needAddAdminAndOrgunit.setAdminId(adminId);
                    needAddAdminAndOrgunit.setDomainId(select_domainId);
                    needAddAdminAndOrgunit.setOrgunitId(orgunitId);
                    needAddAdminAndOrgunitList.add(needAddAdminAndOrgunit);
                }
            }
        }
        //批量插入
        if (StrTool.listNotNull(needAddAdminAndOrgunitList)) {
            adminAndOrgunitServ.addObjs(needAddAdminAndOrgunitList);
        }
    }

    /**
     * 对比OU,如DB中不存在则添加进DB
     * @Date in 2013-6-18,上午03:14:57
     * @param select_domainId 域ID
     * @param select_orgunitId 组织机构ID
     * @param adFormatOUList AD中的最下层OU全路径列表
     * @return 添加的组织机构ID
     * @throws Exception
     */
    private List<Integer> addOU(Integer select_domainId, Integer select_orgunitId, List<String> adFormatOUList)
            throws Exception {
        List<Integer> insertOuList = new ArrayList<Integer>();//用来存放已添加DB的ou列表,以便添加到"otppms_admin_orgunit"表中
        if (StrTool.listNotNull(adFormatOUList)) {
            for (String adFormatOU : adFormatOUList) {
                if (null != getOrgunitIdByOrgunitPath(select_domainId, select_orgunitId, adFormatOU)) {
                    //System.out.println("已存在的adFormatOU为:" + adFormatOU);
                    continue;
                }
                //ou111,ou11,ou1
                String[] splitOU = adFormatOU.split(",");
                int splitOULength = splitOU.length;

                Integer parentId = select_orgunitId;
                String ou = "";
                for (int i = splitOULength - 1; i > -1; i--) {
                    if (i < splitOULength - 1) {
                        ou = splitOU[i] + "," + ou;
                    } else {
                        ou = splitOU[i];
                    }

                    Integer orgunitId = getOrgunitIdByOrgunitPath(select_domainId, select_orgunitId, ou);
                    if (null != orgunitId) {
                        //如果组织机构存在,保存其ID作为下层的父ID
                        parentId = orgunitId;
                    } else {
                        //因OrgunitInfo的域和组织机构为int型,为null时OrgunitInfo表中暂存0值
                        parentId = null == parentId ? 0 : parentId;
                        select_domainId = null == select_domainId ? 0 : select_domainId;
                        //如不存在，插入DB
                        OrgunitInfo orgunitInfo = new OrgunitInfo();
                        int createTime = DateTool.dateToInt(new Date()); //当前UTC秒
                        orgunitInfo.setOrgunitName(splitOU[i]);
                        orgunitInfo.setParentId(parentId);
                        orgunitInfo.setCreateTime(createTime);
                        orgunitInfo.setDomainId(select_domainId);
                        orgunitInfo.setDescp(splitOU[i]);
                        orgunitInfoServ.addObj(orgunitInfo);

                        //记录日志
                        addLog(select_domainId, parentId, orgunitInfo.getOrgunitName());

                        //将新插入的ou作为下层的父ID
                        parentId = getOrgunitIdByOrgunitPath(select_domainId, select_orgunitId, ou);
                        //System.out.println("刚插入的orgunitId为:" + parentId);
                        insertOuList.add(parentId);
                    }
                }
            }
        }

        return insertOuList;
    }

    /**
     * 添加组织机构日志
     * 
     * @Date in Sep 6, 2013,5:20:32 PM
     * @param parentName
     * @param orgunitName
     */
    private void addLog(Integer select_domainId, Integer orgId, String orgunitName) {
        LogCommonObj commonObj = new LogCommonObj();

        String parentName = "";
        try {
            if (null != orgId && orgId.intValue() > 0) {
                OrgunitInfo orgunitInfo = new OrgunitInfo();
                orgunitInfo.setOrgunitId(orgId);
                orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(orgunitInfo);
                parentName = orgunitInfo.getOrgunitName();
            } else {
                DomainInfo domainInfo = new DomainInfo();
                domainInfo.setDomainId(select_domainId);
                domainInfo = (DomainInfo) domainInfoServ.find(domainInfo);
                parentName = domainInfo.getDomainName();
            }
        } catch (Exception e) {
        }

        int acid = AdmLogConstant.log_aid_add;
        int acobj = AdmLogConstant.log_obj_orgunit;
        String desc = orgLogInfoStr(parentName, orgunitName);

        commonObj.addAdminLog(acid, acobj, desc, null, 0);
    }

    //添加组织机构日志信息
    public String orgLogInfoStr(String parentName, String orgunitName) {
        StringBuilder sbr = new StringBuilder();
        String colon = Language.getLangValue("colon", Language.getCurrLang(null), null);
        String str = Language.getLangValue("org_add_child_org", Language.getCurrLang(null), null) + colon;
        sbr.append(Language.getLangValue("org_parent_org", Language.getCurrLang(null), null) + colon + parentName
                + Language.getLangValue("comma", Language.getCurrLang(null), null) + str + orgunitName);

        return sbr.toString();
    }

    /**
     * 根据组织机构层级路径得到ID(每次都查询一次数据库)
     * @Date in 2013-6-18,上午03:46:32
     * @param select_domainId 域ID
     * @param select_orgunitId 组织机构ID
     * @param orgunitPath
     * @return 组织机构ID
     * @throws Exception
     */
    private Integer getOrgunitIdByOrgunitPath(Integer select_domainId, Integer select_orgunitId, String orgunitPath)
            throws Exception {
        List<String[]> dbFormatOUList = queryDBAndFormatOrgunit(select_domainId, select_orgunitId);
        return getOrgunitIdByOrgunitPath(dbFormatOUList, orgunitPath);
    }

    /**
     * 根据组织机构层级路径得到ID
     * @Date in 2013-6-18,上午10:47:38 
     * @param dbFormatOUList 指定域和组织机构下面的组织机构
     * @param orgunitPath OU路径
     * @return 组织机构ID
     * @throws Exception
     */
    private Integer getOrgunitIdByOrgunitPath(List<String[]> dbFormatOUList, String orgunitPath) throws Exception {
        orgunitPath = null == orgunitPath ? "" : orgunitPath;
        Integer orgunitId = null;
        if (null != dbFormatOUList) {
            for (String[] strings : dbFormatOUList) {
                if (strings[1].equals(orgunitPath)) {
                    orgunitId = Integer.parseInt(strings[0]);
                    break;
                }
            }
        }

        return orgunitId;
    }

    /**
     * 查询AD中的组织结构并格式化
     * @Date in 2013-6-18,上午12:56:56
     * @param adOUList 查询的OU列表
     * @param baseDN　查询目录字符串
     * @return 查询的最完整OU路径列表
     */
    private List<String> queryADAndFormatOrgunit(List<String[]> adOUList, String baseDN) {
        baseDN = null == baseDN ? "" : baseDN;
        List<String> adFormatOUList = new ArrayList<String>();

        //检查baseDN是否包含OU,如果存在，构造为所选组织机构的第一级
        String baseDN_ou = getOU(baseDN).trim();
        if (baseDN_ou.length() > 0) {
            adFormatOUList.add(baseDN_ou);
        }

        //将子OU加入列表
        if (StrTool.listNotNull(adOUList)) {
            for (String[] ouItem : adOUList) {
                String ou = getOU(ouItem[0]);
                adFormatOUList.add(ou);
            }
        }

        //清除重复项(取不重复的最完整OU路径,以便拆分后加入数据库)
        List<String> adFormatOUList_final = new ArrayList<String>();
        for (int i = 0; i < adFormatOUList.size(); i++) {
            boolean isAdd = true;
            String ou_i = adFormatOUList.get(i);
            for (int j = 0; j < adFormatOUList.size(); j++) {
                String ou_j = adFormatOUList.get(j);
                if (ou_j.length() > ou_i.length() && ou_j.indexOf(ou_i) > -1) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                adFormatOUList_final.add(ou_i);
            }
        }

        return adFormatOUList_final;
    }

    /**
     * 从db中取得指定域和组织机构下面的组织机构
     * @Date in 2013-6-18,上午12:04:30
     * @param select_domainId 域ID
     * @param select_orgunitId　 组织机构ID
     * @return 形如[组织机构id][OU路径字符串(n级...,二级,一级)]的二维数组列表
     * @throws Exception
     */
    private List<String[]> queryDBAndFormatOrgunit(Integer select_domainId, Integer select_orgunitId) throws Exception {
        //1.从db中取得指定域下面的组织机构
        OrgunitInfo orgunitInfo_param = new OrgunitInfo();
        orgunitInfo_param.setDomainId(select_domainId);
        List<?> orgunitInfos = orgunitInfoServ.queryWholeList(orgunitInfo_param);
        //2.格式化为AD中的ou结构
        List<String[]> dbFormatOUList = new ArrayList<String[]>();
        //已按id从大到小排库,现从小到大组合OU字符串
        int startOrgunitId = 0;//起始组织机构
        if (null != select_orgunitId) {
            startOrgunitId = select_orgunitId.intValue();
        }
        for (int i = 0; i < orgunitInfos.size(); i++) {
            OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfos.get(i);
            int orgunitId = orgunitInfo.getOrgunitId();
            String orgunitName = orgunitInfo.getOrgunitName();
            int parentId = orgunitInfo.getParentId();
            //组合所选组织机构下的项
            if (orgunitId > startOrgunitId) {
                for (String[] orgunitInfoItem : dbFormatOUList) {
                    if (parentId == Integer.parseInt(orgunitInfoItem[0])) {
                        //如果此组织机构为已有组织机构下级，与上级组织机构名称进行组行，格式化为AD中的OU格式
                        orgunitName = orgunitName + "," + orgunitInfoItem[1];
                    }
                }
                String[] orgunitInfoItem = new String[2];
                orgunitInfoItem[0] = String.valueOf(orgunitId);
                orgunitInfoItem[1] = orgunitName;
                dbFormatOUList.add(orgunitInfoItem);
                ////System.out.println("dbOU组织机构ID:" + orgunitId + ",机构名:" + orgunitName);
            }

            ////System.out.println("db组织机构ID:" + orgunitId + ",机构名:" + orgunitName);
        }
        return dbFormatOUList;
    }

    /**
     * 取得ou路径的字符串(n级...,二级,一级)
     * @Date in 2013-3-9,下午04:43:18
     * @param baseOU AD对象位置路径
     * @return
     */
    private static String getOU(String baseOU) {
        baseOU = null == baseOU ? "" : baseOU;
        StringBuffer buffer = new StringBuffer(30);
        int i = 0;
        baseOU = baseOU.replace("ou=", "OU=");
        do {
            int firstIndex = -1;
            firstIndex = baseOU.indexOf("OU=");
            if (firstIndex > -1) {
                int first = firstIndex;
                int end = baseOU.indexOf(",", first);
                if (end == -1) {
                    end = baseOU.length();
                }
                buffer.append(baseOU.substring(first, end)).append(",");
                baseOU = baseOU.substring(end);
            } else {
                i = -1;
            }

        } while (i > -1);
        String ou = buffer.toString();
        if (ou.length() > 0 && ou.lastIndexOf(",") == ou.length() - 1) {
            ou = ou.substring(0, ou.length() - 1);
        }
        ou = ou.replace("OU=", "");
        return ou;
    }

    /***
     * 判断AD用户是否已禁用
     * @Date in 2013-6-13,下午02:56:00
     * @param userAccountControl 用户访问控制权限（由一串数字表示）
     * @return ture为禁用，false为启用
     */
    private boolean isDisableADUser(String userAccountControl) {
        int disFlag = 0;
        try {
            disFlag = Integer.parseInt(userAccountControl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (disFlag & 0x0202) == 514;
    }

    /**
     * 封装LDAP需要的信息
     */
    public LdapInfo getLdapInfo(UserSourceInfo userSourceInfo) {
        LdapInfo ldapInfo = new LdapInfo();
        ldapInfo.setLdapIp(userSourceInfo.getServeraddr());
        ldapInfo.setLdapPort(userSourceInfo.getPort());
        ldapInfo.setLdapPass(userSourceInfo.getPwd());
        ldapInfo.setLdapDn(userSourceInfo.getBasedn());
        ldapInfo.setLdapUser(userSourceInfo.getUsername());
        ldapInfo.setTimeout(userSourceInfo.getTimeout());
        ldapInfo.setFilter(userSourceInfo.getFilter());
        ldapInfo.setLdapAccouts(userSourceInfo.getMapingAttr());

        return ldapInfo;
    }

}
