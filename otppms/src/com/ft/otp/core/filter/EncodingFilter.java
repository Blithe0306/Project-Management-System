package com.ft.otp.core.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 字符编码过滤器 UTF-8
 *
 * @Date in Jul 31, 2011,11:57:55 AM
 *
 * @author TBM
 */
public class EncodingFilter implements Filter {

    /**
     * The default character encoding to set for requests that pass through
     * this filter.
     */
    protected String encoding = null;

    /**
     * The filter configuration object we are associated with. If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    /**
     * Should a character encoding specified by the client be ignored?
     */
    protected boolean ignore = true;

    public EncodingFilter() {
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     *
     * @todo Implement this javax.servlet.Filter method
     */
    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the
     * container each time a request/response pair is passed through the
     * chain due to a client request for a resource at the end of the chain.
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     * @throws IOException
     * @throws ServletException
     * @todo Implement this javax.servlet.Filter method
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpServletRequest httprequest = (HttpServletRequest) request;

            // Conditionally select and set the character encoding to be used
            if (ignore || (null == request.getCharacterEncoding())) {
                String encoding = selectEncoding(request);
                if (StrTool.strNotNull(encoding)) {
                    httprequest.setCharacterEncoding(encoding);
                    httpResponse.setCharacterEncoding(encoding);
                }
            }

            httpResponse.setContentType("text/html;charset=UTF-8");
            httpResponse.setHeader("Cache-Control", "No-Cache");
        } catch (Exception e) {
        }

        chain.doFilter(request, response);
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * placed into service.
     *
     * @param filterConfig FilterConfig
     * @throws ServletException
     * @todo Implement this javax.servlet.Filter method
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        if (!StrTool.strNotNull(value)) {
            this.ignore = true;
        } else if (StrTool.strEqualsIgnoreCase(value, "true")) {
            this.ignore = true;
        } else {
            this.ignore = false;
        }
    }

    /**
     * Select an appropriate character encoding to be used, based on the
     * characteristics of the current request and/or filter initialization
     * parameters. If no character encoding should be set, return
     * <code>null</code>.
     * <p>
     * The default implementation unconditionally returns the value configured
     * by the <strong>encoding</strong> initialization parameter for this
     * filter.
     *
     * @param request The servlet request we are processing
     */
    protected String selectEncoding(ServletRequest request) {
        return (this.encoding);
    }
}