package com.bossien.train.util;

/**
 * Created by RenMingNeng on 2017-08-22.
 */
public class ParamsUtil {

    private static final String PERCENT = "%";

    public static String joinLike(String str){
        if(StringUtil.isEmpty(str)){
            return "";
        }else{
            return PERCENT.concat(str).concat(PERCENT);
        }
    }

    /*public static void main(String[] args) {
        System.out.println(ParamsUtil.joinLike(null));
        System.out.println(ParamsUtil.joinLike(""));
    }*/
}
