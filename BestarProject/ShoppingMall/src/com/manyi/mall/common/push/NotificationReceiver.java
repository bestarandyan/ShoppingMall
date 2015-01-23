package com.manyi.mall.common.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.novell.sasl.client.FileLogUtil;

public final class NotificationReceiver extends BroadcastReceiver {

	public NotificationReceiver() {
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
			String notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
			String notificationApiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
			String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
			String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
			String notificationFrom = intent.getStringExtra(Constants.NOTIFICATION_FROM);
			String packetId = intent.getStringExtra(Constants.PACKET_ID);
			String msgType = intent.getStringExtra(Constants.NOTIFICATION_MSGTYPE);
			
			FileLogUtil.recordResult("title:"+notificationTitle+",msg:"+notificationMessage);
			FileLogUtil.printLog("Received Notification Msg!");
			
			Notifier notifier = new Notifier(context);
			notifier.notify(notificationId, notificationApiKey, notificationTitle, notificationMessage, notificationUri, notificationFrom,
					packetId,msgType);
		}

	}

}
