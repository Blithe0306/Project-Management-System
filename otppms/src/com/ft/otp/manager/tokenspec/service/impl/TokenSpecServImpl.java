/**
 *Administrator
 */
package com.ft.otp.manager.tokenspec.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.BaseService;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.tokenspec.dao.ITokenSpecDao;
import com.ft.otp.manager.tokenspec.entity.TokenSpec;
import com.ft.otp.manager.tokenspec.service.ITokenSpecServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌规格service 业务处理类
 *
 * @Date in Mar 27, 2013,4:59:27 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class TokenSpecServImpl extends BaseService implements ITokenSpecServ {

    private ITokenSpecDao tokenSpecDao = null;

    /**
     * @return the tokenSpecDao
     */
    public ITokenSpecDao getTokenSpecDao() {
        return tokenSpecDao;
    }

    /**
     * @param tokenSpecDao the tokenSpecDao to set
     */
    public void setTokenSpecDao(ITokenSpecDao tokenSpecDao) {
        this.tokenSpecDao = tokenSpecDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        tokenSpecDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return tokenSpecDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return tokenSpecDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.tokenspec.service.ITokenSpecServ#queryAllSpec(java.lang.Object)
     */
    public Map<String, Object> queryAllSpec(Object object) throws BaseException {
        List<?> specList = tokenSpecDao.query(object, new PageArgument());
        Map<String, Object> speclMap = new HashMap<String, Object>();
        if (StrTool.listNotNull(specList)) {
            TokenSpec[] arrTokenSpecs = specList.toArray(new TokenSpec[specList.size()]);
            for (TokenSpec tokenSpec : arrTokenSpecs) {
                speclMap.put(tokenSpec.getSpecid(), tokenSpec);
            }
        }

        return speclMap;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.tokenspec.service.ITokenSpecServ#importTokenSpec(java.util.List)
     */
    public int importTokenSpec(List<Object> specList) throws BaseException {
        return tokenSpecDao.importTokenSpec(specList);
    }

}
