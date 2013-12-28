package com.github.greengerong;

/**
 * Created with IntelliJ IDEA.
 * User: set321go
 * Date: 12/27/2013
 * Time: 9:56 PM
 */
public class ResponseResult {
    private final int statusCode;
    private final String responseBody;

    public ResponseResult(int code, String body) {
        statusCode = code;
        responseBody = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
