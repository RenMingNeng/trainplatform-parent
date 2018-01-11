package com.bossien.train.kit;

import com.bossien.train.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/9/21.
 */
public class PermissionUtil {

    private static String  contain_exam_type = "3567";
    private static String  contain_exercise_type = "2467";
    private static String  contain_train_type = "1457";

    /**
     * 是否有进入答题功能
     * @param projectType
     * @return
     */
     public static  boolean hasPermissionQuestion(String projectType){
        //含有练习的
        if(StringUtils.contains(contain_exercise_type,projectType)){
            return true;
        }
        return false;
    }

    /***
     * 是否有学习的功能
     * @param projectType
     * @return
     */
    public static boolean hasPermissionStudy(String projectType){
        //含有培训的
        if(StringUtils.contains(contain_train_type,projectType)){
            return true;
        }
        return false;
    }
    /***
     * 是否含有考试
     * @param projectType
     * @return
     */
    public static boolean hasPermissionExam(String projectType){
        //含有考试的
        if(StringUtils.contains(contain_exam_type,projectType)){
            return true;
        }
        return false;
    }

}
