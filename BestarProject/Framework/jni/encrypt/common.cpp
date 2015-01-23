#include <cstring>
#include <sstream>
#include <stdlib.h>

//#define HUOQIU_ANDROID_NDK
/*
 Debug for Android
*/
#ifdef HUOQIU_ANDROID_NDK
#include <android/log.h>
#define TAG "Encrypt"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO , TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN , TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR , TAG, __VA_ARGS__)
#endif

#include "common.h"
//#include "md5.h"
#include <openssl/evp.h>

char ROUND1_KEY[] = "huoqiu_not_manyi_key";
char ROUND2_KEY[] = "huoqiu_not_manyi_key_";

const std::string ANDROID_LOVEHOUSE_PACKAGE  = "com.manyi.lovehouse";
const std::string ANDROID_FYB_PACKAGE        = "com.manyi.fybao";
const std::string ANDROID_IWAGENT_PACKAGE    = "com.manyi.iagent";
const std::string ANDROID_IWHELPER_PACKAGE   = "com.manyi.iwhelper";
const std::string ANDROID_SALEAGENT_PACKAGE   = "com.manyi.saleagent";
const std::string IOS_LOVEHOUSE_PACKAGE      = "com.superjia.iHouse";
const std::string IOS_FYB_PACKAGE            = "com.fyb365.Fybao";

char ANDROID_LOVEHOUSE_CIPHER[] = "10daa7cae6e5ca4c948c6f6fbcf94aad8a57a57e8a3fdc9b28a46196a5c47694cb022e2d0a8b807f63cf307ad9e016d187f5d0d602ed9eefee767bcbe209b2cfcee781b0b19824566828a279cdcd947272f377f0ebd488968fb62ed2b0d6e308dfe4c79f825f7227";
char ANDROID_FYB_CIPHER[]       = "772ee8898ace4d08f6b7f47bf9fc0a2fadb52e5143a5e7dbe38c70b24e17ba2dce3e5cf8271c38434845350a960f7dc556547030dbf4db752fb58a00a8747c455e2b15de588ef1195fe424a09b50fec05ddce20b20654b98ce9f7c107128da961cd80bfba985a968";
char ANDROID_IWAGENT_CIPHER[]   = "0531995c04abc4a09accf09c6383dc1638d408bbc6e66e5fdd08f69d5c5d52e15c48de2084efa096ca30fbce9560ee79ad61de0d299beab9d8d7fc9a0ffe2f4b8c45f1aca7a9a2fb04965b32e0b276161601b39d829f90e6495b20900482f79b887bc9232c8c949b";
char ANDROID_IWHELPER_CIPHER[]  = "e260518f327dc657f90d8554ab075160b9e63a46dfdc66bdfc31d1d08c5c18583223e44c00aeb54a70bb4b0f7ddeed4c23fe41bc1ecced69fbe984c5988184b5a3621c2c0d92954e55d38760b432bf440fea73e8760732013ef48d968e902770423f7837eb668cca";
char ANDROID_SALEAGENT_CIPHER[] = "6e2457c7890226787262c9c16f346a514eb31f0a2e91d52a335c27e527fe79417f0fef73de7692028592599063313cbf1b461db7261089fc0b26d6114b02d0dd9018adc0cdf804e600f4a0a10ef67d592d420cf4600c73ef2b059260df90c1486704d2f60403582d";
char IOS_LOVEHOUSE_CIPHER[]     = "18acccb253404a7e1745a9c9543ca0d8a4e4775c75cc998193a13c5506d7deba7834c5759501695b160b1bf169a4970183381f3853be8492715449721ac17a45d28c94efbd1c2ba698611a374cd3cdd8e495b1764d5a5841ee16fb180985d552a8679c190d4e5369";
char IOS_FYB_CIPHER[]           = "0bf60935fc3a1a2e3d916728ce185cbb42e8dd71667ecf555c355c2552dfea938e11e1fb5620cf37d7a3152549aaab27fa0e557007521816e492fc732add97284f080909ba86610a6191057f631f36e1820a134965a9d94e298ca788edd999460335b476bc9dce8b";
char DEFAULT_CIPHER[]           = "a4ed0f689079861fc41d6903e188e2e19f66af7c16a49e5b46155356051353d6fee2d8857735218b0243158dcd735146c110ba6118cd65fa340647a4f747a7c6ba307d17d68f4f19541b6328954b91561bfa5d041fa1036b3671db2fa5e82ba00f8cc35d767f92ee";

struct CipherSection
{
	int sectionSize;
	std::string cipher;
};

char* stringToHexArray(std::string& numbers)
{
	std::vector<std::string> hexNumbers;

	for (int i = 0; i < numbers.size() - 1; i += 2)
	{
		hexNumbers.push_back(numbers.substr(i, 2));
	}
	char* result = new char[hexNumbers.size()];
	for (int i = 0; i < hexNumbers.size(); ++i)
	{
		result[i] = strtol(hexNumbers.at(i).c_str(), NULL, 16);
	}

	return result;
}

