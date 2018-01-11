package com.bossien.train.controller;

import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Msg;
import com.bossien.train.domain.MsgText;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.page.SpringDataPageable;
import com.bossien.train.service.IMsgService;
import com.bossien.train.util.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by A on 2017/12/18. 通知公告
 */
@Controller
@RequestMapping("popup/msg")
public class MsgController extends BasicController {
    private static final String ATTACHMENT_PATH = PropertiesUtils.getValue("ATTACHMENT_PATH");
    /**
     * 消息内容表
     */
    private static final String mongo_msg_text = "message_text";
    @Autowired
    private IMsgService msgService;
    /**
     * 发送者与接收者关联关系
     */
    private final String mongo_msg = "message";
    /**
     * 消息内容表
     */
    @Autowired
    private MongoOperations mongoTemplate;

    /**
     * 通知公告 首页
     * @return
     */
    @RequestMapping
    public String index(){
        return "popup/msg/index";
    }

    /**
     * 通知公告异步列表
     * @param request
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="msgTitle", required = false) String msgTitle,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Response<Object> resp = new Response<Object>();
        List<MsgText> msgTextList = new ArrayList<MsgText>();
        Page<Map<String, Object>> page = new Page<>();
        User user = getCurrUser(request);
        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        // 查询分页条件
        SpringDataPageable pageable = new SpringDataPageable();
        // 查询条件
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("recId").is(user.getId())
        ));
        Long count = mongoTemplate.count(query, Msg.class, mongo_msg);

        if(count == 0){
            page.setDataList(new ArrayList<Map<String, Object>>());
            resp.setCode(Code.SUCCESS.getCode());
            resp.setResult(page);
        }

        //发送者与接收者关联关系 列表
        List<Msg> msgList = mongoTemplate.find(query, Msg.class, mongo_msg);
        if(null != msgList && msgList.size() < 1){
            page.setDataList(new ArrayList<Map<String, Object>>());
            resp.setCode(Code.SUCCESS.getCode());
            resp.setResult(page);
            return resp;
        }

        Set<String> msgIds = new HashSet<String>();
        Map<String, Msg> msgMap = MapUtils.newHashMap();
        for(Msg msg: msgList){
            msgIds.add(msg.getMsgId());
            msgMap.put(msg.getMsgId(), msg);
        }

        Query query_text = new Query(new Criteria().andOperator(
                Criteria.where("msgId").in(msgIds)
        ));

        if(StringUtils.isNotEmpty(msgTitle)){
            query_text.addCriteria(Criteria.where("msgTitle").regex("^.*?" + msgTitle + ".*$"));
        }
        Long count1 = mongoTemplate.count(query_text, MsgText.class, mongo_msg_text);


        // 默认按时间降序
        List<Sort.Order> orders = Lists.newArrayList();
        orders.add(new Sort.Order(Sort.Direction.ASC, "isTop"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Sort sort = new Sort(orders);
        // 页码
        pageable.setPageNumber(pageNum);
        // 每页条数
        pageable.setPageSize(pageSize);
        // 排序
        pageable.setSort(sort);

        //总条数
        page = new Page(count1.intValue(), pageNum, pageSize);
        //学员自学统计信息集合
        msgTextList = mongoTemplate.find(query_text.with(pageable),MsgText.class,mongo_msg_text);

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(MsgText msgText: msgTextList){
            map = MapUtils.newHashMap();
            MapUtils.putAll(map, msgText);
            MapUtils.putAll(map, msgMap.get(msgText.getMsgId()));
            result.add(map);
        }

        page.setDataList(result);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);

       /* User user = this.getCurrUser(request);
        if(null == user){
            return new Response<Object>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        param.put("userId",user.getId());

        Page<Map<String, Object>> page = msgService.queryForPagination(param, pageNum, pageSize, user);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);*/
        return resp;
    }

    /**
     * 新增页面
     * @return
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request){
        //判断是否登录
        User user = this.getCurrUser(request);
        if(null == user){
            return "";
        }
        request.setAttribute("userName", user.getUserName());
        request.setAttribute("curDate", DateUtils.currentDateString());
        return "popup/msg/add";
    }

    /**
     * 新增数据
     * @param request
     * @param msgText
     * @param companys
     * @param depts
     * @return
     */
    @RequestMapping("/saveAdd")
    @ResponseBody
    public Object saveAdd(HttpServletRequest request, MsgText msgText, String companys, String depts) {
        //判断是否登录
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<Object>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        //更改文章中的图片
        msgText.setMsgContent(replaceByPath(msgText.getMsgContent()));

        //判断数据是否有效
        if(StringUtil.isEmpty(companys) && StringUtil.isEmpty(depts)){
            return new Response<Object>(Code.BAD_REQUEST.getCode(), Code.BAD_REQUEST.getMessage());
        }
        //批量添加
        msgService.insertList(msgText, user, Arrays.asList(companys.split(",")), Arrays.asList(depts.split(",")));
        //返回
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 预览
     * @param request
     * @param msgId
     * @return
     */
    @RequestMapping("/view")
    public String view(HttpServletRequest request, String msgId){
        //判断是否登录
        User user = this.getCurrUser(request);
        if(null == user){
            return "";
        }

        //判断数据是否有效
        if(StringUtil.isEmpty(msgId)){
            return "";
        }
        MsgText msgText = msgService.selectOne(msgId);
        if(null == msgText){
            return "";
        }

        //修改状态
        msgService.update(user.getId(), msgId);

        //更改文章中的图片
        msgText.setMsgContent(replaceByPath(msgText.getMsgContent()));

        String msgFile = msgText.getMsgFile();
        if(!StringUtil.isEmpty(msgFile)){
            List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();
            String[] array = msgFile.split(";");
            Map<String, Object> data;
            for(String ay: array){
                if(!StringUtil.isEmpty(ay) && ay.split(",").length > 1){
                    String[] ary = ay.split(",");
                    data = MapUtils.newHashMap();
                    data.put("name", ary[1]);
                    data.put("url", ary[0]);
                    files.add(data);
                }
            }
            request.setAttribute("files", files);
        }
        request.setAttribute("msgText", msgText);
        return "popup/msg/view";
    }

    /**
     * 下载
     * @param response
     * @return
     */
    @RequestMapping("/download/{fid:.+}")
    public void download(HttpServletResponse response, @PathVariable String fid, String fileName) throws Exception{
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
        UploadFileUtil.download_file(fid, response.getOutputStream());
    }

    /**
     * 通知公告内容图片链接转换
     * @param content
     * @return
     */
    public String replaceByPath(String content){
        if(StringUtil.isEmpty(content)){
            return "";
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.select("img");
        for(Element element: elements){
            String src = element.attr("src");
            if(src.indexOf(AppConstant.CONTENT_SERVER_HOST_PORT_WAN) != -1){
                element.attr("src", split(src));
            }else{
                element.attr("src", FastDFSUtil.getHttpNginxURLWithToken(src));
            }
        }
        return document.select("body").html().toString();
    }

    public static String split(String url){
        String[] arry = url.split("\\&");
        for(String ary: arry){
            if(ary.indexOf("srcFid") != -1 && ary.split("\\=").length > 1){
                return ary.split("\\=")[1];
            }
        }
        return "";
    }
}
