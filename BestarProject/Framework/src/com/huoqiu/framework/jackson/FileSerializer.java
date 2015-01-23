/**
 * 
 */
package com.huoqiu.framework.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author hongci.li
 * 
 */
public class FileSerializer extends JsonSerializer<File> {
	@Override
	public void serialize(File value, JsonGenerator gen, SerializerProvider p) throws IOException, JsonProcessingException {
		FileInputStream fis = new FileInputStream(value);
		//yteArrayOutputStream bos = new ByteArrayOutputStream();
		System.out.println("$$$$$->"+fis.available());
		gen.writeBinary(fis,-1);

//		byte[] bytes = new byte[10240];
//		int len = -1;
//		while ((len=fis.read(bytes)) != -1) {
//			//bos.write(bytes);
//			gen.writeBinary(bytes,0,len);
//		}



		fis.close();
		//os.close();
	}

}
