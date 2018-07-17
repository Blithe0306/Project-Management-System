/**
 *Administrator
 */
package com.ft.otp.manager.tokenspec.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.TokenSpecNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.tokenspec.dao.ITokenSpecDao;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 令牌规格DAO实现类
 *
 * @Date in Mar 27, 2013,4:15:11 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class TokenSpecDaoImpl extends BaseSqlMapDAO implements ITokenSpecDao {

    // 获取sqlmap中的命名空间
    protected String getNameSpace() {
        return TokenSpecNSpace.TOKENSPEC_INFO_NS;
    }

    public TokenSpec getTokenSpecInfo(Object object) {
        TokenSpec tokenSpecInfo = (TokenSpec) object;
        if (null == tokenSpecInfo) {
            tokenSpecInfo = new TokenSpec();
        }
        return tokenSpecInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        TokenSpec tokenSpecInfo = (TokenSpec) object;
        insert(TokenSpecNSpace.TOKENSPEC_INFO_INSERT, tokenSpecInfo);
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
        TokenSpec tokenSpecInfo = (TokenSpec) object;
        return queryForObject(TokenSpecNSpace.TOKENSPEC_INFO_FIND_TP, tokenSpecInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        TokenSpec tokenSpecInfo = getTokenSpecInfo(object);
        return queryForList(TokenSpecNSpace.TOKENSPEC_INFO_SELECT_TKSPEC, tokenSpecInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.tokenspec.dao.ITokenSpecDao#importTokenSpec(java.util.List)
     */
    public int importTokenSpec(final List<Object> specList) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                TokenSpec tokenSpec = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < specList.size(); i++) {
                    tokenSpec = (TokenSpec) specList.get(i);
                    insert(TokenSpecNSpace.TOKENSPEC_INFO_INSERT, tokenSpec);
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
