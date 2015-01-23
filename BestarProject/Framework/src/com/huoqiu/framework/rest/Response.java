package com.huoqiu.framework.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * @author leo.li
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Response {
    private int errorCode = 0;
    private String message;

    /**
     * 
     */
    public Response() {
        // TODO Auto-generated constructor stub
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
