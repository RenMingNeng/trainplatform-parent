package com.bossien.train.domain.eum;

import java.util.List;

/**
 * Created by huangzhaoyong on 2017/7/31.
 *  题库类型
 */
public enum QuestionsTypeEmun {
    QUESTIONTYPE_SINGLE("01", "单选"), QUESTIONTYPE_MANY("02", "多选"), QUESTIONTYPE_JUDGE(
            "03", "判断"), QUESTIONTYPE_FILLOUT("04", "填空"), QUESTIONTYPE_QUESANS("05", "问答 ");

    private String value;
    private String name;

    QuestionsTypeEmun(String value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(String value) {
        QuestionsTypeEmun[] arys = QuestionsTypeEmun.values();
        for (QuestionsTypeEmun type: arys) {
            if(type.value.equals(value)){
                return type.name;
            }
        }
        return "";
    }

    public static List<Object> getValues() {

        return QuestionsTypeEmun.getValues();
    }
}
