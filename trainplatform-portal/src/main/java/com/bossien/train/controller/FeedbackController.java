package com.bossien.train.controller;

import com.bossien.train.domain.Feedback;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ProblemTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IFeedbackService;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/feedback")
public class FeedbackController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    private static final Long max_size = 2 * 1024 * 1024L;

    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private IFeedbackService feedbackService;

    @RequestMapping("")
    public String feedback(String url, ModelMap modelMap) {
        modelMap.addAttribute("url", URLDecoder.decode(url));
        return "feedback";
    }

    @RequestMapping("/insert")
    public void insert(HttpServletRequest request,HttpServletResponse response,
                         @RequestParam(value="user_id", required = false, defaultValue = "") String user_id,
                         @RequestParam(value="user_name", required = false, defaultValue = "") String user_name,
                         @RequestParam(value="problem_type", required = false, defaultValue = "") String problem_type,
                         @RequestParam(value="content", required = false, defaultValue = "") String content,
                         @RequestParam(value="feedBack_url", required = false, defaultValue = "") String feedBack_url,
                         @RequestParam(value="contact_way", required = false, defaultValue = "") String contact_way,
                         @RequestParam(value="fileList", required = false, defaultValue = "") String fileList
        ) throws Exception {
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        content = "反馈来源地址:"+ feedBack_url + "<br>" +content;
        // 附件上传至mongodb
        String gridFSFileId = "";
        List<Map<String, Object>> attachments = new LinkedList<Map<String, Object>>();
        Map<String, Object> attachment = null;
        if(!fileList.equals("")){
            for(String fileInfo : fileList.split(",")) {
                if(StringUtil.isEmpty(fileInfo)
                        || fileInfo.split(";").length < 2){
                    continue;
                }
                attachment = new HashMap<String, Object>();
                attachment.put("gridFSFileId", fileInfo.split(";")[0]);
                attachment.put("originalFilename", fileInfo.split(";")[1]);
                attachments.add(attachment);
            }
        }

        Feedback feedback = new Feedback(
                sequenceService.generator(),
                user_id,
                user_name,
                problem_type,
                content,
                contact_way,
                new Gson().toJson(attachments),
                getCurrUser(request).getUserName(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );

        feedbackService.insert(feedback);

        writeMessageUft8(response, resp);
    }

    @RequestMapping("upload")
    @ResponseBody
    public void upload(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value="file", required = false, defaultValue = "") MultipartFile file) throws Exception{
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        if(!checkMaxSize(file.getSize())){
            resp.setResult("");
            writeMessageUft8(response, resp);
        }
        Map<String, Object> attachment = new HashMap<String, Object>();
        String gridFSFileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType()).getId().toString();
        attachment.put("gridFSFileId", gridFSFileId);
        attachment.put("name", file.getOriginalFilename());
        resp.setResult(attachment);
        writeMessageUft8(response, resp);
    }

    private boolean checkMaxSize(long size) {
        if(0 == size) {
            return false;
        }
        if(size > max_size) {
            return false;
        }
        return true;
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="problemType", required = false, defaultValue = "") String problemType,
                       @RequestParam(value="pageSize", required = false, defaultValue = "20") Integer pageSize,
                       @RequestParam(value="pageNum", required = false, defaultValue = "1") Integer pageNum
    ) throws ServiceException{
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<Object>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        params.put("userId", user.getId());

        if(!StringUtil.isEmpty(problemType)){
            params.put("problemType", problemType);
        }
        Integer count = feedbackService.selectCount(params);
        Page page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("pageSize", page.getPageSize());
        List<Feedback> feedbacks = feedbackService.selectList(params);
        for(Feedback feedback: feedbacks){
            feedback.setProblemType(ProblemTypeEnum.get(feedback.getProblemType()).getName());
        }
        page.setDataList(feedbacks);
        resp.setResult(page);

        return resp;
    }
}
