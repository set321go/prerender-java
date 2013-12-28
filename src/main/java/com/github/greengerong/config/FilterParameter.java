package com.github.greengerong.config;

/**
 * Created with IntelliJ IDEA.
 * User: set321go
 * Date: 12/26/2013
 * Time: 11:26 PM
 */
public enum FilterParameter {
    CRAWLER_USER_AGENTS("crawlerUserAgents", "googlebot,yahoo,bingbot,baiduspider,facebookexternalhit,twitterbot"),
    EXTENSIONS_TO_IGNORE("extensionsToIgnore", ".js,.css,.less,.png,.jpg,.jpeg,.gif,.pdf,.doc,.txt,.zip,.mp3,.rar," +
            ".exe,.wmv,.doc,.avi,.ppt,.mpg,.mpeg,.tif,.wav,.mov,.psd,.ai,.xls,.mp4,.m4a,.swf,.dat,.dmg,.iso,.flv,.m4v,.torrent"),
    WHITELIST("whitelist", ""),
    BLACKLIST("blacklist", ""),
    SERVICE_URL("prerenderServiceUrl", "http://prerender.herokuapp.com/"),
    PROXY_HOST("proxy", ""),
    PROXY_PORT("proxyPort", "80");

    private String paramName;
    private String defaultValue;

    private FilterParameter(String paramName, String defaultValue){
        this.paramName = paramName;
        this.defaultValue = defaultValue;
    }

    public String getName(){
        return paramName;
    }

    public String getDefaultValue(){
        return defaultValue;
    }
}
