/**
 *
 */
package com.manyi.mall.cachebean.search;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 */
public class AdvertDetailResponse extends Response {
    private int errorCode = 0;
    private String message;
    private int id;
    private String title;
    private String content;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
