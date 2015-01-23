package com.manyi.mall.common.push;

import java.util.Random;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.huoqiu.framework.app.AppConfig;
import com.manyi.mall.R;

public class Notifier {

	private static final Random random = new Random(System.currentTimeMillis());

	private Context context;

	private NotificationManager notificationManager;

	public Notifier(Context context) {
		this.context = context;
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@SuppressWarnings("deprecation")
	public void notify(String notificationId, String apiKey, String title, String message, String uri, String from, String packetId,
			String msgType) {

		// Notification
		Notification notification = new Notification();
		notification.icon = R.drawable.launcher_icon;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.when = System.currentTimeMillis();
		notification.tickerText = message;

		
		Intent intent = AppConfig.application.getPackageManager().getLaunchIntentForPackage("com.manyi.fybao");
		intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
		intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
		intent.putExtra(Constants.NOTIFICATION_TITLE, title);
		intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
		intent.putExtra(Constants.NOTIFICATION_URI, uri);
		intent.putExtra(Constants.NOTIFICATION_FROM, from);
		intent.putExtra(Constants.PACKET_ID, packetId);
		intent.putExtra(Constants.NOTIFICATION_MSGTYPE, msgType);
		intent.putExtra(Constants.TAG, "NOTIFICATION");

		PendingIntent contentIntent = PendingIntent.getActivity(context, random.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, title, message, contentIntent);
		notificationManager.notify(random.nextInt(), notification);
	}

	public static void sendReadedInfo(String packetId, String to) {
		IQ result = new IQ() {
			@Override
			public String getChildElementXML() {
				return null;
			}
		};
		result.setType(Type.RESULT);
		result.setPacketID(packetId);
		result.setTo(to);
		try {
			Constants.xmppManager.getConnection().sendPacket(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private boolean isNotificationEnabled() {
	// return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED, true);
	// }

	// private boolean isNotificationSoundEnabled() {
	// return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
	// }

	// private boolean isNotificationVibrateEnabled() {
	// return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
	// }
}
