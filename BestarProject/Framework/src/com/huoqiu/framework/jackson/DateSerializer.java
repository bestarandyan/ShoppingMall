/**
 * 
 */
package com.huoqiu.framework.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author hongci.li
 *
 */
public class DateSerializer extends JsonSerializer<Date> {

	@SuppressLint("SimpleDateFormat")
	@Override
	public void serialize(Date value, JsonGenerator gen,SerializerProvider p) throws IOException,JsonProcessingException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");//yyyy-MM-dd HH:mm:ss
		String formattedDate = formatter.format(value);
		gen.writeString(formattedDate);
	}

}
