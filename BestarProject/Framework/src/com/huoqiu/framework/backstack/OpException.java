package com.huoqiu.framework.backstack;

public class OpException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public OpException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public OpException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
