package com.ft.otp.manager.orgunit.orgunit.action.aide;

import java.util.List;

import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;

/**
 * 组织机构实现的辅助类功能说明
 * 这个类其实是个entity 统一域和组织机构的数据结构 进行树的展示和维护
 * 
 * @Date in January 22, 2013,14:00:00 PM
 *
 * @author BYL
 */
public class TreeOrgunitInfo {

    private int id;// 此id对应 域中的 domainId 或 组织机构中的orgunitId
    private String mark; //此mark对应 域中的 domainsn 或组织机构中的 orgunitnumber
    private String name;// 此name对应 域中的 domainname 或组织机构中的 orgunitname
    private String descp; // 此descp对应 域中的 descp 或组织机构中的 descp
    private long createTime; //此createtime对应域中的createtime 或组织机构中的createtime
    private int isdefault; //只对应域中的 isdefault
    private int parentId; //只对应组织机构中的parentid
    private int domainId; //只对应组织机构中的domainid
    private List<?> children;
    private int flag; //1 表示 域  2表示 组织机构
    private int readWriteFlag = 2; // 2=可读可写 1=可读  0=不可视 ； 默认是2
    private List<?> admins;
    private TreeOrgunitInfo parentOrgunitInfo; //父组织机构 可能是域信息 也可能是组织机构 所以用中间model 展示时使用
    private DomainInfo domainInfo;
    private int userCount; //机构下的令牌
    private int tokenCount; //机构下的令牌
    private String orgunitArr; // 域下所有组织机构ID集合，格式 domainId:orgunitid1,domainId:orgunitid2,...
    private String orgunitNameArr; // 域下所有组织机构名称集合，格式 域名称,组织机构名称1,组织机构名称2,...
    private String orgParentName; //组织机构父名称

    public String getOrgunitNameArr() {
        return orgunitNameArr;
    }

    public void setOrgunitNameArr(String orgunitNameArr) {
        this.orgunitNameArr = orgunitNameArr;
    }

    public String getOrgunitArr() {
        return orgunitArr;
    }

    public void setOrgunitArr(String orgunitArr) {
        this.orgunitArr = orgunitArr;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the mark
     */
    public String getMark() {
        return mark;
    }

    /**
     * @param mark the mark to set
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the descp
     */
    public String getDescp() {
        return descp;
    }

    /**
     * @param descp the descp to set
     */
    public void setDescp(String descp) {
        this.descp = descp;
    }

    /**
     * @return the isdefault
     */
    public int getIsdefault() {
        return isdefault;
    }

    /**
     * @param isdefault the isdefault to set
     */
    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    /**
    /**
     * @return the children
     */
    public List<?> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<?> children) {
        this.children = children;
    }

    /**
     * @return the flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * @return the parentId
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the domainId
     */
    public int getDomainId() {
        return domainId;
    }

    /**
     * @param domainId the domainId to set
     */
    public void setDomainId(int domainId) {
        this.domainId = domainId;
    }

    /**
     * @return the readWriteFlag
     */
    public int getReadWriteFlag() {
        return readWriteFlag;
    }

    /**
     * @param readWriteFlag the readWriteFlag to set
     */
    public void setReadWriteFlag(int readWriteFlag) {
        this.readWriteFlag = readWriteFlag;
    }

    /**
     * @return the admins
     */
    public List<?> getAdmins() {
        return admins;
    }

    /**
     * @param admins the admins to set
     */
    public void setAdmins(List<?> admins) {
        this.admins = admins;
    }

    /**
     * @return the parentOrgunitInfo
     */
    public TreeOrgunitInfo getParentOrgunitInfo() {
        return parentOrgunitInfo;
    }

    /**
     * @param parentOrgunitInfo the parentOrgunitInfo to set
     */
    public void setParentOrgunitInfo(TreeOrgunitInfo parentOrgunitInfo) {
        this.parentOrgunitInfo = parentOrgunitInfo;
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
     * @return the createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the userCount
     */
    public int getUserCount() {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    /**
     * @return the tokenCount
     */
    public int getTokenCount() {
        return tokenCount;
    }

    /**
     * @param tokenCount the tokenCount to set
     */
    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public String getOrgParentName() {
        return orgParentName;
    }

    public void setOrgParentName(String orgParentName) {
        this.orgParentName = orgParentName;
    }
}
