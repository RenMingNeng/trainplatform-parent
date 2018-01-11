package com.bossien.train.service;

import com.bossien.train.domain.User;

import java.util.Map;

/**
 * Created by Administrator on 2017-08-03.
 */
public interface ICreatePublicProjectService {

    /**
     * 公开型项目保存
     * @param params
     * @param user
     */
    void savePublicProject(Map<String, Object> params, User user) throws Exception;

    /**
     * 公开型项目修改保存
     * @param params
     * @param user
     */
    void updatePublicProject(Map<String, Object> params, User user) throws Exception;

    /**
     * 修改组卷策略
     * @param params
     */
    void updateExamStrategy(Map<String, Object> params);

    /**
     * 公开型项目删除
     * @param params
     */
    void delete(Map<String, Object> params);
}
