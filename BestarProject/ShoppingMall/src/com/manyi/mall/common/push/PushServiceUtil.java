package com.manyi.mall.common.push;

import android.content.Context;
import android.content.Intent;


public class PushServiceUtil {
	public static void stopNotificationService(Context context){
		Intent intent = NotificationService.getIntent();
		context.stopService(intent);
	}
	
	public static void startNotificationService(Context context){
		Intent intent = NotificationService.getIntent();
		context.startService(intent);
	}
}
