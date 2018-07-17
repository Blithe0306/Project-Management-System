/**
 *Administrator
 */
package com.ft.otp.manager.tokenspec.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 令牌规格DAO功能接口
 *
 * @Date in Mar 27, 2013,4:00:39 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface ITokenSpecDao extends IBaseDao {

    /**
     * 导入规格
     * 方法说明
     * @Date in Mar 27, 2013,5:19:11 PM
     * @param specList
     * @return
     * @throws BaseException
     */
    int importTokenSpec(List<Object> specList) throws BaseException;
}
