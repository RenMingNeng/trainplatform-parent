package com.bossien.train.common;

/**
 * 常量类
 *
 * @author gengxiefeng
 */
public class AppConstant {

    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     *
     */
    public static final String T_ADMIN = "t_admin";

    public static final String T_ROLE = "t_role";

    /**
     *
     */
    public static final String ADMIN_TPL = "admin/";

    /**
     * 超级管理员
     */
    public static final String SUPPER_MANAGER = "admin";

    /**
     * 分页每页数量
     */
    public static final Integer PAGE_SIZE = 15;

    /**
     * 资源分隔符
     */
    public static final String RESOURCE_DELIMITER = ":";

    /**
     * 日志保存天数
     */
    public static final Integer LOG_SAVE_DAY = Integer.parseInt(InitProperties.getValue("log.save.day"));

    /**
     * 上传目录
     */
    public static final String UPLOAD_DIR =InitProperties.getValue("upload.dir");

    /**
     * FastDFS服务器
     */
    public static final String STORAGE_SERVER_HOST_PORT = InitProperties.getValue("storage_server_host_port");
    /**
     * FastDFS服务器
     */
    public static final String FASTDFS_USERKEY = InitProperties.getValue("fastDFS.userKey");

    /**
     * 根节点：id(唯一标示)
     */
    public static final String ROOT_ID = "root";

    /**
     * 根节点：pid(唯一标示)
     */
    public static final String ROOT_PID = "-1";

    /**
     * 操作类型：移动
     */
    public static final String OPERATE_MOVE = "move";

    /**
     * 操作类型：重命名
     */
    public static final String OPERATE_RENAME = "rename";

    /**
     * 操作类型：新增 - 节点前
     */
    public static final String ADDNODETYPE_BEFORE = "before";

    /**
     * 操作类型：新增 - 节点后
     */
    public static final String ADDNODETYPE_AFTER = "after";

    /**
     * 操作类型：新增 - 子节点
     */
    public static final String ADDNODETYPE_CHILD = "child";

    public static final String TP_EXCEL_TEMP_PATH = System.getProperty("java.io.tmpdir");

    /**
     * 视音频文件后缀
     */
    public static final String VIDEO_SUFFIX = ".+\\.(flv|mp4)$";
    /**
     * FastDFS服务器（内网）
     */
    public static String STORAGE_SERVER_HOST_PORT_LAN = InitProperties.getValue("storage_server_host_port_lan");
    /**
     * FastDFS服务器
     */
    public static String FASTDFS_USER = InitProperties.getValue("fastDFS.user");
    /**
     * 返回状态--成功
     */
    public static final String UPLOAD_RESULT_SUCCESS = "200";
    /**
     * 防盗链秘钥
     */
    public static String ANTI_STEAL_LINK_KEY =InitProperties.getValue("antiStealLink.key");
    /**
     * 防盗链超时时间 秒
     */
    public static int ANTI_STEAL_TIMEOUT = Integer.valueOf(InitProperties.getValue("antiStealLink.timeOut"));
    /**
     * content DFS服务器（外网）
     */
    public static String CONTENT_SERVER_HOST_PORT_WAN =InitProperties.getValue("content_server_host_port_wan");
    /**
     * media 服务 给应用分配的ID
     */
    public static String MEDIA_APPID =InitProperties.getValue("media.appId");
}

