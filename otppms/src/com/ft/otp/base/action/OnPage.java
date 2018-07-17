package com.ft.otp.base.action;

import javax.servlet.http.HttpServletRequest;
import com.ft.otp.common.page.PageTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 分页处理入口
 *
 * @Date in Apr 28, 2010,3:44:13 PM
 *
 * @author TBM
 */
public class OnPage {

    /**
     * 根据定义好的参数标识进行分页
     * @Date in Apr 29, 2010,10:00:26 AM
     * @param request
     * @param totalRows
     * @return
     */
    public static PageTool getPager(HttpServletRequest request, int totalRow) {
    	//定义pager对象，用于传到页面
    	PageTool pager =null;
    	//从Request对象中获取当前页号
        String currentPage = request.getParameter("currentPage");
        //从Request对象中获取当前页面大小
        String pageSize = request.getParameter("pageSize");
        if (StrTool.strNotNull(currentPage) && StrTool.strNotNull(pageSize)) {
            pager = new PageTool(totalRow,StrTool.parseInt(pageSize),StrTool.parseInt(currentPage));
        }
        if (StrTool.strNotNull(pageSize)) {
        	pager = new PageTool(totalRow,StrTool.parseInt(pageSize));
        }
        else{
        	pager = new PageTool(totalRow);
        }
      
//        PageTool pager = new PageTool(totalRow);
        //如果当前页号为空，表示为首次查询该页
        //如果不为空，则刷新page对象，输入当前页号等信息
        if (StrTool.strNotNull(currentPage)) {
            pager.refresh(StrTool.parseInt(currentPage));
        }

        //获取当前执行的方法，首页，前一页，后一页，尾页，跳转至指定页。
        String pagerMethod = request.getParameter("pageMed");

        if (StrTool.strNotNull(pagerMethod)) {
            if (pagerMethod.equals(PageTool.PG_FIRST)) {
                pager.first();
            } else if (pagerMethod.equals(PageTool.PG_PREVIOUS)) {
                pager.previous();
            } else if (pagerMethod.equals(PageTool.PG_NEXT)) {
                pager.next();
            } else if (pagerMethod.equals(PageTool.PG_LAST)) {
                pager.last();
            } else if (pagerMethod.equals(PageTool.PG_SKIP)) {
                pager.skip();
            }
        }
        return pager;
    }
}