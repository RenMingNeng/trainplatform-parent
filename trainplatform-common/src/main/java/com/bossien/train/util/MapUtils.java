package com.bossien.train.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class MapUtils {
	
	private static final Logger LOGGER = LogManager.getLogger(MapUtils.class);
	
	private static void putAll(Map<String,Object> map, Object obj, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if ("serialVersionUID".equals(fieldName)) {
				continue;
			}
			String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getMethod = null;
			try {
				getMethod = clazz.getDeclaredMethod(getMethodName, new Class<?>[]{});
				if (!getMethod.isAccessible()) {
					getMethod.setAccessible(true);
				}
				Object value = getMethod.invoke(obj, new Object[]{});
				if (value != null) {
					map.put(fieldName, value);
				}
			} catch(NoSuchMethodException ex) {
				LOGGER.error("NoSuchMethodException:" + ex.getMessage());
				continue;
			} catch(InvocationTargetException ex) {
				LOGGER.error("InvocationTargetException:" + ex.getMessage());
				continue;
			} catch(IllegalAccessException ex) {
				LOGGER.error("IllegalAccessException:" + ex.getMessage());
				continue;
			}
		}
		
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			putAll(map, obj, superClass);
		}
	}
	
	public static void putAll(Map<String,Object> map, Object bean) {
		if (bean != null) {
			Class<?> clazz = bean.getClass();
			putAll(map, bean, clazz);
		}
	}
	
	public static <K,V> Map<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	
	public static <K,V> Map<K,V> newLinkedHashMap() {
		return new LinkedHashMap<K,V>();
	}
	
	public static <K,V> Map<K,V> newTreeMap() {
		return new TreeMap<K,V>();
	}
	
	public static <K,V> Map<K,V> newHashtable() {
		return new Hashtable<K,V>();
	}
	
	public static <K,V> Map<K,V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K,V>();
	}

}
