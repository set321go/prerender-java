package com.github.greengerong.config;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import javax.servlet.FilterConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: set321go
 * Date: 12/26/2013
 * Time: 11:17 PM
 */
public class SeoFilterConfig {
    public static final String LIST_SEPERATOR = ",";

    private final String prerenderServiceUrl;
    private final List<String> blacklist;
    private final List<String> whitelist;
    private final List<String> extensions;
    private final List<String> crawlers;
    private final String proxy;
    private final int proxyPort;

    public SeoFilterConfig(FilterConfig filterConfig){
        crawlers = loadCrawlerUserAgents(filterConfig);
        extensions = loadExtensionsToIgnore(filterConfig);
        whitelist = loadWhitelist(filterConfig);
        blacklist = loadBlacklist(filterConfig);
        prerenderServiceUrl = loadPrerenderServiceUrl(filterConfig);
        proxy = loadProxyHost(filterConfig);
        proxyPort = loadProxyPort(filterConfig);
    }

    private List<String> loadCrawlerUserAgents(FilterConfig filterConfig) {
        List<String> crawlerUserAgents = Lists.newArrayList(FilterParameter.CRAWLER_USER_AGENTS.getDefaultValue().split(LIST_SEPERATOR));
        final String crawlerUserAgentsFromConfig = filterConfig.getInitParameter(FilterParameter.CRAWLER_USER_AGENTS.getName());
        if (StringUtils.isNotBlank(crawlerUserAgentsFromConfig)) {
            crawlerUserAgents.addAll(Arrays.asList(crawlerUserAgentsFromConfig.trim().split(LIST_SEPERATOR)));
        }

        return crawlerUserAgents;
    }

    private List<String> loadExtensionsToIgnore(FilterConfig filterConfig) {
        List<String> extensionsToIgnore = Lists.newArrayList(FilterParameter.EXTENSIONS_TO_IGNORE.getDefaultValue().split(LIST_SEPERATOR));
        final String extensionsToIgnoreFromConfig = filterConfig.getInitParameter(FilterParameter.EXTENSIONS_TO_IGNORE.getName());
        if (StringUtils.isNotBlank(extensionsToIgnoreFromConfig)) {
            extensionsToIgnore.addAll(Arrays.asList(extensionsToIgnoreFromConfig.trim().split(LIST_SEPERATOR)));
        }

        return extensionsToIgnore;
    }

    private String loadPrerenderServiceUrl(FilterConfig filterConfig) {
        final String prerenderServiceUrl = filterConfig.getInitParameter(FilterParameter.SERVICE_URL.getName());
        return StringUtils.isNotBlank(prerenderServiceUrl) ? prerenderServiceUrl : FilterParameter.SERVICE_URL.getDefaultValue();
    }

    private List<String> loadWhitelist(FilterConfig filterConfig) {
        final String whitelist = filterConfig.getInitParameter(FilterParameter.WHITELIST.getName());
        if (StringUtils.isNotBlank(whitelist)) {
            return Arrays.asList(whitelist.trim().split(LIST_SEPERATOR));
        }
        return Collections.emptyList();
    }

    private List<String> loadBlacklist(FilterConfig filterConfig) {
        final String blacklist = filterConfig.getInitParameter(FilterParameter.BLACKLIST.getName());
        if (StringUtils.isNotBlank(blacklist)) {
            return Arrays.asList(blacklist.trim().split(LIST_SEPERATOR));
        }
        return Collections.emptyList();
    }

    private String loadProxyHost(FilterConfig filterConfig){
        final String proxy = filterConfig.getInitParameter(FilterParameter.PROXY_HOST.getName());
        return StringUtils.isNotBlank(proxy) ? proxy : FilterParameter.PROXY_HOST.getDefaultValue();
    }

    private int loadProxyPort(FilterConfig filterConfig){
        final String proxyPort = filterConfig.getInitParameter(FilterParameter.PROXY_PORT.getName());
        if (StringUtils.isNotBlank(proxy) && StringUtils.isNotBlank(proxyPort)) {
            return Integer.parseInt(proxyPort);
        }else {
            return Integer.parseInt(FilterParameter.PROXY_PORT.getDefaultValue());
        }
    }

    public String getPrerenderServiceUrl() {
        return prerenderServiceUrl;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public List<String> getCrawlers() {
        return crawlers;
    }

    public String getProxy() {
        return proxy;
    }

    public int getProxyPort() {
        return proxyPort;
    }
}
