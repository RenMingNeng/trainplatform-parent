package com.bossien.train.domain.eum;

/**
 *
 * 树类型枚举
 */
public enum TreeTypeEnum {
    TREE_TYPE_1("1", "根节点"),
    TREE_TYPE_2("2", "父节点"),
    TREE_TYPE_3("3", "叶子节点");

    private String value;
    private String name;

    TreeTypeEnum(String value, String name) {
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

    public static TreeTypeEnum get(String value) {
        TreeTypeEnum[] values = TreeTypeEnum.values();
        for (TreeTypeEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }

}