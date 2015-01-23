package com.manyi.mall.common.push;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;

public class NotificationPacketListener implements PacketListener {

	private final XmppManager xmppManager;

	public NotificationPacketListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
	}

	@Override
	public void processPacket(Packet packet) {

		if (packet instanceof NotificationIQ) {
			NotificationIQ notification = (NotificationIQ) packet;

			if (notification.getChildElementXML().contains("androidpn:iq:notification")) {
				String notificationId = notification.getId();
				String notificationApiKey = notification.getApiKey();
				String notificationTitle = notification.getTitle();
				String notificationMessage = notification.getMessage();
				// String notificationTicker = notification.getTicker();
				String notificationUri = notification.getUri();
				String notificationFrom = notification.getFrom();
				String packetId = notification.getPacketID();
				String msgType = notification.getMsgType();

				Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
				intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
				intent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
				intent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
				intent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
				intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
				intent.putExtra(Constants.NOTIFICATION_MSGTYPE, msgType);
				intent.putExtra(Constants.NOTIFICATION_FROM, notificationFrom);
				intent.putExtra(Constants.PACKET_ID, packetId);

				IQ result = NotificationIQ.createResultIQ(notification);

				try {
					xmppManager.getConnection().sendPacket(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				xmppManager.getContext().sendBroadcast(intent);
			}
		}

	}
}
