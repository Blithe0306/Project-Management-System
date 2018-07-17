package com.ft.otp.core.ibatisext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.dao.client.Dao;
import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoTransaction;
import com.ibatis.dao.client.template.JdbcDaoTemplate;
import com.ibatis.dao.engine.transaction.ConnectionDaoTransaction;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * 加上iBatis的NameSpace,方便子类直观的编写
 * 继承Spring的SqlMapClientDaoSupport,并兼容iBatis的JDBC事物
 *
 * @Date in Apr 2, 2011,7:05:53 PM
 *
 * @author TBM
 */

public class BaseSqlMapDAO extends SqlMapClientDaoSupport implements SqlMapExecutor, Dao {

    private ExtJdbcDaoTemplate jdbcDaoTemplate;
    private DaoManager daoManager;

    private class ExtJdbcDaoTemplate extends JdbcDaoTemplate {
        public ExtJdbcDaoTemplate(DaoManager daoManager) {
            super(daoManager);
        }

        public Connection getConnection(Dao dao) {
            DaoTransaction trans = daoManager.getTransaction(dao);
            if (!(trans instanceof ConnectionDaoTransaction)) {
                throw new DaoException("The DAO manager of type " + daoManager.getClass().getName()
                        + " cannot supply a JDBC Connection for this template, and is therefore not"
                        + "supported by JdbcDaoTemplate.");
            }
            return ((ConnectionDaoTransaction) trans).getConnection();
        }
    }

    public BaseSqlMapDAO(DaoManager daoManager) {
        this.daoManager = daoManager;
        jdbcDaoTemplate = new ExtJdbcDaoTemplate(daoManager);
    }

    public BaseSqlMapDAO() {
    }

    /**
     * 返回命名空间名称,主要是提供给子类实现返回真正的命名空间
     * @return
     */
    protected String getNameSpace() {
        return "";
    }

    /**
     * 转换SqlMap的ID,默认自动加上nameSpace
     * @param oldId
     * @return
     */
    protected String convertSqlId(String oldId) {
        if (getNameSpace() == null || getNameSpace().length() == 0) {
            return oldId;
        } else {
            return getNameSpace() + "." + oldId;
        }
    }

    /**
     * 直接获取Connection, 需要从DAOFactory中获取Dao实例,此Dao實例只對此方法有效,
     * 獲取Connection后,在外面實現縣官數據庫操作,不需要关心事物。
     */
    protected Connection getConnection() {
        if (jdbcDaoTemplate == null) {
            //需从DAOFactory中获取Dao实例才能调用此方法
            throw new RuntimeException("Dao need to get an instance from the DAOFactory can call this method");
        } else {
            return jdbcDaoTemplate.getConnection(this);
        }
    }

    /**
     * 删除操作
     */
    public int delete(String id, Object parameterObject) {
        if (jdbcDaoTemplate != null) {
            return -1;
        } else {
            return getSqlMapClientTemplate().delete(convertSqlId(id), parameterObject);
        }
    }

    /**
     * 添加操作
     */
    public Object insert(String id, Object parameterObject) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().insert(convertSqlId(id), parameterObject);
        }
    }

    /**
     * 分页查询操作
     */
    public List<?> queryForList(String id, Object parameterObject, int skip, int max) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForList(convertSqlId(id), parameterObject, skip, max);
        }
    }

    /**
     * 查询操作 parameterObject表示参数对象
     */
    public List<?> queryForList(String id, Object parameterObject) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForList(convertSqlId(id), parameterObject);
        }

    }

    /**
     * 查询操作 返回MAP
     */
    public Map queryForMap(String id, Object parameterObject, String keyProp, String valueProp) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForMap(convertSqlId(id), parameterObject, keyProp, valueProp);
        }
    }

    /**
     * 查询操作 返回MAP
     */
    public Map queryForMap(String id, Object parameterObject, String keyProp) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForMap(convertSqlId(id), parameterObject, keyProp);
        }
    }

    /**
     * 查询操作 查询一个对象
     */
    public Object queryForObject(String id, Object parameterObject, Object resultObject) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForObject(convertSqlId(id), parameterObject, resultObject);
        }
    }

    /**
     * 查询操作 查询一个对象
     */
    public Object queryForObject(String id, Object parameterObject) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForObject(convertSqlId(id), parameterObject);
        }
    }

    /**
     * iBatis内置分页查询
     */
    @SuppressWarnings("deprecation")
    public PaginatedList queryForPaginatedList(String id, Object parameterObject, int pageSize) {
        if (jdbcDaoTemplate != null) {
            return null;
        } else {
            return getSqlMapClientTemplate().queryForPaginatedList(convertSqlId(id), parameterObject, pageSize);
        }
    }

    /**
     * 查询操作
     */
    public void queryWithRowHandler(String id, Object parameterObject, RowHandler rowHandler) {
        if (jdbcDaoTemplate != null) {
        } else {
            getSqlMapClientTemplate().queryWithRowHandler(convertSqlId(id), parameterObject, rowHandler);
        }
    }

    /**
     * 更新操作
     */
    public int update(String id, Object parameterObject) {
        if (jdbcDaoTemplate != null) {
            return -1;
        } else {
            return getSqlMapClientTemplate().update(convertSqlId(id), parameterObject);
        }
    }

    /**
     * 初始化批量操作
     */
    public void startBatch() {
        if (jdbcDaoTemplate != null) {
        } else {
            try {
                getSqlMapClient().startBatch();
            } catch (SQLException e) {
                throw new DaoException("Failed to startBatch.Cause: " + e, e);
            }
        }
    }

    /**
     * 执行批量操作
     */
    public int executeBatch() {
        if (jdbcDaoTemplate != null) {
            return -1;
        } else {
            try {
                return getSqlMapClient().executeBatch();
            } catch (SQLException e) {
                throw new DaoException("Failed to executeBatch.Cause: " + e, e);
            }
        }
    }

}