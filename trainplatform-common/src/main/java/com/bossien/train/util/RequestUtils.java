package com.bossien.train.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 */
public class RequestUtils {

    public static String getCNParam(HttpServletRequest request, String param){
        try {
            if(StringUtils.isEmpty(param)) {
                return "";
            }
            if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                return URLEncoder.encode(param, "UTF-8");
            }
            return new String(param.getBytes(),"ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Map<String, String> getParamsByGet(String queryString){
        Map<String, String> params = new LinkedHashMap<String, String>();
        try {
            if(StringUtils.isEmpty(queryString)) {
                return params;
            }
            String[] arr = queryString.split("&");
            String key = null;
            String value = null;
            for(int i=0; i<arr.length; i++){
                key = arr[i].substring(0, arr[i].indexOf("="));
                value = arr[i].substring(arr[i].indexOf("=")+1);
                params.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static void addParam(HttpServletRequest request, String key, Object value){
        if(null == request) {
            return;
        }
        if(StringUtils.isEmpty(key)) {
            return;
        }
        request.setAttribute(key, value);
    }

    public static Object getParam(HttpServletRequest request, String key) {
        if(null == request) {
            return null;
        }
        if(StringUtils.isEmpty(key)) {
            return null;
        }
        return request.getAttribute(key);
    }

    public static void add2Map(Map map, String key, Object value){
        if(null == map) {
            return;
        }
        if(StringUtils.isEmpty(key)) {
            return;
        }
        map.put(key, value);
    }

    public static boolean isAjax(HttpServletRequest request) {
        return (!StringUtils.isEmpty(request.getHeader("x-requested-with")) &&
                request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest"));
    }

}
