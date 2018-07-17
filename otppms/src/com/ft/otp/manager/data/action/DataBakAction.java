package com.ft.otp.manager.data.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.config.DBUpGradeConfig;
import com.ft.otp.common.config.LanguageConfig;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.task.RegScheduler;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.listener.InitializationLoad;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.data.entity.DBBakConfInfo;
import com.ft.otp.manager.data.entity.TableInfo;
import com.ft.otp.manager.data.service.DataBakServ;
import com.ft.otp.manager.data.service.FtpUpload;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.tool.CreateFileUtil;
import com.ft.otp.util.tool.StrTool;

/**
 * 数据备份、恢复 action
 * 
 * @Date in January 27,2013, 14:00:00 PM
 * @author byl
 * 
 */
public class DataBakAction extends BaseAction {
    private Logger logger = Logger.getLogger(DataBakAction.class);
    private static final long serialVersionUID = 8170022397734703544L;

    public IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    // 定制任务帮助类
    private ITaskInfoServ taskInfoServ = (ITaskInfoServ) AppContextMgr.getObject("taskInfoServ");
    // 角色接口
    private IRoleInfoServ roleInfoServ = (IRoleInfoServ) AppContextMgr.getObject("roleInfoServ");

    private DBBakConfInfo dbBakConfInfo; //参数对象
    private DBBakConfInfo oldDbBakConfInfo;//旧的参数对象

    private String filePath; // 文件名
    private String goBackTag; // 是否显示返回按钮 定义 1显示返回按钮 0不显示

    private LogCommonObj commonObj = new LogCommonObj();

    public LogCommonObj getCommonObj() {
        return commonObj;
    }

    public void setCommonObj(LogCommonObj commonObj) {
        this.commonObj = commonObj;
    }

