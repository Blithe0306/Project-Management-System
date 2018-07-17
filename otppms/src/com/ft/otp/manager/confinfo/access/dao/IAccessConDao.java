/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.access.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 访问控制策略DAO接口
 *
 * @Date in Dec 27, 2012,5:39:56 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IAccessConDao extends IBaseDao {

    /**
     * 查询允许访问IP段列表
     * @Date in Jan 4, 2013,10:20:29 AM
     * @param object
     * @return
     * @throws BaseException
     */
    public List<?> queryIPDList(Object object, PageArgument pageArg) throws BaseException;
}
