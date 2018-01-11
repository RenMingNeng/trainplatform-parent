package com.bossien.train.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class PropertiesUtils {

	private static Properties p = new Properties();
	
	private static Map<String, String> propertieMap = new HashMap<String, String>();
	
	
	/**
	 * 读取applicationContext.properties配置文件信息
	 */
	static {
		try {
			p.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("web.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key得到value的值
	 */
	public static String getValue(String key) {
		String result = propertieMap.get(key);
        if (result == null) {
            result = p.getProperty(key);
            propertieMap.put(key, result);
        }
        return result;
	}
	/**
	 *根据key值修改value
	 */
	public static void setValue(Map<String,String> map) throws Exception {
        //String path = PropertiesUtils.class.getResource("/web.properties").getPath();

          if(map != null){
			  OutputStream out = new FileOutputStream("web.properties");
			  //获取key值集合
			  Set<String> keys = map.keySet();
			  for(String key:keys){

				  p.setProperty(key,map.get(key));
				  propertieMap.put(key,map.get(key));
			  }
			  p.store(out,"update properties");

		  }

	}

	/**
	 * 读取properties所有键和值
	 * @return
	 */
	public static List<List<String>> getValues(){
		List<List<String>> properties = new ArrayList<List<String>>();
		Set<String> keys = p.stringPropertyNames();
		for (String key:keys) {
			List<String> strs = new ArrayList<String>();
			String value = p.getProperty(key);
			strs.add(key);
			strs.add(value);
			properties.add(strs);
		}
		return properties;
	}
}
