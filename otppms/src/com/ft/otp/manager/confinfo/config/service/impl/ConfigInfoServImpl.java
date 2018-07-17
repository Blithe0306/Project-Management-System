/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.config.dao.IConfigInfoDao;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 配置接口实现类
 * @Date in Nov 15, 2012,5:24:51 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ConfigInfoServImpl extends BaseService implements IConfigInfoServ {

    private IConfigInfoDao configDao;

    public IConfigInfoDao getConfigDao() {
        return configDao;
    }

    public void setConfigDao(IConfigInfoDao configDao) {
        this.configDao = configDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
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
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return configDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        configDao.updateObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.config.service.IConfigInfoServ#queryConfInfo(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryConfInfo(Object object, PageArgument pageArg) throws BaseException {
        return configDao.queryConfInfo(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.config.service.IConfigInfoServ#batchUpdateConf(java.util.List)
     */
    public void batchUpdateConf(List<Object> list) throws BaseException {
        if (!StrTool.listNotNull(list)) {
            return;
        }
        configDao.updateConf(list);
    }

}
