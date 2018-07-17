/**
 *Administrator
 */
package com.ft.otp.manager.lic.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.LicInfoNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.lic.dao.ILicInfoDao;
import com.ft.otp.manager.lic.entity.LicInfo;

/**
 * 授权DAO接口实现类
 *
 * @Date in Feb 21, 2013,2:08:32 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class LicInfoDaoImpl extends BaseSqlMapDAO implements ILicInfoDao {

    protected String getNameSpace() {
        return LicInfoNSpace.LICINFO_NS;
    }

    public LicInfo getLicInfo(Object object) {
        LicInfo licInfo = new LicInfo();
        if (null != object) {
            licInfo = (LicInfo) object;
        }

        return licInfo;
    }

    public void addObj(Object object) throws BaseException {
        LicInfo licInfo = getLicInfo(object);
        insert(LicInfoNSpace.INSERT_LICINFO, licInfo);
    }

    public int count(Object object) throws BaseException {
        return 0;
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(Set<?> set) throws BaseException {
    }

    public Object find(Object object) throws BaseException {
        LicInfo licInfo = getLicInfo(object);
        return queryForObject(LicInfoNSpace.FIND_LICINFO, licInfo);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return null;
    }

    public void updateObj(Object object) throws BaseException {
        LicInfo licInfo = getLicInfo(object);
        update(LicInfoNSpace.UPDATE_LICINFO, licInfo);
    }

}
