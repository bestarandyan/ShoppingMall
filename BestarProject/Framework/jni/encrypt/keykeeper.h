#include <jni.h>

#ifndef com_huoqiu_framework_encrypt_Encrypt_H
#define com_huoqiu_framework_encrypt_Encrypt_H

#ifdef __cplusplus
extern "C" {
#endif

jbyteArray com_huoqiu_framework_encrypt(JNIEnv* env, jclass cls, jbyteArray plainText);

jstring com_huoqiu_framework_decrypt(JNIEnv* env, jclass cls, jbyteArray headers, jbyteArray timestamp, jbyteArray packageName);

JNIEXPORT jboolean JNICALL Java_com_huoqiu_framework_encrypt_Encrypt_saveCipher
  (JNIEnv* env, jclass cls, jbyteArray packageName, jbyteArray cipher);

JNIEXPORT jstring JNICALL Java_com_huoqiu_framework_encrypt_Encrypt_readCipher
  (JNIEnv* env, jclass cls, jbyteArray path);

#ifdef __cplusplus
}
#endif

/*
 * JNI registration.
 */
static JNINativeMethod gMethods[] = {
    /* name, signature, funcPtr */
	{ "native_encrypt", "([B)[B", (void*)com_huoqiu_framework_encrypt},
	{ "native_decrypt", "([B[B[B)Ljava/lang/String;", (void*)com_huoqiu_framework_decrypt},
};

int register_com_huoqiu_framework_encrypt(JNIEnv* env)
{
	jclass cls = env->FindClass("com/huoqiu/framework/encrypt/Encrypt");
	if (cls == NULL) return JNI_ERR;

	int len = sizeof(gMethods) / sizeof(gMethods[0]);
	if (env->RegisterNatives(cls, gMethods, len) < 0)  return JNI_ERR;

	return JNI_OK;
}

#endif
