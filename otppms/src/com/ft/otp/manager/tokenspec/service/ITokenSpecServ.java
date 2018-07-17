/**
 *Administrator
 */
package com.ft.otp.manager.tokenspec.service;

import java.util.List;
import java.util.Map;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 令牌规格Service功能接口
 *
 * @Date in Mar 27, 2013,4:11:15 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface ITokenSpecServ extends IBaseService {
    /**
     * 查询所有的规格 否会map集合 key：specid ,value :object
     * 方法说明
     * @Date in Mar 27, 2013,5:11:22 PM
     * @param object
     * @return
     * @throws BaseException
     */
    public Map<String, Object> queryAllSpec(Object object) throws BaseException;

    /**
     * 导入规格
     * 方法说明
     * @Date in Mar 27, 2013,5:23:57 PM
     * @param specList
     * @return
     * @throws BaseException
     */
    int importTokenSpec(List<Object> specList) throws BaseException;
}
