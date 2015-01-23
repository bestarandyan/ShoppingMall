/**
 * 
 */
package com.huoqiu.framework.exception;

/**
 * @author Administrator
 * 
 */
public class ServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * @param detailMessage
	 */
	public ServerException(String detailMessage) {
		super(detailMessage);
	}

	public ServerException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
