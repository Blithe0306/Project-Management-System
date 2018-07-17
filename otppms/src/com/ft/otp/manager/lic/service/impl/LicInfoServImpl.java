/**
 *Administrator
 */
package com.ft.otp.manager.lic.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.lic.dao.ILicInfoDao;
import com.ft.otp.manager.lic.service.ILicInfoServ;

/**
 * 授权业务接口实现
 *
 * @Date in Feb 21, 2013,2:11:39 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class LicInfoServImpl implements ILicInfoServ {

    private ILicInfoDao licInfoDao;

    public ILicInfoDao getLicInfoDao() {
        return licInfoDao;
    }

    public void setLicInfoDao(ILicInfoDao licInfoDao) {
        this.licInfoDao = licInfoDao;
    }

    public void addObj(Object object) throws BaseException {
        licInfoDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return 0;
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(Set<?> keys) throws BaseException {
    }

    public Object find(Object object) throws BaseException {
        return licInfoDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return null;
    }

    public void updateObj(Object object) throws Exception {
        licInfoDao.updateObj(object);
    }

}
