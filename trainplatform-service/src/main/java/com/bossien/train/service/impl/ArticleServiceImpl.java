package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ArticleMapper;
import com.bossien.train.domain.Article;
import com.bossien.train.service.IArticleService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ISequenceService sequenceService;

    @Override
    public int insert(Article article) {
        article.setId(sequenceService.generator());
        article.setCreateTime(DateUtils.formatDateTime(new Date()));
        return articleMapper.insert(article);
    }

    @Override
    public void update(Article article) {
        articleMapper.update(article);
    }

    @Override
    public List<Article> selectList(Map<String, Object> params) {

        return articleMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return articleMapper.selectCount(params);
    }

    @Override
    public Article selectOne(Map<String, Object> params) {

        return articleMapper.selectOne(params);
    }

    @Override
    public void delete(String id) {

        articleMapper.delete(id);
    }

    /**
     * @description 本方法在redis实例中的key为："articleCache:selectTop_3"，默认会将cacheName追加在最前面.
     * 查询top条数的公告
     * @param count 前面多少条
     * @return List<Article>
     */
    @Cacheable(value = "articleCache#(60 * 60)", key = "'selectTop'.concat('_').concat(#count)")
    @Override
    public List<Article> selectTop(Integer count) {

        return articleMapper.selectTop(count);
    }
}
