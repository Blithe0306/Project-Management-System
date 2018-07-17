/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.backend.dao;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;

/**
 * 后端认证DAO接口
 *
 * @Date in Jan 21, 2013,8:17:21 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IBackendDao extends IBaseDao {

    /**
     * 验证host, port, domainid联合唯一是否存在
     * @Date in Jan 23, 2013,4:14:29 PM
     * @param object
     * @return
     * @throws BaseException
     */
    public Object queryUKIsExist(Object object) throws BaseException;
    
    /**
     * 启用、禁用后端认证
     */
    void updateEnabled(BackendInfo backendInfo, int enabled) throws BaseException;
}
