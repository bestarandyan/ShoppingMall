package com.huoqiu.framework.backstack;

public interface BackOpController {
	public void push(Op op);
	public Op pop();
	public Op peek();
	public Op pop(String tag);
}
