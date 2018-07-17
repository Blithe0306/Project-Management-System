package com.ft.otp.manager.orgunit.domain.action.aide;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.form.DomainInfoQueryForm;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.action.aide.OrgunitInfoActionAide;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 域实现的辅助类功能说明
 *
 * @Date in January 22, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class DomainInfoActionAide {

    //域服务接口
    IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");

    /**
     * 域信息
     * @param queryForm
     * return
     */
    public DomainInfo getDomainInfo(DomainInfoQueryForm queryForm) {
        DomainInfo domainInfo = null;
        if (StrTool.objNotNull(queryForm)) {
            domainInfo = queryForm.getDomainInfo();
        } else {
            domainInfo = new DomainInfo();
        }
        return domainInfo;
    }

    /**
     * 根据管理员id和角获得管理员管理的域列表
     * @param adminId
     * @param adminRole
     * @param queryForm 其他条件
     * @paran pageArg 分页条件
     * @return
     * @throws BaseException 
     */
    public List<?> getDomainList(String adminId, String adminRole, DomainInfoQueryForm queryForm, PageArgument pageArg)
            throws BaseException {
        List<?> resultDomainList = null;
        pageArg = StrTool.objNotNull(pageArg) ? pageArg : (new PageArgument());
        if (!StrTool.strEquals(StrConstant.SUPER_ADMIN, adminRole)) { //如果不是超级管理员
            DomainInfo diQuery = getDomainInfo(queryForm);
            diQuery.setAdminId(adminId);
            diQuery.setIsFilterTag(NumConstant.common_number_1);//根据管理员获得域列表
            resultDomainList = domainInfoServ.query(diQuery, pageArg);

        } else { //如果是超级管理员
            resultDomainList = domainInfoServ.query(getDomainInfo(queryForm), pageArg);
        }

        return resultDomainList;
    }

    /**
     *  判断一批域下是否存在子机构或用户或令牌
     *   是否存在的标识 和 并返回相应的输出串 
     *  @param orgunitInfoServ 
     *  @param keySet
     *  @return Object[] 
     *          Object[1]存在结果 
     *          Object[2]输出串
     *  @throws BaseException 
     */
    public Object[] isExsitORGUT(IOrgunitInfoServ orgunitInfoServ, Set<?> keySet) throws BaseException {

        Object[] results = new Object[NumConstant.common_number_2];
        //机构源数据
        boolean isExsitTag = false;
        StringBuffer doNotDeleteDomainIds = new StringBuffer();

        List<?> list = orgunitInfoServ.queryWholeList(new OrgunitInfo());
        OrgunitInfo[] orgunitInfos = null;
        if (StrTool.listNotNull(list)) {
            orgunitInfos = list.toArray(new OrgunitInfo[list.size()]);
        }
        Iterator<?> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            int domainId = StrTool.parseInt((String) iterator.next());
            //子机构
            boolean isExsitChildOrg = false;
            if (StrTool.objNotNull(orgunitInfos)) {
                for (int j = 0; j < orgunitInfos.length; j++) {
                    if (orgunitInfos[j].getDomainId() == domainId) {//如果找到了 该域下的组织机构 
                        isExsitChildOrg = true;
                        break;
                    }
                }
            }
            //用户、令牌
            if (!isExsitChildOrg) {
                OrgunitInfoActionAide oiaide = new OrgunitInfoActionAide();
                boolean isExsitUser = oiaide.doIsExsitUsers(domainId, null);
                if (!isExsitUser) {
                    boolean isExsitToken = oiaide.doIsExsitTokens(domainId, null);
                    if (isExsitToken) { //存在令牌
                        isExsitTag = true;
                        if (doNotDeleteDomainIds.indexOf(domainId + ",") == -1) {
                            doNotDeleteDomainIds.append(domainId + ",");
                        }
                    }
                } else { //存在用户
                    isExsitTag = true;
                    if (doNotDeleteDomainIds.indexOf(domainId + ",") == -1) {
                        doNotDeleteDomainIds.append(domainId + ",");
                    }
                }
            } else { //存在子机构
                isExsitTag = true;
                if (doNotDeleteDomainIds.indexOf(domainId + ",") == -1) {
                    doNotDeleteDomainIds.append(domainId + ",");
                }
            }

        }
        list.clear();//清除

        //赋值结果
        results[0] = isExsitTag;
        results[1] = getDomainNames(doNotDeleteDomainIds);
        return results;

    }

    /**
     * 根据域id串找到域对象信息
     * @param doNotDeleteDomainIds
     * @return String
     * @throws BaseException
     */
    public String getDomainNames(StringBuffer doNotDeleteDomainIds) throws BaseException {
        StringBuffer alertStr = new StringBuffer();

        if (!StrTool.objNotNull(doNotDeleteDomainIds)) {
            alertStr.append("");
            return alertStr.toString();
        }

        if (doNotDeleteDomainIds.indexOf(",") != -1) {
            doNotDeleteDomainIds.delete(doNotDeleteDomainIds.length() - NumConstant.common_number_1,
                    doNotDeleteDomainIds.length());
            String[] domainIds = doNotDeleteDomainIds.toString().split(",");
            for (int i = 0; i < domainIds.length; i++) {
                alertStr.append(DomainConfig.getValue(StrTool.parseInt(domainIds[i])) + ", ");
            } //end for
        }
        if (StrTool.objNotNull(alertStr)) {
            if (alertStr.indexOf(", ") != -1) {
                alertStr.delete(alertStr.length() - NumConstant.common_number_2, alertStr.length());
            }
        }

        return alertStr.toString();
    }

    /**
     * 验证域标识或名称是否已存在
     * @param domainInfo 验证信息
     * @param source 针对属性
     * @param domainInfoServ
     * @return boolean
     */
    public boolean isDomainInfoExist(DomainInfo domainInfo, String source, IDomainInfoServ domainInfoServ)
            throws BaseException {
        boolean isTxist = false;
        DomainInfo diQuery = new DomainInfo();
        if (StrTool.objNotNull(source) && StrTool.strNotNull(source) && StrTool.strEquals("name", source)) {
            diQuery.setDomainName(domainInfo.getDomainName());
        } else {
            diQuery.setDomainSn(domainInfo.getDomainSn().toLowerCase()); // 转换为小写
        }
        DomainInfo doIn = (DomainInfo) domainInfoServ.find(diQuery);
        if (StrTool.objNotNull(doIn)) { //如果找到了 
            isTxist = true;
        }
        return isTxist;
    }

    /**
     * 默认域判断
     * @param domainId
     * @return int
     */
    public int isDefaultDomainInfo(int domainId) {
        int result = 0;//非默认域
        String defaultDomainIdStr = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.DEFAULT_DOMAIN_ID);
        int defaultDomainId = StrTool.parseInt(defaultDomainIdStr);
        result = (domainId == defaultDomainId ? NumConstant.common_number_1 : NumConstant.common_number_0);
        return result;
    }

    /**
     * 根据域id 得到域对象（包括管理员）
     * @param domainId
     *      如域ID<=0 则查询所有返回一个
     * @param adminId
     * @param adminRole
     * @return DomainInfo
     * 
     * @throws BaseException 
     */
    public DomainInfo getDomainInfo(int domainId, String adminId, String adminRole) throws BaseException {
        //得到当前域信息
        DomainInfo domainInfo = new DomainInfo();
        if (domainId <= 0) {
            List<?> domainList = domainInfoServ.query(domainInfo, new PageArgument());
            domainInfo = (DomainInfo) domainList.get(0);
            domainId = domainInfo.getDomainId();
        } else {
            domainInfo.setDomainId(domainId);
            domainInfo = (DomainInfo) domainInfoServ.find(domainInfo);
        }

        return domainInfo;
    }
}
