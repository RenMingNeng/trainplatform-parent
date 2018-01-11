package com.bossien.train.service;

import com.bossien.train.domain.Message;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */
public interface IMessageService {
    /**
     * 新增
     * @param userId
     * @param companyId
     * @param messageTitle
     * @param messageContent
     * @return
     */
    void send(String userId, String companyId, String messageTitle,
              String messageContent, String sendId, String sendName);

    /**
     * 批量添加
     * @param messages
     * @return
     */
    void sendList(List<Message> messages);

    /**
     * 修改
     * @param params
     */
    void update(Map<String, Object> params);

    /**
     * 分页
     * @param params
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    Page<Message> queryForPagination(Map<String, Object> params,
                                    Integer pageNum, Integer pageSize, User user);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Message> selectList(Map<String, Object> params);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 查询单条记录
     * @param params
     * @return
     */
    Message selectOne(Map<String, Object> params);

    /**
     * 删除
     * @param ids
     */
    void deleteBatch(List<String> ids);

    /**
     * 发送消息给用户，给予通知
     * @param params
     */
   void sendMessageToUser(Map<String, Object> params);
}
