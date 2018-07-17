/**
 *qiuju
 */
package com.ft.otp.manager.resords.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.ResordsNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.resords.dao.IResordsDao;
import com.ft.otp.manager.resords.entity.Resords;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 类、接口等说明
 *
 * @Date in 2015-5-4,下午5:25:57
 *
 * @version v1.0
 *
 * @author qiuju
 */
public class ResordsDaoImpl extends BaseSqlMapDAO implements IResordsDao {

    @Override
    protected String getNameSpace() {
        return ResordsNSpace.RESORDS_INFO_NS;
    }
    private Resords getResords (Object object) {
        Resords resords = (Resords) object;
        if (resords == null) {
           	resords = new Resords();
        }
        return resords;
    }
    public void addObj(Object object) throws BaseException {
        Resords resords =(Resords)object ;
        insert(ResordsNSpace.RESORDS_INFO_ADD_UI, resords);
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(final Set<?> set) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
            public Object doInSqlMapClient(SqlMapExecutor executor)throws SQLException {
                Resords resords = null;
                int batch = 0;
                executor.startBatch();
                Iterator<?> iter = set.iterator();
                while (iter.hasNext()) {
                    String id = (String)iter.next();
                    resords = new Resords();
                    resords.setId(Integer.parseInt(id));

                    // 删除定制信息
                    delete(ResordsNSpace.RESORDS_INFO_DEL_UI, resords);
                    batch++;

                    // 达到500时，提交
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

    public void updateObj(Object object) throws Exception {
        Resords resords = (Resords)object;
        update(ResordsNSpace.RESORDS_INFO_UPDA_UI, resords);
    }

    public Object find(Object object) throws BaseException {
        Resords resords = (Resords)object;
        return queryForObject(ResordsNSpace.RESORDS_INFO_FIND_UI, resords);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        Resords resords =  getResords(object);
        resords.setPageSize(pageArg.getPageSize());
        resords.setStartRow(pageArg.getStartRow());
        return queryForList(ResordsNSpace.RESORDS_INFO_SEL_UI, resords);
    }

    public int count(Object object) throws BaseException {
        Resords resords =  getResords(object);
        return (Integer) queryForObject(ResordsNSpace.RESORDS_INFO_COUNT_UI, resords);
    }
    /* (non-Javadoc)
     * @see com.ft.otp.manager.resords.dao.IResordsDao#findExceptself(java.lang.Object)
     */
//    public Object findExceptself(Object object) throws BaseException {
//        return null;
//    }

}
