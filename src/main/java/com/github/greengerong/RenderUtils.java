package com.github.greengerong;

import com.github.greengerong.config.SeoFilterConfig;
import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.FluentIterable.from;

/**
 * Created with IntelliJ IDEA.
 * User: set321go
 * Date: 12/27/2013
 * Time: 10:04 PM
 */
public class RenderUtils {
    public static final String ESCAPED_FRAGMENT_PARAMETER = "_escaped_fragment_";
    public static final String USER_AGENT_HEADER = "User-Agent";


    public static boolean hasEscapedFragment(HttpServletRequest request) {
        return request.getParameterMap().containsKey(ESCAPED_FRAGMENT_PARAMETER);
    }

    public static boolean isInBlackList(final String url, final String referer, List<String> blacklist) {
        return from(blacklist).anyMatch(new Predicate<String>() {
            @Override
            public boolean apply(String regex) {
                final Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(url).matches() ||
                        (!StringUtils.isBlank(referer) && pattern.matcher(referer).matches());
            }
        });
    }

    public static boolean isInWhiteList(final String url, List<String> whitelist) {
        return from(whitelist).anyMatch(new Predicate<String>() {
            @Override
            public boolean apply(String regex) {
                return Pattern.compile(regex).matcher(url).matches();
            }
        });
    }

    public static boolean ignoreFileExtension(final String url, SeoFilterConfig config) {
        return from(config.getExtensions()).anyMatch(new Predicate<String>() {
            @Override
            public boolean apply(String item) {
                return url.contains(item.toLowerCase());
            }
        });
    }

    public static boolean isValidUserAgent(HttpServletRequest request, SeoFilterConfig config) {
        final String useAgent = request.getHeader(USER_AGENT_HEADER);

        if (StringUtils.isBlank(useAgent)) {
            return false;
        }else{
            return from(config.getCrawlers()).anyMatch(new Predicate<String>() {
                @Override
                public boolean apply(String item) {
                    return item.equalsIgnoreCase(useAgent);
                }
            });
        }
    }
}
