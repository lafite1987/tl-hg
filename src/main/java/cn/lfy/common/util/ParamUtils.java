package cn.lfy.common.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ParamUtils.class.getName());
	
	public static String getStrByList(List<Object> list, int index){
		Iterator<Object> iterator = list.iterator();
		String value = "";
		while(iterator.hasNext()){
			Object[] object =  (Object[]) iterator.next();
			if(object[index] != null && !object[index].equals("") && !object[index].equals("null")){
				value += ","+object[index];
			}
		}
		if(value.startsWith(","))
			value = value.replaceFirst(",", "");
		return value;
	}
	
	public static String getStrUTF8(String str){
		try {
			str = !str.equals("") ? new String(str.getBytes("ISO-8859-1")) : "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static Map<String, String> wrapMap(Object...obj){
		if(obj.length%2 > 0)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < obj.length; i += 2) {
			map.put(String.valueOf(obj[i]), String.valueOf(obj[i+1]));
		}
		return map;
	}
	
	/**
	 * 将value为数组类型的map转换成普通Map
	 * @param map	Map<String, String[]>
	 * @return		Map<String, Object>
	 */
	public static Map<String, Object> conversionToGeneralMap(Map<String, String[]> map){
		Map<String, Object> generalMap = new HashMap<String, Object>();
		String key = "";
		String value = "";
		for(Entry<String, String[]> entry : map.entrySet()){
			key = ""; value = "";
			key = entry.getKey();
			Object object = entry.getValue();
			if(object == null){
				value = "";
			}else if(object instanceof String[]){
				String[] array = (String[])object;
				for (int i = 0; i < array.length; i++) {
					if(i == 0) {
						value += array[i];
					} else {
						value += "," + array[i];
					}
				}
			}else{
				value = object.toString();
			}
			generalMap.put(key, value);
		}
		return generalMap;
	}
	
	public static Map<String, Object> getParameter(HttpServletRequest req){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(req != null){
			Map<String, String[]> paraMap = req.getParameterMap();
			if(paraMap != null) {
				resultMap = conversionToGeneralMap(paraMap);
			}
		}
		return resultMap;
	}
	
	/**
	 * 将指定字符串分割成二维数组
	 * @param str		要分割的字符
	 * @param oneSeg	第一维分割符
	 * @param twoSeg	第二维分割符
	 * @return			String [][]
	 */
	public static String[][] parseStrToArray(String str, String oneSeg, String twoSeg){
		if(null == str || "".equals(str))
			return null;
		String[] oneArray = str.split(oneSeg);
		String[][] twoArray = new String[oneArray.length][8];
		
		for (int i = 0; i < oneArray.length ; i++) {
			
			String[] tempArray = oneArray[i].split(twoSeg);
			
			for (int j = 0; j < tempArray.length; j++) {
				
				twoArray[i][j] = tempArray[j];
			}
		}
		return twoArray;
	}
	
	public static long getCompareDate(String startDate) throws ParseException {
	     SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
	     Date date1=formatter.parse(startDate);    
	     long l = new Date().getTime() - date1.getTime();
	     long d = l/(24*60*60*1000);
	     return d;
	 }
	
	/**
	 * 随机生成指定长度的字符
	 * @param n		随机数长度
	 * @return		String
	 */
	public static String getRandomString(int n) {
		String[] s = {"a","b","c","d","e","f","g","h","i","j","" +
				"k","l","m","n","o","p","q","r","s","t","u","v","w","" +
						"x","y","z","0","1","2","3","4","5","6","7","8","9"};
		String rs = "";
		int i = 0;
		Random r = new Random();
		while (i < n) {
			int j = r.nextInt(s.length);
			rs += s[j];
			i++;
		}
		return rs;
	}
	
	
	/**
     * Class<T> beanClass可以接受任何类型的javaBean,使用泛型调用者不用进行强转
     * @param <T>
     * @param request
     * @param beanClass
     * @return
     */
    public static <T> T request2Bean(HttpServletRequest request, Class<T> beanClass) {
        try {
            T bean = beanClass.newInstance();
			Map<String, String[]> map = request.getParameterMap();
            BeanUtils.populate(bean, map);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T toBean(Map<String, String> map, Class<T> cls) {
    	try {
    		T bean = cls.newInstance();
    		BeanUtils.populate(bean, map);
    		return bean;
    	} catch (Exception e) {
    		LOG.error("toBean error map: {}, Class: {}", map, cls);
    		e.printStackTrace();
    	}
    	return null;
    }
    
	public static Map<String, Object> toMap(Object bean) {
    	try {
    		@SuppressWarnings("unchecked")
			Map<String, Object> map = PropertyUtils.describe(bean);
    		return map;
    	} catch (Exception e) {
    		LOG.error("toBean error bean: {}", bean);
    		e.printStackTrace();
    	}
    	return null;
    }
	
	/**
     * 将Map数据组装成URL参数串
     * 如：accountId=12221222211123amount=100.00callbackInfo=custominfo=xxxxx#user=xxxxcpOrderId=1234567
     * 
     * @param params 		参数Map
     * @param notIn 		拼接的参数列表
     * @param qStringSplit	key-value之间的分隔符,当qStringSplit=& 返回结果
     * 如：accountId=12221222211123&amount=100.00&callbackInfo=&custominfo=xxxxx#&user=xxxx&cpOrderId=1234567
     * @return URL参数串
     */
    public static String getParameterText(Map<String, Object> params, String[] notIn, String qStringSplit) {
        if (null == params) {
            return null;
        }
        
        StringBuilder content = new StringBuilder(200);

        // 按照key排序
        List<String> notInList = null;
        if (null != notIn) {
            notInList = Arrays.asList(notIn);
        }
        
        int index = 1;
        for (Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();

            if (notIn != null && notInList.contains(key))
                continue;
            
            String value = params.get(key) == null ? "" : params.get(key).toString();
            if (index == 1) {//拼接时，不包括第一个&字符
                content.append(key).append("=").append(value);
            } else {
            	content.append(qStringSplit).append(key).append("=").append(value);
            }
            index++;
		}
        String result = content.toString();
        return result;
    }
    
    public static String getParameterTextTwo(Map<String, String> params, String[] notIn, String qStringSplit) {
        if (null == params) {
            return null;
        }
        
        StringBuilder content = new StringBuilder(200);

        // 按照key排序
        List<String> notInList = null;
        if (null != notIn) {
            notInList = Arrays.asList(notIn);
        }
        
        int index = 1;
        for (Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();

            if (notIn != null && notInList.contains(key))
                continue;
            
            String value = params.get(key) == null ? "" : params.get(key).toString();
            if (index == 1) {//拼接时，不包括第一个&字符
                content.append(key).append("=").append(value);
            } else {
            	content.append(qStringSplit).append(key).append("=").append(value);
            }
            index++;
		}
        String result = content.toString();
        return result;
    }
    
}
