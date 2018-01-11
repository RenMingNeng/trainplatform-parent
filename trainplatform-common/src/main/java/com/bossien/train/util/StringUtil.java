package com.bossien.train.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 */
public class StringUtil {

    private static Pattern BRACE_PATTERN = Pattern.compile("\\{(\\S+?)\\}");

    //去除List<List<String>>集合中空的集合
    public static List<List<String>> removeListByEmpty(List<List<String>> list){
        List<List<String>> result = new ArrayList<List<String>>();
        for (List<String> str : list) {
            if(!isListEmpty(str)){
                result.add(str);
            }
        }
        return result;
    }

    public static boolean isListEmpty(List<String> list) {
        if(null==list) {
            return true;
        }
        if(list.size()==0) {
            return true;
        }
        for(String str : list){
            if(!StringUtils.isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 取两个集合中公共部分
     * @param src1
     * @param src2
     * @return
     */
    public static <T> List<T> retainAll(List<T> src1, List<T> src2){
        List<T> temp = Lists.newCopyOnWriteArrayList(src1);
        //取出src1中不在src2中的数据，删除相同的部分
        temp.removeAll(src2);
        //src1删除不在src2中的数据，留相同部分
        src1.removeAll(temp);
        return src1;
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str.toString());
    }

    /**
     * 提取{}里面的字符串：
     * 例如："如图{123.jpg}和{abc.jpg}所示"--->["123.jpg","abc.jpg"]
     * @param str
     * @return
     */
    public static List<String> pickUp(String str){
        if(isEmpty(str)) {
            return null;
        }
        List<String> list=new ArrayList<String>();

//        Pattern p = Pattern.compile("\\{(\\S+?)\\}");
        Matcher matcher = BRACE_PATTERN.matcher(str);
        while(matcher.find()){
            list.add(matcher.group(1));
        }
        return list;
    }


    /**
     * 提取{}里面的字符串：
     * 例如："如图{123.jpg}和{abc.jpg}所示"--->["123.jpg","abc.jpg"]
     * @param str
     * @return
     */
    public static String pickUpToString(String str){
        if(isEmpty(str)) {
            return null;
        }
//        Pattern p = Pattern.compile("\\{(\\S+?)\\}");
        Matcher matcher = BRACE_PATTERN.matcher(str);
        while(matcher.find()){
            return matcher.group(1);
        }
        return str;
    }

    /**
     * 重命名文件名称 例如： " 文件安全.docx" ----> "474050061d4a46ee8010a7865ca15468.docx"
     * @param fileName
     * @return
     */
    public static String rename(String fileName){
        String suffix=suffix(fileName);
        String file_new_name = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
        return file_new_name;
    }

    /**
     * 获取文件后缀  例如: "文件安全.docx"------>"docx"
     * @param fileName
     * @return
     */
    public static String suffix(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return suffix;
    }

    // list按blockSize大小等分,最后多余的单独一份
    public static <T> List<List<T>> subList(List<T> list, int blockSize){
        List<List<T>> lists = new ArrayList<List<T>>();
        if(list!=null){
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

    public static String trim(String str){
        if(isEmpty(str)){
            return "";
        }
        return str.trim().replaceAll("\\s*", "").replace("　", "");
    }

    public static void main(String[] args) {
        String str = " 1 3 44 4 4 55  5 5 6 ";
        System.out.println("----:" + trim(" 　") + "," + trim(" 　").length());
    }
}
