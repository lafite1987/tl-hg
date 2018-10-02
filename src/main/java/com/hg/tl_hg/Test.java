package com.hg.tl_hg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 36进制由0-9，a-z，共36个字符表示，最小为'0'
'0'~'9'对应十进制的0~9，'a'~'z'对应十进制的10~35
例如：'1b' 换算成10进制等于 1 * 36^1 + 11 * 36^0 = 36 + 11 = 47
要求按照加法规则计算出任意两个36进制正整数的和
如：按照加法规则，计算'1b' + '2x' = '48'
要求：
不允许把36进制数字整体转为10进制数字，计算出10进制累加结果再转回为36进制
 * @author honeyleo
 *
 */
public class Test {
    private Map<Character, Integer> cache = new HashMap<Character, Integer>();
    private Map<Integer, Character> cache2 = new HashMap<Integer, Character>();
    public Test() {
        cache.put('0', 0);
        cache.put('1', 1);
        cache.put('2', 2);
        cache.put('3', 3);
        cache.put('b', 11);
        cache.put('x', 33);
        cache.put('y', 34);
        
        cache2.put(0, '0');
        cache2.put(2, '2');
        cache2.put(1, '1');
        cache2.put(4, '4');
        cache2.put(8, '8');
        cache2.put(9, '9');
        cache2.put(11, 'b');
        cache2.put(33, 'x');
    }
    public String parse(String a1, String a2) {
        char[] a1Arr = a1.toCharArray();
        char[] a2Arr = a2.toCharArray();
        int dist = 0;
        int length = a1.length() > a2.length() ? a1.length() : a2.length();
        List<Integer> sum = new ArrayList<Integer>();
        for(int i = length - 1; i >= 0; i--) {
            int b1 = 0, b2 = 0;
            if(i < a1.length()) {
                b1 = cache.get(a1Arr[i]);
            }
            if(i < a2.length()) {
                b2 = cache.get(a2Arr[i]);
            }
            int value = b1 + b2 + dist;
            dist = value/36;
            int mod = value%36;
            sum.add(mod);
        }
        String exp = "";
        for(int i = 0; i < sum.size(); i++) {
            int x = sum.get(i);
            exp = cache2.get(x) + exp;
        }
        return exp;
    }
    public static void main(String[] args) {
    	Test test = new Test();
    	String exp = test.parse("1b", "2x");
    	System.out.println(exp);
	}
}
