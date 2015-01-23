package com.huoqiu.framework.util;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

public class DataUtil {
	/*
	 * 计算剩余时间
	 */

	@SuppressLint("UseValueOf")
	public static int getTimeSubtract(Date date1, Date date2) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		long l = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		int hours = new Long(l / (1000 * 60 * 60 * 24 * 24)).intValue();

		return hours;

	}

	public static String toFullHex(byte data) {
		int val = ((int) data) & 0xff;

		if (val < 16)
			return "0" + Integer.toHexString(val);
		else
			return Integer.toHexString(val);
	}

	public static String toHex(int value, int bit) {
		String temp = Integer.toHexString(value);
		for (int i = temp.length(); i < bit; i++) {
			temp = "0" + temp;
		}
		return temp;
	}

	public static String toString(Serializable[] sers, String... separator) {
		String result = "";
		String _separator = separator.length > 0 ? separator[0] : ",";
		if (sers != null && sers.length > 0) {
			for (Serializable ser : sers) {
				if (ser != null)
					result = result + ser + _separator;
			}
			result = result.substring(0, result.length() - 1);
		}

		return result;
	}

	public static void main(String args[]) {
		Float[] data = new Float[] { 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f };
		System.out.println(toString(data));
	}

	/**
	 * URL-encodes a string.
	 */
	public static String escape(String s, String... enc) {
		if (s == null)
			return null;

		try {
			return URLEncoder.encode(s, enc.length > 0 ? enc[0] : "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * URL-decodes a string.
	 */
	public static String unescape(String s, String... enc) {
		if (s == null)
			return null;

		try {
			return URLDecoder.decode(s, enc.length > 0 ? enc[0] : "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String, String> splitQueryString(String queryString) {
		Map<String, String> result = new HashMap<String, String>(0);
		String[] keyValuePairs = queryString.split("\\&");
		for (String keyValuePair : keyValuePairs) {
			String[] keyValue = keyValuePair.split("=");
			result.put(keyValue[0], keyValue[1]);
		}
		return result;
	}
}
