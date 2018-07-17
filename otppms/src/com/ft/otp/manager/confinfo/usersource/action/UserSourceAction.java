/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.usersource.action;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.confinfo.usersource.service.IUserSourceServ;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用户来源配置实现
 * 
 * @Date in Jun 7, 2011,11:16:43 AM
 * 
 * @author YYF
 */
public class UserSourceAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -4702373506987885717L;
    private Logger logger = Logger.getLogger(UserSourceAction.class);
    // 数据来源配置接口
    private IUserSourceServ userSourceServ;

    //定时时间信息服务接口
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");

    private UserSourceInfo userSourceInfo = null;

    /**
     * 
     * 方法说明
     * @Date in Mar 5, 2013,5:37:48 PM
     * @return
     */
    public UserSourceInfo getUserSourceInfo() {
        if (!StrTool.objNotNull(userSourceInfo)) {
            userSourceInfo = new UserSourceInfo();
        }
        return userSourceInfo;
    }

    /**
     * 
     * 方法说明
     * @Date in Mar 5, 2013,5:38:09 PM
     * @return
     */
    public IUserSourceServ getUserSourceServ() {
        return userSourceServ;
    }

    /**
     * 
     * 方法说明
     * @Date in Mar 5, 2013,5:38:14 PM
     * @param userSourceServ
     */
    public void setUserSourceServ(IUserSourceServ userSourceServ) {
        this.userSourceServ = userSourceServ;
    }

    /**
     * 
     * 方法说明
     * 添加用户来源
     * @Date in Mar 5, 2013,5:37:48 PM
     * @return
     */
    public String add() {

        try {
            if (StrTool.objNotNull(userSourceInfo)) {
                userSourceInfo = setDomainAndOrgunit(userSourceInfo, userSourceInfo.getOrgunitIds());
                userSourceServ.addObj(userSourceInfo);
                PubConfConfig.reload();
                UserSourceInfo usInfo = new UserSourceInfo();
                usInfo.setSourcename(userSourceInfo.getSourcename());
                usInfo.setSourcetype(-1);
                usInfo = (UserSourceInfo) userSourceServ.find(usInfo);
                outPutOperResult(Constant.alert_succ, usInfo.getId());
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 方法说明
     * 封装域和组织机构作为令牌查询条件的一部分
     * @Date in Apr 28, 2011,6:11:38 PM
     * @param userSourceInfo
     * @param orgunitStr
     * @return UserSourceInfo
     */
    public UserSourceInfo setDomainAndOrgunit(UserSourceInfo userSourceInfo, String orgunitStr) {
        if (StrTool.strNotNull(orgunitStr)) {
            String orgunits = orgunitStr.substring(0, orgunitStr.length() - 1);
            String orgunit[] = orgunits.split(":");
            //组织机构所在的域
            if (StrTool.strNotNull(orgunit[0])) {
                userSourceInfo.setDomainid(StrTool.parseInt(orgunit[0]));
            }
            //组织机构
            if (StrTool.strNotNull(orgunit[1]) && !StrTool.strEquals(StrConstant.common_number_0, orgunit[1])) {
                userSourceInfo.setOrgunitid(StrTool.parseInt(orgunit[1]));
            }
        }

        return userSourceInfo;
    }

    /**
     * 
     * 方法说明
     * 删除用户来源
     * @Date in Mar 5, 2013,5:37:48 PM
     * @return
     */
    public String delete() {
        Set<?> keys = getDelIds("delIds");
        try {
            if (StrTool.objNotNull(keys)) {
                //删除用户来源
                userSourceServ.delObj(keys);
                //删除用户来源的定时执行任务
                int i = 0;
                Scheduler scheduler = TaskConfig.getScheduler();
                TaskInfo taskInfo = new TaskInfo();
                int batchStr[] = new int[keys.size()];
                for (Iterator<?> iterator = keys.iterator(); iterator.hasNext();) {
                    int confInfoId = StrTool.parseInt(String.valueOf(iterator.next()));
                    batchStr[i] = confInfoId;
                    i++;
                }
                taskInfo.setBatchIdsInt(batchStr);
                taskInfo.setSourcetype(0);
                //设置删除时不考虑是否启用状态
                taskInfo.setEnabled(-1);
                List<?> timeInfoList = taskInfoServ.query(taskInfo, new PageArgument());
                if (StrTool.listNotNull(timeInfoList)) {
                    for (int j = 0; j < timeInfoList.size(); j++) {
                        TaskInfo timInfo = (TaskInfo) timeInfoList.get(j);
                        RegScheduler.delTask(scheduler, timInfo.getTaskid());
                    }
                }
                //删除用户来源定时时间
                taskInfoServ.delObj(keys);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
            }
        } catch (BaseException e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(e.getMessage(), e);
        }
        PubConfConfig.reload();
        return null;
    }

    /**
     * 
     * 方法说明
     * 查找用户来源
     * @Date in Mar 5, 2013,5:37:48 PM
     * @return
     */
    public String find() {

        return null;
    }

    /**
     * 方法说明
     * 行数统计 分页处理
     * 
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = userSourceServ.count(getUserSourceInfo());
        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 
     * 方法说明
     * 初始化用户来源列表
     * @Date in Mar 5, 2013,5:37:48 PM
     * @return
     */
    public String init() {
        try {
            PageArgument pageArg = pageArgument();
            pageArg.setPageSize(getPagesize());
            pageArg.setCurPage(getPage());
            List<?> resultList = query(pageArg);
            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(json);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 方法说明
     * 根据查询分页列表
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param pageArg
     * @return serverconfList
     */
    private List<?> query(PageArgument pageArg) {
        List<?> serverconfList = null;
        List<?> timingList = null;
        TaskInfo timingInfo = new TaskInfo();
        timingInfo.setEnabled(-1);
        timingInfo.setSourcetype(0);
        try {
            serverconfList = userSourceServ.query(getUserSourceInfo(), pageArg);
            timingList = taskInfoServ.query(timingInfo, new PageArgument());
            serverconfList = getUsourceList(serverconfList, timingList);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return serverconfList;
    }

    /**
     * 方法说明
     * 根据定时信息状态，确定用户来源更新的状态
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param usList
     * @param timingList
     * @return newusList
     */
    public List<?> getUsourceList(List<?> usList, List<?> timingList) {
        List<Object> newusList = new ArrayList<Object>();
        if (StrTool.listNotNull(usList)) {
            Iterator<?> iter = usList.iterator();
            while (iter.hasNext()) {
                UserSourceInfo usInfo = (UserSourceInfo) iter.next();
                if (StrTool.listNotNull(timingList)) {
                    for (int i = 0; i < timingList.size(); i++) {
                        TaskInfo taskInfo = (TaskInfo) timingList.get(i);
                        if (taskInfo.getSourceid() == usInfo.getId()) {
                            usInfo.setTimingState(taskInfo.getEnabled());
                        }
                        continue;
                    }
                }

                newusList.add(usInfo);
            }
        }

        return newusList;

    }

    /**
     * 方法说明
     * 修改用户来源
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(userSourceInfo)) {
                userSourceInfo = setDomainAndOrgunit(userSourceInfo, userSourceInfo.getOrgunitIds());
                userSourceServ.updateObj(userSourceInfo);
                PubConfConfig.reload();
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 方法说明
     * 对用户来源进行分页
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String page() {
        PageArgument pageArg = getArgument(request, 0);
        query(pageArg);

        return SUCCESS;
    }

    /**
     * 方法说明
     * 查看用户来源
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String view() {
        String flag = request.getParameter("flag");
        try {
            //根据id查找用户来源信息
            if (StrTool.objNotNull(userSourceInfo)) {
                UserSourceInfo usInfo = new UserSourceInfo();
                usInfo.setId(userSourceInfo.getId());
                usInfo.setSourcetype(-1);
                userSourceInfo = userSourceServ.getUserSourceInfo(usInfo);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        if (StrTool.strEquals(flag, StrConstant.common_number_1)) {
            return "settiming";
        } else {
            request.setAttribute("isEdit", "isEdit");
            request.setAttribute("usType", userSourceInfo.getSourcetype());

            return "usconfig";
        }
    }

    /**
     * 方法说明
     * 检查同一类型下是否存在同名的配置
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String checkUSName() {
        try {
            String uSName = userSourceInfo.getSourcename();
            String oldUsName = userSourceInfo.getOldsourcename();
            int uSType = userSourceInfo.getSourcetype();

            uSName = MessyCodeCheck.iso885901ForUTF8(uSName);
            oldUsName = MessyCodeCheck.iso885901ForUTF8(oldUsName);
            if (StrTool.strEquals(uSName, oldUsName)) {
                setResponseWrite("true");
                return null;
            }

            userSourceInfo.setSourcename(uSName);
            userSourceInfo.setSourcetype(-1);
            UserSourceInfo resultUSInfo = (UserSourceInfo) userSourceServ.find(userSourceInfo);
            if (null == resultUSInfo) {
                setResponseWrite("true");
                return null;
            }
        } catch (BaseException e) {
            outPutOperResult(Constant.alert_error, "error");
            logger.error(e.getMessage(), e);
        }
        setResponseWrite("false");
        return null;
    }

    /**
     * 验证用户来源名称
     * @Date in Jul 3, 2013,4:41:07 PM
     * @return
     */
    public String validateUSName() {
        try {
            String uSName = userSourceInfo.getSourcename();
            if (!StrTool.strNotNull(uSName)) {
                return null;
            }
            uSName = MessyCodeCheck.iso885901ForUTF8(uSName);
            userSourceInfo.setSourcename(uSName);
            userSourceInfo.setSourcetype(-1);
            UserSourceInfo resultUSInfo = (UserSourceInfo) userSourceServ.find(userSourceInfo);
            if (null == resultUSInfo) {
                setResponseWrite("true");
                return null;
            }
        } catch (BaseException e) {
            outPutOperResult(Constant.alert_error, "error");
            logger.error(e.getMessage(), e);
        }
        setResponseWrite("false");
        return null;
    }

    /**
     * 用户来源连接测试
     * 
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String testConnection() {
        try {
            if (userSourceServ.testUsConn(userSourceInfo)) {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_conn_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_conn_error_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_conn_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 方法说明
     * 更新用户来源数据
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String updateUserInfo() {
        String isEdit = request.getParameter("isEdit");
        int sourceid = userSourceInfo.getId();
        String taskname = userSourceInfo.getSourcename();
        Scheduler scheduler = TaskConfig.getScheduler();
        String taskId = "";
        TaskInfo tkInfo = geTaskInfo(userSourceInfo);
        boolean flag = false;
        boolean isExist = false;
        try {
            if (StrTool.objNotNull(tkInfo)) {
                if (userSourceInfo.getTaskInfo().getEnabled() == NumConstant.common_number_1) {//启用定时任务
                    if (StrTool.strEquals("false", isEdit)) {//新增用户来源启用定时任务
                        //新添加用户来源，启用定时加入一个新的定时任务
                        Task task = new UserSourceTask(tkInfo);
                        taskId = RegScheduler.addTask(scheduler, task, tkInfo.getTaskminute(), tkInfo.getTaskhour(),
                                tkInfo.getTaskday(), tkInfo.getTaskmonth(), tkInfo.getTaskweek());

                    } else {//修改定时任务编辑
                        taskId = tkInfo.getTaskid();
                        if (StrTool.strNotNull(taskId) && StrTool.strNotNull(StrTool.intToString(sourceid))) {
                            //任务存在，更新任务执行时间
                            RegScheduler.updateTask(scheduler, taskId, tkInfo.getTaskminute(), tkInfo.getTaskhour(),
                                    tkInfo.getTaskday(), tkInfo.getTaskmonth(), tkInfo.getTaskweek());
                            isExist = false;
                        } else {//任务不存在
                            // 加入一个新的定时任务
                            Task task = new UserSourceTask(tkInfo);
                            taskId = RegScheduler.addTask(scheduler, task, tkInfo.getTaskminute(),
                                    tkInfo.getTaskhour(), tkInfo.getTaskday(), tkInfo.getTaskmonth(), tkInfo
                                            .getTaskweek());
                            isExist = true;
                        }

                    }
                    tkInfo.setTaskid(taskId);

                    //修改用户来源，更新定时时间 
                    if (StrTool.strEquals("true", isEdit) && !isExist) {
                    	tkInfo.setLogFlag(1); // 是否记录日志标志
                        taskInfoServ.updateObj(tkInfo);
                        flag = true;
                    } else {//添加用户来源
                        //获取用户来源Id
                        UserSourceInfo usInfo = (UserSourceInfo) userSourceServ.find(userSourceInfo);
                        if (StrTool.objNotNull(usInfo)) {
                            sourceid = usInfo.getId();
                            taskname = usInfo.getSourcename();
                        }
                        tkInfo.setSourceid(sourceid);
                        tkInfo.setTaskname(taskname);
                        tkInfo.setSourcetype(0);
                        tkInfo.setLogFlag(1); // 是否记录日志标志
                        taskInfoServ.addObj(tkInfo);
                        flag = true;
                    }
                } else { //禁用定时任务 更新状态
                	tkInfo.setLogFlag(1); // 是否记录日志标志
                	//删除任务表中的信息
                	Set <Integer> set = new HashSet<Integer>();
                	set.add(tkInfo.getSourceid());
                	taskInfoServ.delObj(set);
                	//删除内存中的定时任务
                    RegScheduler.delTask(scheduler, tkInfo.getTaskid());
                    flag = true;
                }
            }

            if (!flag) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_update_error_tip"));
                return null;
            }
            
            // 重新加载定时任务
            TaskConfig.reload();
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_update_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_update_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 方法说明
     * 由数据库类型得到所有表名列表
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param 
     * @return 
     */
    public String queryAllTableName() {
        String tableNames = "";
        try {
            tableNames = userSourceServ.getAllTableName(userSourceInfo);
            outPutOperResult(Constant.alert_succ, tableNames);
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, "failed");
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 方法说明
     * 由数据库表名得到字段列表
     * @Date in Mar 5, 2013,5:36:00 PM
     * @return
     */
    public String queryFieldsByTabName() {
        String tableName = "";
        String dbtablename = request.getParameter("dbtablename");
        try {
            if (StrTool.strNotNull(dbtablename)) {
                userSourceInfo.setDbtablename(dbtablename);
            }
            tableName = userSourceServ.queryFieldsByTableName(userSourceInfo);
            outPutOperResult(Constant.alert_succ, tableName);
            return null;
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, "failed");
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
      * 方法说明
      * 用户来源手动更新
      * @Date in Mar 13, 2012,7:02:49 PM
      * @return
      */
    @SuppressWarnings("unchecked")
    public String manuallyUpdate() {
        String usid = request.getParameter("usId");
        String usType = request.getParameter("usType");
        String usName = request.getParameter("usName");
        try {
            UserSourceInfo userSourceInfo = getUserSourceInfo();
            if (StrTool.strNotNull(usid)) {
                userSourceInfo.setId(StrTool.parseInt(usid));
            }
            if (StrTool.strNotNull(usName) && !StrTool.strEquals(usName, "undefined")) {
                userSourceInfo.setSourcename(usName);
            }
            userSourceInfo.setSourcetype(StrTool.parseInt(usType));
            userSourceInfo = userSourceServ.getUserSourceInfo(userSourceInfo);

            Map<String, Object> map = null;
            try {
                map = userSourceServ.updateUserInfo(userSourceInfo);
            } catch (Exception e) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_update_error_tip"));
                logger.error(e.getMessage(), e);
                return null;
            }

            String updateMessage = "";
            if (StrTool.strNotNull((String) map.get("updateResultStr"))) {
                updateMessage = map.get("updateResultStr") + "";
                outPutOperResult(Constant.alert_succ, updateMessage);
                return null;
            } else {
                if (StrTool.strEquals((String) map.get("tableNameNull"), "tableNameNull")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request,
                            "usource_no_get_date_source_tab"));
                } else if (StrTool.strEquals((String) map.get("columnNull"), "columnNull")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request,
                            "usource_no_get_column_relation"));
                } else if (StrTool.strEquals((String) map.get("connNull"), "connNull")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "usource_no_get_db_conn"));
                } else if (StrTool.strEquals((String) map.get("ldapNull"), "ldapNull")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "usource_no_get_ldap_info"));
                } else if (StrTool.strEquals((String) map.get("usInfoNotExist"), "usInfoNotExist")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "usource_no_userinfo"));
                } else if (StrTool.strEquals((String) map.get("usNull"), "usNull")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "usource_no_get_usource_info"));
                } else if (StrTool.strEquals((String) map.get("errorStr"), "errorStr")) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "usource_update_usource_err"));
                }
                return null;
            }
        } catch (BaseException e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_update_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /** 
     * 
     * 方法说明
     * 根据选择的ldap类型，动态加载database.jsp或ldap.jsp、domino.jsp
     * @Date in Mar 13, 2012,7:02:49 PM
     * @return
     */
    public String freshPage() {
        //初始化request
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);

        int ustype = userSourceInfo.getSourcetype();
//        userSourceInfo.setUsername("");
        String usName = userSourceInfo.getSourcename();
        String usDecp = userSourceInfo.getDescp();
        request.setAttribute("usName", usName);
        request.setAttribute("usType", ustype);
        request.setAttribute("usDescp", usDecp);
        request.setAttribute("reload", "true");

        return "usconfig";
    }

    /**
     * 方法说明
     * 封装定时数据格式
     * @Date in Mar 13, 2012,7:02:49 PM
     * @return
     */
    public TaskInfo geTaskInfo(UserSourceInfo userSourceInfo) {
        TaskInfo taskInfo = new TaskInfo();
        int taskmode1 = userSourceInfo.getTaskInfo().getTaskmode1();//定时时间设置方式
        int taskmode2 = userSourceInfo.getTaskInfo().getTaskmode2();//是否按每n小时/分钟的形式
        int sourceid = userSourceInfo.getId();
        int enabled = userSourceInfo.getTaskInfo().getEnabled();//定时状态
        String taskname = userSourceInfo.getSourcename();
        String taskId = userSourceInfo.getTaskInfo().getTaskid();

        if (taskmode1 == NumConstant.common_number_1) {//按天设置
            if (taskmode2 == NumConstant.common_number_1) {//按每天的n、n+1时执行
                String taskhour = userSourceInfo.getTaskInfo().getTaskhour();
                taskInfo.setTaskhour(taskhour);

            } else if (taskmode2 == NumConstant.common_number_2) {//按每n小时执行一次
                taskInfo.setTaskhour("*/" + userSourceInfo.getTaskInfo().getSelAccHour());

            }
            taskInfo.setTaskminute("0");
            taskInfo.setTaskmonth("*");
            taskInfo.setTaskweek("*");
            taskInfo.setTaskday("*");

        } else if (taskmode1 == NumConstant.common_number_2) {//按周执行
            String taskweek = userSourceInfo.getTaskInfo().getTaskweek();
            taskInfo.setTaskhour("0");
            taskInfo.setTaskminute("0");
            taskInfo.setTaskmonth("*");
            taskInfo.setTaskweek(taskweek);
            taskInfo.setTaskday("*");
        }

        taskInfo.setSourceid(sourceid);
        taskInfo.setTaskname(taskname);
        taskInfo.setTaskmode1(taskmode1);
        taskInfo.setTaskmode2(taskmode2);
        taskInfo.setEnabled(enabled);
        taskInfo.setTaskid(taskId);

        return taskInfo;
    }
}
