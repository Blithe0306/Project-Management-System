package com.ft.otp.manager.orgunit.orgunit.action;

import java.util.List;

import net.sf.json.JSONObject;

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
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.action.aide.OrgunitInfoActionAide;
import com.ft.otp.manager.orgunit.orgunit.action.aide.TreeOrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.action.aide.UInfoActionAide;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构业务操作Action
 *
 * @Date in January 17, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class OrgunitInfoAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -1019704519575620516L;
    private Logger logger = Logger.getLogger(OrgunitInfoAction.class);

    /**业务接口**/
    private IDomainInfoServ domainInfoServ = null;//域服务接口
    private IOrgunitInfoServ orgunitInfoServ = null;//组织机构服务接口
    private IAdminAndOrgunitServ adminAndOrgunitServ = null;//管理员——组织机构 关系服务接口
    private IUserInfoServ userInfoServ = null;//用户业务操作实现
    private ITokenServ tokenServ = null;//令牌业务操作实现

    /**帮助类**/
    private OrgunitInfoActionAide orgunitInfoAide = new OrgunitInfoActionAide();//组织机构业务操作辅助类
    private DomainInfoActionAide domainInfoAide = new DomainInfoActionAide();//域业务操作辅助类
    private AdminUserActionAide adminUserActionAide = new AdminUserActionAide();//管理员业务操作辅助类
    private UInfoActionAide userInfoAide = new UInfoActionAide();//用户业务操作辅助类

    /**实体**/
    private TreeOrgunitInfo treeOrgunitInfo = null;//组织机构和域 合体
    private OrgunitInfo oldOrgInfo;//记录日志实体
    private OrgunitInfo orgInfo;
    private DomainInfo oldDomainInfo;//记录日志实体
    private DomainInfo domainIn;
    
    /**
     *添加组织机构 
     *@return String
     */
    public String add() {
        try {
            if (!StrTool.objNotNull(treeOrgunitInfo)) {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "org_info_isnull"));
                return null;
            }

            //转换成机构添加机构
            OrgunitInfo orgunitInfo = orgunitInfoAide.exchangeOrgunitInfo(treeOrgunitInfo);
            int createTime = StrTool.timeSecond();
            orgunitInfo.setCreateTime(createTime);
            orgunitInfoServ.addObj(orgunitInfo);
            this.setOldOrgInfo(orgunitInfo); // 日志记录

            //添加组织机构和管理员的对应关系,找到组织机构id
            OrgunitInfo orgInfo = (OrgunitInfo) orgunitInfoServ.findOG(orgunitInfo);

            //组装组织机构和管理员的对应关系
            List<AdminAndOrgunit> adminAndOrgunitList = orgunitInfoAide.initAdminAndOrgRelation(orgInfo.getDomainId(),
                    orgInfo.getOrgunitId(), orgunitInfo.getAdmins());

            if (StrTool.listNotNull(adminAndOrgunitList)) {
                adminAndOrgunitServ.addObjs(adminAndOrgunitList);
            }

            String successInfo = Language.getLangStr(request, "common_add_succ_tip") + "," + orgInfo.getOrgunitId()
                    + ",2,add";
            outPutOperResult(Constant.alert_succ, successInfo); //这里将机构id返回 为了实现左右窗口信息联动，2 表示组织机构

        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 删除域或组织机构
     * 删除前给出提示
     * @return String
     */
    public String delete() {
        try {
            String crrentOrgId = request.getParameter("crrentOrgId");//获得要删除的组织机构id号
            String flag = request.getParameter("flag");
            String domainIdstr = request.getParameter("domainId");

            if (!StrTool.strNotNull(crrentOrgId) || StrTool.strEquals(crrentOrgId, StrConstant.common_number_0)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_info_select_tip"));
                return null;
            }

            int domainId = 0;
            int orgunitId = 0;
            if (StrTool.strEquals(flag, StrConstant.common_number_1)) { //如果是域
                domainId = StrTool.parseInt(crrentOrgId);
            } else if (StrTool.strNotNull(crrentOrgId) && StrTool.strEquals(flag, StrConstant.common_number_2)) {
                domainId = StrTool.strNotNull(domainIdstr) ? StrTool.parseInt(domainIdstr) : 0;
                orgunitId = StrTool.parseInt(crrentOrgId);
            }

            //检查该组织机构下是否有子组织机构
            boolean isExsitChild = orgunitInfoAide.orgunitIsExsitChildOrg(domainId, orgunitId);
            if (isExsitChild) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_info_delete_error"));
                return null;
            }

            //检查机构下是否存在用户
            boolean isExsitUsers = orgunitInfoAide.doIsExsitUsers(domainId, orgunitId);
            if (isExsitUsers) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_info_delete_error"));
                return null;
            }

            //检查机构下是否存在令牌
            boolean isExsitTokens = orgunitInfoAide.doIsExsitTokens(domainId, orgunitId);
            if (isExsitTokens) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_info_delete_error"));
                return null;
            }

            if (StrTool.strEquals(flag, StrConstant.common_number_1)) { //如果是域 删除域
                DomainInfo domainInfo = new DomainInfo();
                domainInfo.setDomainId(domainId);
                domainInfoServ.delObj(domainInfo);

                DomainConfig.reload();
            } else if (StrTool.strNotNull(crrentOrgId) && StrTool.strEquals(flag, StrConstant.common_number_2)) { //如果是组织机构 删除组织机构
                OrgunitInfo orgunitInfo = new OrgunitInfo();
                orgunitInfo.setOrgunitId(orgunitId);
                //查询为了记录日志
                orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(orgunitInfo);

                // 添加管理员，删除中间表数据
                AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(null, orgunitId, domainId);
                adminAndOrgunitServ.delObj(adminAndOrgunit);
                orgunitInfoServ.delObj(orgunitInfo);
                this.setOldOrgInfo(orgunitInfo); // 日志记录
            }

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
        }

        return null;
    }

    /**
     * 查询返回页面进行添加或编辑操作
     * @return String
     */
    public String find() {
        try {
            String mod = super.getSource(request);

            if (!StrTool.objNotNull(treeOrgunitInfo)) {
                return "error";
            }

            TreeOrgunitInfo treeOrgInfo = treeOrgunitInfo;
            String curLoginUserId = (String) super.getCurLoginUser();//管理员id号
            String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //角色

            if (treeOrgunitInfo.getFlag() == 1) { //如果是域
                int domainId = treeOrgunitInfo.getId();

                //查询获取域信息
                DomainInfo domainInfo = domainInfoAide.getDomainInfo(domainId, curLoginUserId, curLoginUserRoleMark);
                //赋值管理该域的管理员
                List<?> domainAdminsIds = orgunitInfoAide.getDomainorOrgAdmins(domainId, null, curLoginUserId,
                        curLoginUserRoleMark);
                domainInfo.setAdminIds(domainAdminsIds);

                //转换为中间对象用作展示
                treeOrgInfo = orgunitInfoAide.exchangeTreeOrgunitInfo(domainInfo, treeOrgInfo);

                if (StrTool.strEquals("add", mod)) { //给域添加子机构 所以显示当前的域就是父机构
                    //设置要显示的父机构
                    TreeOrgunitInfo parentOrgunitInfo = new TreeOrgunitInfo();
                    parentOrgunitInfo.setId(NumConstant.common_number_0); //域作为机构时 id是0
                    parentOrgunitInfo.setName(domainInfo.getDomainName());
                    treeOrgInfo.setParentOrgunitInfo(parentOrgunitInfo);
                    treeOrgInfo.setId(NumConstant.common_number_0); //特殊设置 为了判断是添加
                }

            } else if (treeOrgunitInfo.getFlag() == 2) { //组织机构
                int orgunitId = treeOrgunitInfo.getId();

                //得到机构信息
                OrgunitInfo oi = orgunitInfoAide.getOrgunitInfo(orgunitId, curLoginUserId, curLoginUserRoleMark);
                if (StrTool.strEquals("add", mod)) { //如果因为是添加子机构 所以当前的机构就是父机构
                    //父机构
                    TreeOrgunitInfo parentOrgunitInfo = new TreeOrgunitInfo();
                    parentOrgunitInfo.setId(oi.getOrgunitId());
                    parentOrgunitInfo.setName(oi.getOrgunitName());
                    oi.setParentOrgunitInfo(parentOrgunitInfo);
                    oi.setOrgunitId(NumConstant.common_number_0); //为了判断是添加
                }
                //转换成 TreeOrgunitInfo
                treeOrgInfo = orgunitInfoAide.exchangeTreeOrgunitInfo(oi, treeOrgInfo);

            }

            this.setTreeOrgunitInfo(treeOrgInfo);

            //跳转       
            if (StrTool.strNotNull(mod) && StrTool.strEquals("move", mod)) {//迁移机构
                return "move";
            } else {
                return SUCC_FIND;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "error";
        }
    }

    /**
     * 组织机构树数据初始化主方法
     * 该函数 完成如下逻辑
     * 1、查询域列表 域是组织机构的根机构
     * 2、查询组织机构列表
     * 3、域和组织机构 及 组织机构和组织机构之间的上下级数据打包(数据层包) 
     * 4、将数据层包转化成json串
     * 特说明，赞不考虑管理员能管理的域和组织机构；即便某个管理员没有权限但依然可以让其可见
     * @return String
     */
    public String init() {
        try {
            //1、查询域全部列表
            List<?> domainInfos = domainInfoServ.query(new DomainInfo(), new PageArgument());

            List<?> adminAndOrgunits = null;//当前管理员管理域的对应关系信息
            if (!StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) { //如果不是超级管理员 得到当前管理员所管理的域
                AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(super.getCurLoginUser(), 0, 0);
                adminAndOrgunits = adminAndOrgunitServ.queryAdminAndOrgunitByAdminId(adminAndOrgunit);
            }

            //2、查询组织机构列表
            List<?> orgunitInfos = orgunitInfoServ.queryWholeList(new OrgunitInfo());

            //3、域和组织机构 及 组织机构和组织机构之间的上下级数据打包(数据层包)
            List<TreeOrgunitInfo> treeOrgunitInfos = orgunitInfoAide.reformOrgunitInfo(domainInfos, orgunitInfos);

            //4、过滤 空children机构
            JSONObject jsonObject = orgunitInfoAide.fliterChildren(treeOrgunitInfos);
            /*-----至此 完全的组织结构树已经形成 （任何机构 任何管理员 都<可读可写>）----*/

            /*
             * 管理员角色（除超级管理员外） 对组织机构树的 2<可读可写> 1<可读> 0<不可视> 判断； 并且去掉<不可视>项 打标识<可读可写>和<可读>项
             * 可读可写：管理员管理的组织机构
             * 可读：   管理员管理的组织机构 的上级机构和直接子机构
             * 不可视： 除 可读可写 可读 之外的组织机构
             */
            if (!StrConstant.SUPER_ADMIN.equals(super.getCurLoginUserRole())) { //如果不是超级管理员 过滤组织机构树 区分编辑权限
                jsonObject = orgunitInfoAide.makeMarkDrop(adminAndOrgunits, jsonObject);
            }

            //输出
            String json = jsonObject.toString();
            setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     *修改
     *@return String
     */
    public String modify() {
        try {
            if (!StrTool.objNotNull(treeOrgunitInfo)) {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "org_info_isnull"));
                return null;
            }

            int crrentId = treeOrgunitInfo.getId();
            int domainId = 0;
            Integer orgunitId = null;
            List<?> newAdmins = null;
            int resultFlag = 0;
            int readWriteFlag = treeOrgunitInfo.getReadWriteFlag();
            String curLoginUserId = (String) super.getCurLoginUser();//管理员id号
            String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //角色
            /**修改域或机构信息**/
            if (treeOrgunitInfo.getFlag() == NumConstant.common_number_1) { //如果是域
                DomainInfo domainInfo = orgunitInfoAide.exchangeDomainInfo(treeOrgunitInfo);
                
                //Start,记录日志
                domainInfo.setOldDomainName(request.getParameter("oldname"));
                this.setOldDomainInfo(domainInfo);
                List<?> domainAdminsIds = orgunitInfoAide.getDomainorOrgAdmins(domainId, null, curLoginUserId,
                        curLoginUserRoleMark);
                DomainInfo domain = new DomainInfo();
                domain.setDomainId(crrentId);
                domain = (DomainInfo) domainInfoServ.find(domain);
                domain.setAdminIds(domainAdminsIds);
                this.setDomainIn(domain);
                //End,记录日志
                
                domainInfoServ.updateObj(domainInfo);
                domainId = domainInfo.getDomainId();
                newAdmins = domainInfo.getAdminIds();
                resultFlag = NumConstant.common_number_1;
                DomainConfig.reload();
            } else {
                OrgunitInfo orgunitInfo = orgunitInfoAide.exchangeOrgunitInfo(treeOrgunitInfo);
                
                // Start,记录日志
                List<String> admins = orgunitInfoAide.getDomainorOrgAdmins(0, orgunitInfo.getOrgunitId(), curLoginUserId, curLoginUserRoleMark);
                OrgunitInfo orgunit = new OrgunitInfo();
                orgunit.setOrgunitId(orgunitInfo.getOrgunitId());
                orgunit = (OrgunitInfo) orgunitInfoServ.find(orgunit);
                orgunit.setAdmins(admins);
                this.setOrgInfo(orgunit);
                // End,记录日志
                
                orgunitInfoServ.updateObj(orgunitInfo);
                domainId = orgunitInfo.getDomainId();
                orgunitId = orgunitInfo.getOrgunitId();
                newAdmins = orgunitInfo.getAdmins();
                resultFlag = NumConstant.common_number_2;

                orgunitInfo.setOldOrgName(request.getParameter("oldname"));
                this.setOldOrgInfo(orgunitInfo);
            }

            /**修改域机构和管理员关系，仅重置该域或机构和管理员及该管理员创建的子管理员的关系**/
            //1.将原来的存在的域或机构管理员关系删除
            if (!StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) { //如果不是超级管理员       				
                AdminUser queryUser = new AdminUser();
                queryUser.setLoginUser(super.getCurLoginUser());
                List<?> adminUserList = adminUserActionAide.getCurrLoginUserChildUsers(queryUser, null);

                AdminUser[] adminUserArray = adminUserList.toArray(new AdminUser[adminUserList.size()]);
                for (int i = 0; i < adminUserArray.length; i++) {
                    AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(adminUserArray[i].getAdminid(), orgunitId,
                            domainId);
                    adminAndOrgunitServ.delObj(adminAndOrgunit);
                }
            } else {//如果是超级管理员      				
                AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit("", orgunitId, domainId);
                adminAndOrgunitServ.delObj(adminAndOrgunit);
            }

            //2.生成新的管理员域或机构关系
            if (StrTool.listNotNull(newAdmins)) {
                String[] adminIdArray = newAdmins.toArray(new String[newAdmins.size()]);
                for (int i = 0; i < adminIdArray.length; i++) {
                    AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(adminIdArray[i], orgunitId, domainId);
                    adminAndOrgunitServ.addObj(adminAndOrgunit);
                }
            }

            //3.返回结果
            String successInfo = Language.getLangStr(request, "common_edit_succ_tip") + "," + crrentId + ","
                    + resultFlag + ",update," + readWriteFlag;
            outPutOperResult(Constant.alert_succ, successInfo); //这里将域或机构id返回 为了实现 左右窗口信息联动;
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 找到指定id的机构信息 并计算权限后转换成treeOguinitInfo 
     * 可能是域信息
     * 可能是组织机构信息
     * @return String
     */
    public String findData() {
        try {
            //得到必要的参数
            String crrentId = request.getParameter("objId");
            String flag = request.getParameter("flag");
            String curLoginUserId = (String) super.getCurLoginUser();//获得当前管理员id号
            String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //当前管理员所拥有的角色 对应角色表中的rolemark字段

            JSONObject jsonObject = null;
            if (StrTool.strEquals(flag, StrConstant.common_number_1)) { //如果是域
                //当前域
                DomainInfo diQuery = new DomainInfo();
                diQuery.setDomainId(StrTool.parseInt(crrentId));
                DomainInfo domainInfo = (DomainInfo) domainInfoServ.find(diQuery);
                jsonObject = orgunitInfoAide.objToTreeOrgunitInfo(domainInfo, curLoginUserId, curLoginUserRoleMark,
                        adminAndOrgunitServ);
            } else if (StrTool.strEquals(flag, StrConstant.common_number_2)) {//如果是组织机构
                OrgunitInfo oiQuery = new OrgunitInfo();
                oiQuery.setOrgunitId(StrTool.parseInt(crrentId));
                oiQuery.setOrgunitNumber(null);
                OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);
                jsonObject = orgunitInfoAide.objToTreeOrgunitInfo(orgunitInfo, curLoginUserId, curLoginUserRoleMark,
                        adminAndOrgunitServ);
            }

            //输出
            String json = jsonObject.toString();
            setResponseWrite(json);
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_info_message_isnull"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 是否存在 treeOrgunitInfo 的标识
     * @return String
     */
    public String findIsExist() {
        try {
            //查找是否存在
            String source = super.getSource(request);
            boolean isTxist = domainInfoAide.isDomainInfoExist(orgunitInfoAide.exchangeDomainInfo(treeOrgunitInfo),
                    source, domainInfoServ);
            if (isTxist) { //如果找到了 那么不能用  
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 是否存在 treeOrgunitInfo 的标识
     * 针对机构
     * @return String
     */
    public String findOrgunitNumberIsExist() {
        try {
            if (!StrTool.objNotNull(treeOrgunitInfo)) {
                return null;
            }

            //查找是否存在
            OrgunitInfo oiQuery = new OrgunitInfo();
            oiQuery.setOrgunitNumber(treeOrgunitInfo.getMark());
            oiQuery.setDomainId(treeOrgunitInfo.getDomainId());
            OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);
            if (StrTool.objNotNull(orgunitInfo)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 是否存在 treeOrgunitInfo 的标示
     * 针对机构名称
     * 同一域下、同一组织机构的直接下级机构名称不能重复
     * @return String
     */
    public String findOrgnameIsExist() {
        try {
            if (!StrTool.objNotNull(treeOrgunitInfo)) {
                return null;
            }

            //查找是否存在
            boolean isExsit = orgunitInfoAide.isExsitDoubleChildOrg(treeOrgunitInfo.getDomainId(), treeOrgunitInfo
                    .getParentId(), treeOrgunitInfo.getName());

            if (isExsit) {//如果存在
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 机构迁移
     * @return String
     */
    public String moveOrgunit() {
        try {
            OrgunitInfo logOrgInfo = new OrgunitInfo();
            String newparentorg = request.getParameter("neworgname");
            String moveChildTag = request.getParameter("moveChildTag");//是否带走直接子机构 null否 1是
            String changeTokenOrgTag = request.getParameter("moveUserTokenTag");//是否带走 机构下的用户和令牌 null否 1是
            String newDOrgunitId = request.getParameter("newDOrgunitId");

            if (newDOrgunitId.indexOf(",") == -1) {//这个条件一定不能成立
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_operate_err_3"));
                return null;
            }

            //新机构
            int[] results = userInfoAide.outIds(newDOrgunitId);
            int newDomainId = results[0];
            int newOrgunitId = results[1];

            //要迁移的机构
            OrgunitInfo oiQuery = new OrgunitInfo();
            oiQuery.setOrgunitId(treeOrgunitInfo.getId());
            OrgunitInfo orgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);

            logOrgInfo.setOrgParentName(newparentorg);
            logOrgInfo.setOrgunitName(orgunitInfo.getOrgunitName());
            this.setOldOrgInfo(logOrgInfo);

            //如果新的父机构下是否已经存在相同的子机构名称
            boolean isExsit = orgunitInfoAide.isExsitDoubleChildOrg(orgunitInfo.getDomainId(), newOrgunitId,
                    orgunitInfo.getOrgunitName());

            if (isExsit) {//如果存在
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_operate_err_2"));
                return null;
            }

            //新父机构是当前机构那么提示
            if (newDomainId == orgunitInfo.getDomainId() && newOrgunitId == orgunitInfo.getOrgunitId()) {//新父机构是当前机构那么提示
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_operate_err"));
                return null;
            }

            //移动子机构
            boolean mChildTag = StrTool.strNotNull(moveChildTag) && StrTool.strEquals(moveChildTag, "1"); //true 带走 false不带走
            //带走令牌
            boolean cTokenOrgTag = StrTool.strNotNull(changeTokenOrgTag) && StrTool.strEquals(changeTokenOrgTag, "1");//true 带走 false不带走
            //向下移动机构
            boolean isOriChildTag = orgunitInfoAide.newParentOrgIsOldChildOrg(newDomainId, newOrgunitId, orgunitInfo);//判断是否新父机构是原来的子构

            if (isOriChildTag && mChildTag) {//如果 向下迁移 并且 带走子机构
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "org_operate_err_1"));
            } else {//如果是
                int parentId = orgunitInfo.getParentId(); // 临时取出

                //迁移机构
                orgunitInfo.setParentId(newOrgunitId);
                orgunitInfoServ.updateObj(orgunitInfo);

                orgunitInfo.setParentId(parentId);
                if (!mChildTag) {//如果不带走子机构 子机构升级
                    orgunitInfoAide.upChild(orgunitInfo);
                }
                if (!cTokenOrgTag) {//如果不带走用户令牌  用户令牌归属域
                    orgunitInfoAide.moveUserToken(orgunitInfo);
                }

                String successInfo = Language.getLangStr(request, "common_opera_succ_tip") + ","
                        + treeOrgunitInfo.getId() + "," + treeOrgunitInfo.getFlag() + ","
                        + treeOrgunitInfo.getReadWriteFlag() + ",view";
                outPutOperResult(Constant.alert_succ, successInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
        }

        return null;
    }

    /**
     * 点击组织机构树节点（或默认初始化组织机构管理）查看
     */
    public String view() {
        try {
            TreeOrgunitInfo treeOrgInfo = treeOrgunitInfo; // 输出对象
            if (!StrTool.objNotNull(treeOrgunitInfo)) {// 空对象
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
                return null;
            }

            int commenDomainId = 0;
            Integer comenOrgunitId = null;
            String curLoginUserId = (String) super.getCurLoginUser();//管理员id号
            String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); //角色

            int isDomainOrOrg = treeOrgunitInfo.getFlag();// 查看域或者是组织机构 1，域，2，组织机构
            if (isDomainOrOrg == NumConstant.common_number_1) { //如果是域                
                //得到当前域信息
                DomainInfo domainInfo = domainInfoAide.getDomainInfo(commenDomainId, curLoginUserId,
                        curLoginUserRoleMark);

                //赋值管理该域的管理员
                List<?> domainAdminsIds = orgunitInfoAide.getDomainorOrgAdmins(commenDomainId, null, curLoginUserId,
                        curLoginUserRoleMark);
                domainInfo.setAdminIds(domainAdminsIds);
                commenDomainId = domainInfo.getDomainId();

                //将domainInfo 转换成 TreeOrgunitInfo 到页面显示
                treeOrgInfo = orgunitInfoAide.exchangeTreeOrgunitInfo(domainInfo, treeOrgInfo);

                if (!StrTool.listNotNull(domainInfo.getAdminIds())// 如果此域无管理员管理 并且 不等于超级管理员
                        && !StrTool.strEquals(StrConstant.SUPER_ADMIN, curLoginUserRoleMark)) {
                    treeOrgInfo.setReadWriteFlag(1);//只读状态
                }

                //                // start 域信息中，用户列表取域下所有的组织机构ID，名称
                //                String orgunitArr = StrTool.intToString(treeOrgInfo.getDomainId()) + ':' + '0'; // 该域下所有组织机构ID集合
                //                String orgunitNameArr = treeOrgInfo.getName(); // 该域下所有组织机构名称集合
                //                // 查出该域下所有的组织机构
                //                OrgunitInfo orgQuery = new OrgunitInfo();
                //                orgQuery.setDomainId(treeOrgInfo.getDomainId());
                //                List<OrgunitInfo> orgList = (List<OrgunitInfo>) orgunitInfoServ.queryOrgunit(orgQuery);
                //                if (StrTool.listNotNull(orgList)) {
                //                    for (int i = 0; i < orgList.size(); i++) {
                //                        orgQuery = orgList.get(i);
                //                        orgunitArr = orgunitArr + ',' + StrTool.intToString(treeOrgInfo.getDomainId()) + ':'
                //                                + StrTool.intToString(orgQuery.getOrgunitId()); // 组装ID
                //                        orgunitNameArr = orgunitNameArr + ',' + orgQuery.getOrgunitName(); // 组装名称
                //                    }
                //                }
                //                treeOrgInfo.setOrgunitNameArr(orgunitNameArr + ','); // 所组成名称格式：域名称,组织机构名称1,组织机构名称2,...
                //                treeOrgInfo.setOrgunitArr(orgunitArr + ','); // 所组成ID格式： 域ID:组织机构ID1, 域ID:组织机构ID2,....
                //                // End,域信息中，用户列表取域下所有的组织机构ID，名称

                //如果对域有读写权限（即ReadWriteFlag=2）查看页面有令牌列表 查询令牌列表时 需要将组织机构信息带过去
                if (treeOrgInfo.getReadWriteFlag() == 2) {
                    //赋值域的机构查询条件
                    this.setDomainQueryCondition(treeOrgInfo);
                }

            } else if (isDomainOrOrg == NumConstant.common_number_2) { //组织机构
                comenOrgunitId = treeOrgunitInfo.getId();
                //得到机构信息
                OrgunitInfo orgInfo = orgunitInfoAide.getOrgunitInfo(comenOrgunitId, curLoginUserId,
                        curLoginUserRoleMark);
                commenDomainId = orgInfo.getDomainId();

                //转换成 TreeOrgunitInfo
                treeOrgInfo = orgunitInfoAide.exchangeTreeOrgunitInfo(orgInfo, treeOrgInfo);
            }

            //机构下的用户数量、令牌数量
            int userCount = orgunitInfoAide.getUserCount(commenDomainId, comenOrgunitId);//机构下的用户数量
            int tokenCount = orgunitInfoAide.getTokenCount(commenDomainId, comenOrgunitId);//机构下的令牌数量
            treeOrgInfo.setUserCount(userCount);
            treeOrgInfo.setTokenCount(tokenCount);

            //返回页面显示
            this.setTreeOrgunitInfo(treeOrgInfo);

            if (isDomainOrOrg == NumConstant.common_number_1) {
                return "domainView";
            } else {
                return "orgunitView";
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
        }

        return null;
    }

    /**
     * 赋值域和域的组织机构查询条件
     * 赋值说明：IDS 格式所组成ID格式： 域ID:组织机构ID1, 域ID:组织机构ID2,....；
     *         names 格式：所组成名称格式：域名称,组织机构名称1,组织机构名称2,...;
     * 
     * @Date in Nov 20, 2013,10:48:34 AM
     * @param treeOrgInfo
     */
    private void setDomainQueryCondition(TreeOrgunitInfo treeOrgInfo) throws BaseException {
        StringBuilder orgIdsSB = new StringBuilder();
        StringBuilder orgNamesSB = new StringBuilder();

        orgIdsSB.append(StrTool.intToString(treeOrgInfo.getDomainId())).append(":").append("0");// 该域下所有组织机构ID集合
        orgNamesSB.append(treeOrgInfo.getName());// 域名称

        OrgunitInfo orgQuery = new OrgunitInfo();
        orgQuery.setDomainId(treeOrgInfo.getDomainId());
        List<?> orgList = orgunitInfoServ.queryOrgunit(orgQuery);// 查出该域下所有的组织机构
        if (StrTool.listNotNull(orgList)) {
            for (int i = 0; i < orgList.size(); i++) {
                orgQuery = (OrgunitInfo) orgList.get(i);

                orgIdsSB.append(",").append(StrTool.intToString(treeOrgInfo.getDomainId()));
                orgIdsSB.append(":").append(StrTool.intToString(orgQuery.getOrgunitId()));// 组装ID

                orgNamesSB.append(",").append(orgQuery.getOrgunitName());// 组装名称
            }
        }

        treeOrgInfo.setOrgunitArr(orgIdsSB.append(",").toString());
        treeOrgInfo.setOrgunitNameArr(orgNamesSB.append(",").toString());
    }

    public String page() {
        return null;
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
     * @return the treeOrgunitInfo
     */
    public TreeOrgunitInfo getTreeOrgunitInfo() {
        return treeOrgunitInfo;
    }

    /**
     * @param treeOrgunitInfo the treeOrgunitInfo to set
     */
    public void setTreeOrgunitInfo(TreeOrgunitInfo treeOrgunitInfo) {
        this.treeOrgunitInfo = treeOrgunitInfo;
    }

    /**
     * @return the userInfoServ
     */
    public IUserInfoServ getUserInfoServ() {
        return userInfoServ;
    }

    /**
     * @param userInfoServ the userInfoServ to set
     */
    public void setUserInfoServ(IUserInfoServ userInfoServ) {
        this.userInfoServ = userInfoServ;
    }

    /**
     * @return the tokenServ
     */
    public ITokenServ getTokenServ() {
        return tokenServ;
    }

    /**
     * @param tokenServ the tokenServ to set
     */
    public void setTokenServ(ITokenServ tokenServ) {
        this.tokenServ = tokenServ;
    }

    public OrgunitInfo getOldOrgInfo() {
        return oldOrgInfo;
    }

    public void setOldOrgInfo(OrgunitInfo oldOrgInfo) {
        this.oldOrgInfo = oldOrgInfo;
    }

    public DomainInfo getOldDomainInfo() {
        return oldDomainInfo;
    }

    public void setOldDomainInfo(DomainInfo oldDomainInfo) {
        this.oldDomainInfo = oldDomainInfo;
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

	public OrgunitInfo getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrgunitInfo orgInfo) {
		this.orgInfo = orgInfo;
	}

	public DomainInfo getDomainIn() {
		return domainIn;
	}

	public void setDomainIn(DomainInfo domainIn) {
		this.domainIn = domainIn;
	}
    
}
