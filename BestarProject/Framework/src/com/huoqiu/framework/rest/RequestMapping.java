/**
 * 
 */
package com.huoqiu.framework.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;


/**
 * @author leo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface RequestMapping {
	
	Class<? extends HttpMessageConverter<Object>>[] converters() default { Jackson2HttpMessageConverter.class };

	Class<? extends ClientHttpRequestInterceptor>[] interceptors() default {AppAuthInterceptor.class};
	
	HttpMethod method() default HttpMethod.POST;
	
	String accept() default MediaType.APPLICATION_JSON_VALUE;
	
	String value();//url
}