    /**
     * 备份参数展示
     * 
     * @return
     */
    public String find() {
        String goPage = "";
        try {
            String source = super.getSource(request); // 来源

            // 获取备份设置信息
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_BAK);
            List<?> confList = confInfoServ.queryConfInfo(config, new PageArgument());
            DBBakConfInfo oldParam = DBBakConfInfo.getCenterInfoByList(confList);

            // 获取定时器相关信息
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setSourcetype(3);
            taskInfo.setSourceid(0);
            taskInfo = (TaskInfo) taskInfoServ.find(taskInfo);
            oldParam.setTaskInfo(taskInfo);

            oldParam = reformDir(oldParam, 1); // 剪切目录

            // 判断备份地址是否为"0",如果是默认为空
            if (StrTool.strEquals("0", oldParam.getDir())) {
                oldParam.setDir("");
            }

            this.setDbBakConfInfo(oldParam);

            if (!StrTool.objNotNull(source)) { // 首次判断
                boolean isExsit = DataBakServ.isExsitConfigParam(oldParam);

                if (isExsit) { // 存在配置 跳转到备份页 dataData.jsp
                    goPage = SUCC_FIND;
                } else if (!isExsit) { // 不存在配置 跳转到修改页 不显示返回按钮 bakParam.jsp
                    goPage = SUCC_EDIT;
                    this.setGoBackTag("0");
                }
            } else { // 视图与修改的跳转
                if (StrTool.strEquals(SUCC_FIND, source)) { // 跳转到备份页 dataData.jsp
                    goPage = SUCC_FIND;
                } else if (StrTool.strEquals(SUCC_EDIT, source)) { // 跳转到修改页 显示返回按钮
                    // bakParam.jsp
                    goPage = SUCC_EDIT;
                    this.setGoBackTag("1");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return goPage;

    }

    /**
     * 备份参数写入数据库中 设置完后 如果是自动定时备份 需将任务放入定时器中
     */
    public String bakConfig() {
        try {
            if (!StrTool.objNotNull(dbBakConfInfo)) {
                return null;
            }

            dbBakConfInfo = reformDir(dbBakConfInfo, 0); // 拼接目录

            if (dbBakConfInfo.getIsremote() == 0) {// 如果是本地
                // 判断是否存在目录
                boolean isExsit = CreateFileUtil.isFileExists(dbBakConfInfo.getDir());
                if (!isExsit) {
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_1"));
                    return null;
                }
            }

            // 获取定时任务信息
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setSourcetype(3);
            taskInfo = (TaskInfo) taskInfoServ.find(taskInfo);

            if (dbBakConfInfo.getIstimeauto() == 1) {//自动
                // 判断是否有task 如有修改taskinfo和 定时器
                if (StrTool.objNotNull(taskInfo)) {// 修改定时器信息
                    taskInfo = dbBakConfInfo.getTaskInfo();// 赋值定时器时间

                    DataBakTask dataBakTask = new DataBakTask(dbBakConfInfo);
                    if (taskInfo.getEnabled() == 0) {// 未启用
                        taskInfo.setEnabled(1);// 修改为启用
                    } else {// 修改任务  由于修改后可能路径等信息发生变化且为了不让每次执行定时都去查数据库，所以需要删除后重新添加
                        //RegScheduler.updateTask(TaskConfig.getScheduler(), taskInfo.getTaskid(), taskInfo);
                        RegScheduler.delTask(TaskConfig.getScheduler(), taskInfo.getTaskid());
                    }

                    // 添加任务
                    String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), dataBakTask, taskInfo);
                    taskInfo.setTaskid(taskId);
                    taskInfoServ.updateObj(taskInfo);

                } else {// 添加任务
                    taskInfo = dbBakConfInfo.getTaskInfo();// 赋值定时器时间
                    DataBakTask dataBakTask = new DataBakTask(dbBakConfInfo);

                    String taskId = RegScheduler.addTask(TaskConfig.getScheduler(), dataBakTask, taskInfo);

                    taskInfo.setTaskid(taskId);
                    taskInfo.setEnabled(1);
                    taskInfoServ.addObj(taskInfo);
                }

            } else {//手动
                //删除定时任务，taskinfo 设置为未启用
                if (StrTool.objNotNull(taskInfo) && taskInfo.getEnabled() == 1) {
                    RegScheduler.delTask(TaskConfig.getScheduler(), taskInfo.getTaskid());
                    taskInfo.setEnabled(0);
                    taskInfoServ.updateObj(taskInfo);
                }
            }

            List<Object> confList = dbBakConfInfo.getListByCenterInfo(dbBakConfInfo);
            confInfoServ.batchUpdateConf(confList);

            // 数据备份记录日志
            this.setOldDbBakConfInfo(dbBakConfInfo);

            // 数据备份记录日志
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "databak_vd_set_succ"));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_set_error"));
        }

        return null;
    }

    /**
     * 远程登录测试 return String
     * 
     */
    public String loginTest() {
        try {
            if (!StrTool.objNotNull(dbBakConfInfo)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_2"));
                return null;
            }

            // 得到备份参照信息
            FtpUpload ftpUpload = FtpUpload.getInstance();
            int islogin = ftpUpload.connectServer(dbBakConfInfo);
            if (islogin == 1) {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "databak_connect_succ"));
                ftpUpload.closeConnect();
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_2"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_2"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 数据备份
     * 
     * @return String
     */
    public String bak() {
        String plaint = Language.getLangStr(request, "plaint"); // 叹号
        try {
            if (!StrTool.objNotNull(dbBakConfInfo)) {
                commonObj.addAdminLog(AdmLogConstant.log_aid_bak, AdmLogConstant.log_obj_data, Language.getLangStr(
                        request, "databak_vd_err_7"), null, 1);
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_7"));
                return null;
            }

            dbBakConfInfo = reformDir(dbBakConfInfo, 0); // 拼接目录

            if (dbBakConfInfo.getIsremote() == 0) {// 如果是本地
                // 判断是否存在目录
                // 路径是否存在
                boolean isExsit = CreateFileUtil.isFileExists(dbBakConfInfo.getDir());
                if (!isExsit) {
                    commonObj.addAdminLog(AdmLogConstant.log_aid_bak, AdmLogConstant.log_obj_data, Language.getLangStr(
                            request, "databak_vd_err_1"), null, 1);
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_1") + plaint);
                    return null;
                }
            }

            // 得到备份参照信息
            Map<String, TableInfo> tableInfos = DataBakServ.getTablesAndCols(dbBakConfInfo.getIsbaklog());
            int bakResult = DataBakServ.createBakFile(tableInfos, dbBakConfInfo, null);

            // 数据备份记录日志
            dbBakConfInfo.setLogFlag(bakResult);
            this.setOldDbBakConfInfo(dbBakConfInfo);
            // 数据备份记录日志

            switch (bakResult) {
                case 4:
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "databak_data_succ"));
                    break;
                case 3:
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_3"));
                    break;
                case 2:
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_4"));
                    break;
                case 1:
                    outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_5"));
                    break;
                case 0:
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "databak_vd_err_6"));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            commonObj.addAdminLog(AdmLogConstant.log_aid_bak, AdmLogConstant.log_obj_data, Language.getLangStr(request,
                    "databak_vd_err_6"), null, 1);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_6"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 数据恢复
     * 
     * @return String
     */
    public String revert() {
        try {
            if (!StrTool.strNotNull(filePath)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_8"));
                return null;
            }

            // 服务器上路径是否存在 因为恢复只能恢复服务器上了路径的文件
            boolean isExsit = CreateFileUtil.isFileExists(filePath);
            if (!isExsit) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_8"));
                return null;
            }

            /*
             * File fp =DataBakServ.getFile(filePath);
             * if(!StrTool.objNotNull(fp)){ //不存在
             * outPutOperResult(Constant.alert_error, "恢复文件不存在!"); return
             * null; }
             */

            // 恢复操作
            boolean reverResult = DataBakServ.revertSql(filePath);
            if (reverResult) {
            	
            	// 查询一下数据库中是否有超级管理员
            	if(findSuperAdmin()){
            		request.getSession().getServletContext().setAttribute(Constant.DATABASE_IF_SUPERMAN, true);
            	}
            	
            	// 重新加载系统配置
            	InitializationLoad.destroyed();//销毁系统配置项
            	LanguageConfig.loadLanguage();//加载多语言资源文件
            	InitializationLoad.configLoad();//加载系统配置项
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "databak_recover_succ"));
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        outPutOperResult(Constant.alert_error, Language.getLangStr(request, "databak_vd_err_9"));

        return null;
    }
    
    /**
     * 检查是否存在超级管理员
     * @author LXH
     * @date Nov 18, 2014 2:07:44 PM
     * @return
     */
    public boolean findSuperAdmin(){
    	boolean flag = false;
    	RoleInfo roleInfo = new RoleInfo();
    	roleInfo.setRolemark("ADMIN");  // 超级管理员标识
    	try {
			roleInfo = (RoleInfo) roleInfoServ.find(roleInfo);
			if(StrTool.objNotNull(roleInfo)){
				flag = true;
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return flag;
    }

    /**
     * 因为系统处理是绝对路径 所以拼接路径 给用户展示的相对路径 所以剪切路径
     * 
     * 远程备份 拼接备份文件的临时存放目录
     * 
     * 远程备份 剪切备份文件的临时存放目录
     * 
     * @param bakParam
     * @param flag
     *            0拼接 1剪切
     * @return BakParam
     */
    public DBBakConfInfo reformDir(DBBakConfInfo bakParam, int flag) {
        String basePath = getBasePath(); // 相对项目webapp路径
        if (flag == 0) {// 拼接
            if (bakParam.getIsremote() == 1) {// 备份到远程
                bakParam.setTempdir(basePath + Constant.WEB_TEMP_DATA_BAK);
            } else {
                bakParam.setTempdir("");
            }
        } else if (flag == 1) {// 剪切
            if (bakParam.getIsremote() == 1) {// 远程
                bakParam.setTempdir(Constant.WEB_TEMP_DATA_BAK);
            } else {
                bakParam.setTempdir("");
            }
        }
        return bakParam;
    }

    /**
     * 得到当前应用的webapps 目录
     * 
     * @return String
     */
    public String getBasePath() {
        String basePath = getSession().getServletContext().getRealPath(""); // 相对项目webapp路径
        return basePath;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the goBackTag
     */
    public String getGoBackTag() {
        return goBackTag;
    }

    /**
     * @param goBackTag
     *            the goBackTag to set
     */
    public void setGoBackTag(String goBackTag) {
        this.goBackTag = goBackTag;
    }

    /**
     * @return the dbBakConfInfo
     */
    public DBBakConfInfo getDbBakConfInfo() {
        return dbBakConfInfo;
    }

    /**
     * @param dbBakConfInfo the dbBakConfInfo to set
     */
    public void setDbBakConfInfo(DBBakConfInfo dbBakConfInfo) {
        this.dbBakConfInfo = dbBakConfInfo;
    }

    /**
     * @return the oldDbBakConfInfo
     */
    public DBBakConfInfo getOldDbBakConfInfo() {
        return oldDbBakConfInfo;
    }

    /**
     * @param oldDbBakConfInfo the oldDbBakConfInfo to set
     */
    public void setOldDbBakConfInfo(DBBakConfInfo oldDbBakConfInfo) {
        this.oldDbBakConfInfo = oldDbBakConfInfo;
    }

}
