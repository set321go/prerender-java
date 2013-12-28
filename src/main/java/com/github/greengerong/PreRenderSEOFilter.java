package com.github.greengerong;


import com.github.greengerong.config.SeoFilterConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PreRenderSEOFilter implements Filter {
    public static final String REFERER_HEADER = "Referer";
    private static final String FORWARD_SLASH = "/";

    public static Logger LOG = Logger.getLogger(PreRenderSEOFilter.class);

    private SeoFilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = new SeoFilterConfig(filterConfig);
        LOG.info("Initialization Complete");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (shouldShowPrerenderedPage(request)) {
            try {
                final ResponseResult result = getPrerenderedPageResponse(request);
                if (result.getStatusCode() == HttpStatus.SC_OK) {
                    final PrintWriter writer = servletResponse.getWriter();
                    writer.write(result.getResponseBody());
                    writer.flush();
                }
            } catch (Exception e) {
                LOG.warn("An error occured processing the request", e);
            }
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        config = null;
        LOG.info("Shutting down");
    }

    private ResponseResult getPrerenderedPageResponse(HttpServletRequest request) throws IOException {
        final String apiUrl = getApiUrl(request, config);
        final HttpClient httpClient = new HttpClient();
        final GetMethod getMethod = new GetMethod(apiUrl);
        setProxy(httpClient);
        setHttpHeader(getMethod);
        final int code = httpClient.executeMethod(getMethod);
        final String responseBody = getMethod.getResponseBodyAsString();
        String body = new String(responseBody.getBytes("utf-8"));
        return new ResponseResult(code, body);
    }

    public static String getApiUrl(HttpServletRequest request, SeoFilterConfig config) {
        String prerenderServiceUrl = config.getPrerenderServiceUrl();
        if (!prerenderServiceUrl.endsWith(FORWARD_SLASH)) {
            prerenderServiceUrl += FORWARD_SLASH;
        }
        return prerenderServiceUrl + getFullUrl(request);
    }

    private static String getFullUrl(HttpServletRequest request) {
        final StringBuffer url = request.getRequestURL();
        final String queryString = request.getQueryString();
        if (queryString != null) {
            url.append('?');
            url.append(queryString);
        }
        return url.toString();
    }

    private void setHttpHeader(HttpMethod httpMethod) {
        httpMethod.setRequestHeader("Cache-Control", "no-cache");
        httpMethod.setRequestHeader("Content-Type", "text/html");
    }

    private void setProxy(HttpClient httpClient) {
        if (StringUtils.isNotBlank(config.getProxy())) {
            httpClient.getHostConfiguration().setProxy(config.getProxy(), config.getProxyPort());
        }
    }

    private boolean shouldShowPrerenderedPage(HttpServletRequest request){
        if (RenderUtils.hasEscapedFragment(request)) {
            return true;
        }

        if (!RenderUtils.isValidUserAgent(request, config)) {
            return false;
        }

        final String url = request.getRequestURI();

        if (RenderUtils.ignoreFileExtension(url, config)) {
            return false;
        }

        final List<String> whiteList = config.getWhitelist();
        if (!whiteList.isEmpty() && !RenderUtils.isInWhiteList(url, whiteList)) {
            return false;
        }

        final List<String> blacklist = config.getBlacklist();
        if (!blacklist.isEmpty() && RenderUtils.isInBlackList(url, request.getHeader(REFERER_HEADER), blacklist)) {
            return false;
        }

        return true;
    }
}
