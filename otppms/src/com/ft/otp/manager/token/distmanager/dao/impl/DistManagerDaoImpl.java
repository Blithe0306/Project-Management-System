/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.TokenNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.token.distmanager.dao.IDistManagerDao;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
 
import com.ibatis.sqlmap.client.SqlMapExecutor;
 

/**
 * 令牌分发Dao类功能说明
 *
 * @Date in Apr 18, 2011,11:19:56 AM
 *
 * @author ZJY
 */
public class DistManagerDaoImpl extends BaseSqlMapDAO implements
        IDistManagerDao {

    //获取sqlmap中的命名空间
    protected String getNameSpace() {
        return TokenNSpace.TOKEN_DIST_NS;
    }

    public DistManagerInfo getDistManagerInfo(Object object) {
        DistManagerInfo distManagerInfo = (DistManagerInfo) object;
        if (null == distManagerInfo) {
            distManagerInfo = new DistManagerInfo();
        }
        return distManagerInfo;
    }

    public void addObj(Object object) throws BaseException {
    }

    public int count(Object object) throws BaseException {
        DistManagerInfo distManagerInfo = getDistManagerInfo(object);
        return (Integer) queryForObject(TokenNSpace.TOKEN_DIST_COUNT_TD,
                distManagerInfo);
    }

    public void delObj(Object object) throws BaseException {

    }

    public void delObj(final Set<?> keys) throws BaseException {

    }

    public Object find(Object object) throws BaseException {
        DistManagerInfo distManagerInfo = (DistManagerInfo) object;
        return queryForObject(TokenNSpace.TOKEN_DIST_FIND_TD, distManagerInfo);
    }

    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        DistManagerInfo distManagerInfo = getDistManagerInfo(object);
        distManagerInfo.setPageSize(pageArg.getPageSize());
        distManagerInfo.setStartRow(pageArg.getStartRow());
        return queryForList(TokenNSpace.TOKEN_DIST_SELECT_TD, distManagerInfo);
    }

    public void updateObj(Object object) throws BaseException {
        DistManagerInfo distManagerInfo = (DistManagerInfo) object;
        update(TokenNSpace.TOKEN_DIST_UPDATE_TD, distManagerInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.token.distmanager.dao.IDistManagerDao#importExtToken(java.util.List)
     */
    public int importExtToken(final List<Object> objList) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {
                DistManagerInfo extTokenInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < objList.size(); i++) {
                    extTokenInfo = (DistManagerInfo) objList.get(i);
                    insert(TokenNSpace.TOKEN_INFO_INSERT_EXTTK, extTokenInfo);
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
 
        
         return 0;
    }

}
