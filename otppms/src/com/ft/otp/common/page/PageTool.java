package com.ft.otp.common.page;

/**
 * 分页处理工具类
 *
 * @Date in Apr 28, 2010,3:42:57 PM
 *
 * @author TBM
 */
public class PageTool {

    public static final String PG_FIRST = "1001"; //首页
    public static final String PG_PREVIOUS = "1002"; //上一页
    public static final String PG_NEXT = "1003"; //下一页
    public static final String PG_LAST = "1004"; //尾页
    public static final String PG_SKIP = "1005"; //跳转至指定页

    //设置每页显示的数据
    private static final int PAGE_NUMBER = 20;
    private int totalRows; //总行数
    private int pageSize; //每页显示的行数
    private int currentPage; //当前页号
    private int totalPages; //总页数
    private int startRow; //当前页在数据库中的起始行

    public PageTool(int _totalRows) {
        pageSize = PAGE_NUMBER;
        totalRows = _totalRows;
        totalPages = totalRows / pageSize;
        int mod = totalRows % pageSize;
        if (mod > 0) {
            totalPages++;
        }
        if (_totalRows == 0) {
            currentPage = 0;
        } else {
            currentPage = 1;
        }
        startRow = 0;
    }

    public PageTool(int _totalRows, int _pageSize) {
        pageSize = _pageSize;
        totalRows = _totalRows;
        totalPages = totalRows / pageSize;
        int mod = totalRows % pageSize;
        if (mod > 0) {
            totalPages++;
        }
        if (_totalRows == 0) {
            currentPage = 0;
        } else {
            currentPage = 1;
        }
        startRow = 0;
    }

    public PageTool(int _totalRows, int _pageSize, int curPage) {
        pageSize = _pageSize;
        totalRows = _totalRows;
        totalPages = totalRows / pageSize;
        int mod = totalRows % pageSize;
        if (mod > 0) {
            totalPages++;
        }
        if (_totalRows == 0) {
            currentPage = 0;
        } else {
            currentPage = curPage;
        }
        startRow = 0;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void first() {
        currentPage = 1;
        startRow = 0;
    }

    public void previous() {
        if (currentPage == 1) {
            return;
        }
        currentPage--;
        startRow = (currentPage - 1) * pageSize;
    }

    public void next() {
        if (currentPage < totalPages) {
            currentPage++;
        }
        startRow = (currentPage - 1) * pageSize;
    }

    public void last() {
        currentPage = totalPages;
        startRow = (currentPage - 1) * pageSize;
    }

    public void refresh(int _currentPage) {
        currentPage = _currentPage;
        if (currentPage > totalPages && totalPages != 0) {
            last();
        }
    }

    public void skip() {
        startRow = (currentPage - 1) * pageSize;
    }
}