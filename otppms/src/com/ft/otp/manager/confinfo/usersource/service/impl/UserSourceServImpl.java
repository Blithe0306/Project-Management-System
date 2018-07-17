/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.usersource.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.DBconnection;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.confinfo.usersource.dao.IUserSourceDao;
import com.ft.otp.manager.confinfo.usersource.dao.LdapInfo;
import com.ft.otp.manager.confinfo.usersource.dao.impl.UserSourceDaoImpl;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.confinfo.usersource.service.IUserSourceServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户来源配置业务接口实现说明
 * 
 * @Date in Jun 7, 2011,11:15:50 AM
 * 
 * @author YYF
 */
public class UserSourceServImpl extends BaseService implements IUserSourceServ {
    private Logger logger = Logger.getLogger(UserSourceServImpl.class);
    //用户服务接口对象
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");
    //管理中心配置接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    private IUserSourceDao userSourceDao;

    //组织结构所用接口
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");

    //管理员-组织机构关系接口
    private IAdminAndOrgunitServ adminAndOrgunitServ = (IAdminAndOrgunitServ) AppContextMgr
            .getObject("adminAndOrgunitServ");
    
    private LogCommonObj commonObj = new LogCommonObj();

    /**
     * @return the userSourceDao
     */
    public IUserSourceDao getUserSourceDao() {
        return userSourceDao;
    }

