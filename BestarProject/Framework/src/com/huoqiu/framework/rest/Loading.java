/**
 * 
 */
package com.huoqiu.framework.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.Window;

/**
 * @author leo
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loading {
	int container() default Window.ID_ANDROID_CONTENT;// loading的父容器

	int errorLayout() default 0;

	int retryButton() default 0;

	int callButton() default 0;
	
	int layout() default 0;// loading layout

	int value() default 0;//loading id
}
