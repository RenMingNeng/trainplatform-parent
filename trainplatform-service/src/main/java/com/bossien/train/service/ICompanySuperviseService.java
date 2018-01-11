package com.bossien.train.service;

import com.bossien.train.domain.CompanySupervise;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-31.
 */
public interface ICompanySuperviseService {
    /**
     * 初始化
     */
    List<CompanySupervise> initSupervise(String companyId);

    /**
     * 新增
     * @param companySupervise
     */
    void insert(CompanySupervise companySupervise);

    /**
     * 批量
     * @param companySupervises
     */
    void insertBatch(List<CompanySupervise> companySupervises);

    /**
     * 修改
     * @param companySupervise
     */
    void update(CompanySupervise companySupervise);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<CompanySupervise> selectList(Map<String, Object> params);

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
    CompanySupervise selectOne(Map<String, Object> params);

    /**
     * 删除
     * @param params
     */
    void delete(Map<String, Object> params);

    /**
     * 根据公司id获取监控树
     * @param companyId
     * @return
     */
    String getSuperviseTree(String companyId);

    /**
     * 保存子公司
     * @param user
     * @param pid
     * @param companyIds
     */
    void sendMessage(User user, String companyName, String pid, String companyIds);

    /**
     * 添加监管消息处理
     * @param messageId
     */
    void addSupervise(String messageId, String pid, String isTrue, User user, String companyName);

    /**
     * 上移
     * @param companyId
     * @param id
     * @param pid
     */
    void upNode(String companyId, String id, String pid);

    /**
     * 下移
     * @param companyId
     * @param id
     * @param pid
     */
    void downNode(String companyId, String id, String pid);

    /**
     * 升级
     * @param companyId
     * @param id
     * @param pid
     */
    void upgrade(String companyId, String id, String pid, Integer orderNum);

    /**
     * 降级
     * @param companyId
     * @param id
     * @param pid
     */
    void degrade(String companyId, String id, String pid, Integer orderNum);

    /**
     * 根据公司id生成json
     * @param companyId
     * @return
     */
    String highchartsgetJson(String companyId);

    /**
     * 查询监管中所有子企业id
     * @param rootId
     * @param companyId
     * @return
     */
    String getChildCompanyIds(String rootId, String companyId);
}
