package com.bossien.train.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-02.
 */
public class CollectionUtils {

    public static <E> List<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> List<E> newLinkedList() {
        return new LinkedList<>();
    }

    // list按blockSize大小等分,最后多余的单独一份
    public static <T> List<List<T>> subList(List<T> list, int blockSize){
        List<List<T>> lists = new ArrayList<List<T>>();
        if(list!=null && list.size()>0 ){
            if(blockSize<=0){
                lists.add(list);
                return lists;
            }
            int listSize = list.size();
            if(listSize<=blockSize){
                lists.add(list);
                return lists;
            }
            int batchSize = listSize/blockSize;
            int remain = listSize%blockSize;
            for(int i=0; i<batchSize; i++){
                int fromIndex = i*blockSize;
                int toIndex = fromIndex + blockSize;
                lists.add(list.subList(fromIndex, toIndex));
            }
            if(remain>0){
                lists.add(list.subList(listSize-remain, listSize));
            }
        }
        return lists;
    }

    /**
     * 将list根据key进行分组
     * @param list
     * @param key
     * @param <T>
     * @return
     */
    public static <T> Map<String, List<T>> listToMap(List<T> list, String key){
        if(null == list){
            return MapUtils.newHashMap();
        }
        if(StringUtil.isEmpty(key)){
            return MapUtils.newHashMap();
        }
        Map<String, List<T>> result = MapUtils.newHashMap();
        for(T t: list){
            try {
                Field field = t.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object value = field.get(t);
                if(null == value || StringUtils.isEmpty(value.toString())){
                    continue;
                }

                List<T> data = new ArrayList<T>();
                if(null != result.get(value.toString())){
                    data = result.get(value.toString());
                }
                data.add(t);
                result.put(value.toString(), data);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public static <T> Map<String, T> listToMapObject(List<T> list, String key){
        if(null == list){
            return MapUtils.newHashMap();
        }
        if(StringUtil.isEmpty(key)){
            return MapUtils.newHashMap();
        }
        Map<String, T> result = MapUtils.newHashMap();
        for(T t: list){
            try {
                Object value;
                if(t instanceof Map){
                    value = ((Map) t).get(key);
                }else{
                    Field field = t.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    value = field.get(t);
                }
                if(null == value || StringUtils.isEmpty(value.toString())){
                    continue;
                }
                result.put(value.toString(), t);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("userId", "123");
        list.add(map);

        listToMapObject(list, "userId");
    }
}
