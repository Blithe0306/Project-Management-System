/**
 *Administrator
 */
package com.ft.otp.manager.task.dao.impl;

import java.util.List;
import java.util.Set;
 
import com.ft.otp.base.dao.namespace.TaskNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.task.dao.ITaskInfoDao;
import com.ft.otp.manager.task.entity.TaskInfo;

 
 
 
/**
 * 用户来源定时时间信息DAO类说明
 *
 * @Date in Mar 9, 2012,9:40:29 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class TaskInfoDaoImpl extends BaseSqlMapDAO implements ITaskInfoDao {

    protected String getNameSpace() {
        return TaskNSpace.TASK_INFO;
    }

    private TaskInfo geTaskInfo(Object obj) {
        TaskInfo taskInfo = (TaskInfo) obj;
        if (null == taskInfo) {
            taskInfo = new TaskInfo();
        }
        return taskInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        TaskInfo taskInfo = geTaskInfo(object);
        insert(TaskNSpace.INSERT_TASK_INFO, taskInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        TaskInfo taskInfo = geTaskInfo(object);
        delete(TaskNSpace.DEL_TASK_INFO, taskInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(Set<?> set) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return queryForObject(TaskNSpace.FIND_TASK_INFO, object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        TaskInfo taskInfo = geTaskInfo(object);
        return queryForList(TaskNSpace.QUERY_TASK_INFO, taskInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
          update(TaskNSpace.UPDATE_TASK_INFO, object);
    }

 

     

}
