package com.ft.otp.manager.orgunit.domain.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.admin.user.action.aide.AdminUserActionAide;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.orgunit.domain.action.aide.DomainInfoActionAide;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.form.DomainInfoQueryForm;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.action.aide.OrgunitInfoActionAide;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 域业务操作Action
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class DomainInfoAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -4463683229032999486L;
    private Logger logger = Logger.getLogger(DomainInfoAction.class);

    /**服务接口**/
    private IDomainInfoServ domainInfoServ = null;//域服务接口
    private IAdminAndOrgunitServ adminAndOrgunitServ = null;//管理员-组织机构关系
    private IOrgunitInfoServ orgunitInfoServ = null;//组织机构服务接口

    /**实体**/
    private DomainInfoQueryForm queryForm = null;
    private DomainInfo domainInfo = null;

    /**帮助类**/
    private DomainInfoActionAide domainInfoAide = new DomainInfoActionAide();//域业务操作辅助类
    private AdminUserActionAide adminUserActionAide = new AdminUserActionAide();//管理员业务操作辅助类
    private OrgunitInfoActionAide oiaide = new OrgunitInfoActionAide();//组织机构操作辅助类

    /**
     * 行数统计分页处理
     * @throws BaseException
     * @return
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = 0;//总记录数
        try {
            String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
            String curLoginUserRole = (String) super.getCurLoginUserRole();//角色
            List<?> list = domainInfoAide.getDomainList(curLoginUserId, curLoginUserRole, queryForm, null);
            if (StrTool.objNotNull(list)) {
                totalRow = list.size();
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 处理数据查询
     * @param pageArg
     */
    private List<?> query(PageArgument pageArg) {
        List<?> domainInfos = null;
        try {
            String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
            String curLoginUserRole = (String) super.getCurLoginUserRole();//角色
            domainInfos = domainInfoAide.getDomainList(curLoginUserId, curLoginUserRole, queryForm, pageArg);
            if (!StrTool.objNotNull(domainInfos)) {//是 null
                domainInfos = new ArrayList<Object>();
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        return domainInfos;
    }

    /**
     *  添加 域
     */
    public String add() {
        try {
            if (StrTool.objNotNull(domainInfo)) {
                int createTime = DateTool.dateToInt(new Date());
                domainInfo.setCreateTime(createTime); //当前UTC秒
                domainInfoServ.addObj(domainInfo);

                //关系
                DomainInfo di = (DomainInfo) domainInfoServ.find(domainInfo);
                if (StrTool.objNotNull(di)) {
                    //组装域和管理员的对应关系
                    List<AdminAndOrgunit> adminAndOrgunitList = oiaide.initAdminAndOrgRelation(di.getDomainId(), null,
                            domainInfo.getAdminIds());
                    if (StrTool.listNotNull(adminAndOrgunitList)) {
                        adminAndOrgunitServ.addObjs(adminAndOrgunitList);
                    }
                }

                //结果
                String successInfo = Language.getLangStr(request, "domain_info_add_succ") + "," + di.getDomainId()
                        + ",1,add";
                outPutOperResult(Constant.alert_succ, successInfo); //这里将域id返回 为了实现 左右窗口信息联动; 1 表示域

                DomainConfig.reload();
            } else {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "domain_info_isnull"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "domain_info_add_fail"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 删除域 
     */
    public String delete() {
        try {
            //获得要删除的域id串
            String alertType = "";
            String outInfo = "";

            Set<?> keySet = super.getDelIds("delDomainIds");
            if (StrTool.setNotNull(keySet)) {
                Object[] results = domainInfoAide.isExsitORGUT(orgunitInfoServ, keySet);
                boolean isExsitTag = ((Boolean) results[NumConstant.common_number_0]).booleanValue();
                if (!isExsitTag) {//如果不存在下级组织机构 删除域
                    domainInfoServ.delObj(keySet);
                    alertType = Constant.alert_succ;
                    outInfo = Language.getLangStr(request, "domain_info_delete_succ");

                    DomainConfig.reload();
                } else {
                    alertType = Constant.alert_error;
                    String alertStr = (String) results[NumConstant.common_number_1];
                    outInfo = alertStr + Language.getLangStr(request, "domain_info_delete_error");
                }
            } else {
                alertType = Constant.alert_error;
                outInfo = Language.getLangStr(request, "domain_info_delete_isnull");
            }
            outPutOperResult(alertType, outInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, "");
        }

        return null;
    }

    /** 
     * 查询域信息
     * 
     */
    public String find() {
        DomainInfo di = null;
        try {
            String curLoginUserId = (String) super.getCurLoginUser();//管理员id号
            String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //角色
            int domainId = (StrTool.objNotNull(domainInfo) ? domainInfo.getDomainId() : NumConstant.common_number_0);
            //域信息 包括管理员
            di = domainInfoAide.getDomainInfo(domainId, curLoginUserId, curLoginUserRoleMark);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setDomainInfo(di);
        return SUCC_FIND;
    }

    /** 
     * 域列表初始化
     */
    public String init() {
        if (isNeedClearForm()) {
            queryForm = null;
        }
        try {
            PageArgument pageArg = pageArgument();
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> resultList = query(pageArg);
            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(json);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 修改域信息及关联逻辑
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(domainInfo)) {
                domainInfo.setOldDomainName(request.getParameter("oldDname"));//记录日志

                domainInfoServ.updateObj(domainInfo);//更新域信息
                this.setDomainInfo(domainInfo);

                //仅重置该域和管理员及该管理员创建的子管理员的关系   
                String curLoginUserId = (String) super.getCurLoginUser();
                String curLoginUserRoleMark = (String) super.getCurLoginUserRole();

                //1.将原来的存在的域管理员关系删除 （该域该管理员本身、子管理员）
                if (!StrTool.strEquals(StrConstant.SUPER_ADMIN, curLoginUserRoleMark)) { //如果不是超级管理员 
                    AdminUser queryUser = new AdminUser();
                    queryUser.setLoginUser(curLoginUserId);
                    List<?> adminUserList = adminUserActionAide.getCurrLoginUserChildUsers(queryUser, null);//获得该管理员及子管理员
                    if (StrTool.listNotNull(adminUserList)) {
                        AdminUser[] adminUserArray = adminUserList.toArray(new AdminUser[adminUserList.size()]);
                        for (int i = 0; i < adminUserArray.length; i++) {
                            AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(adminUserArray[i].getAdminid(), null,
                                    domainInfo.getDomainId());
                            adminAndOrgunitServ.delObj(adminAndOrgunit);
                        }
                    }
                } else {//如果是超级管理员 
                    AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit("", null, domainInfo.getDomainId());
                    adminAndOrgunitServ.delObj(adminAndOrgunit);
                }

                //2.生成新的管理员域关系
                if (StrTool.listNotNull(domainInfo.getAdminIds())) {
                    String[] adminIdArray = domainInfo.getAdminIds().toArray(
                            new String[domainInfo.getAdminIds().size()]);
                    for (int i = 0; i < adminIdArray.length; i++) {
                        AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(adminIdArray[i], null, domainInfo
                                .getDomainId());
                        adminAndOrgunitServ.addObj(adminAndOrgunit);
                    }
                }

                DomainConfig.reload();
                String successInfo = Language.getLangStr(request, "domain_info_edit_succ") + ","
                        + domainInfo.getDomainId() + ",1,update";
                outPutOperResult(Constant.alert_succ, successInfo);//这里将域id返回 为了实现 左右窗口信息联动; 1 表示域
            } else {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "domain_info_isnull"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "domain_info_edit_fail"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#page()
     */
    public String page() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#view()
     */
    public String view() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 添加域之前检查域是否已经存在
     * @return String
     */
    public String findDomainInfoExist() {
        try {
            String source = super.getSource(request);
            boolean isTxist = domainInfoAide.isDomainInfoExist(domainInfo, source, domainInfoServ);
            if (isTxist) { //如果找到了 那么不能用  
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @return the domainInfo
     */
    public DomainInfo getDomainInfo() {
        return domainInfo;
    }

    /**
     * @param domainInfo the domainInfo to set
     */
    public void setDomainInfo(DomainInfo domainInfo) {
        this.domainInfo = domainInfo;
    }

    /**
     * @return the adminAndOrgunitServ
     */
    public IAdminAndOrgunitServ getAdminAndOrgunitServ() {
        return adminAndOrgunitServ;
    }

    /**
     * @param adminAndOrgunitServ the adminAndOrgunitServ to set
     */
    public void setAdminAndOrgunitServ(IAdminAndOrgunitServ adminAndOrgunitServ) {
        this.adminAndOrgunitServ = adminAndOrgunitServ;
    }

    /**
     * @return the orgunitInfoServ
     */
    public IOrgunitInfoServ getOrgunitInfoServ() {
        return orgunitInfoServ;
    }

    /**
     * @param orgunitInfoServ the orgunitInfoServ to set
     */
    public void setOrgunitInfoServ(IOrgunitInfoServ orgunitInfoServ) {
        this.orgunitInfoServ = orgunitInfoServ;
    }

    /**
     * @return the domainInfoServ
     */
    public IDomainInfoServ getDomainInfoServ() {
        return domainInfoServ;
    }

    /**
     * @param domainInfoServ the domainInfoServ to set
     */
    public void setDomainInfoServ(IDomainInfoServ domainInfoServ) {
        this.domainInfoServ = domainInfoServ;
    }

    /**
     * @return the queryForm
     */
    public DomainInfoQueryForm getQueryForm() {
        return queryForm;
    }

    /**
     * @param queryForm the queryForm to set
     */
    public void setQueryForm(DomainInfoQueryForm queryForm) {
        this.queryForm = queryForm;
    }

}
