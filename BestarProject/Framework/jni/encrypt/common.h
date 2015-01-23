#ifndef com_huoqiu_framework_encrypt_Common_H
#define com_huoqiu_framework_encrypt_Common_H

#include <string>
#include <vector>

#ifdef __cplusplus
extern "C" {
#endif

void doEncrypt(char* outbuf, int* outLength, char* inbuf, int inLength, char* key, int mode);
std::string generateKey(std::string headers, std::string& timestamp, std::string& packageName);
void printHex(std::string prefix, char* str);

#ifdef __cplusplus
}
#endif

#endif
