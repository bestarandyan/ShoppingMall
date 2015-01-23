package com.manyi.mall.common.push;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class NotificationService extends Service {

    public static final String SERVICE_NAME = "com.manyi.fybao.push.NotificationService";

    private TelephonyManager telephonyManager;

    private BroadcastReceiver notificationReceiver;

    private BroadcastReceiver connectivityReceiver;
    
    private BroadcastReceiver userPresentReceiver;

    private PhoneStateListener phoneStateListener;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private XmppManager xmppManager;

    public NotificationService() {
        notificationReceiver = new NotificationReceiver();
        connectivityReceiver = new ConnectivityReceiver(this);
        userPresentReceiver = new UserPresentReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
        executorService = Executors.newSingleThreadExecutor();
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }

    @Override
    public void onCreate() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // Get deviceId
//        deviceId = telephonyManager.getDeviceId();
//        // Log.d(LOGTAG, "deviceId=" + deviceId);
//        Editor editor = sharedPrefs.edit();
//        editor.putString(Constants.DEVICE_ID, deviceId);
//        editor.commit();

        // If running on an emulator
//        if (deviceId == null || deviceId.trim().length() == 0
//                || deviceId.matches("0+")) {
//            if (sharedPrefs.contains("EMULATOR_DEVICE_ID")) {
//                deviceId = sharedPrefs.getString(Constants.EMULATOR_DEVICE_ID,
//                        "");
//            } else {
//                deviceId = (new StringBuilder("EMU")).append(
//                        (new Random(System.currentTimeMillis())).nextLong())
//                        .toString();
//                editor.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
//                editor.commit();
//            }
//        }
//        Log.d(LOGTAG, "deviceId=" + deviceId);

        xmppManager = new XmppManager(this);

        Constants.xmppManager = xmppManager;
        taskSubmitter.submit(new Runnable() {
            public void run() {
                NotificationService.this.start();
            }
        });
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public void onDestroy() {
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public static Intent getIntent() {
        return new Intent(SERVICE_NAME);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public TaskSubmitter getTaskSubmitter() {
        return taskSubmitter;
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }

    public XmppManager getXmppManager() {
        return xmppManager;
    }

    public void connect() {
        taskSubmitter.submit(new Runnable() {
            public void run() {
                NotificationService.this.getXmppManager().connect();
            }
        });
    }

    public void disconnect() {
        taskSubmitter.submit(new Runnable() {
            public void run() {
                NotificationService.this.getXmppManager().disconnect();
            }
        });
    }

    private void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
        registerReceiver(notificationReceiver, filter);
    }

    private void unregisterNotificationReceiver() {
        unregisterReceiver(notificationReceiver);
    }

    private void registerConnectivityReceiver() {
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    private void unregisterConnectivityReceiver() {
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);
    }
    
    private void registerUserPresentReceiver(){
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_USER_PRESENT);
    	registerReceiver(userPresentReceiver,filter);
    }
    
    private void unregisterUserPresentReceiver(){
    	unregisterReceiver(userPresentReceiver);
    }

    private void start() {
        registerNotificationReceiver();
        registerConnectivityReceiver();
        registerUserPresentReceiver();
        xmppManager.connect();
    }

    private void stop() {
        unregisterNotificationReceiver();
        unregisterConnectivityReceiver();
        unregisterUserPresentReceiver();
        xmppManager.disconnect();
        executorService.shutdown();
    }

    public class TaskSubmitter {

        final NotificationService notificationService;

        public TaskSubmitter(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        @SuppressWarnings("rawtypes")
		public Future submit(Runnable task) {
            Future result = null;
            if (!notificationService.getExecutorService().isTerminated()
                    && !notificationService.getExecutorService().isShutdown()
                    && task != null) {
                result = notificationService.getExecutorService().submit(task);
            }
            return result;
        }

    }

    public class TaskTracker {

        final NotificationService notificationService;

        public int count;

        public TaskTracker(NotificationService notificationService) {
            this.notificationService = notificationService;
            this.count = 0;
        }

        public void increase() {
            synchronized (notificationService.getTaskTracker()) {
                notificationService.getTaskTracker().count++;
            }
        }

        public void decrease() {
            synchronized (notificationService.getTaskTracker()) {
                notificationService.getTaskTracker().count--;
            }
        }
    }

}
