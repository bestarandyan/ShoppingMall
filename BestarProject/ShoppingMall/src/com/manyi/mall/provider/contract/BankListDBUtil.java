package com.manyi.mall.provider.contract;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.huoqiu.framework.app.AppConfig;

public class BankListDBUtil {

	// 获取单例数据库对象
	public synchronized static BankListDBUtil getInstance() {
		if (null == instance) {
			instance = null;
			instance = new BankListDBUtil(AppConfig.application);
		}
		return instance;
	}

	/**
	 * 获取对应Key的Value值
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList<String> getBankList(String bankName) {
		ArrayList<String> bankList = new ArrayList<String>();
		Cursor cursor;
		cursor = contentResolver.query(BankListContract.CONTENT_URI, new String[] { BankListContract.BANK_NAME }, BankListContract.BANK_NAME + " LIKE ? ",
				new String[] { "%" + bankName + "%" }, null);
		if (cursor != null && cursor.moveToFirst()) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				bankList.add(cursor.getString(cursor.getColumnIndex(BankListContract.BANK_NAME)));
			}
		}
		cursor.close();
		return bankList;
	}

	private static BankListDBUtil instance;
	private ContentResolver contentResolver;

	private BankListDBUtil(Context context) {
		contentResolver = context.getContentResolver();
	}
}
