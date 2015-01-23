package com.huoqiu.framework.encrypt;

public class Encrypt {
	static {
        System.loadLibrary("encrypt");
    }
	
	public static native byte[] native_encrypt(byte[] plainText);
	public static native String native_decrypt(byte[] headers, byte[] timestamp, byte[] packageName);
	
	public static native String readCipher(byte[] path);
	public static native boolean saveCipher(byte[] packageName, byte[] cipher);
	
	public static byte[] encryptKey(byte[] plainText) {
		return native_encrypt(plainText);
	}
	
	public static String decryptKey(String headers, String timestamp, String packageName) {
		return native_decrypt(headers.getBytes(), timestamp.getBytes(), packageName.getBytes());
	}
	
	public static String getCipher(byte[] path) {
		return readCipher(path);
	}
	
	public static boolean syncCipher(byte[] packageName, byte[] cipher) {
		return saveCipher(packageName, cipher);
	}
}
