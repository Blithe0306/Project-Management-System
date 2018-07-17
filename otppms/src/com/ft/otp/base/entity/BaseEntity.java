/**
 *Administrator
 */
package com.ft.otp.base.entity;

/**
 * 基础实体类，提供通用Get、Set方法
 *
 * @Date in Apr 10, 2011,3:18:39 PM
 *
 * @author TBM
 */
public class BaseEntity {

    public BaseEntity() {
    }

    /**
     * 分页显示
     */
    //启始行
    private int startRow = 0;
    //每页显示行大小
    private int pageSize = 2147483647;
    //结束行 endRow

    //判断是否进行模糊查询、统计 0 模糊查询，1 完全匹配查询
    private int queryMark = 0;

    //登陆用户
    private String loginUser = "";

    //登录用户角色
    private String curLoginUserRole = "";

    //批量操作对象时用于临时存放ID串
    private String[] batchIds;

    //int 类型ID
    private int[] batchIdsInt;

    /**
     * @return the batchIds
     */
    public String[] getBatchIds() {
        return batchIds;
    }

    /**
     * @param batchIds the batchIds to set
     */
    public void setBatchIds(String[] batchIds) {
        this.batchIds = batchIds;
    }

    /**
     * @return the startRow
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * @param startRow the startRow to set
     */
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //结束行
    public int getEndRow() {
        return startRow + pageSize;
    }

    /**
     * @return the queryMark
     */
    public int getQueryMark() {
        return queryMark;
    }

    /**
     * @param queryMark the queryMark to set
     */
    public void setQueryMark(int queryMark) {
        this.queryMark = queryMark;
    }

    /**
     * @return the loginUser
     */
    public String getLoginUser() {
        return loginUser;
    }

    /**
     * @param loginUser the loginUser to set
     */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    /**
     * @return the curLoginUserRole
     */
    public String getCurLoginUserRole() {
        return curLoginUserRole;
    }

    /**
     * @param curLoginUserRole the curLoginUserRole to set
     */
    public void setCurLoginUserRole(String curLoginUserRole) {
        this.curLoginUserRole = curLoginUserRole;
    }

    /**
     * @return the batchIdsInt
     */
    public int[] getBatchIdsInt() {
        return batchIdsInt;
    }

    /**
     * @param batchIdsInt the batchIdsInt to set
     */
    public void setBatchIdsInt(int[] batchIdsInt) {
        this.batchIdsInt = batchIdsInt;
    }

}
