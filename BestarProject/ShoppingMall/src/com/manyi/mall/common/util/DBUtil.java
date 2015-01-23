package com.manyi.mall.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.City;
import com.manyi.mall.provider.ManyiProvider;
import com.manyi.mall.provider.contract.BankListContract;
import com.manyi.mall.provider.contract.CityContract;
import com.manyi.mall.provider.contract.SystemPropertiesContract;

public class DBUtil {
	private final static String TAG = DBUtil.class.getSimpleName();

	private final static String EXTERNAL_RAW_DATABASE_NAME = "manyi.sqlite3.tmp";

	/** system properties 表 */
	public static final String version_name = "version_name";
	public static final String version_code = "version_code";
	public static final String city_table_version = "city_table_version";
    public static final String ip = "ip";
    public static final String port = "port";


	private static DBUtil instance;

	// 获取单例数据库对象
	public synchronized static DBUtil getInstance() {
		if (null == instance) {
			instance = null;
			instance = new DBUtil();
		}
		return instance;
	}

	/**
	 * 获取城市列表
	 */
	public ArrayList<City> getCityList(Context context) {
		ArrayList<City> cityList = new ArrayList<City>();

		Cursor cursor = context.getContentResolver().query(CityContract.CONTENT_URI, null, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			City temp = new City();
			int cityID = cursor.getColumnIndex(CityContract.CITY_LIST_CITY_ID);
			int cityName = cursor.getColumnIndex(CityContract.CITY_LIST_CITY_NAME);
			int code = cursor.getColumnIndex(CityContract.CITY_LIST_CODE);
			int parentID = cursor.getColumnIndex(CityContract.CITY_LIST_PARENT_ID);
			int path = cursor.getColumnIndex(CityContract.CITY_LIST_PATH);
			int remark = cursor.getColumnIndex(CityContract.CITY_LIST_REMARK);
			int status = cursor.getColumnIndex(CityContract.CITY_LIST_STATUS);
			int serialCode = cursor.getColumnIndex(CityContract.CITY_LIST_SERIAL_CODE);
			int userId = cursor.getColumnIndex(CityContract.CITY_LIST_USER_ID);
			temp.setAreaId(StringUtil.toInt((cursor.getString(cityID))));
			temp.setName(cursor.getString(cityName));
			temp.setCode(StringUtil.toInt((cursor.getString(code))));
			temp.setParentId(StringUtil.toInt((cursor.getString(parentID))));
			temp.setPath(cursor.getString(path));
			temp.setRemark(cursor.getString(remark));
			temp.setStatus(StringUtil.toInt(cursor.getString(status)));
			temp.setSerialCode(cursor.getString(serialCode));
			temp.setUserId(StringUtil.toInt(cursor.getString(userId)));
			cityList.add(temp);
		}
		cursor.close();

		return cityList;
	}

	/**
	 * 更新 metrostation中的数据 地铁站 biaowang
	 * 
	 * @see #TABLE_METRO
	 * @param itemList
	 */
	public void updateCityTable(Context context, final String versionID, final List<City> itemList) {
		if (itemList.size() == 0)
			return;

		//context.getContentResolver().delete(CityContract.CONTENT_URI, null, null);
		ContentValues contentValues = new ContentValues();
		for (City city : itemList) {
			contentValues = new ContentValues();
			contentValues.put(CityContract.CITY_LIST_CITY_ID, city.getAreaId() + "");// 城市ID
			contentValues.put(CityContract.CITY_LIST_CITY_NAME, city.getName());// 城市名称
			contentValues.put(CityContract.CITY_LIST_CODE, city.getCode() + "");// 城市ID
			// contentValues.put(CITY_LIST_create_time, city.getCreateTime().toString());// 城市ID
			contentValues.put(CityContract.CITY_LIST_PATH, city.getPath());// 城市ID
			contentValues.put(CityContract.CITY_LIST_REMARK, city.getRemark());// 城市ID
			contentValues.put(CityContract.CITY_LIST_PARENT_ID, city.getParentId() + "");// 城市ID
			contentValues.put(CityContract.CITY_LIST_STATUS, city.getStatus() + "");
			contentValues.put(CityContract.CITY_LIST_SERIAL_CODE, city.getSerialCode());
			contentValues.put(CityContract.CITY_LIST_USER_ID, city.getUserId() + "");
			context.getContentResolver().insert(CityContract.CONTENT_URI, contentValues);
		}

		// update version
		contentValues = new ContentValues();
		contentValues.put(SystemPropertiesContract.VALUE, versionID);
		context.getContentResolver().update(SystemPropertiesContract.CONTENT_URI, contentValues, SystemPropertiesContract.NAME + "='city_table_version'", null);
	}

	/**
	 * 查询用户设置
	 * 
	 * @param  context 查询的选项
	 * @param name 所查选项的值
	 */
	public String getUserSetting(Context context, String name) {
		String optionValue = "";
		Cursor cursor = context.getContentResolver().query(SystemPropertiesContract.CONTENT_URI, new String[] { SystemPropertiesContract.VALUE },
				SystemPropertiesContract.NAME + "=?", new String[] { name }, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			optionValue = cursor.getString(cursor.getColumnIndex(SystemPropertiesContract.VALUE));
		}
		cursor.close();

		return optionValue;
	}

