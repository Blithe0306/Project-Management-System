/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 25, 2014,9:02:23 AM
 */
package com.ft.otp.manager.prjinfo.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.PrjinfoNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.prjinfo.dao.IPrjinfoDao;
import com.ft.otp.manager.prjinfo.entity.Prjinfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 类、接口等说明
 *
 * @Date Dec 25, 2014,9:02:23 AM
 * @version v1.0
 * @author ZWX
 */
public class PrjinfoDaoImpl extends BaseSqlMapDAO implements IPrjinfoDao { 
    
    protected String getNameSpace() {
        return PrjinfoNSpace.PRJINFO_NS;
    }

    //获取定制信息实例对象
    private Prjinfo getPrjinfo(Object object) {
        Prjinfo prjinfo = (Prjinfo) object;
        if (null == prjinfo) {
            prjinfo = new Prjinfo();
        }
        return prjinfo;
    }

    public void addObj(Object object) throws BaseException {
        Prjinfo prjinfo = (Prjinfo)object;
        insert(PrjinfoNSpace.PRJINFO_ADD, prjinfo);
    }

    public int count(Object object) throws BaseException {
        Prjinfo prjinfo = (Prjinfo)object;
        return (Integer)queryForObject(PrjinfoNSpace.PRJINFO_COUNT, prjinfo);
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(final Set<?> set) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
            public Object doInSqlMapClient(SqlMapExecutor executor)throws SQLException {
                Prjinfo prjinfo = null;
                int batch = 0;
                executor.startBatch();
                Iterator<?> iter = set.iterator();
                while (iter.hasNext()) {
                    String id = (String)iter.next();
                    prjinfo = new Prjinfo();
                    prjinfo.setId(id);

                    // 删除定制信息
                    delete(PrjinfoNSpace.PRJINFO_DEL, prjinfo);
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

    public Object find(Object object) throws BaseException {
        Prjinfo prjinfo = (Prjinfo)object;
        return queryForObject(PrjinfoNSpace.PRJINFO_FIND, prjinfo);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        Prjinfo prjinfo = getPrjinfo(object);
        prjinfo.setStartRow(pageArg.getStartRow());
        prjinfo.setPageSize(pageArg.getPageSize());
        return queryForList(PrjinfoNSpace.PRJINFO_FIND, prjinfo);
    }

    public void updateObj(Object object) throws Exception {
        Prjinfo prjinfo = (Prjinfo)object;
        update(PrjinfoNSpace.PRJINFO_UPDA, prjinfo);
    }

}
