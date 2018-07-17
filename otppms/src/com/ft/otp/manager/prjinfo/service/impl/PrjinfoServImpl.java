/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 25, 2014,10:20:25 AM
 */
package com.ft.otp.manager.prjinfo.service.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.prjinfo.dao.IPrjinfoDao;
import com.ft.otp.manager.prjinfo.service.IPrjinfoServ;

/**
 * 定制信息服务实现
 * @Date Dec 25, 2014,10:20:25 AM
 * @version v1.0
 * @author ZWX
 */
public class PrjinfoServImpl extends BaseService implements IPrjinfoServ {
    
    private IPrjinfoDao prjinfoDao;

    public IPrjinfoDao getPrjinfoDao() {
        return prjinfoDao;
    }

    public void setPrjinfoDao(IPrjinfoDao prjinfoDao) {
        this.prjinfoDao = prjinfoDao;
    }

    public void addObj(Object object) throws BaseException {
        prjinfoDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return prjinfoDao.count(object);
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(Set<?> keys) throws BaseException {
        prjinfoDao.delObj(keys);
    }

    public Object find(Object object) throws BaseException {
        return prjinfoDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return prjinfoDao.query(object, pageArg);
    }

    public void updateObj(Object object) throws Exception {
        prjinfoDao.updateObj(object);
    }

}
