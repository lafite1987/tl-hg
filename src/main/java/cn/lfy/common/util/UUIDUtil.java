package cn.lfy.common.util;

import java.util.UUID;

public class UUIDUtil {

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String salt() {
		return UUID.randomUUID().toString().substring(30);
	} 
	
}
