package com.bossien.train.controller;

import com.bossien.train.domain.Article;
import com.bossien.train.domain.Page;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IArticleService;
import com.bossien.train.util.ParamsUtil;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by A on 2017/8/31.
 */

@Controller
@RequestMapping("/article")
public class ArticleController extends BasicController{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private IArticleService articleService;
    private static final String ATTACHMENT_PATH = PropertiesUtils.getValue("ATTACHMENT_PATH");

    @RequestMapping("")
    public String index(){

        return "article/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="pageSize", required = true, defaultValue = "20") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum,
                       @RequestParam(value="search", required = false, defaultValue = "") String search,
                       @RequestParam(value="personType", required = false, defaultValue = "") String personType) throws ServiceException {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        String resourcePath = request.getServletContext().getInitParameter("resource_path");

        Map<String, Object> params = newMap();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        if(!StringUtils.isEmpty(search)) {
            params.put("search", ParamsUtil.joinLike(search));
        }
        params.put("personType", personType);
        Integer count = articleService.selectCount(params);

        Page<Article> page = new Page<Article>(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Article> list = articleService.selectList(params);
        for(Article article: list){
            article.setContent(replaceByPath(article.getContent(), resourcePath));
        }

        page.setDataList(list);
        resp.setResult(page);

        return resp;
    }

    @RequestMapping("add")
    public String add(){
        return "article/article_add";
    }

    @ResponseBody
    @RequestMapping("save")
    public Object save(HttpServletRequest request, Article article){
        String resourcePath = request.getServletContext().getInitParameter("resource_path");
        article.setContent(replaceByPath(article.getContent(), resourcePath));
        articleService.insert(article);

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    @RequestMapping("update")
    public String update(HttpServletRequest request,
            @RequestParam(value="id", required = true, defaultValue = "") String id){
        String resourcePath = request.getServletContext().getInitParameter("resource_path");
        Map<String, Object> params = newMap();
        params.put("id", id);
        Article article = articleService.selectOne(params);
        article.setContent(replaceByPath(article.getContent(), resourcePath));
        request.setAttribute("article", article);
        return "article/article_update";
    }

    @ResponseBody
    @RequestMapping("update_save")
    public Object update_save(HttpServletRequest request, Article article){
        String resourcePath = request.getServletContext().getInitParameter("resource_path");

        if(null != article.getId()){
            article.setContent(replaceByPath(article.getContent(), resourcePath));
            articleService.update(article);
        }

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    @ResponseBody
    @RequestMapping("fileUpload")
    public Object fileUpload(HttpServletRequest request,
                             @RequestParam(value="action", required = false, defaultValue = "") String action){

        Map<String,Object> rs = new HashMap<String, Object>();
        MultipartHttpServletRequest mReq  =  null;
        MultipartFile multipartFile = null;
        InputStream is = null ;
        String fileName = "";
        // 原始文件名   UEDITOR创建页面元素时的alt和title属性
        String originalFileName = "";
        String filePath = "";
        String attachment = "";
        try {
            mReq = (MultipartHttpServletRequest)request;
            // 从config.json中取得上传文件的ID
            multipartFile = mReq.getFile("upfile");
            // 取得文件的原始文件名称
            fileName = multipartFile.getOriginalFilename();
            originalFileName = fileName;

            attachment = gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), multipartFile.getContentType()).getId().toString();
            /*你的处理图片的代码*/
            rs.put("state", "SUCCESS");// UEDITOR的规则:不为SUCCESS则显示state的内容
            rs.put("url","article/attachment/" + attachment);         //能访问到你现在图片的路径
            rs.put("title", originalFileName);
            rs.put("original", originalFileName);

        } catch (Exception e) {
            rs.put("state", "文件上传失败!"); //在此处写上错误提示信息，这样当错误的时候就会显示此信息
            rs.put("url","");
            rs.put("title", "");
            rs.put("original", "");
        }
        return rs;
    }

//    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
//    public String detail(HttpServletRequest request,
//            @PathVariable String id, HttpServletResponse response) throws IOException {
//        String resourcePath = request.getServletContext().getInitParameter("resource_path");
//        Map<String, Object> params = newMap();
//        params.put("id", id);
//        Article article = articleService.selectOne(params);
//        article.setContent(replaceByPath(article.getContent(), resourcePath));
//        request.setAttribute("article", article);
//
//        return "article/article_detail";
//    }

    @RequestMapping(value = "attachment/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        Query query = new Query();
        Criteria criteria = GridFsCriteria.where("_id").is(id);
        query.addCriteria(criteria);
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(query);
        gridFSDBFile.writeTo(response.getOutputStream());
    }

    public String replaceByPath(String content, String resourcePath){
        if(StringUtil.isEmpty(content)){
            return "";
        }

        if(content.indexOf("<img") != -1){
            if(content.indexOf(ATTACHMENT_PATH) != -1){
                content = content.replaceAll(ATTACHMENT_PATH, resourcePath + "/article/attachment");
            }else if(content.indexOf(resourcePath) != -1){
                content = content.replaceAll(resourcePath, "");
                content = content.replaceAll("/article/attachment", ATTACHMENT_PATH);
            }
        }
        return content;
    }

    @ResponseBody
    @RequestMapping("clearCache")
    public Object clearCache(HttpServletRequest request, Article article){

        // 清除"articleCache#(60 * 60):selectTop_3"
        Set<String> keys = redisTemplate.keys("articleCache*");
        redisTemplate.delete(keys);

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }
}