    /**
     * @param userSourceDao
     *            the userSourceDao to set
     */
    public void setUserSourceDao(IUserSourceDao userSourceDao) {
        this.userSourceDao = userSourceDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = (UserSourceInfo) object;
        try {
            userSourceDao.addObj(userSourceInfo);
            //插入用户来源主数据
            UserSourceInfo usInfo = (UserSourceInfo) userSourceDao.find(userSourceInfo);
            if (StrTool.objNotNull(usInfo)) {
                addUSAttr(userSourceInfo, usInfo.getId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 管理中心配置
     * 根据属性类型、类型名称获取属性值
     * @param confType
     * @param confName
     * @return confvalue
     * @throws BaseException 
     */
    public String getCenterConfigValue(String confType, String confName) throws BaseException {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setConftype(confType);
        configInfo.setConfname(confName);
        ConfigInfo confInfo = (ConfigInfo) confInfoServ.find(configInfo);
        String confvalue = "";
        if (StrTool.objNotNull(confInfo)) {
            confvalue = confInfo.getConfvalue();
        }

        return confvalue;
    }

    //用户来源添加和修改公用方法
    public boolean addUSAttr(UserSourceInfo userSourceInfo, int usid) {
        try {
            if (StrTool.objNotNull(userSourceInfo)) {
                //插入用户来源关系属性字段
                String mappingAttr = userSourceInfo.getMapingAttr();
                if (StrTool.strNotNull(mappingAttr)) {
                    String columnStr[] = mappingAttr.split(",");
                    if (StrTool.arrNotNull(columnStr)) {
                        for (int i = 0; i < columnStr.length; i++) {
                            String attrs[] = columnStr[i].split(":");
                            userSourceInfo.setId(usid);
                            userSourceInfo.setLocaluserattr(attrs[0]);
                            userSourceInfo.setSourceuserattr(attrs[1]);
                            userSourceDao.addUSAttr(userSourceInfo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return userSourceDao.count(object);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
        UserSourceInfo userSourceInfo = new UserSourceInfo();
        java.util.Iterator<?> iter = keys.iterator();
        int i = 0;
        int ibaths[] = new int[keys.size()];
        while (iter.hasNext()) {
            int confInfoId = StrTool.parseInt(String.valueOf(iter.next()));
            ibaths[i] = confInfoId;
            i++;
        }
        userSourceInfo.setBatchIdsInt(ibaths);
        userSourceDao.delObj(userSourceInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return userSourceDao.find(object);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object,
     *      com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return userSourceDao.query(object, pageArg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = (UserSourceInfo) object;
        try {
            userSourceDao.updateObj(object);
            //删除用户来源关联数据
            userSourceDao.delUSAttr(userSourceInfo);
            if (StrTool.objNotNull(userSourceInfo)) {
                addUSAttr(userSourceInfo, userSourceInfo.getId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 通过数据来源id得到用户来源详细配置信息,并封装进UserSourceInfo对象
     * 
     * @param object
     * @return
     * @throws BaseException
     */
    public UserSourceInfo getUserSourceInfo(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = (UserSourceInfo) userSourceDao.find(object);

        if (!StrTool.objNotNull(userSourceInfo)) {
            return userSourceInfo;
        }

        //设置任务信息
        TaskInfo timingInfo = new TaskInfo();
        timingInfo.setSourceid(userSourceInfo.getId());
        TaskInfo timeInfo = (TaskInfo) taskInfoServ.find(timingInfo);
        if (StrTool.objNotNull(timeInfo)) {
            userSourceInfo.setTaskInfo(getTimingInfo(timeInfo));
        }

        String mappingAttr = "";
        if (userSourceInfo.getSourcetype() == NumConstant.common_number_1) {

        } else {
            //其它数据来源
            List<?> usAttrList = userSourceDao.queryUSAttrs(userSourceInfo);
            if (StrTool.listNotNull(usAttrList)) {
                for (int i = 0; i < usAttrList.size(); i++) {
                    UserSourceInfo usInfo = (UserSourceInfo) usAttrList.get(i);
                    mappingAttr += usInfo.getLocaluserattr() + ":" + usInfo.getSourceuserattr() + ",";
                }
            }
            mappingAttr = mappingAttr.substring(0, mappingAttr.length() - 1);
        }

        userSourceInfo.setMapingAttr(mappingAttr);

        // 设置域和组织机构信息
        String domainId = "";
        String orginId = "";
        if (userSourceInfo.getOrgunitid() == null) {
            orginId = "0";
        } else {
            orginId = StrTool.intToString(userSourceInfo.getOrgunitid());
        }
        if (userSourceInfo.getDomainid() != null) {
            domainId = StrTool.intToString(userSourceInfo.getDomainid());
        }
        userSourceInfo.setOrgunitIds(domainId + ":" + orginId + ",");

        return userSourceInfo;
    }

    //对显示在页面的时间信息的处理
    private TaskInfo getTimingInfo(TaskInfo taskInfo) {
        //按月设置
        TaskInfo timingInfo = new TaskInfo();
        //按周设置
        if (taskInfo.getTaskmode1() == NumConstant.common_number_2) {
            if (!StrTool.strEquals(taskInfo.getTaskweek(), "*")) {
                timingInfo.setTaskweek(taskInfo.getTaskweek());
            }

        }
        //按天设置
        if (taskInfo.getTaskmode1() == NumConstant.common_number_1) {
            if (taskInfo.getTaskmode2() == NumConstant.common_number_1) {
                if (!StrTool.strEquals(taskInfo.getTaskhour(), "*")) {
                    timingInfo.setTaskhour(taskInfo.getTaskhour());
                }
            }
            if (taskInfo.getTaskmode2() == NumConstant.common_number_2) { //按每n小时
                timingInfo.setSelAccHour(taskInfo.getTaskhour().substring(taskInfo.getTaskhour().indexOf("/") + 1));
            }

        }
        timingInfo.setTaskname(taskInfo.getTaskname());
        timingInfo.setSourceid(taskInfo.getSourceid());
        timingInfo.setTaskmode1(taskInfo.getTaskmode1());
        timingInfo.setTaskmode2(taskInfo.getTaskmode2());
        timingInfo.setEnabled(taskInfo.getEnabled());
        timingInfo.setTaskid(taskInfo.getTaskid());
        return timingInfo;
    }

    /**
     * 得到Domino服务器联接对象
     */
    public Connection getDominoConn(UserSourceInfo dominoInfo) throws BaseException {
        return userSourceDao.getDominoConn(dominoInfo);
    }

    public boolean testUsConn(UserSourceInfo userSourceInfo) throws BaseException {
        boolean result = false;
        try {
            if (StrTool.objNotNull(userSourceInfo)) {
                int userSourceType = userSourceInfo.getSourcetype();
                if (userSourceType == NumConstant.common_number_0) {
                    result = userSourceDao.testDbConn(userSourceInfo);
                } else if (userSourceType == NumConstant.common_number_1) {
                    result = userSourceDao.testLdapConn(getLdapInfo(userSourceInfo));
                } else if (userSourceType == NumConstant.common_number_2) {

                }
            }
        } catch (Exception e) {
            return false;
        }

        return result;
    }

    public String getAllTableName(UserSourceInfo databaseInfo) throws BaseException {
        return userSourceDao.getAllTableName(databaseInfo);
    }

    public String queryFieldsByTableName(UserSourceInfo databaseInfo) throws BaseException {
        return userSourceDao.queryFieldsByTableName(databaseInfo);
    }

    /**
     * 更新用户来源数据
     */
    public Map<String, Object> updateUserInfo(UserSourceInfo userSourceInfo) throws BaseException {
        Map<String, Object> map = new HashMap<String, Object>();// 结果map
        if (!StrTool.objNotNull(userSourceInfo)) {
            map.put("usNull", "usNull");
            return map;
        }

        Integer domainId = userSourceInfo.getDomainid();// 域ID
        Integer orgunitId = userSourceInfo.getOrgunitid();// 组织机构ID
        int userSourceType = userSourceInfo.getSourcetype(); // 用户来源类型

        // 从各用户来源获取的用户
        List<UserInfo> usList = new ArrayList<UserInfo>();
        // 已存在的用户集合
        List<UserInfo> existUserInfoList = new ArrayList<UserInfo>();

        if (userSourceType == NumConstant.common_number_0) {// 从远程数据库更新用户
            Connection conn = null;
            try {
                conn = userSourceDao.getDbConn(userSourceInfo);
                if (!StrTool.objNotNull(conn)) {
                    map.put("connNull", "connNull");
                    return map;
                }

                // 通过数据库更新用户
                String tableName = userSourceInfo.getDbtablename();
                if (!StrTool.strNotNull(tableName)) {
                    map.put("tableNameNull", "tableNameNull");
                    return map;
                }

                // 得到的是本数据库用户表列名和数据来源列名的对应字符串
                String columnName = userSourceInfo.getMapingAttr();
                if (!StrTool.strNotNull(columnName)) {
                    map.put("columnNull", "columnNull");
                    return map;
                }

                // 获取查询远程数据库的sql语句
                String getRemoteUserInfoSql = "select ";
                String[] columnNameMappings = columnName.split(",");
                for (int i = 0; i < columnNameMappings.length; i++) {
                    String columnNameMapping[] = columnNameMappings[i].split(":");
                    if (i > 0) {
                        getRemoteUserInfoSql = getRemoteUserInfoSql + "," + columnNameMapping[1];
                    } else {
                        getRemoteUserInfoSql = getRemoteUserInfoSql + columnNameMapping[1];
                    }
                }
                getRemoteUserInfoSql = getRemoteUserInfoSql + " from " + tableName;

                // 只执行一次查徇，使用Statement提交效率
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(getRemoteUserInfoSql);
                while (rs.next()) {
                    UserInfo userInfo = new UserInfo();
                    for (int i = 0; i < columnNameMappings.length; i++) {
                        String[] columnNameMapping = columnNameMappings[i].split(":");
                        String property = columnNameMapping[0];
                        String value = columnNameMapping[1];
                        if (StrTool.strNotNull(property)) {
                            UserSourceDaoImpl.setUserInfoByProperty(userInfo, property, rs.getString(value));
                        }
                    }

                    // 判断用户是否已经存在
                    if (StrTool.strNotNull(userInfo.getUserId())) {
                        if (!userIdIsExist(userInfo, usList)) {
                            // 对更新的用户数据设置默认值
                            userInfo.setDomainId(domainId);
                            userInfo.setOrgunitId(orgunitId);
                            userInfo.setPwd(PwdEncTool.encPwd(null));

                            usList.add(userInfo);
                        } else {
                            existUserInfoList.add(userInfo);
                        }
                    } else {
                        existUserInfoList.add(userInfo);
                    }
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                map.put("errorStr", "errorStr");
                return map;
            } finally {
                DBconnection.closeConnection(conn);
            }
        } else if (userSourceType == NumConstant.common_number_1) {//从LDAP更新用户
            try {
                UpdateUserByAD updateUserByAD = new UpdateUserByAD(orgunitInfoServ, adminAndOrgunitServ);
                usList = updateUserByAD.updateUserByAD(userSourceInfo, 0);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                map.put("ldapNull", "ldapNull");

                return map;
            }

        } else if (userSourceType == NumConstant.common_number_2) {// 从domino更新用户

        }

        // 处理从数据源更新过来的用户集合
        int findNum = usList.size();
        String updateResultInfoString = "";// 用户来源更新的总的结果信息

        if (StrTool.listNotNull(usList) || findNum < 1) {
            // 过滤已经存在的用户
            List<?> userInfoList = new ArrayList();
            //组织机构的用户
            List<?> orgUInfoList = null;
            try {
                UserInfo queryUserInfo = new UserInfo();
                queryUserInfo.setOrgFlag(-1);
                queryUserInfo.setDomainId(domainId);//查询当前域所有用户
//                int orgattr[] = null;
//                if (orgunitId != null) {
//                    orgattr = new int[1];
//                    orgattr[0] = orgunitId;
//                    queryUserInfo.setOrgunitIds(orgattr);
//                }
                userInfoList = userInfoServ.queryBind(queryUserInfo, new PageArgument());
                
                //如果选择了删除或者禁用本地用户，则查询组织机构下用户，做比较
                if (userSourceInfo.getLocalusermark() != 0) {
                    boolean isUpdateOU = userSourceInfo.getIsupdateou() == 1;//是否更新AD
                    orgUInfoList = new ArrayList();
                    int orgattr[] = null;
                    if (orgunitId != null) {
                        if (isUpdateOU) {
                            //查询此组织机构下的组织机构
                            OrgunitInfo orgInfo = new OrgunitInfo();
                            orgInfo.setParentId(orgunitId);
                            List<OrgunitInfo> orgList = (List<OrgunitInfo>) orgunitInfoServ.queryOrgSonunit(orgInfo);
                            
                            if(StrTool.listNotNull(orgList)) {
                                int len = orgList.size()+1;
                                orgattr = new int[len];
                                orgattr[0] = orgunitId;
                                OrgunitInfo orgunit = null;
                                for (int i=0; i< orgList.size(); i++) {
                                    orgunit = orgList.get(i);
                                    orgattr[i+1] = orgunit.getOrgunitId();
                                }
                            } else {
                                orgattr = new int[1];
                                orgattr[0] = orgunitId;
                            }
                        } else {
                            //指定本地组织机构
                            orgattr = new int[1];
                            orgattr[0] = orgunitId;
                        }
                        queryUserInfo.setOrgunitIds(orgattr);
                        queryUserInfo.setOrgFlag(2);
                        orgUInfoList = userInfoServ.queryBind(queryUserInfo, new PageArgument());
                    } else {
                        //只查询本域下，不包括子机构
                        queryUserInfo.setOrgFlag(3);
                        orgUInfoList = userInfoServ.queryBind(queryUserInfo, new PageArgument());
                    }
                }

                //因为之前查询用户列表的sql语句将OrgunitId和RadProfileId的null值置为了0,而数据库中又需要null的情况，所以将0值对象设为null
                for (Object object : userInfoList) {
                    UserInfo userInfo = (UserInfo) object;
                    if (null != userInfo.getOrgunitId() && userInfo.getOrgunitId().intValue() == 0) {
                        userInfo.setOrgunitId(null);
                    }
                    if (null != userInfo.getRadProfileId() && userInfo.getRadProfileId().intValue() == 0) {
                        userInfo.setRadProfileId(null);
                    }
                }

            } catch (BaseException e) {
                logger.error(e.getMessage(), e);
            }

            Map<String, List<UserInfo>> userList_map = getUpdateLists(userSourceInfo, (List<UserInfo>) userInfoList,
                    usList, 1, (List<UserInfo>)orgUInfoList);
            List<UserInfo> update_list = userList_map.get("update_list");
            List<UserInfo> add_list = userList_map.get("add_list");
            List<UserInfo> disable_list = userList_map.get("disable_list");
            List<UserInfo> del_list = userList_map.get("del_list");
            List<UserInfo> invalidUlist = userList_map.get("invalidUlist");
            
            String comma = Language.getLangValue("comma", Language.getCurrLang(null), null);// ，
            
            //判断AD用户信息是否合法，邮箱、手机（主要是长度校验）
            StringBuffer emailAddSb = new StringBuffer();
            StringBuffer phoneAddSb = new StringBuffer();
            int noInsertNum = 0;
            for(Iterator ite = add_list.iterator(); ite.hasNext();){ 
                UserInfo add_user = (UserInfo)ite.next(); 
                boolean flag = false;
                if (StrTool.strNotNull(add_user.getEmail()) && (add_user.getEmail()).length()>255) {
                    flag = true;
                    emailAddSb.append(add_user.getUserId()).append(comma);
                    ite.remove();
                }
                if (StrTool.strNotNull(add_user.getCellPhone()) && (add_user.getCellPhone()).length()>64) {
                    flag = true;
                    phoneAddSb.append(add_user.getUserId()).append(comma);
                    ite.remove();
                }
                if (flag){
                    noInsertNum++;
                }
            }
            if (StrTool.objNotNull(emailAddSb) && StrTool.strNotNull(emailAddSb.toString())) {
                emailAddSb.append(Language.getLangValue("usource_email_is_long_add", Language.getCurrLang(null), null));
            }
            if (StrTool.objNotNull(phoneAddSb) && StrTool.strNotNull(phoneAddSb.toString())) {
                phoneAddSb.append(Language.getLangValue("usource_tel_is_long_add", Language.getCurrLang(null), null));
            }
            
            StringBuffer emailUpSb = new StringBuffer();
            StringBuffer phoneUpSb = new StringBuffer();
            for(Iterator ite = update_list.iterator(); ite.hasNext();){ 
                UserInfo up_user = (UserInfo)ite.next();  
                if (StrTool.strNotNull(up_user.getEmail()) && (up_user.getEmail()).length()>255) {
                    emailUpSb.append(up_user.getUserId()).append(comma);
                    ite.remove();
                }
                if (StrTool.strNotNull(up_user.getCellPhone()) && (up_user.getCellPhone()).length()>64) {
                    phoneUpSb.append(up_user.getUserId()).append(comma);
                    ite.remove();
                }
            }
            if (StrTool.objNotNull(emailUpSb) && StrTool.strNotNull(emailUpSb.toString())) {
                emailUpSb.append(Language.getLangValue("usource_email_is_long_update", Language.getCurrLang(null), null));
            }
            if (StrTool.objNotNull(phoneUpSb) && StrTool.strNotNull(phoneUpSb.toString())) {
                phoneUpSb.append(Language.getLangValue("usource_tel_is_long_update", Language.getCurrLang(null), null));
            }

            //比较哪些可以添加，哪些可以删除，哪些是需要修改的
            if (StrTool.listNotNull(add_list)) {
                userInfoServ.batchImportUs(add_list);
            }
            if (StrTool.listNotNull(update_list)) {
                userInfoServ.batchUpdateUser(update_list);
            }
            if (StrTool.listNotNull(disable_list)) {
                userInfoServ.batchUpdateUser(disable_list);
            }
            if (StrTool.listNotNull(del_list)) {
                userInfoServ.batchDelUser(del_list);
            }
            
            int addNum = add_list.size();
            int updateNum = update_list.size();
            int existUserInfoCount = 0;
            if (StrTool.listNotNull(invalidUlist)) {
                existUserInfoCount = findNum - invalidUlist.size();
            } else {
                existUserInfoCount = (findNum - addNum - noInsertNum);
            }
            
            String infonum = Language.getLangValue("usource_records_user_info", Language.getCurrLang(null), null);
            String infos = Language.getLangValue("usource_records_userinfo", Language.getCurrLang(null), null);
            updateResultInfoString = Language.getLangValue("usource_find_total", Language.getCurrLang(null), null)
                    + findNum + infonum + comma
                    + Language.getLangValue("usource_succ_insert", Language.getCurrLang(null), null) + addNum + infos;
            if (existUserInfoCount > 0) {
                updateResultInfoString = updateResultInfoString + comma
                        + Language.getLangValue("usource_userinfo_local_is_exist", Language.getCurrLang(null), null)
                        + existUserInfoCount + infos;
            }

            if (updateNum > 0) {
                updateResultInfoString = updateResultInfoString + comma
                        + Language.getLangValue("usource_already_update", Language.getCurrLang(null), null) + updateNum
                        + infos;
            }
            int delNum = del_list.size();
            int disabledNum = disable_list.size();
            if (disabledNum > 0) {
                updateResultInfoString = updateResultInfoString + comma
                + Language.getLangValue("usource_already_disable", Language.getCurrLang(null), null) + disabledNum
                + infos;
            }
            if (delNum > 0) {
                updateResultInfoString = updateResultInfoString + comma
                + Language.getLangValue("usource_already_delete", Language.getCurrLang(null), null) + delNum
                + infos;
            }

            updateResultInfoString = updateResultInfoString
                    + Language.getLangValue("plaint", Language.getCurrLang(null), null);
            
            if(noInsertNum > 0) {
                updateResultInfoString = updateResultInfoString 
                + Language.getLangValue("usource_no_insert_userinfo", Language.getCurrLang(null), null) + noInsertNum 
                + Language.getLangValue("usource_records_userinfo", Language.getCurrLang(null), null);
            }
            
            if (StrTool.objNotNull(emailAddSb) && StrTool.strNotNull(emailAddSb.toString())) {
                updateResultInfoString = updateResultInfoString + emailAddSb.toString();
                commonObj.addAdminLog(AdmLogConstant.log_aid_usersoure_import, AdmLogConstant.log_obj_user, emailAddSb.toString(), null, 1);
            }
            if (StrTool.objNotNull(phoneAddSb) && StrTool.strNotNull(phoneAddSb.toString())) {
                updateResultInfoString = updateResultInfoString + phoneAddSb.toString();
                commonObj.addAdminLog(AdmLogConstant.log_aid_usersoure_import, AdmLogConstant.log_obj_user, phoneAddSb.toString(), null, 1);
            }
            if (StrTool.objNotNull(emailUpSb) && StrTool.strNotNull(emailUpSb.toString())) {
                updateResultInfoString = updateResultInfoString + emailUpSb.toString();
                commonObj.addAdminLog(AdmLogConstant.log_aid_usersoure_import, AdmLogConstant.log_obj_user, emailUpSb.toString(), null, 1);
            }
            if (StrTool.objNotNull(phoneUpSb) && StrTool.strNotNull(phoneUpSb.toString())) {
                updateResultInfoString = updateResultInfoString + phoneUpSb.toString();
                commonObj.addAdminLog(AdmLogConstant.log_aid_usersoure_import, AdmLogConstant.log_obj_user, phoneUpSb.toString(), null, 1);
            }
        } else {
            map.put("usInfoNotExist", "usInfoNotExist");
            return map;
        }

        map.put("updateResultStr", updateResultInfoString);
        map.put("existUserList", existUserInfoList);

        return map;
    }

    /**
     * 判断此用户是否已经在列表中存在
     * @param userInfo
     * @param usList
     * @return
     */
    public boolean userIdIsExist(UserInfo userInfo, List usList) {
        boolean notExist = false;
        for (UserInfo ui : (List<UserInfo>) usList) {
            if ((ui.getUserId().equalsIgnoreCase(userInfo.getUserId()))) {
                notExist = true;
                break;
            }
        }

        return notExist;
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

    /**
     * 根据外部用户数据源和本地用户进行比较,得到可添加或修改的列表
     * @Date in 2013-6-18,下午06:28:00
     * @param userSourceInfo 用户来源数据
     * @param local_user_list 本地用户数据源
     * @param other_user_list 其它用户数据源
     * @param updateType 更新类型(1为完全更新,0为只添加新用户)
     * @param orgUInfoList 用来对比删除或者禁用的用户，对比组织机构下，而不是整个域下
     * @return 返回一个Map,键"update_list"对应要修改的用户列表,"add_list"对应要添加的用户列表
     */
    private Map<String, List<UserInfo>> getUpdateLists(UserSourceInfo userSourceInfo, List<UserInfo> local_user_list,
            List<UserInfo> other_user_list, int updateType, List<UserInfo> orgUInfoList) {
        List<UserInfo> delete_list = new ArrayList<UserInfo>();//要删除的用户信息
        List<UserInfo> disable_list = new ArrayList<UserInfo>();//要禁用的用户信息
        List<UserInfo> update_list = new ArrayList<UserInfo>();//要更新的用户信息
        List<UserInfo> add_list = new ArrayList<UserInfo>();//要更新的用户信息

        //比较两个列表，设置要增加的用户列表，要更新的用户列表，要删除的用户列表
        //先用db比ldap,获取db用户列表中要删除的和要更新的列表
        if (updateType == 1) {
            //完全更新
            for (UserInfo local_userInfo : local_user_list) {
                boolean existInOther = false;//在 ad中存在
                for (UserInfo ldap_userInfo : other_user_list) {
                    if (local_userInfo.getUserId().equals(ldap_userInfo.getUserId())) {
                        //其它信息不同,要更新
                        if (checkIsUpdate(userSourceInfo, local_userInfo, ldap_userInfo)) {
                             if (userSourceInfo.getSourcetype() == 1) {
                                 if (userSourceInfo.getIssyncuserinfo() == 1) {
                                     update_list.add(local_userInfo);//添加到更新列表
                                 } else {
                                     //更新禁用用户
//                                     if (userSourceInfo.getUpinvaliduser() == 1 && ldap_userInfo.getEnabled()==0) {
//                                         update_list.add(local_userInfo);
//                                     }
                                 }
                             }
                        } else {
                            //已存在用户
                        }
                        existInOther = true;//在Other中存在
                        break;
                    }
                }
                if (!existInOther && userSourceInfo.getSourcetype() == 1) {
                    if (userSourceInfo.getLocalusermark() == 1) {
                        if (StrTool.listNotNull(orgUInfoList)) {
                            for (UserInfo orgUser : orgUInfoList) {
                                if (local_userInfo.getUserId().equals(orgUser.getUserId())) {
                                    if (local_userInfo.getEnabled() != 0) {
                                        local_userInfo.setEnabled(0);//将用户禁用
                                        disable_list.add(local_userInfo);
                                    }
                                    break;
                                }
                            }
                        }
                    } else if (userSourceInfo.getLocalusermark() == 2) {
                        if (StrTool.listNotNull(orgUInfoList)) {
                            for (UserInfo orgUser : orgUInfoList) {
                                if (local_userInfo.getUserId().equals(orgUser.getUserId())) {
                                    delete_list.add(local_userInfo);//将db中用户加入删除列表
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        //用ldap和db中数据比,得到要添加的用户
        for (UserInfo other_userInfo : other_user_list) {
            boolean existInLocal = false;//在 db中存在
            for (UserInfo local_userInfo : local_user_list) {
                //找到相同的用户名
                if (other_userInfo.getUserId().equals(local_userInfo.getUserId())) {
                    existInLocal = true;
                    break;
                }
            }
            if (!existInLocal) {
                other_userInfo.setRadProfileId(null);
                add_list.add(other_userInfo);
            }
        }
        
        Map<String, List<UserInfo>> map = new HashMap<String, List<UserInfo>>();
        List<UserInfo> invalidUlist = null;//无效用户，记录统计数
        try {
            //判断AD中是否已存在删除用户，存在就将删除用户移除
            if (StrTool.listNotNull(delete_list)) {
                UpdateUserByAD updateUserByAD = new UpdateUserByAD(orgunitInfoServ, adminAndOrgunitServ);
                List<UserInfo> allUsList = updateUserByAD.updateUserByAD(userSourceInfo, 1);
                for(Iterator it = delete_list.iterator(); it.hasNext();){  
                    UserInfo del_user = (UserInfo)it.next();  
                    for (UserInfo ad_user : allUsList) {
                        if (del_user.getUserId().equals(ad_user.getUserId())) {
                            it.remove();
                            break;
                        }
                    }  
                }  
            }
            //判断AD中是否已存在此禁用用户，存在就将禁用用户移除
            if (StrTool.listNotNull(disable_list)) {
                UpdateUserByAD updateUserByAD = new UpdateUserByAD(orgunitInfoServ, adminAndOrgunitServ);
                List<UserInfo> allUsList = updateUserByAD.updateUserByAD(userSourceInfo, 1);
                for(Iterator ite = disable_list.iterator(); ite.hasNext();){  
                    UserInfo dis_user = (UserInfo)ite.next();  
                    for (UserInfo ad_user : allUsList) {
                        if (dis_user.getUserId().equals(ad_user.getUserId())) {
                            ite.remove();
                            break;
                        }
                    }  
                }  
            }
            
            //不插入禁用用户，如本地不存在且禁用的用户移除
            if (StrTool.listNotNull(add_list) && userSourceInfo.getUpinvaliduser() == 0) {
                invalidUlist = new ArrayList<UserInfo>();
                for(Iterator ite = add_list.iterator(); ite.hasNext();){
                    boolean isExist = false;
                    UserInfo adduser = (UserInfo)ite.next();  
                    for (UserInfo l_user : local_user_list) {
                        if (adduser.getUserId().equals(l_user.getUserId())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist && adduser.getEnabled() == 0) {
                        invalidUlist.add(adduser);
                        ite.remove();
                    }
                }  
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        map.put("update_list", update_list);
        map.put("add_list", add_list);
        map.put("del_list", delete_list);
        map.put("disable_list", disable_list);
        
        map.put("invalidUlist", invalidUlist);
        return map;
    }

    /**
     * 检查用户数据是否发生了变化,如果发生变化，将ldap数据设置到db用户中(暂时只同步真实姓名,邮箱,是否禁用,组织机构信息)
     * @Date in 2013-3-9,下午06:52:01
     * @param userSourceInfo 用户来源数据
     * @param local_userInfo 本地用户
     * @param other_userInfo 其它用户
     * @return 需要更新返回true,否则返回false
     */
    private boolean checkIsUpdate(UserSourceInfo userSourceInfo, UserInfo local_userInfo, UserInfo other_userInfo) {
        boolean flag = false;

        //比较真实姓名
        String ldapUserRealName = other_userInfo.getRealName();
        if (!equals2Str(local_userInfo.getRealName(), ldapUserRealName)) {
            local_userInfo.setRealName(ldapUserRealName);
            flag = true;
        }

        //比较邮箱
        String otherEmail = other_userInfo.getEmail();
        if (!equals2Str(local_userInfo.getEmail(), otherEmail)) {
            local_userInfo.setEmail(otherEmail);
            flag = true;
        }

        //比较手机号
        String cellphone = other_userInfo.getCellPhone();
        if (!equals2Str(local_userInfo.getCellPhone(), cellphone)) {
            local_userInfo.setCellPhone(cellphone);
            flag = true;
        }
        
        //比较是否禁用
        if (local_userInfo.getEnabled() != other_userInfo.getEnabled()) {
            local_userInfo.setEnabled(other_userInfo.getEnabled());
            flag = true;
        }

        //如果用户来源是AD并且查找OU目录下用户时比较更新组织机构,不是OU目录时不比较更新
        if (userSourceInfo.getSourcetype() == NumConstant.common_number_1) {
            LdapInfo ldapInfo = getLdapInfo(userSourceInfo);
            if (null != ldapInfo && null != ldapInfo.getLdapDn()) {
                String baseDN = ldapInfo.getLdapDn();
                baseDN = baseDN.toUpperCase();
//                if (baseDN.indexOf("OU=") > -1) {
                    Integer other_orgunitId = other_userInfo.getOrgunitId();
                    Integer local_orgunitId = local_userInfo.getOrgunitId();
                    if (null == local_orgunitId || null == other_orgunitId) {
                        //有一方是null
                        if (local_orgunitId != other_orgunitId) {
                            local_userInfo.setOrgunitId(other_orgunitId);
                            flag = true;
                        }
                    } else {
                        //都不为null时比其拆箱后的值
                        if (!local_orgunitId.equals(other_orgunitId)) {
                            local_userInfo.setOrgunitId(other_orgunitId);
                            flag = true;
                        }
                    }
//                } else {
//                    //System.out.println("baseDN包不含OU,不比较OU");
//                }
            }
        }
        return flag;
    }

    /**
     * 比较两字符串是否相同,null按空串比较
     * @Date in 2013-6-18,下午05:29:28
     * @param source
     * @param target
     * @return
     */
    private boolean equals2Str(String source, String target) {
        source = null == source ? "" : source.trim();
        target = null == target ? "" : target.trim();
        return source.equals(target);
    }

}
