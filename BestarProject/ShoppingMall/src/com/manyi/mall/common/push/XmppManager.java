package com.manyi.mall.common.push;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.provider.ProviderManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.AESUtil;
import com.novell.sasl.client.FileLogUtil;

public class XmppManager {

	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private String xmppHost;

	private int xmppPort;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private ReconnectionThread reconnection;

	public XmppManager(NotificationService notificationService) {
		context = notificationService;
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_APPEND);
				
		xmppHost = CommonConfig.PUSHSERVER_IP;//sharedPrefs.getString(Constants.XMPP_HOST, "180.166.57.26");
		xmppPort = CommonConfig.PUSHSERVER_PPRT;//sharedPrefs.getInt(Constants.XMPP_PORT, 6502);
		username = sharedPrefs.getString("name", "anonymous_user"+UUID.randomUUID().toString());//Build.PRODUCT.toLowerCase();// sharedPrefs.getString(Constants.XMPP_USERNAME,

		if(!username.startsWith("anonymous_user")){
			try {
				username = AESUtil.decrypt(username, CommonConfig.AES_KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		password = username;
		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	public Context getContext() {
		return context;
	}

	public void connect() {
		submitLoginTask();
	}

	public void disconnect() {
		terminatePersistentConnection();
	}

	public void terminatePersistentConnection() {
		Runnable runnable = new Runnable() {

			final XmppManager xmppManager = XmppManager.this;

			public void run() {
				if (xmppManager.isConnected()) {
					xmppManager.getConnection().removePacketListener(xmppManager.getNotificationPacketListener());
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public PacketListener getNotificationPacketListener() {
		return notificationPacketListener;
	}

	public void startReconnectionThread() {
		synchronized (reconnection) {
			reconnection.needWork.set(true);
			if (!reconnection.isAlive()) {
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void reregisterAccount() {
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public void runTask() {
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
	}

	// private String newRandomUUID() {
	// String uuidRaw = UUID.randomUUID().toString();
	// return uuidRaw.replaceAll("-", "");
	// }

	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	private boolean isAuthenticated() {
		return connection != null && connection.isConnected() && connection.isAuthenticated();
	}

	@SuppressWarnings("unused")
	private boolean isRegistered() {
		// return sharedPrefs.contains(Constants.XMPP_USERNAME)
		// && sharedPrefs.contains(Constants.XMPP_PASSWORD);
		return true;
	}

	private void submitConnectTask() {
		addTask(new ConnectTask());
	}

	private void submitRegisterTask() {
		submitConnectTask();
		//addTask(new RegisterTask());
	}

	private void submitLoginTask() {
		submitRegisterTask();
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable) {
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
	}


	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppManager xmppManager;

		private ConnectTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {

			if (!xmppManager.isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(xmppHost, xmppPort);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				// connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);
				connConfig.setSecurityMode(SecurityMode.disabled);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);

				try {
					// Connect to the server
					connection.connect();

					// packet provider
					ProviderManager.getInstance().addIQProvider("notification", "androidpn:iq:notification", new NotificationIQProvider());
					reconnection.waiting = 0;
					reconnection.needWork.set(false);
				} catch (XMPPException e) {
					e.printStackTrace();
				}

				xmppManager.runTask();

			} else {
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to register a new user onto the server.
	 */
//	private class RegisterTask implements Runnable {
//
//		final XmppManager xmppManager;
//
//		private RegisterTask() {
//			xmppManager = XmppManager.this;
//		}
//
//		public void run() {
//
//			if (!xmppManager.isRegistered()) {
//				final String newUsername = "99999999997";
//				final String newPassword = "99999999997";
//
//				Registration registration = new Registration();
//
//				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(registration.getPacketID()), new PacketTypeFilter(IQ.class));
//
//				PacketListener packetListener = new PacketListener() {
//
//					public void processPacket(Packet packet) {
//						Log.d("RegisterTask.PacketListener", "processPacket().....");
//						Log.d("RegisterTask.PacketListener", "packet=" + packet.toXML());
//
//						if (packet instanceof IQ) {
//							IQ response = (IQ) packet;
//							if (response.getType() == IQ.Type.ERROR) {
//								if (!response.getError().toString().contains("409")) {
//								}
//							} else if (response.getType() == IQ.Type.RESULT) {
//								xmppManager.setUsername(newUsername);
//								xmppManager.setPassword(newPassword);
//
//								Editor editor = sharedPrefs.edit();
//								editor.putString(Constants.XMPP_USERNAME, newUsername);
//								editor.putString(Constants.XMPP_PASSWORD, newPassword);
//								editor.commit();
//								xmppManager.runTask();
//							}
//						}
//					}
//				};
//
//				connection.addPacketListener(packetListener, packetFilter);
//
//				registration.setType(IQ.Type.SET);
//				// registration.setTo(xmppHost);
//				// Map<String, String> attributes = new HashMap<String,
//				// String>();
//				// attributes.put("username", rUsername);
//				// attributes.put("password", rPassword);
//				// registration.setAttributes(attributes);
//				registration.addAttribute("username", newUsername);
//				registration.addAttribute("password", newPassword);
//				connection.sendPacket(registration);
//
//			} else {
//				xmppManager.runTask();
//			}
//		}
//	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		final XmppManager xmppManager;

		private LoginTask() {
			this.xmppManager = XmppManager.this;
		}

		public void run() {
			if (!xmppManager.isAuthenticated()) {
				try {
					xmppManager.getConnection().login(xmppManager.getUsername(), xmppManager.getPassword(), XMPP_RESOURCE_NAME);

					// connection listener
					if (xmppManager.getConnectionListener() != null) {
						xmppManager.getConnection().addConnectionListener(xmppManager.getConnectionListener());
					}

					// packet filter
					PacketFilter packetFilter = new PacketTypeFilter(NotificationIQ.class);
					// packet listener
					PacketListener packetListener = xmppManager.getNotificationPacketListener();
					connection.addPacketListener(packetListener, packetFilter);
					getConnection().startKeepAliveThread();
				} catch (XMPPException e) {
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						//xmppManager.reregisterAccount();
						FileLogUtil.printLog("LoginTask login 401 error!");
						return;
					}
					xmppManager.startReconnectionThread();

				} catch (Exception e) {
					xmppManager.startReconnectionThread();
				}

				xmppManager.runTask();
			} else {
				xmppManager.runTask();
			}
		}
	}

}
