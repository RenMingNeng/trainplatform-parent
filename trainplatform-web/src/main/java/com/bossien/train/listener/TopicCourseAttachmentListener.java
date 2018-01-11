package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Attachment;
import com.bossien.train.domain.CourseAttachment;
import com.bossien.train.domain.dto.AttachmentDTO;
import com.bossien.train.domain.dto.AttachmentMessage;
import com.bossien.train.domain.dto.CourseAttachmentDTO;
import com.bossien.train.domain.dto.CourseAttachmentMessage;
import com.bossien.train.service.IAttachmentService;
import com.bossien.train.service.ICourseAttachmentService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * <p>
 * 消息队列
 * 单位课程附件关联的消息队列
 */
@Component("topicCourseAttachmentListener")
public class TopicCourseAttachmentListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCourseAttachmentListener.class);

    @Autowired
    private ICourseAttachmentService courseAttachmentService;

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
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
            logger.info("=======TopicCourseAttachmentListener==课程附件关联监听器========" + json);
            CourseAttachmentDTO dto = JsonUtils.readValue(json, CourseAttachmentDTO.class);//将json数据转换成javabean
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
                List<CourseAttachment> list_add = new ArrayList<>();//新增集合
                Map<String, Object> params = new HashMap<String, Object>();
                for (CourseAttachmentMessage courseAttachmentMessage : dto.getDataObj()) {
                    params.put("courseAttachmentId", courseAttachmentMessage.getCourseAttachmentId());
                    params.put("intCourseId", courseAttachmentMessage.getCourseId());
                    courseAttachmentService.deleteByPrimaryKey(params);//
                    logger.info("=======TopicCourseAttachmentListener==课程附件关联监听器========先通过课程ID删除====课程id："+courseAttachmentMessage.getCourseId());
                  /*  CourseAttachment courseAttachment = courseAttachmentService.selectById(params);//通过编号查询是否有值*/
                     CourseAttachment courseAttachmentItem = new CourseAttachment();
                    courseAttachmentItem.setIntCourseId(courseAttachmentMessage.getCourseId());
                    courseAttachmentItem.setIntAttachmentId(courseAttachmentMessage.getCourseAttachmentId());
                   /* if (null == courseAttachment) {//说明没有重复的数据*/
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            courseAttachmentService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info("=======TopicCourseAttachmentListener=Course_Attachment_id:" + courseAttachmentMessage.getCourseAttachmentId()
                                + "= CourseAttachment add success== 课程附件关联增加成功========");
                        } else {
                            list_add.add(courseAttachmentItem);
                        }
                   /* } else {//有重复的数据
                        logger.info("=======TopicCourseAttachmentListener=Course_Attachment_id:" + courseAttachmentMessage.getCourseAttachmentId()
                            + "= CourseAttachment update success ==课程附件关联有重复数据========");
                    }*/
                }
                if (list_add.size() != 0) {
                    courseAttachmentService.insertBatch(list_add);
                    logger.info("=======TopicCourseAttachmentListener== CourseAttachment add success== 课程附件关联增加成功========" + JSONObject
                        .toJSONString(list_add));

                }
            }
        } catch (Exception e) {

            logger.error("=======TopicCourseAttachmentListener== "+e.getMessage());

        }
    }

}
