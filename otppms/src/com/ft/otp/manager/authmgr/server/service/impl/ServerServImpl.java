/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.authmgr.server.dao.IServerDao;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;

/**
 * 认证服务器业务逻辑接口提供类-service
 *
 * @Date in Apr 11, 2011,2:03:42 PM
 *
 * @author ZJY
 */
public class ServerServImpl implements IServerServ {

    private IServerDao serverDao;

    /**
     * @return the serverDao
     */
    public IServerDao getServerDao() {
        return serverDao;
    }

    /**
     * @param serverDao the serverDao to set
     */
    public void setServerDao(IServerDao serverDao) {
        this.serverDao = serverDao;
    }

    public void addObj(Object object) throws BaseException {
        //        ServerInfo serverInfo = (ServerInfo) object;
        //        String licId = serverInfo.getLicense().getLicId();
        //        serverInfo.setLicid(licId);
        //        String hostIp = serverInfo.getHostIPAddr();
        //        String chksumStr = hostIp + licId;
        //        chksumStr = chksumStr.trim();
        //        byte[] md5_b = null;
        //        String chksum = null;
        //        try {
        //            md5_b = HMAC_MD5.encodeHMACByte(chksumStr, StrConstant.KEY);
        //            chksum = HMAC_MD5.encode(md5_b);
        //        } catch (Exception ex) {
        //            throw new BaseException(ex);
        //        }
        //        serverInfo.setChksum(chksum);
        //        serverDao.addObj(serverInfo);
        serverDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return serverDao.count(object);
    }

    public void delObj(Object object) throws BaseException {

    }

    public void delObj(Set<?> keys) throws BaseException {
        ServerInfo serverInfo = new ServerInfo();
        String[] tempStr = new String[keys.size()];
        int i = 0;
        for (Iterator<?> it = keys.iterator(); it.hasNext();) {
            String id = (String) it.next();
            tempStr[i] = id;
            i++;
        }
        serverInfo.setBatchIds(tempStr);
        serverDao.delObj(serverInfo);
    }

    public Object find(Object object) throws BaseException {
        return serverDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return serverDao.query(object, pageArg);
    }

    public void updateObj(Object object) throws Exception {
        serverDao.updateObj(object);
    }

    public void updateList(List<?> list) throws BaseException {
        serverDao.updateList(list);
    }

    public void updateHostIp(Object object) throws Exception {
        serverDao.updateHostIp(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.authmgr.server.service.IServerServ#updateNewLicId(java.lang.Object)
     */
    public void updateNewLicId(Object object) throws Exception {
        serverDao.updateNewLicId(object);
    }

}
