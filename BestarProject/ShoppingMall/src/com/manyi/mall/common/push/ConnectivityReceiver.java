package com.manyi.mall.common.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.novell.sasl.client.FileLogUtil;

public class ConnectivityReceiver extends BroadcastReceiver {

	private NotificationService notificationService;

	public ConnectivityReceiver(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			if (networkInfo.isConnected()) {
				notificationService.connect();
                FileLogUtil.printLog("ConnectivityReceiver  connect");
			}
		} else {
			notificationService.disconnect();
            FileLogUtil.printLog("ConnectivityReceiver  disconnect");
		}
	}

}
