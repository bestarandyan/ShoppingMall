package com.huoqiu.framework.backstack;

public abstract class AbsOp implements Op{
	private String tag;
	
	
	public AbsOp(String tag) {
		this.tag = tag;
	}

	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

}
