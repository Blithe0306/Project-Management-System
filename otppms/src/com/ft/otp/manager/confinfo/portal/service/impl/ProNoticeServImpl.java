/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.portal.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.portal.dao.IProNoticeDao;
import com.ft.otp.manager.confinfo.portal.service.IProNoticeServ;

/**
 * 用户门户通知serv接口实现
 *
 * @Date in Apr 27, 2013,1:52:57 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ProNoticeServImpl extends BaseService implements IProNoticeServ {

    private IProNoticeDao noticeDao;

    public void addObj(Object object) throws BaseException {
        noticeDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return noticeDao.count(object);
    }

    public void delObj(Object object) throws BaseException {
        noticeDao.delObj(object);
    }

    public void delObj(Set<?> keys) throws BaseException {
    }

    public Object find(Object object) throws BaseException {
        return noticeDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return noticeDao.query(object, pageArg);
    }

    public void updateObj(Object object) throws Exception {
        noticeDao.updateObj(object);
    }

    public IProNoticeDao getNoticeDao() {
        return noticeDao;
    }

    public void setNoticeDao(IProNoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }

}
