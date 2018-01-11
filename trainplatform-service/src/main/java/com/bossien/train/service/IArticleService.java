package com.bossien.train.service;

import com.bossien.train.domain.Article;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */
public interface IArticleService {
    /**
     * 新增
     * @param article
     */
    int insert(Article article);

    /**
     * 修改
     * @param article
     */
    void update(Article article);

    /**
     * 查询列表
     * @param params
     * @return
     */
    List<Article> selectList(Map<String, Object> params);

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
    Article selectOne(Map<String, Object> params);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    List<Article> selectTop(Integer count);
}
