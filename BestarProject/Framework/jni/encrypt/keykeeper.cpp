#include <string>
#include <cstring>
#include <vector>

#include "keykeeper.h"
#include "common.h"
#include "file.h"
#include "../utils.h"

jbyteArray com_huoqiu_framework_encrypt(JNIEnv* env, jclass cls, jbyteArray plainText)
{
	int len = env->GetArrayLength(plainText);
	char* tmp = new char[len + 1];
	env->GetByteArrayRegion(plainText, 0, len, (jbyte*) tmp);
	std::string tmpStr(tmp, len);
	delete[] tmp;

	char key[] = "huoqiu_not_manyi_key";
	char outbuf[1024];
	int outLength = 0;
	doEncrypt(outbuf, &outLength, (char*) tmpStr.c_str(), tmpStr.size(), key, 1);
	std::string encrypted(outbuf, outLength);

	int size = encrypted.size();
	jbyteArray encryptArray = env->NewByteArray(size);
	jbyte* result = new jbyte[size];
	char* tmpHex = new char[size * 2 + 1];
	for (int i = 0; i < size; i++)
	{
		result[i] = (char) encrypted.c_str()[i];
		sprintf(&tmpHex[i * 2], "%02x", encrypted.c_str()[i]);
	}
	//LOGE("cipher secret: %s", tmpHex);

	env->SetByteArrayRegion(encryptArray, 0, size, result);
	delete[] result;
	delete[] tmpHex;

	return encryptArray;
}

jstring com_huoqiu_framework_decrypt(JNIEnv* env, jclass cls, jbyteArray headers, jbyteArray timestamp, jbyteArray packageName)
{
	int len = env->GetArrayLength(headers);
	char* headerBytes = new char[len];
	env->GetByteArrayRegion(headers, 0, len, (jbyte*) headerBytes);
	std::string headersStr(headerBytes, len);
	delete[] headerBytes;

	len = env->GetArrayLength(timestamp);
	char* nativeTimestamp = new char[len];
	env->GetByteArrayRegion(timestamp, 0, len, (jbyte*) nativeTimestamp);
	std::string timestampStr(nativeTimestamp, len);
	delete[] nativeTimestamp;

	len = env->GetArrayLength(packageName);
	char* nativePackageName = new char[len];
	env->GetByteArrayRegion(packageName, 0, len, (jbyte*) nativePackageName);
	std::string packageNameStr(nativePackageName, len);
	delete[] nativePackageName;

	std::string decrypted = generateKey(headersStr, timestampStr, packageNameStr);
	jstring result = env->NewStringUTF(decrypted.c_str());
	return result;
}

JNIEXPORT jboolean JNICALL Java_com_huoqiu_framework_encrypt_Encrypt_saveCipher
  (JNIEnv* env, jclass cls, jbyteArray packageName, jbyteArray cipher)
{
	jboolean success = JNI_FALSE;
	int len = env->GetArrayLength(packageName);
	char* nativePackageName = new char[len];
	env->GetByteArrayRegion(packageName, 0, len, (jbyte*) nativePackageName);
	std::string packageNameStr(nativePackageName, len);
	delete[] nativePackageName;
	len = env->GetArrayLength(cipher);
	char* nativeCipher = new char[len];
	env->GetByteArrayRegion(cipher, 0, len, (jbyte*) nativeCipher);
	std::string cipherStr(nativeCipher, len);
	delete[] nativeCipher;
	bool isSuccess = saveCiper(packageNameStr, cipherStr);

	if (isSuccess) success = JNI_TRUE;

	return success;
}

JNIEXPORT jstring JNICALL Java_com_huoqiu_framework_encrypt_Encrypt_readCipher
  (JNIEnv* env, jclass cls, jbyteArray path)
{
	int len = env->GetArrayLength(path);
	char* nativePath = new char[len];
	env->GetByteArrayRegion(path, 0, len, (jbyte*) nativePath);
	std::string pathStr(nativePath);
	delete[] nativePath;
	std::string cipher = readCipher(pathStr);
	jstring result = env->NewStringUTF(cipher.c_str());

	return result;
}

