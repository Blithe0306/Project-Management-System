/**
 *Administrator
 */
package com.ft.otp.base.action;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.page.PageTool;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * 基础Action，提供对request,response,session的实现
 *
 * @Date in Apr 2, 2011,3:17:51 PM
 *
 * @author TBM
 */
public class BaseAction extends ActionSupport implements Preparable, ServletRequestAware, ServletResponseAware,
        SessionAware {

    private static final long serialVersionUID = -1444846564259449319L;

    protected static final String SUCC_ADD = "add";
    protected static final String SUCC_EDIT = "edit";
    protected static final String SUCC_FIND = "find";
    protected static final String SUCC_DEL = "del";
    protected static final String SUCC_VIEW = "view";

    private static final String HTML_PAGE_INFO = "html_page_info";
    public static final String HTML_PAGE_LIST = "html_page_list";
    public static final String HTML_PAGE_OBJ = "html_page_obj";
    private static final String HTML_PAGE_MESG = "html_page_mesg";

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Map sessionMap;

    //临时设置Action业务方法的操作结果
    private boolean actionResult = false;

    private int page = 1;
    private int pagesize = 2147483647;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void prepare() throws Exception {
    }

    public void setServletRequest(HttpServletRequest request) {
        if (null == request) {
            request = ServletActionContext.getRequest();
        }
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //设置response编码方式
        response.setCharacterEncoding("UTF-8");
        this.response = response;
    }

    public void setSession(Map sMap) {
        this.sessionMap = sMap;
    }

    /**
     * Action 输出至页面的提示、响应信息
     * @Date in Apr 2, 2011,3:20:17 PM
     * @param request
     * @param info
     */
    protected void setHtmlPageInfo(String info) {
        if (StrTool.strNotNull(info)) {
            response.setCharacterEncoding("UTF-8");
            request.setAttribute(HTML_PAGE_MESG, info);

        }
    }

    /**
     * AJAX方式进行Action请求返回的响应数据或提示
     * @Date in Jun 23, 2011,3:08:09 PM
     * @param response
     * @param info
     */
    protected void setResponseWrite(String info) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(info);
        } catch (IOException ex) {
        } finally {
            try {
                response.flushBuffer();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Action 输出至页面的数据列表
     * @Date in Apr 7, 2011,9:55:40 AM
     * @param request
     * @param list
     */
    protected void setRequestPageList(List<?> list) {
        if (!StrTool.listNotNull(list)) {
            list = new ArrayList<Object>();
        }
        request.setAttribute(HTML_PAGE_LIST, list);
    }

    /**
     * Action 输出至页面的数据对象
     * @Date in Apr 11, 2011,3:49:13 PM
     * @param request
     * @param object
     */
    protected void setRequestPageObj(Object object) {
        request.setAttribute(HTML_PAGE_OBJ, object);
    }

    /**
     * 执行分页操作，初始化PageArgument
     * @Date in Apr 8, 2011,7:44:59 PM
     * @param request
     * @param totalRow
     * @return
     */
    protected PageArgument getArgument(HttpServletRequest request, int totalRow) {
        String talRowStr = request.getParameter("talRow");
        if (StrTool.strNotNull(talRowStr)) {
            totalRow = StrTool.parseInt(talRowStr);
        }
        PageTool page = OnPage.getPager(request, totalRow);
        PageArgument pageArg = new PageArgument(page);
        return pageArg;
    }

    protected PageArgument getArgument(int totalRow) {
        PageArgument pageArg = new PageArgument(page, pagesize);
        pageArg.setTotalRow(totalRow);
        if (totalRow <= pagesize) {
            pageArg.setTotalPage(1);
        } else {
            pageArg.setTotalPage(totalRow % pagesize == 0 ? totalRow / pagesize : (int) totalRow / pagesize + 1);
        }
        pageArg.setPageSize(pagesize);
        pageArg.setCurPage(totalRow > 0 && page == 0 ? 1 : page);
        if (pageArg.getCurPage() > pageArg.getTotalPage()) {
            pageArg.setCurPage(pageArg.getTotalPage());
        }
        pageArg.setStartRow((pageArg.getCurPage() > 0 ? pageArg.getCurPage() - 1 : 0) * pagesize);

        return pageArg;
    }

    /**
     * 输出分页数据，PageArgument数据
     * @Date in Apr 8, 2011,7:17:08 PM
     * @param request
     * @param pageArg
     */
    protected void setPageData(HttpServletRequest request, PageArgument pageArg) {
        request.setAttribute(HTML_PAGE_INFO, pageArg);
        setPageInfo(request, pageArg);
    }

    /**
     * 输出分页信息 上一页 下一页信息
     * @Date in Apr 8, 2011,7:17:38 PM
     * @param request
     * @param pageArg
     */
    private void setPageInfo(HttpServletRequest request, PageArgument pageArg) {
        HttpSession session = request.getSession();
        String firstPage = Language.getLangStr(session, "common_syntax_firstPage");
        String prePage = Language.getLangStr(session, "common_syntax_prePage");
        String nextPage = Language.getLangStr(session, "common_syntax_nextPage");
        String lastPage = Language.getLangStr(session, "common_syntax_lastPage");

        int curPage = 1;
        long totalRow = 0;
        if (pageArg.getTotalPage() > 1) {
            curPage = pageArg.getCurPage();
            totalRow = pageArg.getTotalRow();
            if (curPage > 1) {
                firstPage = "<a href='javascript:TurnPage(" + (curPage + "," + totalRow + "," + PageTool.PG_FIRST)
                        + ")'>" + firstPage + "</a>";
                prePage = "<a href='javascript:TurnPage(" + (curPage + "," + totalRow + "," + PageTool.PG_PREVIOUS)
                        + ")'>" + prePage + "</a>";
            }
            if (curPage < pageArg.getTotalPage()) {
                nextPage = "<a href='javascript:TurnPage(" + (curPage + "," + totalRow + "," + PageTool.PG_NEXT)
                        + ")'>" + nextPage + "</a>";
                lastPage = "<a href='javascript:TurnPage(" + (curPage + "," + totalRow + "," + PageTool.PG_LAST)
                        + ")'>" + lastPage + "</a>";
            }

            request.setAttribute("firstPage", firstPage);
            request.setAttribute("prePage", prePage);
            request.setAttribute("nextPage", nextPage);
            request.setAttribute("lastPage", lastPage);
        }
    }

    /**
     * 返回多选数据key_id Set
     * 用途：共用
     * @Date in Apr 29, 2010,3:29:58 PM
     * @param request
     * @return
     */
    protected Set<?> getDataKeys(HttpServletRequest request) {
        String[] arrKey = request.getParameterValues("key_id");
        Set<?> keys = null;
        if (StrTool.arrNotNull(arrKey)) {
            keys = new HashSet<Object>(Arrays.asList(arrKey));
        }
        return keys;
    }

    /**
     * 返回逗号分隔的数据
     * 用途：共用
     * @Date in Apr 29, 2010,3:29:58 PM
     * @param request
     * @return
     */
    protected Set<?> getDelIds(String paramName) {
        String delIds = request.getParameter(paramName);
        String[] delIdArr = null;
        Set<?> keys = null;
        if (StrTool.strNotNull(delIds)) {
            delIdArr = delIds.split(",");
            keys = new HashSet<Object>(Arrays.asList(delIdArr));
        }

        return keys;
    }

    /**
     * 返回单个token字符串
     * 用途：共用
     * @Date in Apr 29, 2010,3:29:58 PM
     * @param request
     * @return
     */
    protected Set<?> getTokenKey(HttpServletRequest request) {
        String arrKey = request.getParameter("token");
        Set<?> keys = null;
        if (StrTool.strNotNull(arrKey)) {
            keys = new HashSet<Object>(Arrays.asList(arrKey));
        }
        return keys;
    }

    /**
     * 返回request请求来源字符串
     * 用途：共用
     * @Date in Apr 29, 2010,3:29:58 PM
     * @param request
     * @return
     */
    protected String getSource(HttpServletRequest request) {
        String requestSource = request.getParameter("source");
        return requestSource;
    }

    /**
     * 设置列表数据是初始化还是执行查询
     * @Date in Jun 1, 2011,2:29:02 PM
     * @param request
     * @return
     */
    protected boolean getIsInit(HttpServletRequest request) {
        try {
            if (request == null || request.getParameterMap() == null)
                return false;
            String isInitStr = request.getParameter("isInit");
            if (StrTool.strEquals(isInitStr, StrConstant.common_number_1)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 是否需要清空Form,根据客户端传来数据决定,如果是初始查询则需要清空;如果是分页、条件查询、刷新等则不需要清空.
     * 如果需要清空Form,前operType传来即可,否则视为不清空.
     * @Date in Jun 1, 2011,2:29:02 PM
     * @param request
     * @return
     */
    protected boolean isNeedClearForm() {
        try {
            if (null == request || null == request.getParameterMap())
                return false;
            String operate = request.getParameter("operType");
            if (StrTool.strNotNull(operate)) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * 输出JSON格式的Action操作错误信息
     * 
     * @Date in Apr 8, 2012,10:06:06 AM
     * @param result 结果 成功 | 失败
     * @param object 对象，可以为字符串形式的错误信息或操作结果对象
     */
    protected void outPutOperResult(String result, Object object) {
        String jsonStr = JsonTool.getJsonErrorMeg(result, object);
        if (StrTool.strEquals(result, Constant.alert_error) || StrTool.strEquals(result, Constant.alert_warn)) {
            this.setActionResult(false);
        } else {
            this.setActionResult(true);
        }
        setResponseWrite(jsonStr);
    }

    /**
     * 输出String格式的Action操作错误信息
     * 方法说明
     * @Date in Jan 30, 2013,3:37:00 PM
     * @param result 结果 成功 | 失败
     * @param info 输出机构
     */
    protected void outPutOperResultString(String result, String info) {
        if (StrTool.strEquals(result, Constant.alert_error) || StrTool.strEquals(result, Constant.alert_warn)) {
            this.setActionResult(false);
        } else {
            this.setActionResult(true);
        }
        setResponseWrite(info);
    }

    /** web应用绝对路径
    * @Date in May 12, 2011,11:28:23 AM
    * @param filePath
    * @param fileName
    * @return
    */
    protected String appPath(String filePath, String fileName) {
        String servPath = getSession().getServletContext().getRealPath("");

        String pathStr = "";
        if (StrTool.strNotNull(filePath)) {
            pathStr = servPath + filePath;
        }
        if (StrTool.strNotNull(fileName)) {
            pathStr += fileName;
        }
        if (!StrTool.strNotNull(pathStr)) {
            pathStr = servPath;
        }
        return pathStr;
    }

    /**
     * 令牌相关操作生成的文件存储路径获取方法，同时创建
     * 以sessionId命名的文件夹
     * 
     * @Date in Apr 20, 2012,2:46:36 PM
     * @param dirName
     * @param fileName
     * @return
     */
    protected String getFilePath(String dirName, String fileName) {
        String filePath = appPath(dirName, null);
        String sessionId = OnlineUsers.getSessionId(request);

        filePath += sessionId + "/";
        if (StrTool.strNotNull(fileName)) {
            filePath += fileName;
        }

        return filePath;
    }

    /**
     * 从session中获取当前登录用户
     */
    protected String getCurLoginUser() {
        if (null != request && null != request.getSession(true)) {
            return (String) request.getSession(true).getAttribute(Constant.CUR_LOGINUSER);
        } else {
            return "";
        }
    }

    /**
     * 从session中获取当前登录用户角色
     */
    protected String getCurLoginUserRole() {
        if (null != request && null != request.getSession(true)) {
            return (String) request.getSession(true).getAttribute(Constant.CUR_LOGINUSER_ROLE);
        } else {
            return "";
        }
    }

    /**
     * GET Request 方法
     * 
     * @Date in May 26, 2012,2:03:03 PM
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * GET Session方法
     * 
     * @Date in May 26, 2012,2:03:19 PM
     * @return
     */
    public HttpSession getSession() {
        if (null != request) {
            return request.getSession();
        }

        return null;
    }

    /**
     * 取得当前在线用户信息
     * 
     * @Date in Jul 17, 2012,3:35:27 PM
     * @return
     */
    protected LinkUser getLinkUser() {
        LinkUser linkUser = null;
        try {
            String sessionId = getSession().getId();
            if (StrTool.strNotNull(sessionId)) {
                linkUser = OnlineUsers.getUser(sessionId);
            }
        } catch (Exception e) {
            linkUser = new LinkUser();
        }

        return linkUser;
    }

    /**
     * @return the actionResult
     */
    public boolean getActionResult() {
        boolean tempBool = actionResult;
        this.setActionResult(false);

        return tempBool;
    }

    /**
     * @param actionResult the actionResult to set
     */
    public void setActionResult(boolean actionResult) {
        this.actionResult = actionResult;
    }

    /**
     * 调用进度条，如果业务操作失败，则设置进度条为100%，以停止进度条运行
     * 
     * @Date in Jul 17, 2012,3:38:09 PM
     */
    protected void setPercentVal() {
        LinkUser linkUser = getLinkUser();
        if (null != linkUser) {
            linkUser.setPercent(100);
        }
    }

    /**
     * 获取service访问地址
     */
    public String getServiceUrl() {
        String serviceUrl = "";
        //        serviceUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
        //                + request.getContextPath() + "/service?wsdl";

        return serviceUrl;
    }

    /**
     * 获取本地ip地址
     * 方法说明
     * @Date in Mar 9, 2013,11:33:04 AM
     * @return
     */
    public String getLocalIpAddr() {
        String ipAddr = "";
        try {
            InetAddress net = InetAddress.getLocalHost();
            if (StrTool.objNotNull(net)) {
                ipAddr = net.getHostAddress().toString();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return ipAddr;
    }
}
