/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.ConfigNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.config.dao.IConfigInfoDao;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 配置管理DAO实现
 *
 * @Date in Nov 15, 2012,5:22:34 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ConfigInfoDaoImpl extends BaseSqlMapDAO implements IConfigInfoDao {

    protected String getNameSpace() {
        return ConfigNSpace.CONF_INFO_NS;
    }

    private ConfigInfo getConfInfo(Object object) {
        ConfigInfo confInfo = (ConfigInfo) object;
        if (confInfo == null) {
            confInfo = new ConfigInfo();
        }
        return confInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
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
        ConfigInfo confInfo = getConfInfo(object);
        return queryForObject(ConfigNSpace.FIND_CONF_BY_CONFTYPE, confInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        ConfigInfo confInfo = getConfInfo(object);
        update(ConfigNSpace.UPDATE_CENTER_CONF_INFO, confInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.config.dao.IConfigInfoDao#queryConfInfo(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryConfInfo(Object object, PageArgument pageArg) throws BaseException {
        ConfigInfo confInfo = getConfInfo(object);
        return queryForList(ConfigNSpace.QUERY_CONF_BY_CONFTYPE, confInfo, pageArg.getStartRow(), pageArg.getPageSize());

    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.config.dao.IConfigInfoDao#updateConf(java.util.List)
     */
    public void updateConf(final List<Object> list) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                ConfigInfo conf = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < list.size(); i++) {
                    conf = (ConfigInfo) list.get(i);
                    update(ConfigNSpace.UPDATE_CENTER_CONF_INFO, conf);
                    batch++;
                    if (batch == NumConstant.batchCount) {
                        executor.executeBatch();
                        batch = 0;
                    }
                }
                executor.executeBatch();
                return null;
            }
        });
    }

}
