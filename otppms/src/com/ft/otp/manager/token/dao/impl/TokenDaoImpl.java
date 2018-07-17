/**
 *Administrator
 */
package com.ft.otp.manager.token.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.TokenNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.token.action.aide.TokenActionAide;
import com.ft.otp.manager.token.dao.ITokenDao;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 令牌DAO类功能说明
 *
 * @Date in Apr 18, 2011,11:19:56 AM
 *
 * @author ZJY
 */
public class TokenDaoImpl extends BaseSqlMapDAO implements ITokenDao {

    //获取sqlmap中的命名空间
    protected String getNameSpace() {
        return TokenNSpace.TOKEN_INFO_NS;
    }

    public TokenInfo geTokenInfo(Object object) {
        TokenInfo tokenInfo = (TokenInfo) object;
        if (null == tokenInfo) {
            tokenInfo = new TokenInfo();
        }
        return tokenInfo;
    }

    public void addObj(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        insert(TokenNSpace.TOKEN_INFO_INSERT_TK, tokenInfo);
    }

    public int count(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        return (Integer) queryForObject(TokenNSpace.TOKEN_INFO_COUNT_TK, tokenInfo);
    }

    public void delObj(Object object) throws BaseException {
        TokenInfo tokenInfo = (TokenInfo) object;
        delete(TokenNSpace.TOKEN_INFO_DEL_TK, tokenInfo);
    }

    public void delObj(final Set<?> keys) throws BaseException {
        //批量删除令牌
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                TokenInfo tokenInfo = null;
                int batch = 0;
                executor.startBatch();
                Iterator<?> iter = (Iterator<?>) keys.iterator();
                while (iter.hasNext()) {
                    String token = (String) iter.next();
                    tokenInfo = new TokenInfo();
                    tokenInfo.setToken(token);
                    delete(TokenNSpace.TOKEN_INFO_DEL_TK, tokenInfo);
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

    public Object find(Object object) throws BaseException {
        TokenInfo tokenInfo = (TokenInfo) object;
        return queryForObject(TokenNSpace.TOKEN_INFO_FIND_TK, tokenInfo);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        tokenInfo.setPageSize(pageArg.getPageSize());
        tokenInfo.setStartRow(pageArg.getStartRow());

        return queryForList(TokenNSpace.TOKEN_INFO_SELECT_TK, tokenInfo);
    }

    public void updateObj(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        update(TokenNSpace.TOKEN_INFO_UPDATE_TK, tokenInfo);
    }

    public int tokenImport(final List<Object> obList) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                TokenInfo tokenInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < obList.size(); i++) {
                    tokenInfo = (TokenInfo) obList.get(i);
                    insert(TokenNSpace.TOKEN_INFO_INSERT_TK, tokenInfo);
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

    public List<?> countTknByType(Object object, String mark) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        if (StrTool.strEquals(mark, StrConstant.COUNT_BY_PHYSICALTYPE)) {
            return queryForList(TokenNSpace.TOKEN_INFO_COUNT_PHYSICALTYPE, tokenInfo);
        } else {
            return queryForList(TokenNSpace.TOKEN_INFO_COUNT_PRODUCTTYPE, tokenInfo);
        }
    }

    public void updateSoftTkn(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        update(TokenNSpace.TOKEN_INFO_SOFT_DIST, tokenInfo);
    }

    /**
     * 批量操作令牌的组织机构
     */
    public int updateTokenOrg(final List<?> tokens) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                TokenInfo tokenInfo = null;
                int batch = 0;
                executor.startBatch();
                for (int i = 0; i < tokens.size(); i++) {
                    tokenInfo = (TokenInfo) tokens.get(i);
                    update(TokenNSpace.TOKEN_INFO_UPDATE_ORG, tokenInfo);
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

    /**
     * 特殊条件计算
     * 要求 orgunitId 不参与匹配
     * BC byContion
     * */
    public int countBC(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        return (Integer) queryForObject(TokenNSpace.TOKEN_INFO_COUNT_TKBC, tokenInfo);
    }

    /**
     * 特殊条件计算
     * BC byContion
     * */
    public int countCT(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        return (Integer) queryForObject(TokenNSpace.TOKEN_INFO_COUNT_CT, tokenInfo);
    }

    /**
     * 查找一组数据
     * 支持模糊查询
     * 
     * @Date in Apr 2, 2011,4:51:56 PM
     * @return
     * @throws BaseException
     */
    public List<?> queryBC(Object object, PageArgument pageArg) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        tokenInfo.setPageSize(pageArg.getPageSize());
        tokenInfo.setStartRow(pageArg.getStartRow());
        String dbtype = DbEnv.getDbtype();
        if (dbtype.equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(TokenNSpace.TOKEN_INFO_SELECT_TKBC, tokenInfo, pageArg.getStartRow(), pageArg
                    .getPageSize());
        } else {
            return queryForList(TokenNSpace.TOKEN_INFO_SELECT_TKBC, tokenInfo);

        }
    }

    /**
     * 查找一组数据
     * 支持模糊查询
     * 
     * @return
     * @throws BaseException
     */
    public List<?> queryCT(Object object, PageArgument pageArg) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        tokenInfo.setPageSize(pageArg.getPageSize());
        tokenInfo.setStartRow(pageArg.getStartRow());
        String dbtype = DbEnv.getDbtype();
        if (dbtype.equals(DbConstant.DB_TYPE_SYBASE)) {
            return queryForList(TokenNSpace.TOKEN_INFO_SELECT_CT, tokenInfo, pageArg.getStartRow(), pageArg
                    .getPageSize());
        } else {
            return queryForList(TokenNSpace.TOKEN_INFO_SELECT_CT, tokenInfo);

        }
    }

