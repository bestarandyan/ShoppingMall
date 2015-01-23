package com.manyi.mall.cachebean.search;

/**
 * 消息处理Bean
 * 
 * @author dench
 * 
 */
public class NotificationBean {

	public static final NotificationBean getInstance() {
		if (instance == null) {
			instance = new NotificationBean();
		}
		return instance;
	}

	public String msgtype; // 消息类型
	public String message; // 消息内容

	private static NotificationBean instance;

	private NotificationBean() {

	}

}
