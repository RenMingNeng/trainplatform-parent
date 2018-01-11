package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CourseInfo;
import com.bossien.train.domain.CourseQuestion;
import com.bossien.train.domain.dto.CourseQuestionDTO;
import com.bossien.train.domain.dto.CourseQuestionMessage;
import com.bossien.train.service.ICourseInfoService;
import com.bossien.train.service.ICourseQuestionService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 * <p>
 * 消息队列
 * 单位题库管理关系的监听器
 */
@Component("topicCourseQuestionListener")
public class TopicCourseQuestionListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCourseQuestionListener.class);

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private ICourseInfoService courseInfoService;
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("======TopicCourseQuestionListener===课程题库关联监听器========" + json);
            CourseQuestionDTO dto = JsonUtils.readValue(json, CourseQuestionDTO.class);//将json数据转换成javabean
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
                Map<String, Object> params = new HashMap<String, Object>();
                List<CourseQuestion> list_add = new ArrayList<>();//新增集合
                for (CourseQuestionMessage courseQuestionMessage : dto.getDataObj()) {
                    params.put("intQuestionId", courseQuestionMessage.getCourseQuestionId());
                    params.put("intCourseId", courseQuestionMessage.getCourseId());
                    courseQuestionService.deleteByPrimaryKey(params);
                    logger.info(
                        "======TopicCourseQuestionListener===课程题库关联监听器========先通过课程题库删除题库和课程的关联+====课程id：" + courseQuestionMessage.getCourseId());

                    /*   CourseQuestion courseQuestion = courseQuestionService.selectById(params);//通过编号查询是否有值*/
                    CourseQuestion courseQuestionItem = new CourseQuestion();
                    courseQuestionItem.setIntCourseId(courseQuestionMessage.getCourseId());
                    courseQuestionItem.setIntQuestionId(courseQuestionMessage.getCourseQuestionId());
                    if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                        courseQuestionService.insertBatch(list_add);
                        list_add = new ArrayList<>();
                        logger.info("======TopicCourseQuestionListener==CourseQuestion_Id:" + courseQuestionMessage.getCourseQuestionId()
                            + "= CourseQuestion add success==课程题库关联关系添加成功========");

                    } else {

                        list_add.add(courseQuestionItem);
                    }
                }
                if (list_add.size() != 0) {
                    courseQuestionService.insertBatch(list_add);
                    logger.info(
                        "======TopicCourseQuestionListener=== CourseQuestion add success==课程题库关联关系添加成功========" + JSONObject.toJSONString(list_add));
                }
            }
        } catch (Exception e) {
            logger.error("======TopicCourseQuestionListener===" + e.getMessage());

        }
    }
}
