package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Article;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IArticleService;
import com.bossien.train.util.RequestUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article") // 公告管理
public class ArticleController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Value("${ARTICLE_TOP_COUNT}")
    private String ARTICLE_TOP_COUNT;

    @Value("${ATTACHMENT_PATH}")
    private String ATTACHMENT_PATH;

    @Autowired
    private IArticleService articleService;

    @RequestMapping("/top")
    @ResponseBody
    public Object insert(
        ) {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        List<Article> list = articleService.selectTop(Integer.valueOf(ARTICLE_TOP_COUNT));
        resp.setResult(list);

        return resp;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Object getFile(@PathVariable String id, HttpServletRequest request) throws IOException {
        // 查找文章
        Map<String, Object> params = Maps.newConcurrentMap();
        params.put("id", id);
        Article article = articleService.selectOne(params);

        // 图片相对路径替换
        if(null != article && !StringUtils.isEmpty(article.getContent())) {
            article.setContent(article.getContent().replaceAll(ATTACHMENT_PATH, getWebPath(request) + "attachment/"));
        }

        RequestUtils.addParam(request, "article", article);
        return "article/detail";
    }

}
