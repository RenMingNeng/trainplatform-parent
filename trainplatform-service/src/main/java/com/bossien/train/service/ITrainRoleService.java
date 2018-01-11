package com.bossien.train.service;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.UserTrainRole;
import com.bossien.train.domain.dto.TrainRoleMessage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/11.
 */

public interface ITrainRoleService {


    /**
     * 新增
     *
     * @param params
     */
    void insert(TrainRole params);


    /**
     * 修改来源为公司自制
     *
     * @param params
     */
    void update(Map<String, Object> params);

    TrainRole selectByRole(Map<String, Object> params);

    TrainRole selectById(Map<String, Object> params);

    /**
     * 消息队列中授权角色修改
     *
     * @param trainRoleMessage
     */
    void updateMessage(TrainRoleMessage trainRoleMessage);

    int insertBatch(List<Map<String,Object>> list);

    int updateBatch(List<Map<String,Object>> list);

    /**
     * 消息队列授权角色的新增
     *
     * @param trainRoleMessage
     */
    void insertMessage(TrainRoleMessage trainRoleMessage);

    /**
     * 删除
     *
     * @param params
     */
    void delete(Map<String, Object> params);


    /**
     * 分页查询列表
     *
     * @param params
     * @return
     */
    Page<TrainRole> selectList(Map<String, Object> params);

    void queryExportData(HttpServletResponse response, Map<String, Object> params);

    List<TrainRole> selectLists(Map<String, Object> params);

    /**
     * 统计
     *
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 查询单条记录
     *
     * @param params
     * @return
     */
    TrainRole selectOne(TrainRole params);

    /**
     * 通过companyId查询受训角色
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectTrainRoles(Map<String, Object> params);

    List<Map<String, Object>> ConstructRoleTreeNodes(String roles);

    List<Map<String, Object>> queryTrainRoles(Map<String, Object> params);

    List<TrainRole> selectVerify(Map<String, Object> params);
}
