/**
 *Administrator
 */
package com.ft.otp.manager.user_token.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.UserNSpace;
import com.ft.otp.base.dao.namespace.UserTokenNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user_token.dao.IUserTokenDao;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 用户令牌业务数据接口实现类
 *
 * @Date in Apr 21, 2011,6:43:07 PM
 *
 * @author TBM
 */
public class UserTokenDaoImpl extends BaseSqlMapDAO implements IUserTokenDao {

    protected String getNameSpace() {
        return UserTokenNSpace.USER_TOKEN_NS;
    }

    private UserToken getUserToken(Object object) {
        UserToken userToken = (UserToken) object;
        if (null == userToken) {
            userToken = new UserToken();
        }
        return userToken;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        UserToken userToken = (UserToken) object;
        insert(UserTokenNSpace.USER_TOKEN_ADD, userToken);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        UserToken userToken = (UserToken) object;
        return (Integer) queryForObject(UserTokenNSpace.USER_TOKEN_COUNT, userToken);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        UserToken userToken = (UserToken) object;

        //批量解除用户与令牌的对应关系
        delete(UserTokenNSpace.USER_TOKEN_DELS, userToken);
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
    	UserToken userToken = (UserToken) object;
        return queryForObject(UserTokenNSpace.USER_TOKEN_FIND, userToken);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        UserToken userToken = getUserToken(object);
        userToken.setStartRow(pageArg.getStartRow());
        userToken.setPageSize(pageArg.getPageSize());
        if (DbEnv.getDbtype().equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(UserTokenNSpace.USER_TOKEN_QUERY, userToken, userToken.getStartRow(), userToken
                    .getPageSize());
        } else {
            return queryForList(UserTokenNSpace.USER_TOKEN_QUERY, userToken);

        }

    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.dao.IUserTokenDao#addUsrTkn(java.util.List)
     */
    public void addUsrTkn(final List<?> utList) throws BaseException {
        //批量添加用户－令牌绑定关系
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                UserToken userToken = null;
                int batch = 0;

                executor.startBatch();
                for (int i = 0; i < utList.size(); i++) {
                    userToken = (UserToken) utList.get(i);
                    insert(UserTokenNSpace.USER_TOKEN_ADD, userToken);

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

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.dao.IUserTokenDao#query(com.ft.otp.base.form.BaseQueryForm)
     */
    public List<?> query(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_QUERY_IN, userToken);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.dao.IUserTokenDao#queryJoinTkn(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryJoinTkn(Object object, PageArgument pageArg) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_QUERY_JOIN_TKN, userToken, pageArg.getStartRow(), pageArg
                .getPageSize());
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.dao.IUserTokenDao#batchUnBindUT(java.util.List, java.util.List, java.util.List)
     */
    public void batchUnBindUT(final List<Object> utList) throws BaseException {
        //批量解除用户与令牌的对应关系
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                int batch = 0;
                executor.startBatch();

                //删除用户与令牌的对应关系
                Iterator<Object> utIter = utList.iterator();
                while (utIter.hasNext()) {
                    UserToken userToken = (UserToken) utIter.next();
                    delete(UserTokenNSpace.USER_TOKEN_DEL, userToken);

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

    /*
     * 
     * 根据用户账号查询多用户绑定的令牌
     */
    public List<?> queryMulUserToken(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_QUERY_MulUserToken, userToken);
    }
    
    /*
     * 
     * 根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
     */
    public List<?> queryTokenOth(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_QUERY_TOKENOTH, userToken);
    }
    
    /*
     * 
     * 根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
     */
    public List<?> queryToken(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_QUERY_TOKEN, userToken);
    }


    /*
     * 删除指定用户令牌关系
     * 小批量 几十 条记录
     */
    public void delObjs(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        delete(UserTokenNSpace.USER_TOKEN_DEL, userToken);
    }
    
    /*
     * 删除指定用户外绑定同一令牌的令牌关系
     */
    public void delObjUs(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        delete(UserTokenNSpace.USER_TOKEN_DEL_ONE, userToken);
    }

    /*
     * 查出用户与令牌所有的绑定数据 
     *  2013-5-3
     */
    public List<?> selObjs(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_SEL, userToken);
    }

    /*
     * 根据用户ID查出此用户所有的绑定令牌号 
     *  2013-5-13
     */
    public List<?> selTokens(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_SELTOKENS, userToken);
    }
    
    /*
     * 用户与令牌绑定关系，查询条件用户ID与域ID，用于解绑操作
     * 2013-8-6
     */
    public List<?> selUserTokens(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.USER_TOKEN_SEL_BIND, userToken);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.dao.IUserTokenDao#selectAdminTokens(java.lang.Object)
     */
    public List<?> selectAdminTokens(Object object) throws BaseException {
        UserToken userToken = getUserToken(object);
        return queryForList(UserTokenNSpace.ADMIN_TOKEN_SELADMBINDTOKENS, userToken);
    }
}
