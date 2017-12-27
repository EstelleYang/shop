package com.o2o.Util.util;

import java.io.*;

/**
 * 序列化工具类
 * @author 401681
 *
 */
public class SerializeUtils {
	
	/**
	 * 序列化JAVA对象
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj){
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			oos = new ObjectOutputStream(baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(oos!=null){
				oos.writeObject(obj);
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(baos!=null){
			bytes = baos.toByteArray();
		}
		return bytes;
	}
	
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes){
		ByteArrayInputStream bais = null;
		Object obj = null;
		bais = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
}
