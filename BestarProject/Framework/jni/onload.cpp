#include <jni.h>
#include <unistd.h>

#include "utils.h"

#ifndef com_huoqiu_framework_encrypt_Encrypt_H
#define com_huoqiu_framework_encrypt_Encrypt_H

extern int register_com_huoqiu_framework_encrypt(JNIEnv*);

jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	JNIEnv *env;
	if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK){
		LOGE("Manyi encryption lib initialize failed.");
		return JNI_ERR; /* JNI version not supported */
	}

	/*
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	char buffer [16];
	pid_t threadId = gettid();
	snprintf(buffer, sizeof(buffer), "%d", threadId);
	args.name = buffer;
	args.group = NULL;
	if (jvm->AttachCurrentThread(&env, &args) != JNI_OK)
	{
		LOGE("Encryption attach thread failed.");
	    return JNI_ERR;
	}
	*/

	register_com_huoqiu_framework_encrypt(env);
	LOGI("Manyi encryption lib has been initialized.");
	return JNI_VERSION_1_6;
}

void JNICALL JNI_OnUnload(JavaVM *jvm, void *reserved)
{
}

#endif
