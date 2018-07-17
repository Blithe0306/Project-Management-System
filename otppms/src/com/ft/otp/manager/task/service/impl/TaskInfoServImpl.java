/**
 *Administrator
 */
package com.ft.otp.manager.task.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.task.dao.ITaskInfoDao;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.manager.task.service.ITaskInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户来源定时时间信息业务实现类
 *
 * @Date in Mar 9, 2012,9:43:25 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class TaskInfoServImpl implements ITaskInfoServ {

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    private ITaskInfoDao taskInfoDao;

    /**
     * @return the taskInfoDao
     */
    public ITaskInfoDao getTaskInfoDao() {
        return taskInfoDao;
    }

    /**
     * @param taskInfoDao the taskInfoDao to set
     */
    public void setTaskInfoDao(ITaskInfoDao taskInfoDao) {
        this.taskInfoDao = taskInfoDao;
    }

    public void addObj(Object object) throws BaseException {
        taskInfoDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
        TaskInfo timingInfo = new TaskInfo();
        int batch[] = new int[keys.size()];
        int i = 0;
        for (Iterator<?> it = keys.iterator(); it.hasNext();) {
            int id = StrTool.parseInt(String.valueOf(it.next()));
            batch[i] = id;
            i++;
        }
        timingInfo.setBatchIdsInt(batch);
        taskInfoDao.delObj(timingInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return taskInfoDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return taskInfoDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        taskInfoDao.updateObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.config.center.usersource.timing.service.ItaskInfoServ#updateList(java.util.List)
     */
    public void updateList(List<Object> list) throws BaseException {
        // timingDao.updateList(list);
    }

   

}
