package com.ft.otp.manager.orgunit.orgunit.action.aide;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.admin.user.action.aide.AdminUserActionAide;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.orgunit.domain.action.aide.DomainInfoActionAide;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.json.JsonEntity;
import com.ft.otp.util.tool.StrTool;

/**
 * 组织机构实现的辅助类功能说明
 *
 * @Date in January 22, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class OrgunitInfoActionAide {

    private Logger logger = Logger.getLogger(OrgunitInfoActionAide.class);
    /**帮助类**/
    private DomainInfoActionAide domainInfoAide = new DomainInfoActionAide();
    private AdminUserActionAide adminUserActionAide = new AdminUserActionAide();

    /**服务接口**/
    private IOrgunitInfoServ orgunitInfoServ = (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");//组织机构服务接口
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ"); //域服务接口
    private IAdminAndOrgunitServ adminAndOrgunitServ = (IAdminAndOrgunitServ) AppContextMgr
            .getObject("adminAndOrgunitServ");
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");//用户服务接口
    private ITokenServ tokenServ = (ITokenServ) AppContextMgr.getObject("tokenServ"); //令牌服务接口

    /**
     * 对象实例数据转换 TreeOrgunitInfo转换为OrgunitInfo
     * TreeOrgunitInfo->OrgunitInfo
     * @param treeOrgunitInfo
     * @return OrgunitInfo
     */
    public OrgunitInfo exchangeOrgunitInfo(TreeOrgunitInfo treeOrgunitInfo) {
        OrgunitInfo orgunitInfo = new OrgunitInfo();
        orgunitInfo.setOrgunitId(treeOrgunitInfo.getId());
        orgunitInfo.setOrgunitNumber(treeOrgunitInfo.getMark());
        orgunitInfo.setOrgunitName(treeOrgunitInfo.getName());
        orgunitInfo.setDescp(treeOrgunitInfo.getDescp());
        orgunitInfo.setParentId(treeOrgunitInfo.getParentId());
        orgunitInfo.setAdmins(treeOrgunitInfo.getAdmins());
        orgunitInfo.setDomainId(treeOrgunitInfo.getDomainId());
        orgunitInfo.setDomainInfo(treeOrgunitInfo.getDomainInfo());
        orgunitInfo.setParentOrgunitInfo(treeOrgunitInfo.getParentOrgunitInfo());

        orgunitInfo.setOrgParentName(treeOrgunitInfo.getOrgParentName());
        return orgunitInfo;
    }

    /**
     * 对象实例数据转换TreeOrgunitInfo转换为DomainInfo
     * TreeOrgunitInfo->DomainInfo
     * @param treeOrgunitInfo
     * @return  
     */
    public DomainInfo exchangeDomainInfo(TreeOrgunitInfo treeOrgunitInfo) {
        DomainInfo domainInfo = new DomainInfo();
        domainInfo.setDomainId(treeOrgunitInfo.getId());
        domainInfo.setDomainName(treeOrgunitInfo.getName());
        domainInfo.setDomainSn(treeOrgunitInfo.getMark());
        domainInfo.setDescp(treeOrgunitInfo.getDescp());
        domainInfo.setAdminIds(treeOrgunitInfo.getAdmins());
        //domainInfo.setIsDefault(treeOrgunitInfo.getIsdefault());
        return domainInfo;
    }

    /**
     * domainInfo对象转换为 TreeOrgunitInfo 对象
     * DomainInfo->TreeOrgunitInfo
     * @param domainInfo
     * @param treeOrgunitInfo
     * @return TreeOrgunitInfo
     */
    public TreeOrgunitInfo exchangeTreeOrgunitInfo(DomainInfo domainInfo, TreeOrgunitInfo treeOrgunitInfo) {
        TreeOrgunitInfo toi = null;
        if (StrTool.objNotNull(treeOrgunitInfo)) {
            toi = treeOrgunitInfo;
        } else {
            toi = new TreeOrgunitInfo();
        }
        toi.setId(domainInfo.getDomainId());
        toi.setMark(domainInfo.getDomainSn());
        toi.setName(domainInfo.getDomainName());
        toi.setDescp(domainInfo.getDescp());
        toi.setIsdefault(domainInfoAide.isDefaultDomainInfo(domainInfo.getDomainId()));
        toi.setFlag(1);//表示是域
        toi.setParentId(0); //因为域没有父
        toi.setDomainId(domainInfo.getDomainId()); //所属域
        toi.setDomainInfo(domainInfo);////所属域信息
        toi.setAdmins(domainInfo.getAdminIds());
        return toi;
    }

    /**
     * OrgunitInfo 对象转换为 TreeOrgunitInfo 对象
     * OrgunitInfo->TreeOrgunitInfo
     * @param orgunitInfo
     * @param treeOrgunitInfo
     * @return  TreeOrgunitInfo
     */
    public TreeOrgunitInfo exchangeTreeOrgunitInfo(OrgunitInfo orgunitInfo, TreeOrgunitInfo treeOrgunitInfo) {
        TreeOrgunitInfo toi = null;
        if (StrTool.objNotNull(treeOrgunitInfo)) {
            toi = treeOrgunitInfo;
        } else {
            toi = new TreeOrgunitInfo();
        }
        toi.setId(orgunitInfo.getOrgunitId());
        toi.setMark(orgunitInfo.getOrgunitNumber());
        toi.setName(orgunitInfo.getOrgunitName());
        toi.setDomainId(orgunitInfo.getDomainId());
        toi.setDescp(orgunitInfo.getDescp());
        toi.setAdmins(orgunitInfo.getAdmins());
        toi.setParentId(orgunitInfo.getParentId());
        toi.setParentOrgunitInfo(orgunitInfo.getParentOrgunitInfo());
        toi.setDomainInfo(orgunitInfo.getDomainInfo());
        toi.setFlag(2);//表示是组织机构
        return toi;
    }

    /**
     * 域和组织机构 及 组织机构和组织机构之间的 上下级数据整理
     * 最后 转换成 TreeOrgunitInfo 对象列表
     * @Date in January 22, 2013,14:00:00 PM
     * @param domainInfos
     * @param orgunitInfos
     * @return List
     * @throws BaseException
     */
    public List<TreeOrgunitInfo> reformOrgunitInfo(List<?> domainInfos, List<?> orgunitInfos) throws BaseException {
        List<TreeOrgunitInfo> treeOrgunitInfos = new ArrayList<TreeOrgunitInfo>();//最终 树 数据

        if (!StrTool.listNotNull(domainInfos) || (orgunitInfos == null)) {
            return treeOrgunitInfos;
        }

        DomainInfo[] domainInfoArray = (DomainInfo[]) domainInfos.toArray(new DomainInfo[domainInfos.size()]);
        OrgunitInfo[] orgunitInfoArray = (OrgunitInfo[]) orgunitInfos.toArray(new OrgunitInfo[orgunitInfos.size()]);

        //遍历 域 其中默认域第一位处理
        for (int i = 0; i < domainInfoArray.length; i++) {
            TreeOrgunitInfo treeOrgunitInfo_domain = exchangeTreeOrgunitInfo(domainInfoArray[i], null);
            List<TreeOrgunitInfo> childrenList = new ArrayList<TreeOrgunitInfo>(); //该域下的直接组织机构

            for (int j = 0; j < orgunitInfoArray.length; j++) {
                if (orgunitInfoArray[j].getDomainId() == treeOrgunitInfo_domain.getId()
                        && orgunitInfoArray[j].getParentId() == NumConstant.common_number_0) { //如果该域下的某个组织机构的父节点是0 那么这个组织机构就是这个域的直接子机构

                    TreeOrgunitInfo treeOrgunitInfo_orgunit = exchangeTreeOrgunitInfo(orgunitInfoArray[j], null);
                    //如果这个组织机构还有子机构 将子组织机构封装  这是个递归函数
                    hasChildrenAndReFormTree(treeOrgunitInfo_orgunit, orgunitInfoArray);
                    //封装这个组织机构
                    childrenList.add(treeOrgunitInfo_orgunit);
                }
            }

            treeOrgunitInfo_domain.setChildren(childrenList);
            treeOrgunitInfos.add(treeOrgunitInfo_domain);
        }

        return treeOrgunitInfos;
    }

    /**
     * 判断并赋值当前组织机构是否有子组织机构
     * @param treeOrgunitInfo_orgunit 当前组织机构对象
     * @param orgunitInfos 组织机构对象列表
     * @throws BaseException 
     */
    private void hasChildrenAndReFormTree(TreeOrgunitInfo treeOrgunitInfo_orgunit, OrgunitInfo[] orgunitInfos)
            throws BaseException {
        boolean result = false;

        List<TreeOrgunitInfo> treeOrgunitInfoList = new ArrayList<TreeOrgunitInfo>(); //打包这些子组织机构
        for (int i = 0; i < orgunitInfos.length; i++) {
            if (treeOrgunitInfo_orgunit.getDomainId() == orgunitInfos[i].getDomainId()
                    && treeOrgunitInfo_orgunit.getId() == orgunitInfos[i].getParentId()) { //如果该域下 有其他组织机构的父节点id 和当前组织机构id相等 那么证明当前组织机构有子机构

                TreeOrgunitInfo treeOrgunitInfo_orgunit_ = exchangeTreeOrgunitInfo(orgunitInfos[i], null);
                //如果这个组织机构还有子机构 将子组织机构封装  这是个递归函数
                hasChildrenAndReFormTree(treeOrgunitInfo_orgunit_, orgunitInfos);

                //封装这个组织机构
                treeOrgunitInfoList.add(treeOrgunitInfo_orgunit_);
                result = true;
            }
        }
        if (result == true) { //如果有组织机构那么就封装
            treeOrgunitInfo_orgunit.setChildren(treeOrgunitInfoList);
        }
    }

    /**
     * 过滤空children
     * @param treeOrgunitInfos
     * @return JSONObject
     * @throws BaseException
     */
    public JSONObject fliterChildren(List<?> treeOrgunitInfos) {
        JsonEntity entity = new JsonEntity();
        if (StrTool.listNotNull(treeOrgunitInfos)) {
            entity.setItems(treeOrgunitInfos);
            entity.setResults(treeOrgunitInfos.size());
        }
        JSONObject JsonObject = JSONObject.fromObject(entity);

        JSONArray jsonArray = JsonObject.getJSONArray("items");//得到 JsonObject数组
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);//得到 json对象
            if (jo.getJSONArray("children").size() == NumConstant.common_number_0) { //如果 这个对象的children 对应的value 是[] 即空数组 那么删除
                jo.remove("children");
            } else { //如果不是空数组 那么进行迭代 下一层数据
                removeEmptyChildren(jo);
            }
        }

        return JsonObject;
    }

    /**
     * 递归清空json对象中的空jsonArray(针对 "children"：[])
     * @param jsonObject
     */
    private void removeEmptyChildren(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("children");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);
            if (jo.getJSONArray("children").size() == NumConstant.common_number_0) {
                jo.remove("children");
            } else {//如果不是空数组 那么进行迭代 下一层数据
                removeEmptyChildren(jo);
            }
        }
    }

    /**
     * 根据管理员角色
     * 对组织机构树的 2<可读可写> 1<可读> 0<不可视> 判断； 
     * 打标识<可读可写>和<可读>项
     * 去掉<不可视>项
     * @param adminAndOrgunits
     * @param jsonObject
     * @return JSONObject
     */
    public JSONObject makeMarkDrop(List<?> adminAndOrgunits, JSONObject jsonObject) throws BaseException {
        try {
            //1、全部标识为 <不可视>项
            JSONArray jsonArray = jsonObject.getJSONArray("items");//得到 JsonObject数组
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);//得到 json对象
                jo.element("readWriteFlag", NumConstant.common_number_0);
                if (jo.has("children")) { //如果 这个对象 还有children 将children中的对象打标识为 <不可视>即0 
                    markChildren(jo, NumConstant.common_number_0);
                }
            }

            //2、标识 <可读可写>项  这里域和组织机构的id值是两个字段
            AdminAndOrgunit[] adminAndOrgunitArray = null;
            if (adminAndOrgunits != null) { //仅仅非空
                adminAndOrgunitArray = adminAndOrgunits.toArray(new AdminAndOrgunit[adminAndOrgunits.size()]);
                for (int i = 0; i < jsonArray.size(); i++) { //因为组织机构的第一层就是域 所以不用迭代
                    JSONObject jo = jsonArray.getJSONObject(i);//得到 json对象
                    if ((Integer) jo.get("flag") == NumConstant.common_number_1) {//如果是域
                        for (int j = 0; j < adminAndOrgunitArray.length; j++) { //对域的打标
                            if (adminAndOrgunitArray[j].getDomainId() == (Integer) jo.get("id")
                                    && (adminAndOrgunitArray[j].getOrgunitId() == NumConstant.common_number_0)) { //如果匹配域
                                jo.element("readWriteFlag", NumConstant.common_number_2); //2 表示 <可读可写>
                                break;
                            }
                        } //end for i
                    } else if ((Integer) jo.get("flag") == NumConstant.common_number_2) {
                        for (int j = 0; j < adminAndOrgunitArray.length; j++) { //对组织机构的打标
                            if (adminAndOrgunitArray[j].getOrgunitId().equals((Integer) jo.get("id"))
                                    && !adminAndOrgunitArray[j].getOrgunitId().equals(NumConstant.common_number_0)) { //如果匹配域
                                jo.element("readWriteFlag", NumConstant.common_number_2); //2 表示 <可读可写>
                                break;
                            }
                        } //end for i
                    }
                    if (jo.has("children")) { //如果有子机构  子机构都是 组织机构
                        JSONArray joArray = jo.getJSONArray("children");
                        for (int k = 0; k < joArray.size(); k++) {
                            JSONObject joobj = joArray.getJSONObject(k);
                            for (int j = 0; j < adminAndOrgunitArray.length; j++) {//在这个数组中找符合的组织机构
                                if (adminAndOrgunitArray[j].getOrgunitId().equals((Integer) joobj.get("id"))
                                        && adminAndOrgunitArray[j].getDomainId() == (Integer) joobj.get("domainId")) { //如果匹配域 必须是同一个域中的组织机构
                                    joobj.element("readWriteFlag", NumConstant.common_number_2); //2 表示 <可读可写>
                                    break;
                                }
                            }
                            if (joobj.has("children")) {
                                markChildren(joobj, adminAndOrgunitArray);
                            }
                        } //end for				 
                    }
                } //end for j
            }

            //3、标识 <可读>项   <可读可写>项的上级至顶级 以及直接下级 在没有标识<可读可写>的前提下打标识为<可读>
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);//得到 json对象
                //向下遍历
                if ((Integer) jo.get("readWriteFlag") == NumConstant.common_number_0) { //如果该节点 自身是 <不可视>  那么所有下级机构中 有<可读可写>标识 将自身标识为<可读>
                    if (jo.has("children")) {
                        JSONArray joArray = jo.getJSONArray("children");
                        for (int j = 0; j < joArray.size(); j++) {
                            JSONObject jo_temp = joArray.getJSONObject(j);
                            if ((Integer) jo_temp.get("readWriteFlag") == NumConstant.common_number_2) { //如果直接子机构有标识为<可读可写> 那么上级节点标识为<可读>
                                jo.element("readWriteFlag", NumConstant.common_number_1);
                                break; //结束循环
                            } else { //如果没有 继续向下遍历
                                changeReadWriteFlag(jo, jo_temp);
                            }
                        }
                    } //end has children

                } else if ((Integer) jo.get("readWriteFlag") == NumConstant.common_number_2) {//如果该机构自身已是<可读可写> 那么将直接子机构改为<可读> 前提是子结构不是<可读可写>状态 
                    if (jo.has("children")) {
                        JSONArray joArray = jo.getJSONArray("children"); //直接子机构 
                        for (int j = 0; j < joArray.size(); j++) {
                            JSONObject jo_temp = joArray.getJSONObject(j);
                            if ((Integer) jo_temp.get("readWriteFlag") == NumConstant.common_number_0) { //如果直接子机构是 <不可视>标识 那么改为<可读>
                                jo_temp.element("readWriteFlag", NumConstant.common_number_1);
                            }
                        } //end for
                    }
                }
                //处理子节点
                if (jo.has("children")) {
                    JSONArray joArray = jo.getJSONArray("children");
                    for (int j = 0; j < joArray.size(); j++) {
                        JSONObject jo_temp = joArray.getJSONObject(j);
                        changeReadWriteFlag(jo_temp);
                    }
                }
            }

            //4、移除<不可视>项	
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);//得到 json对象
                if ((Integer) jo.get("readWriteFlag") == NumConstant.common_number_0) {//如果 该节点是<不可视项>
                    jsonArray.remove(i); //移除该节点
                    i--;//这里注意 如果不减减； 那么 移除该节点后 i的步伐快1
                } else {//如果 可视（<可读可写>或<可读>） 那么 处理子机构
                    if (jo.has("children")) {
                        removeChildren(jo); //将<不可视>的子节点删除
                    }
                }
                //如果该节点还有子节点 并且 是空的(意味着所有子机构对象都删除了 变成了空数组)
                if (jo.has("children") && jo.getJSONArray("children").size() == NumConstant.common_number_0) {
                    jo.remove("children");
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return jsonObject;

    }

    /**
     * 递归 标识项
     * @param jsonObject
     * @param value
     */
    private void markChildren(JSONObject jsonObject, int value) {
        JSONArray jsonArray = jsonObject.getJSONArray("children");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);
            jo.element("readWriteFlag", NumConstant.common_number_0);
            if (jo.has("children")) { //如果 这个对象 还有children 将children中的对象打标识为 <不可视>即0 
                markChildren(jo, NumConstant.common_number_0);
            }
        }
    }

    /**
     * 递归 标识项
     * @param jsonObject
     * @param adminAndOrgunitArray 匹配范围
     */
    private void markChildren(JSONObject jo, AdminAndOrgunit[] adminAndOrgunitArray) {
        JSONArray joArray = jo.getJSONArray("children");
        for (int k = 0; k < joArray.size(); k++) {
            JSONObject joobj = joArray.getJSONObject(k);
            for (int j = 0; j < adminAndOrgunitArray.length; j++) {//在这个数组中找符合的组织机构
                if (adminAndOrgunitArray[j].getOrgunitId().equals((Integer) joobj.get("id"))
                        && adminAndOrgunitArray[j].getDomainId() == (Integer) joobj.get("domainId")) { //如果匹配域 必须是同一个域中的组织机构
                    joobj.element("readWriteFlag", NumConstant.common_number_2); //2 表示 <可读可写>
                    break;
                }
            }

            if (joobj.has("children")) {
                markChildren(joobj, adminAndOrgunitArray);
            }
        } //end for	    	
    }

    /**
     * 递归 标识项
     * @param jo 
     * @param jo_temp
     */
    private void changeReadWriteFlag(JSONObject jo, JSONObject jo_temp) {
        if (jo_temp.has("children")) {
            JSONArray joArray = jo_temp.getJSONArray("children");
            for (int j = 0; j < joArray.size(); j++) {
                JSONObject json = joArray.getJSONObject(j);
                if ((Integer) json.get("readWriteFlag") == NumConstant.common_number_2) { //如果直接子机构有标识为<可读可写> 那么上级节点标识为<可读>
                    jo.element("readWriteFlag", NumConstant.common_number_1);
                    break; //结束循环

                } else { //如果没有 继续向下遍历
                    changeReadWriteFlag(jo, json);
                }
            }
        } //end has children

    }

    /**
     * 递归 寻标识的条件
     * @param jo
     */
    private void changeReadWriteFlag(JSONObject jo) {
        //向下遍历
        if ((Integer) jo.get("readWriteFlag") == NumConstant.common_number_0) { //如果该节点 自身是 <不可视>  那么所有下级机构中 有<可读可写>标识 将自身标识为<可读>
            if (jo.has("children")) {
                JSONArray joArray = jo.getJSONArray("children");
                for (int j = 0; j < joArray.size(); j++) {
                    JSONObject jo_temp = joArray.getJSONObject(j);
                    if ((Integer) jo_temp.get("readWriteFlag") == NumConstant.common_number_2) { //如果直接子机构有标识为<可读可写> 那么上级节点标识为<可读>
                        jo.element("readWriteFlag", NumConstant.common_number_1);
                        break; //结束循环

                    } else { //如果没有 继续向下遍历
                        changeReadWriteFlag(jo, jo_temp);
                    }
                }
            } //end has children

        } else if ((Integer) jo.get("readWriteFlag") == NumConstant.common_number_2) {//如果该机构自身已是<可读可写> 那么将直接子机构改为<可读> 前提是子结构不是<可读可写>状态 
            if (jo.has("children")) {
                JSONArray joArray = jo.getJSONArray("children"); //直接子机构 
                for (int j = 0; j < joArray.size(); j++) {
                    JSONObject jo_temp = joArray.getJSONObject(j);
                    if ((Integer) jo_temp.get("readWriteFlag") == NumConstant.common_number_0) { //如果直接子机构是 <不可视>标识 那么改为<可读>
                        jo_temp.element("readWriteFlag", NumConstant.common_number_1);
                    }
                } //end for
            }
        }
        //遍历子
        if (jo.has("children")) {
            JSONArray joArray = jo.getJSONArray("children");
            for (int j = 0; j < joArray.size(); j++) {
                JSONObject jo_temp = joArray.getJSONObject(j);
                changeReadWriteFlag(jo_temp);
            }

        }

    }

    /**
     * 递归 删除子机构
     * @param jsonObject
     */
    public void removeChildren(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("children");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);//得到 json对象
            if ((Integer) jo.get("readWriteFlag") == NumConstant.common_number_0) {//如果 该节点是<不可视项> 
                jsonArray.remove(i); //移除该节点
                i--; //这里注意 如果不减减； 那么 移除该节点后 i的步伐快1
            } else {//如果 可视（<可读可写>或<可读>） 那么 处理子机构
                if (jo.has("children")) {
                    removeChildren(jo); //将<不可视>的子节点删除
                }
            }

            //如果该节点还有子节点 并且 是空的(意味着所有子机构对象都删除了 变成了空数组)
            if (jo.has("children") && jo.getJSONArray("children").size() == NumConstant.common_number_0) {
                jo.remove("children");
            }
        }
    }

    /**
     * 将一个域对象 或者 组织机构对象 
     * 计算读写权限后 封装成  treeOrgunitInfo 对象 
     * @param obj
     * @param curLoginUserId
     * @param curLoginUserRoleMark
     * @param adminAndOrgunitServ
     * @return JSONObject
     */
    public JSONObject objToTreeOrgunitInfo(Object obj, String curLoginUserId, String curLoginUserRoleMark,
            IAdminAndOrgunitServ adminAndOrgunitServ) {
        JSONObject jsonObject = null;
        try {
            TreeOrgunitInfo toi = null;
            int domainId = 0;
            Integer orgunitId = null;

            if (obj instanceof DomainInfo) { //如果是域对象
                DomainInfo di = (DomainInfo) obj;
                toi = exchangeTreeOrgunitInfo(di, null);
                toi.setDomainId(di.getDomainId());
                //重设条件
                domainId = di.getDomainId();
                orgunitId = NumConstant.common_number_0;

            } else if (obj instanceof OrgunitInfo) { //如果是组织机构对象
                OrgunitInfo oi = (OrgunitInfo) obj;
                toi = exchangeTreeOrgunitInfo(oi, null);
                //重设条件
                domainId = oi.getDomainId();
                orgunitId = oi.getOrgunitId();
            }

            //过滤  空children机构 并转换成JSONObject
            List<TreeOrgunitInfo> toiList = new ArrayList<TreeOrgunitInfo>();
            toi.setReadWriteFlag(NumConstant.common_number_2);

            //重设
            if (!StrConstant.SUPER_ADMIN.equals(curLoginUserRoleMark)) { //如果不是超级管理员 过滤组织机构树 区分编辑权限
                AdminAndOrgunit aaoQuery = new AdminAndOrgunit(curLoginUserId, orgunitId, domainId);
                List<?> adminAndOrgunits = adminAndOrgunitServ.queryAdminAndOrgunitByAdminId(aaoQuery);//查询此域或机构是否被当前登录管理员管理

                if (StrTool.listNotNull(adminAndOrgunits)) {// 有管理权限
                    toiList.add(toi);
                    jsonObject = fliterChildren(toiList);
                    jsonObject = makeMarkDrop(adminAndOrgunits, jsonObject);// 重置可读可写权限
                } else {// 当前管理员没有管理此机构
                    toi.setReadWriteFlag(NumConstant.common_number_1);// 只读
                    toiList.add(toi);
                    jsonObject = fliterChildren(toiList);
                }
            } else {// 超级管理员
                toiList.add(toi);
                jsonObject = fliterChildren(toiList);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return jsonObject;
    }

    /**
     * 指定机构下的用户数量
     * @param userInfoServ
     * @param domainId
     * @param orgunitId
     * @return int
     */
    public int getUserCount(int domainId, Integer orgunitId) {
        int userCount = NumConstant.common_number_0;
        UserInfo uiQuery = new UserInfo();
        uiQuery.setDomainId(domainId);
        if (StrTool.objNotNull(orgunitId)) {
            uiQuery.setOrgunitId(orgunitId == NumConstant.common_number_0 ? null : orgunitId);
        } else {
            uiQuery.setOrgunitId(orgunitId);
        }
        try {
            userCount = userInfoServ.countBind(uiQuery);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return userCount;
    }

    /**
     * 指定机构下的令牌数量
     * @param domainId
     * @param orgunitId
     * @return int
     */
    public int getTokenCount(int domainId, Integer orgunitId) {
        int tokenCount = 0;
        try {
            TokenInfo tiQuery = new TokenInfo();
            tiQuery.setDomainid(domainId);

            if (StrTool.objNotNull(orgunitId)) {
                tiQuery.setOrgunitid(orgunitId == 0 ? null : orgunitId);
            } else {
                tiQuery.setOrgunitid(orgunitId);
            }

            tiQuery.setAuflag(1);
            tokenCount = tokenServ.countBC(tiQuery);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return tokenCount;
    }

    /**
     * 检测组织机构下是否存在子组织机构
     * @param domainId
     * @param orgunitId
     * @return boolean
     */
    public boolean orgunitIsExsitChildOrg(int domainId, int orgunitId) {
        boolean isExsitTag = false; //不存在

        OrgunitInfo oiQquery = new OrgunitInfo();
        oiQquery.setDomainId(domainId);
        oiQquery.setParentId(orgunitId);

        //不参与条件匹配
        oiQquery.setOrgunitId(0);
        oiQquery.setOrgunitNumber(null);
        oiQquery.setCreateTime(0);
        try {
            int count = orgunitInfoServ.count(oiQquery);
            if (count > 0) {
                isExsitTag = true;
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return isExsitTag;
    }

    /**
     * 检测域或组织机构下是否存在用户
     * @param domainId
     * @param orgunitId
     * @return boolean 
     *          false:不存在、true：存在
     */
    public boolean doIsExsitUsers(int domainId, Integer orgunitId) {
        boolean isExsitTag = false; //不存在

        int count = getUserCount(domainId, orgunitId);
        if (count > NumConstant.common_number_0) {
            isExsitTag = true;
        }

        return isExsitTag;
    }

    /**
     * 检测域或组织机构下是否存在令牌
     * @param domainId
     * @param orgunitId
     * @return boolean
     *          false：不存在、true ：存在
     */
    public boolean doIsExsitTokens(int domainId, Integer orgunitId) {
        boolean isExsitTag = false; //不存在

        int count = getTokenCount(domainId, orgunitId);
        if (count > 0) {
            isExsitTag = true;
        }

        return isExsitTag;
    }

    /**
     * 获取管理域或机构的管理员ID集合
     * 并过滤掉非当前管理员创建的管理员ID
     * @param domainId
     * @param orgunitId 
     *        如为null 则只查属于域的
     * @param adminId
     * @param adminRole
     * @return List
     * @throws BaseException
     */
    public List<String> getDomainorOrgAdmins(int domainId, Integer orgunitId, String adminId, String adminRole)
            throws BaseException {
        List<String> admins = null;

        //查找管理此域和机构的 管理员
        AdminAndOrgunit aaoQuery = new AdminAndOrgunit("", orgunitId, domainId);
        List<?> adminAndOrgunitList = adminAndOrgunitServ.queryAdminAndOrgunitByDomainId(aaoQuery);

        //筛选或遍历管理员
        if (StrTool.strEquals(StrConstant.SUPER_ADMIN, adminRole)) {//超级管理员 
            admins = this.getAdminList(null, adminAndOrgunitList, 1);//遍历获取ID集合
        } else {// 普通管理员过滤掉 非 自己创建的管理员ID
            AdminUser queryUser = new AdminUser();
            queryUser.setLoginUser(adminId);
            List<?> adminUserList = adminUserActionAide.getCurrLoginUserChildUsers(queryUser, null);//获得当前管理员及其子管理员

            admins = this.getAdminList(adminUserList, adminAndOrgunitList, 2);//取得交集            
        }

        //判断结果
        if (!StrTool.objNotNull(admins)) {
            admins = new ArrayList<String>();
        }

        return admins;
    }

    /**
     * 取得域或机构的管理员id集合  
     * @param adminUserList
     * @param adminAndOrgunitList
     * @param flag 1：超级管理员遍历获取 管理员域机构关系中 所有管理员ID；
     *        flag 2：普通管理员获取 管理员域机构关系中 当前管理员以及自己创建的管理员  
     * @return List
     */
    public List<String> getAdminList(List<?> adminUserList, List<?> adminAndOrgunitList, int flag) {
        List<String> adminIdList = new ArrayList<String>();

        if (!StrTool.listNotNull(adminAndOrgunitList)) {
            return adminIdList;
        }

        AdminAndOrgunit[] aaoArray = adminAndOrgunitList.toArray(new AdminAndOrgunit[adminAndOrgunitList.size()]);
        if (flag == NumConstant.common_number_1) { //超级管理员
            for (int i = 0; i < aaoArray.length; i++) {//关系遍历
                if (!adminIdList.contains(aaoArray[i].getAdminId())) {//判断list中是否已添加过
                    adminIdList.add(aaoArray[i].getAdminId());
                }
            }
        } else { //普通管理员
            if (!StrTool.listNotNull(adminUserList)) {
                return null;
            }

            AdminUser[] auArray = adminUserList.toArray(new AdminUser[adminUserList.size()]);
            for (int i = 0; i < aaoArray.length; i++) {//关系遍历
                String aaoAdminId = aaoArray[i].getAdminId();
                for (int j = 0; j < auArray.length; j++) {//管理员遍历 
                    String auAdminId = auArray[j].getAdminid();
                    if (StrTool.strEquals(aaoAdminId, auAdminId)) { //交集
                        if (!adminIdList.contains(auAdminId)) {
                            adminIdList.add(auAdminId);
                            break;
                        }
                    }
                }
            }
        }

        return adminIdList;
    }

    /**
     * 找到组织机构的父机构（域或者组织机构）
     * @param parentId
     * @param domainId
     * @return TreeOrgunitInfo
     */
    public TreeOrgunitInfo getParentTreeOrgunitInfo(int parentId, int domainId) throws BaseException {
        TreeOrgunitInfo parentOrgunitInfo = new TreeOrgunitInfo();
        if (parentId == NumConstant.common_number_0) { //如果父机构是0 表示该机构的父机构是个域
            DomainInfo diQuery = new DomainInfo();
            diQuery.setDomainId(domainId);
            DomainInfo pDomainInfo = (DomainInfo) domainInfoServ.find(diQuery);

            //转化
            parentOrgunitInfo.setId(NumConstant.common_number_0); //并非域id 仅仅是表示父机构是个域
            parentOrgunitInfo.setName(pDomainInfo.getDomainName()); //仅仅将域的名称设置
        } else {
            OrgunitInfo oiQuery = new OrgunitInfo();
            oiQuery.setOrgunitId(parentId);
            oiQuery.setOrgunitNumber(null);
            OrgunitInfo pOrgunitInfo = (OrgunitInfo) orgunitInfoServ.find(oiQuery);

            //转化
            parentOrgunitInfo.setId(pOrgunitInfo.getOrgunitId());
            parentOrgunitInfo.setName(pOrgunitInfo.getOrgunitName());

        }

        return parentOrgunitInfo;
    }

    /**
     * 父机构迁移后子机构升级
     * @param parentOrgunitInfo
     */
    public void upChild(OrgunitInfo parentOrgunitInfo) throws Exception {
        List<?> orgunits = orgunitInfoServ.queryWholeList(new OrgunitInfo());
        if (StrTool.listNotNull(orgunits)) {
            int size = orgunits.size();
            OrgunitInfo[] oiArray = orgunits.toArray(new OrgunitInfo[size]);

            for (int i = 0; i < size; i++) {
                if (oiArray[i].getDomainId() == parentOrgunitInfo.getDomainId()
                        && oiArray[i].getParentId() == parentOrgunitInfo.getOrgunitId()) {
                    OrgunitInfo oi = oiArray[i];
                    oi.setParentId(parentOrgunitInfo.getParentId());
                    orgunitInfoServ.updateObj(oiArray[i]);
                }
            }
        }
    }

    /**
     * 指定机构以及所有子机构下的令牌和用户 释放到域
     * @param orgunitInfo
     * @param userInfoServ
     * @param tokenServ
     */
    public void moveUserToken(OrgunitInfo orgunitInfo) throws BaseException {
        //释放用户
        UserInfo uiUpdate = new UserInfo();
        uiUpdate.setDomainId(orgunitInfo.getDomainId());
        uiUpdate.setOrgunitId(orgunitInfo.getOrgunitId());
        uiUpdate.setNewOrgunitId(null);
        userInfoServ.updateUserOrgunits(uiUpdate);

        //释放令牌
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setDomainid(orgunitInfo.getDomainId());
        tokenInfo.setOrgunitid(null);
        tokenInfo.setNewDomainId(orgunitInfo.getDomainId());
        tokenInfo.setNewOrgunitId(orgunitInfo.getOrgunitId());

        tokenServ.updateTokenOrg(tokenInfo);

        /**释放子结构的用户和令牌**/
        // 查询子机构  将子机构的令牌也释放到域
        OrgunitInfo orgQuery = new OrgunitInfo();
        orgQuery.setDomainId(orgunitInfo.getDomainId());
        orgQuery.setParentId(orgunitInfo.getOrgunitId());
        //不参与条件匹配
        orgQuery.setOrgunitId(0);
        orgQuery.setOrgunitNumber(null);
        orgQuery.setCreateTime(0);
        List<?> chOrgs = orgunitInfoServ.queryWholeList(orgQuery);
        if (StrTool.listNotNull(chOrgs)) {
            for (int i = 0; i < chOrgs.size(); i++) {
                OrgunitInfo org = (OrgunitInfo) chOrgs.get(0);
                // 迭代修改所有子机构
                moveUserToken(org);
            }
        }
    }

    /**
     * 判断新父机构是否是原子机构
     * @param newDomainId
     * @param newOrgunitId
     * @param orgunitInfo
     * @return boolean
     *           true：是、false ：否
     */
    public boolean newParentOrgIsOldChildOrg(int newDomainId, int newOrgunitId, OrgunitInfo orgunitInfo)
            throws BaseException {
        boolean resultTag = false; //不是

        if (newOrgunitId == 0) {
            return resultTag;
        }

        List<?> orgunits = orgunitInfoServ.queryWholeList(new OrgunitInfo());

        if (!StrTool.listNotNull(orgunits)) {
            return resultTag;
        }

        int size = orgunits.size();
        OrgunitInfo[] oiArray = orgunits.toArray(new OrgunitInfo[size]);
        for (int i = 0; i < size; i++) {
            if (oiArray[i].getDomainId() == newDomainId && oiArray[i].getOrgunitId() == newOrgunitId) {//找到出发点
                if (oiArray[i].getDomainId() == newDomainId && oiArray[i].getParentId() == orgunitInfo.getOrgunitId()) {
                    resultTag = true; //向下迁移
                    break;
                } else {
                    if (oiArray[i].getParentId() != 0) {//新父机构不是个域
                        resultTag = sumParentId(oiArray[i].getParentId(), newDomainId, orgunitInfo.getOrgunitId(),
                                oiArray);
                    }
                }
            }
        }

        return resultTag;
    }

    /**
     * 递归判断向下迁移逻辑
     * @param objId
     * @param objDomainId
     * @param orgunitId
     * @param oiArray
     * @return boolean
     */
    private boolean sumParentId(int objId, int objDomainId, int orgunitId, OrgunitInfo[] oiArray) {
        boolean resultTag = false; //不是
        int size = oiArray.length;
        for (int i = 0; i < size; i++) {
            if (oiArray[i].getDomainId() == objDomainId && oiArray[i].getOrgunitId() == objId) {//找到上一级对象
                if (oiArray[i].getParentId() == orgunitId) {
                    resultTag = true; //向下迁移
                    break;
                } else {
                    if (oiArray[i].getParentId() != 0) {//新父机构不是个域
                        resultTag = sumParentId(oiArray[i].getParentId(), objDomainId, orgunitId, oiArray);
                    }
                }
            }
        }
        return resultTag;
    }

    /**
     * 组装初始化 1个组织机构-N个管理员 关系列表
     * @param domainId
     * @param orgunitId
     * @param adminIds
     * @return List
     */
    public List<AdminAndOrgunit> initAdminAndOrgRelation(int domainId, Integer orgunitId, List<?> adminIds)
            throws BaseException {
        List<AdminAndOrgunit> adminAndOrgunitList = new ArrayList<AdminAndOrgunit>();

        if (!StrTool.listNotNull(adminIds)) {
            return adminAndOrgunitList;
        }

        String[] adminIdArray = (String[]) adminIds.toArray(new String[adminIds.size()]);
        for (int i = 0; i < adminIdArray.length; i++) {
            AdminAndOrgunit aao = new AdminAndOrgunit(adminIdArray[i], orgunitId, domainId);
            adminAndOrgunitList.add(aao);
        }

        return adminAndOrgunitList;
    }

    /**
     * 同一机构下的双重机构名称验证
     * @param domainId
     * @param parentId
     * @param orgunitName
     * @return boolean
     *      false :不存在、true存在
     * @throws BaseException
     */
    public boolean isExsitDoubleChildOrg(int domainId, int parentId, String orgunitName) throws BaseException {
        boolean isExsit = false; //0表示不存在  （orgunitName,parentId） 唯一

        List<?> oiList = orgunitInfoServ.queryWholeList(new OrgunitInfo());

        if (!StrTool.listNotNull(oiList)) {
            return isExsit;
        }

        OrgunitInfo[] orgArray = oiList.toArray(new OrgunitInfo[oiList.size()]);
        for (int i = 0; i < orgArray.length; i++) {
            if (StrTool.strEquals(orgArray[i].getOrgunitName().toLowerCase(), orgunitName.toLowerCase())
                    && orgArray[i].getParentId() == parentId && orgArray[i].getDomainId() == domainId) {
                isExsit = true;
                break;
            }
        }

        return isExsit;
    }

    /**
     * 根据组织机构id 得到组织机构对象（包括机构信息、管理该组织机构的管理员、改机构的父机构信息、所属域信息）
     * @param orguntId
     * @param adminId
     * @param adminRole
     * @return OrgunitInfo
     * 
     * @throws BaseException 
     */
    public OrgunitInfo getOrgunitInfo(int orguntId, String adminId, String adminRole) throws BaseException {
        //得到机构信息
        OrgunitInfo orgQuery = new OrgunitInfo();
        orgQuery.setOrgunitId(orguntId);
        orgQuery.setOrgunitNumber(null);
        OrgunitInfo oi = (OrgunitInfo) orgunitInfoServ.find(orgQuery);

        //机构的管理员
        List<String> admins = this.getDomainorOrgAdmins(0, oi.getOrgunitId(), adminId, adminRole);
        oi.setAdmins(admins);

        //找到父机构信息
        TreeOrgunitInfo parentOrgunitInfo = getParentTreeOrgunitInfo(oi.getParentId(), oi.getDomainId());
        oi.setParentOrgunitInfo(parentOrgunitInfo);

        //找到所属域信息
        DomainInfo domainInfo = new DomainInfo();
        domainInfo.setDomainId(oi.getDomainId());
        domainInfo.setDomainName(DomainConfig.getValue(oi.getDomainId()));
        oi.setDomainInfo(domainInfo);

        return oi;
    }

}
