package com.bossien.train.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author mo.xf
 *
 */
public class JsonUtils {

	static ObjectMapper mapper = null;
	public static Logger logger = Logger.getLogger(JsonUtils.class);

	private static Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

	static {
		mapper = new ObjectMapper();
		// 为 null 的不转换
		// mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL.NON_NULL);
	}

	public static <T> T toObject(String src, Class<T> returnClass) throws Exception {
		try {
			return mapper.readValue(src, returnClass);
		} catch (Exception e) {
			throw new Exception("JSON字符串转换对象异常", e);
		}
	}

	public static <T> String toString(T t) throws Exception {
		try {
			return mapper.writeValueAsString(t);
		} catch (Exception e) {
			throw new Exception("对象转换JSON字符串异常", e);
		}
	}

	public static <T> T readValue(String content, Class<T> clazz) {
		T t = null;
		// mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);  
		try {
			t = mapper.readValue(content, clazz);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return t;
	}

	public static <T> List<T> joinToList(String content, Class<T> clazz){
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
		try {
			List<T> list = mapper.readValue(content, javaType);
			return list;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	public static String writeValueAsString(Object o) {
		String str = null;
		try {
			str = mapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return str;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if(null != str) {
//			Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
			Matcher matcher = BLANK_PATTERN.matcher(str);
			dest = matcher.replaceAll("");
		}
		return dest;
	}

	@SuppressWarnings("rawtypes")
	public static Object readValues(String content, Class CollectionType,
			Class clazz) {
		Object o = null;

		try {
			o = mapper.readValue(content,
					getCollectionType(CollectionType, clazz));
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return o;
	}

	public static JavaType getCollectionType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readJson2Map(String json){
		 Map<String, Object> map = null ;
		 try {
			 map = mapper.readValue(json, Map.class);
		    } catch (JsonParseException e) {
		        e.printStackTrace();
		    } catch (JsonMappingException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		 
		 return map ;
	}

	public static List<Map<String, Object>> json2map(String json){
		List<Map<String, Object>> lMap = null ;
		try {
			Type collectionType = new TypeToken<Collection<Map<String, Object>>>(){}.getType();
			lMap = new Gson().fromJson(json, collectionType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lMap;
	}

	/**
	 * ztree组装树
	 *
	 * @param lm
	 * @return
	 */
	public static List<Map<String, Object>> loadTreeNodes(List<Map<String, Object>> lm) {
		Map<String, List<Map<String, Object>>> listToMap_pid = listToMap(lm,
				"pid");
		Map<String, List<Map<String, Object>>> listToMap_id = listToMap(lm,
				"id");

		List<Map<String, Object>> tree = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : lm) {
			if (null == listToMap_id.get(map.get("pid"))) {
				map.put("children",
						recursion(listToMap_pid, map.get("id").toString()));
				tree.add(map);
			}
		}
		return tree;
	}

	/**
	 * ztree组装树
	 *
	 * @param lm
	 * @return
	 */
	public static Map<String, Object> loadTreeNodes(List<Map<String, Object>> lm, String id) {
		Map<String, List<Map<String, Object>>> listToMap_pid = listToMap(lm,
				"pId");

		Map<String, Object> tree = MapUtils.newHashMap();
		for (Map<String, Object> map : lm) {
			if (null != map.get("id") && map.get("id").toString().equals(id)) {
				map.put("children",
						recursion(listToMap_pid, map.get("id").toString()));
				tree = map;
			}
		}
		return tree;
	}

	/**
	 * 递归
	 * @param listToMap
	 * @param pid
	 * @return
	 */
	public static List<Map<String, Object>> recursion(
			Map<String, List<Map<String, Object>>> listToMap, String pid) {

		List<Map<String, Object>> data = listToMap.get(pid);
		if (null == data) {
			return new ArrayList<Map<String, Object>>();
		}

		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			Object id = map.get("id");
			if (null != id) {
				List<Map<String, Object>> children = listToMap.get(id
						.toString());
				if (null == children) {
					children = new ArrayList<Map<String, Object>>();
				} else {
					children = recursion(listToMap, id.toString());
					data.get(i).put("children", children);
				}
			}
		}
		return data;
	}

	/**
	 * 重组map
	 *
	 * @param list
	 * @param key
	 * @return
	 */
	public static Map<String, List<Map<String, Object>>> listToMap(
			List<Map<String, Object>> list, String key) {
		Map<String, List<Map<String, Object>>> mlist = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> courseType : list) {
			Object pid = courseType.get(key);
			if (null != pid) {
				List<Map<String, Object>> types = new ArrayList<Map<String, Object>>();
				if (null != mlist.get(pid.toString())) {
					types = mlist.get(pid.toString());
				}
				types.add(courseType);
				mlist.put(pid.toString(), types);
			}
		}
		return mlist;
	}


	public static void main(String[] args) {
		//Message m = new Message(true , "test") ;
		//System.out.println(JsonUtils.writeValueAsString(m)) ;
		
		//Map m = JsonUtils.readValue("{\"success\":true,\"msg\":\"test\"}", Map.class) ;
		//String json = "{\"beginTime\":\"2017-08-09\",\"endTime\":\"2017-09-08\"}";
		//Map<String, Object> lMap = JsonUtils.readJson2Map(json);
		//System.out.println("1111"+lMap);
	}
}
