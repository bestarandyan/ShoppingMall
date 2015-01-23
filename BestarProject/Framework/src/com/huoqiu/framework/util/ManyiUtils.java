package com.huoqiu.framework.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.huoqiu.framework.R;

public class ManyiUtils {
	/**
	 * 关闭键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void closeKeyBoard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (((Activity) context).getCurrentFocus() != null && ((Activity) context).getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 打开键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void showKeyBoard(Context context, View view) {
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private static AlertDialog mDialog; // GPS设置提示弹框

	/**
	 * 检测GPS打开状态，若GPS关闭则弹出设置GPS弹框
	 * 
	 * @param context
	 */
	public static void checkouLocationSetting(final Context context) {
		// 判断GPS是否打开
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return;
		}
		// 防止重复的弹Dialog
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog.cancel();
		}
		// 生成Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.check_location_title); // GPS not found
		builder.setMessage(R.string.check_location_message); // Want to enable?
		builder.setPositiveButton(R.string.check_location_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		mDialog = builder.create();
		mDialog.setCancelable(false);
		mDialog.show();
	}

	/**
	 * 拨打电话
	 */
	public static void dial(Context context, String phoneNum) {
		if (phoneNum != null) {
			Uri phone = Uri.parse("tel:" + phoneNum);
			Intent intent = new Intent(Intent.ACTION_DIAL, phone);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
