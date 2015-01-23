/**
 * 
 */
package com.huoqiu.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author leo
 *
 */
public class F {
	private F() {
		// TODO Auto-generated constructor stub
	}
	
	public static <T> T findFirstViewByClass(ViewGroup container,Class<T> clazz){
		for(int i=0;i<container.getChildCount();i++){
			View child = container.getChildAt(i);
			try{
				return clazz.cast(child);
			}catch(ClassCastException e){
				if(child instanceof ViewGroup){
					T t = findFirstViewByClass((ViewGroup)child, clazz);
					if(t!=null)
						return t;
				}
			}
		}
		return null;
	}
	
	public static <T> List<T> findViewByClass(ViewGroup container,Class<T> clazz){
		List<T> result = new ArrayList<T>(1);
		for(int i=0;i<container.getChildCount();i++){
			View child = container.getChildAt(i);
			try{
				result.add(clazz.cast(child));
			}catch(ClassCastException e){
				if(child instanceof ViewGroup){
					result.addAll(findViewByClass((ViewGroup)child, clazz));
				}
			}
		}
		return result;
	}
	
    public static boolean pointInView(View view,float localX, float localY) {
        return localX >= 0 && localX < (view.getRight() - view.getLeft())
                && localY >= 0 && localY < (view.getBottom() - view.getTop());
    }
	
	public static <T> T getDeclaredField(String name, Object o,Class<?> targetClazz, Class<T> clazz){
		try {
			Field temp = targetClazz.getDeclaredField(name);
			temp.setAccessible(true);
			return clazz.cast(temp.get(o));
		} catch (Exception e) {
			return null;
		}
	}

}
