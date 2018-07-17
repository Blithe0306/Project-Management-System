/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.portal.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.NoticeNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.portal.dao.IProNoticeDao;
import com.ft.otp.manager.confinfo.portal.entity.ProNoticeInfo;

/**
 * 用户门户系统通知DAO实现
 *
 * @Date in Apr 27, 2013,1:54:48 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class ProNoticeDaoImpl extends BaseSqlMapDAO implements IProNoticeDao {

    protected String getNameSpace() {
        return NoticeNSpace.NOTICE_INFO;
    }
    
    private ProNoticeInfo getNoticeInfo(Object object) {
        ProNoticeInfo nInfo = (ProNoticeInfo) object;
        if (nInfo == null) {
            nInfo = new ProNoticeInfo();
        }
        return nInfo;
    }
   
    public void addObj(Object object) throws BaseException {
        ProNoticeInfo noticeInfo = getNoticeInfo(object);
        insert(NoticeNSpace.NOTICE_INFO_ADD, noticeInfo);
    }

    public int count(Object object) throws BaseException {
        ProNoticeInfo noticeInfo = getNoticeInfo(object);
        return (Integer) queryForObject(NoticeNSpace.NOTICE_INFO_COUNT, noticeInfo);
    }

    public void delObj(Object object) throws BaseException {
        ProNoticeInfo noticeInfo = getNoticeInfo(object);
        delete(NoticeNSpace.NOTICE_INFO_DELETE, noticeInfo);
    }
    
    public void delObj(Set<?> set) throws BaseException {
    }

    public Object find(Object object) throws BaseException {
        ProNoticeInfo noticeInfo = getNoticeInfo(object);
        return queryForObject(NoticeNSpace.NOTICE_INFO_FIND, noticeInfo);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        ProNoticeInfo noticeInfo = getNoticeInfo(object);
        noticeInfo.setPageSize(pageArg.getPageSize());
        noticeInfo.setStartRow(pageArg.getStartRow());
        return queryForList(NoticeNSpace.NOTICE_INFO_SELECT, noticeInfo);
    }

    public void updateObj(Object object) throws Exception {
        ProNoticeInfo noticeInfo = getNoticeInfo(object);
        update(NoticeNSpace.NOTICE_INFO_UPDATE, noticeInfo);
    }

}
