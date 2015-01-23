#ifndef com_huoqiu_framework_encrypt_file_H
#define com_huoqiu_framework_encrypt_file_H

#include <string>
#include <vector>

#ifdef __cplusplus
extern "C" {
#endif

std::string readCipher(std::string& filePath);
//please add write lock
bool saveCiper(std::string& packageName, std::string& cipher);

//rewrite implementation for ios
std::string getCipherPath(std::string& packageName);

#ifdef __cplusplus
}
#endif

#endif
