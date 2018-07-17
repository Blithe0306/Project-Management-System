/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 配置管理接口
 *
 * @Date in Nov 15, 2012,5:23:45 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IConfigInfoServ extends IBaseService {

    /**
     * 获取配置属性集合
     * @Date in Nov 15, 2012,6:19:15 PM
     * @param object
     * @param pageArg
     * @return
     * @throws BaseException
     */
    public List<?> queryConfInfo(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 批量修改配置信息
     * @Date in Nov 19, 2012,9:44:47 AM
     * @param list
     * @throws BaseException
     */
    public void batchUpdateConf(List<Object> list) throws BaseException;

}
