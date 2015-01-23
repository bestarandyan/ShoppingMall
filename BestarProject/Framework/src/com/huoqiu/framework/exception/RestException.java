/**
 * 
 */
package com.huoqiu.framework.exception;

/**
 * @author Administrator
 * 
 */
public class RestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public static final int NO_ERROR = 0;
    public static final int SYSTEM_ERROR = 1;
    public static final int INVALID_TOKEN = 2;

	private int errorCode;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param detailMessage
	 */
	public RestException(String detailMessage, int errorCode) {
		super(detailMessage);
		setErrorCode(errorCode);
	}

}
