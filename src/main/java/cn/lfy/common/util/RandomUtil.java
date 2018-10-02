package cn.lfy.common.util;

public class RandomUtil {

	public static String random(int digit) {
		int d = (int)((Math.random()*9+1)*Math.pow(10, digit));
		return String.valueOf(d);
	}
	public static void main(String[] args) {
		System.out.println(random(8));
	}
}
