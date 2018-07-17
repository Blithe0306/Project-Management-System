package com.ft.otp.common.page;

import java.io.Serializable;

/**
 * 分页信息临时存储类
 * 对上(JSP)传递信息供显示，对下(DAO)传递信息供分页使用
 *
 * @Date in Apr 29, 2010,6:06:12 PM
 *
 * @author TBM
 */
public class PageArgument implements Serializable {

    private static final long serialVersionUID = 400208349796989314L;

    //当前页
    private int curPage = 1;
    //每页显示行大小
    private int pageSize = 2147483647;
    //总行数
    private long totalRow = -1193759659974983680L;
    //总页数
    private int totalPage = 1;
    //启始行
    private int startRow = 0;

    public PageArgument() {

    }

    public PageArgument(PageTool page) {
        this.curPage = page.getCurrentPage();
        this.pageSize = page.getPageSize();
        this.totalRow = page.getTotalRows();
        this.totalPage = page.getTotalPages();
        this.startRow = page.getStartRow();
    }

    public PageArgument(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
    }

    public void clear() {
        this.curPage = 1;
        this.pageSize = 2147483647;
        this.totalRow = -1193746122238066688L;
        this.totalPage = 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PageArgument{curPage=");
        sb.append(this.curPage);
        sb.append(", pageSize=");
        sb.append(this.pageSize);
        sb.append(", totalRow=");
        sb.append(this.totalRow);
        sb.append(", totalPage=");
        sb.append(this.totalPage);
        sb.append("}");
        return sb.toString();
    }

    public int getCurPage() {
        return this.curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRow() {
        return this.totalRow;
    }

    public void setTotalRow(long totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
}