void parseCipher(std::vector<CipherSection>& sections, std::string& packageName)
{
	std::string content(DEFAULT_CIPHER);
	if (packageName == ANDROID_LOVEHOUSE_PACKAGE)
	{
		content = ANDROID_LOVEHOUSE_CIPHER;
	} else if (packageName == ANDROID_FYB_PACKAGE)
	{
		content = ANDROID_FYB_CIPHER;
	}  else if (packageName == ANDROID_IWAGENT_PACKAGE)
	{
		content = ANDROID_IWAGENT_CIPHER;
	} else if(packageName == ANDROID_IWHELPER_PACKAGE)
	{
      	content = ANDROID_IWHELPER_CIPHER;
    }  else if(packageName == ANDROID_SALEAGENT_PACKAGE)
	{
      	content = ANDROID_SALEAGENT_CIPHER;
    } else if (packageName == IOS_LOVEHOUSE_PACKAGE)
	{
		content = IOS_LOVEHOUSE_CIPHER;
	} else if (packageName == IOS_FYB_PACKAGE)
	{
		content = IOS_FYB_CIPHER;
	}

	char outbuf[256];
	int outLength = 0;
	char* tmp = stringToHexArray(content);
	doEncrypt(outbuf, &outLength,tmp, content.size()/2, ROUND2_KEY, 0);
	std::string line(outbuf, outLength);
	delete[] tmp;

	int cipherLen1, cipherLen2, cipherLen3 = 0;
	CipherSection firstSection, secondSection, thirdSection;
	cipherLen1 = strtol(line.substr(0, 2).c_str(), NULL, 16);
	firstSection.sectionSize = cipherLen1;
	cipherLen2 = strtol(line.substr(2, 2).c_str(), NULL, 16);
	secondSection.sectionSize = cipherLen2;
	cipherLen3 = strtol(line.substr(4, 2).c_str(), NULL, 16);
	thirdSection.sectionSize = cipherLen3;
	firstSection.cipher = line.substr(6, cipherLen1);
	secondSection.cipher = line.substr(6 + cipherLen1, cipherLen2);
	thirdSection.cipher = line.substr(6 + cipherLen1 + cipherLen2, cipherLen3);

	sections.push_back(firstSection);
	sections.push_back(secondSection);
	sections.push_back(thirdSection);
}

/* mode=1, encrypt; mode=0, decrypt */
void doEncrypt(char* outbuf, int* outLength, char* inbuf, int inLength, char* key, int mode)
{
	int outlen, tmplen;
	EVP_CIPHER_CTX ctx;
	EVP_CIPHER_CTX_init (&ctx);
	EVP_CipherInit_ex(&ctx, EVP_bf_ecb(), NULL, NULL, NULL, mode);
	EVP_CIPHER_CTX_set_padding(&ctx, 1);
	EVP_CIPHER_CTX_set_key_length(&ctx, strlen((char*) key));
	/* We finished modifying parameters so now we can set key and IV */
	EVP_CipherInit_ex(&ctx, NULL, NULL, (unsigned char*)key, NULL, mode);

	if (!EVP_CipherUpdate(&ctx, (unsigned char*)outbuf, &outlen, (unsigned char*)inbuf, inLength))
	{
		/* Error */
		*outLength = 0;
	}
	/* Buffer passed to EVP_EncryptFinal() must be after data just
	 * encrypted to avoid overwriting it.
	 */
	if (!EVP_CipherFinal_ex(&ctx, (unsigned char*)outbuf + outlen, &tmplen))
	{
		/* Error */
		*outLength = 0;
	}
	outlen += tmplen;
	*outLength = outlen;
	EVP_CIPHER_CTX_cleanup(&ctx);
}

std::string getMD5(std::string& str)
{
	unsigned char buf[32];
	unsigned int len = 0;
	EVP_MD_CTX* ctx = EVP_MD_CTX_create();
	EVP_MD_CTX_init(ctx);
	EVP_DigestInit_ex(ctx, EVP_md5(), NULL);
	EVP_DigestUpdate(ctx, str.c_str(), str.size());
	EVP_DigestFinal_ex(ctx, buf, &len);
	EVP_MD_CTX_destroy(ctx);

	char hex[128];
	for (int i = 0; i < len; i++)
	{
		sprintf(&hex[i * 2], "%02x", buf[i]);
	}

	std::string result(hex, len * 2);
	return result;
}

std::string generateKey(std::string headers, std::string& timestamp, std::string& packageName)
{
	std::string secret;

	std::vector<CipherSection> sections;
	parseCipher(sections, packageName);

	char outbuf[256];
	int length;
	std::string tmp;
	for (int i = 0; i < sections.size(); ++i)
	{
		tmp = sections.at(i).cipher;
		char* inBuf = stringToHexArray(tmp);
		doEncrypt(outbuf, &length, inBuf, tmp.size() / 2, ROUND1_KEY, 0);
		delete[] inBuf;
		std::string plainCipher(outbuf, length);
		secret += plainCipher;
	}

	std::stringstream ss;
	ss << headers << "&" << secret << "&" << timestamp;
	//LOGE("JNI secret: %s", ss.str().c_str());

	std::string result = ss.str();
	std::string md5 = getMD5(result);
	return md5;
}

#ifdef HUOQIU_ANDROID_NDK
void printHex(std::string prefix, char* str)
{
	/****************delete when completing debug *****************/
	int size = strlen(str);
	char* hex = new char[size * 2 + 1];
	for (int i = 0; i < size; i++)
	{
		sprintf(&hex[i * 2], "%02x", str[i]);
	}

	LOGE("%s: %s", prefix.c_str(), hex);
	delete[] hex;
	/****************************************************************/
}
#endif
