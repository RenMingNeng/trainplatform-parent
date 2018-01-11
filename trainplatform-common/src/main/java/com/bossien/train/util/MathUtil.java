package com.bossien.train.util;

import java.text.NumberFormat;

/**
 * String 工具类
 */
public class MathUtil {

    public static String percent(Double number1, Double number2){
        if(null==number1) {
            number1 = 0.00d;
        }
        if(null==number2 || number2 == 0.00d) {
            return "0%";
        }
        // 获取格式化对象
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        // 设置百分比精度(小数点后两位)
        numberFormat.setMaximumFractionDigits(2);
        // 计算百分比
        return numberFormat.format(number1/number2);
    }

    public static String getHour(Long argo){
        if(null == argo){
            return "0";
        }
        Double number = Double.parseDouble(argo.toString());
        // 获取格式化对象
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(number/3600);
    }
    public static String getHour(Double args){
        if(null == args){
            return "0";
        }

        // 获取格式化对象
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(args/3600);
    }

    /**
     * 数组中添加数据
     * @param ary
     * @param text
     * @return
     */
    public static String[] modifyArrayData(String[] ary, String text){
        String[] newArray = new String[ary.length + 1];
        System.arraycopy(ary, 0, newArray, 0, ary.length);
        newArray[newArray.length - 1] = text;
        return newArray;
    }
}
