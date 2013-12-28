package com.github.greengerong;

import com.github.greengerong.config.SeoFilterConfig;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: set321go
 * Date: 12/27/2013
 * Time: 10:16 PM
 */
public class RenderUtilsTest {
    HttpServletRequest mockRequest;
    SeoFilterConfig mockSeoFilterConfig;

    IMocksControl control;

    @Before
    public void initMocks(){
         control = EasyMock.createControl();
    }

    @Test
    public void testHasEscapedFragment() throws Exception {
        mockRequest = control.createMock(HttpServletRequest.class);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(RenderUtils.ESCAPED_FRAGMENT_PARAMETER, "_escaped_fragment_");

        expect(mockRequest.getParameterMap()).andReturn(paramMap);

        control.replay();
        assertTrue("The method should have returned true", RenderUtils.hasEscapedFragment(mockRequest));
        control.verify();
    }

    @Test
    public void testHasNoEscapedFragment() throws Exception {
        mockRequest = control.createMock(HttpServletRequest.class);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("myParameter", "testParam");

        expect(mockRequest.getParameterMap()).andReturn(paramMap);

        control.replay();
        assertFalse("The method should have returned false", RenderUtils.hasEscapedFragment(mockRequest));
        control.verify();
    }

    @Test
    public void testIsValidUserAgent() throws Exception {
        mockRequest = control.createMock(HttpServletRequest.class);
        mockSeoFilterConfig = control.createMock(SeoFilterConfig.class);

        String testBot = "googlebot";
        List<String> crawlers = new ArrayList<String>();
        crawlers.add(testBot);

        expect(mockRequest.getHeader(RenderUtils.USER_AGENT_HEADER)).andReturn(testBot);
        expect(mockSeoFilterConfig.getCrawlers()).andReturn(crawlers);

        control.replay();

        assertTrue("This method should have returned true", RenderUtils.isValidUserAgent(mockRequest, mockSeoFilterConfig));

        control.verify();
    }

    @Test
    public void testIsNotValidUserAgent() throws Exception {
        mockRequest = control.createMock(HttpServletRequest.class);
        mockSeoFilterConfig = control.createMock(SeoFilterConfig.class);

        String testBot = "googlebot";
        List<String> crawlers = new ArrayList<String>();
        crawlers.add("testbot");

        expect(mockRequest.getHeader(RenderUtils.USER_AGENT_HEADER)).andReturn(testBot);
        expect(mockSeoFilterConfig.getCrawlers()).andReturn(crawlers);

        control.replay();

        assertFalse("This method should have returned false", RenderUtils.isValidUserAgent(mockRequest, mockSeoFilterConfig));

        control.verify();
    }

    @Test
    public void testIsInBlackList() throws Exception {
        String url = "http://test.com";
        String referer = "http://referer.com";
        List<String> blacklist = new ArrayList<String>();
        blacklist.add("http://test.com");


        assertTrue("This method should have returned true", RenderUtils.isInBlackList(url, referer, blacklist));
    }

    @Test
    public void testIsNotInBlackList() throws Exception {
        String url = "http://test.com";
        String referer = "http://referer.com";
        List<String> blacklist = new ArrayList<String>();
        blacklist.add("http://blacklist.com");


        assertFalse("This method should have returned false", RenderUtils.isInBlackList(url, referer, blacklist));
    }

    @Test
    public void testIsInWhiteList() throws Exception {
        String url = "http://test.com";
        List<String> whitelist = new ArrayList<String>();
        whitelist.add("http://test.com");


        assertTrue("This method should have returned true", RenderUtils.isInWhiteList(url, whitelist));
    }

    @Test
    public void testIsNotInWhiteList() throws Exception {
        String url = "http://test.com";
        List<String> whitelist = new ArrayList<String>();
        whitelist.add("http://dummy.com");


        assertFalse("This method should have returned false", RenderUtils.isInWhiteList(url, whitelist));
    }

    @Test
    public void testIgnoreFileExtension() throws Exception {
        mockSeoFilterConfig = control.createMock(SeoFilterConfig.class);

        String url = "http://test.com/myImg.jpg";
        List<String> extensions = new ArrayList<String>();
        extensions.add(".jpg");

        expect(mockSeoFilterConfig.getExtensions()).andReturn(extensions);

        control.replay();

        assertTrue("This method should have returned true", RenderUtils.ignoreFileExtension(url, mockSeoFilterConfig));

        control.verify();
    }

    @Test
    public void testDoNotIgnoreFileExtension() throws Exception {
        mockSeoFilterConfig = control.createMock(SeoFilterConfig.class);

        String url = "http://test.com/my-page.html";
        List<String> extensions = new ArrayList<String>();
        extensions.add(".jpg");

        expect(mockSeoFilterConfig.getExtensions()).andReturn(extensions);

        control.replay();

        assertFalse("This method should have returned false", RenderUtils.ignoreFileExtension(url, mockSeoFilterConfig));

        control.verify();
    }
}
