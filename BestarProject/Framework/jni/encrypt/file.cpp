#include <fstream>

#include "file.h"

char CIPHER_IN_FILE[] = "";

std::string readCipher(std::string& filePath)
{
	std::ifstream inputFile;
	std::string line;
	inputFile.open(filePath.c_str(), std::ios::in);
	if (!inputFile.good()) return "";

	while (!inputFile.eof())
	{
		inputFile >> line;
	}
	inputFile.close();
	return line;
}

bool saveCiper(std::string& packageName, std::string& cipher)
 {
	std::string path = getCipherPath(packageName);
	bool isSuccess = false;
	std::ofstream outputFile(path.c_str(), std::ios::trunc);
	if (outputFile.is_open())
	{
		if (cipher.size() == 0)
		{
			outputFile << CIPHER_IN_FILE;
		} else
		{
			outputFile << cipher;
		}

		outputFile.close();
		isSuccess = true;
	}
	return isSuccess;
}

//rewrite implementation for ios
std::string getCipherPath(std::string& packageName)
{
	std::string path = "/data/data/" + packageName + "/files/save.bin";
	return path;
}