	/**
	 * 设置UserSetting
	 * 
	 * @param context
	 * @param option
	 * @param optionValue
	 */
	public void setUserSetting(Context context, final String option, final String optionValue) {
		Cursor cursor = context.getContentResolver().query(SystemPropertiesContract.CONTENT_URI, null, SystemPropertiesContract.NAME + "=?",
				new String[] { option }, null);

		ContentValues values = new ContentValues();
		values.put(SystemPropertiesContract.NAME, option);
		values.put(SystemPropertiesContract.VALUE, optionValue);
		if (cursor.getCount() == 0) {
			context.getContentResolver().insert(SystemPropertiesContract.CONTENT_URI, values);
		} else {
			context.getContentResolver().update(SystemPropertiesContract.CONTENT_URI, values, SystemPropertiesContract.NAME + "=?", new String[] { option });
		}
		cursor.close();
	}

	public static boolean isNewVersion(Context context) {
		boolean newVersion = false;
		String currentVersion = DBUtil.getInstance().getUserSetting(context, DBUtil.version_name);
		try {
			String newVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			newVersion = (currentVersion == null || !currentVersion.equals(newVersionName));
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}

		return newVersion;
	}

	public static void loadDataFromRawDatabase(Context context) {
		try {
			if (!isNewVersion(context)) {
				return;
			}

			context.getContentResolver().delete(SystemPropertiesContract.CONTENT_URI, null, null);
			context.getContentResolver().delete(CityContract.CONTENT_URI, null, null);
			context.getContentResolver().delete(BankListContract.CONTENT_URI, null, null);

			InputStream input = context.getResources().openRawResource(R.raw.manyi);
			OutputStream output = null;
			File ourDir = context.getFilesDir();
			String tmpDatabaseFullPath = ourDir.getAbsolutePath() + File.separator + EXTERNAL_RAW_DATABASE_NAME;
			output = new FileOutputStream(tmpDatabaseFullPath);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			input.close();
			output.close();

			// copy data
			SQLiteDatabase db = SQLiteDatabase.openDatabase(tmpDatabaseFullPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			// SystemProperties table
			Cursor cursor = db.query(SystemPropertiesContract.TABLE_NAME, null, null, null, null, null, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				values.put(SystemPropertiesContract.NAME, cursor.getString(cursor.getColumnIndex(SystemPropertiesContract.NAME)));
				values.put(SystemPropertiesContract.VALUE, cursor.getString(cursor.getColumnIndex(SystemPropertiesContract.VALUE)));
				context.getContentResolver().insert(SystemPropertiesContract.CONTENT_URI, values);
			}
			cursor.close();

			// CityTable
			cursor = db.query(CityContract.TABLE_NAME, null, null, null, null, null, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				values.put(CityContract.CITY_LIST_CITY_NAME, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_CITY_NAME)));
				values.put(CityContract.CITY_LIST_CITY_ID, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_CITY_ID)));
				values.put(CityContract.CITY_LIST_CODE, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_CODE)));
				values.put(CityContract.CITY_LIST_CREATE_TIME, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_CREATE_TIME)));
				values.put(CityContract.CITY_LIST_PARENT_ID, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_PARENT_ID)));
				values.put(CityContract.CITY_LIST_PATH, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_PATH)));
				values.put(CityContract.CITY_LIST_REMARK, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_REMARK)));
				values.put(CityContract.CITY_LIST_SERIAL_CODE, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_SERIAL_CODE)));
				values.put(CityContract.CITY_LIST_STATUS, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_STATUS)));
				values.put(CityContract.CITY_LIST_USER_ID, cursor.getString(cursor.getColumnIndex(CityContract.CITY_LIST_REMARK)));
				context.getContentResolver().insert(CityContract.CONTENT_URI, values);
			}
			cursor.close();

			// CityTable
			cursor = db.query(BankListContract.TABLE_NAME, null, null, null, null, null, null);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				values.put(BankListContract.BANK_NAME, cursor.getString(cursor.getColumnIndex(BankListContract.BANK_NAME)));
				context.getContentResolver().insert(BankListContract.CONTENT_URI, values);
			}
			cursor.close();

			db.close();

			File tmpFile = new File(tmpDatabaseFullPath);
			tmpFile.delete();

			if (Configuration.DEFAULT == Configuration.TEST) {
				copyFile(context, null, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFile(Context context, String inputFilePath, String outFilePath) {
		try {
			if (inputFilePath == null || outFilePath == null) {
				inputFilePath = context.getDatabasePath(ManyiProvider.DATABASE_NAME).getAbsolutePath();
				outFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ManyiProvider.DATABASE_NAME;
			}
			File outFile = new File(outFilePath);
			if (outFile.exists())
				outFile.delete();

			InputStream input = new FileInputStream(inputFilePath);
			OutputStream output = new FileOutputStream(outFilePath);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			input.close();
			output.close();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
}
