/**
 * 
 */
package com.manyi.mall.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.manyi.mall.common.Constants;

/**
 * @author bestar
 *
 */
public class ObjectFileOperator {
	/**
	 * 将一个对象写入文件
	 * @param object 对象类型
	 * @param filename 文件名称
	 */
	public static void saveObject(Object object,String filename){
	    ObjectOutput out = null;
	    try {
	    	File dir = new File(Constants.OBJECT_FILE_PATH+File.separator);
	    	if(!dir.exists()){
	    		dir.mkdirs();
	    	}
	        out = new ObjectOutputStream(new FileOutputStream(new File(Constants.OBJECT_FILE_PATH,"")+File.separator+filename+".txt"));
	        out.writeObject(object);
	        out.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 从文件中读取一个对象
	 * @param filename 文件名称
	 * @return
	 */
	@SuppressWarnings("resource")
	public static Object getObject(String filename){
	    ObjectInputStream input;
	    try {
	        input = new ObjectInputStream(new FileInputStream(new File(Constants.OBJECT_FILE_PATH+File.separator+filename)));
	       return input.readObject();
	    } catch (StreamCorruptedException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
