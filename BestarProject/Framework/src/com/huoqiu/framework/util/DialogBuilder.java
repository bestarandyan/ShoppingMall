package com.huoqiu.framework.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.Toast;

public abstract class DialogBuilder {
	public static void showSimpleDialog(String message, Context context) {
		showSimpleDialog(message, context, null);
	}

	public static void showSimpleDialog(String message, Context context, OnClickListener listener) {
		new AlertDialog.Builder(context).setPositiveButton("我知道了", listener).setMessage(message).setCancelable(false).show();
	}

	public static void showSimpleDialog(String message, String posMessage, Context context, OnClickListener listener) {
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton(posMessage, listener).show();
	}

	public static void showSimpleDialog(String message, String posMessage, String negMessage, Context context, OnClickListener listener) {
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton(posMessage, listener).setNegativeButton(negMessage, null).show();
	}

	private static Toast toast = null;

	/**
	 * 统一弹Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showSimpleToast(Context context, String message) {
		if (toast == null) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showCancelableToast(Context context, String message) {
		// TextView tv = new TextView(context);
		// tv.setText(message);
		// mCancelableDialog = new AlertDialog.Builder(context).setView(tv).setCancelable(true).create();
		mCancelableDialog = new AlertDialog.Builder(context).setMessage(message).setCancelable(true).create();
		mCancelableDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (mTimer != null) {
					mTimer.cancel();
				}
			}
		});
		mCancelableDialog.show();

		if (mTimer != null) {
			mTimer.start();
		}

	}

	private static Dialog mCancelableDialog = null;
	private static CountDownTimer mTimer = new CountDownTimer(5000, 1000) {
		@Override
		public void onTick(long arg0) {
		}

		@Override
		public void onFinish() {
			if (mCancelableDialog != null && mCancelableDialog.isShowing()) {
				mCancelableDialog.dismiss();
				mCancelableDialog = null;
			}
		}
	};

}
