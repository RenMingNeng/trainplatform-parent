package com.bossien.train.service;

import com.bossien.train.domain.CompanyTj;
import com.bossien.train.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */
public interface ICompanyTjService {
    /**
     * 添加
     * @param params
     * @return
     */
    int insert(Map<String,Object> params);

    /**
     * 修改
     * @param params
     * @return
     */
    int update(Map<String,Object> params);

    /**
     * 查询单条
     * @param params
     * @return
     */
    CompanyTj selectOne(Map<String,Object> params);

    /**
     * 缺少时新增默认的
     * @param companyId
     * @param userName
     * @return
     */
    CompanyTj selectOne(String companyId, String userName);

    /**
     * 初始化数据
     * @return
     */
    CompanyTj initData();

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<CompanyTj> selectList(Map<String, Object> params);

    /**
     * 统计
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 分页
     * @param companyId
     * @param companyName
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> queryForPagination(String companyId, String searchName, String companyName,
               String startTime, String endTime, User user, Integer pageNum, Integer pageSize);

    /**
     * 统计
     * @param params
     * @return
     */
    Map<String,Object> anaylze(Map<String, Object> params);

    /**
     * 更新统计（更新人员）
     * @param countUser
     * @param companyId
     * @param userName
     */
    void updateCompanyTj(int countUser, String companyId, String userName);
}
