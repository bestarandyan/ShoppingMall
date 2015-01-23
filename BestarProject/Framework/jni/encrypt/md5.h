#ifndef com_huoqiu_framework_encrypt_MD5_H
#define com_huoqiu_framework_encrypt_MD5_H

#include <string>

#ifdef __cplusplus
extern "C" {
#endif

typedef union uwb {
    unsigned w;
    unsigned char b[4];
} WBunion;

unsigned* md5( const char *msg, int mlen);

#ifdef __cplusplus
}
#endif

#endif
