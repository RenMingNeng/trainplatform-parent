package com.bossien.train.service;

import com.bossien.train.domain.*;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IMsgService {
    /**
     * 新增
     * @param msg
     * @param msgText
     */
    void insert(Msg msg, MsgText msgText);

    /**
     * 批量添加
     * @param msgs
     */
    void insert(List<Msg> msgs);

    /**
     * 批量添加
     * @param msgText
     * @param user
     * @param recCompanyIds
     * @param recDeptIds
     */
    void insertList(MsgText msgText, User user, List<String> recCompanyIds, List<String> recDeptIds);

    /**
     * 修改状态
     * @param recId
     * @param msgId
     */
    void update(String recId, String msgId);

    /**
     * 分页
     * @param param
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    Page<Map<String, Object>> queryForPagination(Map<String,Object> param, Integer pageNum, Integer pageSize, User user);

    /**
     * 查询列表
     * @param param
     * @return
     */
    List<Map<String, Object>> selectList(Map<String,Object> param);

    /**
     * 查询列表
     * @param param
     * @param start
     * @param size
     * @return
     */
    List<Map<String, Object>> selectList(Map<String,Object> param, Long start, Long size);

    /**
     * 统计--recId接收者id
     * @param recId
     * @return
     */
    Integer selectCount(String recId);

    /**
     * 查看公告
     * @param msgId
     * @return
     */
    MsgText selectOne(String msgId);

    /**
     * 删除
     * @param msgIds
     */
    void deleteBatch(List<String> msgIds);
}
