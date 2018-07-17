/**
 *qiuju
 */
package com.ft.otp.manager.resords.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.resords.dao.IResordsDao;
import com.ft.otp.manager.resords.service.IResordsServ;

/**
 * 类、接口等说明
 *
 * @Date in 2015-5-4,下午5:31:41
 *
 * @version v1.0
 *
 * @author qiuju
 */
public class ResordsServImpl extends BaseService implements IResordsServ {

    private IResordsDao resordsDao;
    
    public IResordsDao getResordsDao() {
        return resordsDao;
    }
    public void  setResordsDao(IResordsDao resordsDao) {
        this.resordsDao=resordsDao;
        
    }
    public void addObj(Object object) throws BaseException {
        resordsDao.addObj(object);
    }


    public void delObj(Object object) throws BaseException {
        resordsDao.delObj(object);
    }


    public void delObj(Set<?> keys) throws BaseException {
        resordsDao.delObj(keys);
    }

    public void updateObj(Object object) throws Exception {
        resordsDao.updateObj(object);
    }

    public Object find(Object object) throws BaseException {
        return resordsDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return resordsDao.query(object, pageArg);
    }

    public int count(Object object) throws BaseException {
        return resordsDao.count(object);
    }
    /* (non-Javadoc)
     * @see com.ft.otp.manager.resords.service.IResordsServ#findExceptself(java.lang.Object)
     */
//    public Object findExceptself(Object object) throws BaseException {
//        return resordsDao.findExceptself(object);
//    }

}
