package com.bossien.train.dao.tp;

import com.bossien.train.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Repository
public interface MessageMapper {
    /**
     * 新增
     * @param message
     */
    int insert(Message message);

    /**
     * 批量添加
     * @param messages
     * @return
     */
    int insertBatch(List<Message> messages);

    /**
     * 修改
     * @param params
     */
    void update(Map<String, Object> params);

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
}