    /**
     * 令牌分配
     * 迁移令牌的组织机构
     */
    public int updateTokenOrg(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        return update(TokenNSpace.TOKEN_INFO_UPDATE_ORG, tokenInfo);
    }

    /**
     * 根据令牌号查询令牌的信息（含PUBKEY）
     * 
     */
    public Object findObj(Object object) throws BaseException {
        TokenInfo tokenInfo = (TokenInfo) object;
        return queryForObject(TokenNSpace.TOKEN_INFO_FIND_NTK, tokenInfo);
    }

    /**
     * 令牌更换后，更新令牌状态
     */
    public int updateTokenState(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        return update(TokenNSpace.TOKEN_INFO_UPDATE_STATE, tokenInfo);
    }

    /**
     * 批量进行令牌的状态修改(启用/停用、锁定/解锁、挂失/解挂、注销等)
     */
    public int updateTokenState(final List<?> tokens, final int operType) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                TokenInfo tokenInfo = null;
                int batch = 0;
                executor.startBatch();
                Iterator<?> iter = tokens.iterator();
                while (iter.hasNext()) {
                    tokenInfo = (TokenInfo) iter.next();
                    tokenInfo = TokenActionAide.getTokenInfo(tokenInfo, operType);
                    if (operType == NumConstant.common_number_8) {
                        delete(TokenNSpace.TOKEN_INFO_DEL_TK, tokenInfo);
                    } else if (operType == NumConstant.common_number_9) {
                        update(TokenNSpace.TOKEN_INFO_UPDATE_ORG, tokenInfo);
                    } else {
                        update(TokenNSpace.TOKEN_INFO_UPDATE_STATE, tokenInfo);
                    }
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

    /**
     * 根据条件查询令牌号
     */
    public List<?> findToken(Object object) throws BaseException {
        TokenInfo tokenInfo = geTokenInfo(object);
        return queryForList(TokenNSpace.TOKEN_INFO_FIND_TOKEN, tokenInfo);
    }
    
    public boolean tokenLocked(final Set<?> keys, final int locked, final int loginlocktime) throws BaseException {

        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {

                Map<String, Object> map = null;
                Iterator<?> iter = (Iterator<?>) keys.iterator();
                int batch = 0;
                boolean bool = true;
                executor.startBatch();
                while (iter.hasNext()) {
                    String token = (String) iter.next();
                    map = new HashMap<String, Object>();
                    map.put("token", token);
                    map.put("locked", locked);
                    map.put("loginlocktime", loginlocktime);
                    int k = update(TokenNSpace.TOKEN_INFO_LOCKED_TK, map);
                    if (k == -1) {
                        bool = false;
                    }
                    batch++;
                    if (batch == NumConstant.batchCount) {
                        executor.executeBatch();
                        batch = 0;
                    }
                }
                executor.executeBatch();
                return bool;
            }
        });
        return true;
    }

}
