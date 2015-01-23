/**
 * 
 */
package com.huoqiu.framework.exception;

/**
 * @author Administrator
 * 
 */
public class ClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final int REPEATED_REQUEST = 1;// 重复请求
	public static final int REQUEST_ABORTED = 2;// 请求被取消
	public static final int REQUEST_EXCEPTION = 3;// 服务异常
	public static final int REQUEST_NETWORK = 4;// 当前网络不可用
	public static final int NATIVE_SYSTEM_EXCEPTION = 0;// 本地系统异常
	public static final int FRAGMENT_DETACHED = 5;// Fragment已经Detached
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
	public ClientException(String detailMessage, int errorCode) {
		super(detailMessage);
		setErrorCode(errorCode);
	}

	public ClientException(String detailMessage, int errorCode, Throwable throwable) {
		super(detailMessage, throwable);
		setErrorCode(errorCode);
	}

}
