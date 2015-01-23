package com.manyi.mall.common.push;

import java.util.concurrent.atomic.AtomicBoolean;

import com.novell.sasl.client.FileLogUtil;

public class ReconnectionThread extends Thread {

	private final XmppManager xmppManager;

	public int waiting;
	
	public AtomicBoolean needWork;

	ReconnectionThread(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
		this.waiting = 0;
		this.needWork = new AtomicBoolean();
	}

	public void run() {

		while (!isInterrupted()) {
			if (needWork.get()) {
				try {
						FileLogUtil.printLog("ReconnectionThread try to recon, reconnect times ->" + waiting + ",will wait " + waiting()
								+ " seconds!");
						Thread.sleep((long) waiting() * 1000L);
						xmppManager.connect();
						waiting++;
				} catch (final InterruptedException e) {
					FileLogUtil.printLog("ReconnectionThread reconnect fail!");
					xmppManager.getConnectionListener().reconnectionFailed(e);
				}
			} else {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private int waiting() {
		if (waiting > 20) {
			return 600;
		}
		if (waiting > 13) {
			return 300;
		}
		return waiting <= 7 ? 10 : 60;
	}
}
