package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/25.
 * 用户表的额外字段表
 */
public class UserExtra implements Serializable {

    private String user_id;                    //用户id
    private String guide_mark;            //引导页标示0：显示 1隐藏
    private String create_time;
    private String oper_time;
    public UserExtra (){}
    public UserExtra(String user_id,String guide_mark){
        this.user_id = user_id;
        this.guide_mark = guide_mark;
    }
    public UserExtra (String user_id,String guide_mark,String create_time,String oper_time){
        this.user_id = user_id;
        this.guide_mark = guide_mark;
        this.create_time = create_time;
        this.oper_time = oper_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGuide_mark() {
        return guide_mark;
    }

    public void setGuide_mark(String guide_mark) {
        this.guide_mark = guide_mark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOper_time() {
        return oper_time;
    }

    public void setOper_time(String oper_time) {
        this.oper_time = oper_time;
    }

   /* // 引导页 0：显示； 1：隐藏
    public enum GuideMark {
        TYPE_1("显示", "0"), TYPE_2("隐藏", "1");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static UserExtra.GuideMark getEnum(String value) {
            UserExtra.GuideMark[] is = UserExtra.GuideMark.values();
            for (UserExtra.GuideMark i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        GuideMark(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }*/
}
