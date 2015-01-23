package com.manyi.mall.common.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.novell.sasl.client.FileLogUtil;

public class UserPresentReceiver extends BroadcastReceiver{

	private NotificationService notificationService;
	
	public UserPresentReceiver(NotificationService notificationService){
		this.notificationService = notificationService;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 FileLogUtil.printLog("UserPresentReceiver invoked");
		 notificationService.connect();
	}

}