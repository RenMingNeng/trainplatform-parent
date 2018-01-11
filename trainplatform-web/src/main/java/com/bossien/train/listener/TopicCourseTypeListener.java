package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CourseType;
import com.bossien.train.service.ICourseTypeService;
import com.bossien.train.util.CollectionUtils;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息队列
 * 课程类型的监听器
 */
@Component("topicCourseTypeListener")
public class TopicCourseTypeListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCourseTypeListener.class);

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ICourseTypeService courseTypeService;
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try{
        if (StringUtils.isEmpty(text)) {
            return;
        }
        String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
        logger.info("======TopicCourseTypeListener===课程类型监听器========" + json);
        Map<String, Object> maps = JsonUtils.readJson2Map(json);
        if (!platform_id.equals(maps.get("platformCode"))) {
            return;
        }
        if (maps.get("cmdType").equals("add")) {
            List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
            courseTypeService.deleteByAll();
            logger.info("======TopicCourseTypeListener======courseType全部删除成功====");
            Integer a = dataObj.size() / 900 + 1;
            List<List<Map<String, String>>> subList = CollectionUtils.subList(dataObj, dataObj.size() / a);
            List<CourseType> courseTypes = null;
            for (List<Map<String, String>> list : subList) {
                courseTypes = new ArrayList<CourseType>();
                for (Map map : list) {
                    CourseType courseType = new CourseType();
                    courseType.setIntId(map.get("courseTypeId").toString());
                    courseType.setVarName(map.get("courseTypeName").toString());
                    courseType.setVarDesc(map.get("courseTypeDesc").toString());
                    courseType.setIntPid(map.get("courseTypePid").toString());
                    courseType.setIntOrder(map.get("courseTypeOrder").toString());
                    courseType.setIntLevel(map.get("courseTypeLevel").toString());
                    courseType.setChrStatus(map.get("courseTypeStatus").toString());
                    courseType.setVarCreateUser(map.get("createUser").toString());
                    courseType.setDatCreateDate(map.get("createDate").toString());
                    courseType.setVarOperUser(map.get("operUser").toString());
                    courseType.setDatOperDate(map.get("operDate").toString());
                    courseTypes.add(courseType);
                }
                courseTypeService.insertBatch(courseTypes);
                logger.info("======TopicCourseTypeListener===课程类型增加成功========"+ JSONObject.toJSONString(courseTypes));
            }

        }
        }catch (Exception e){

            logger.error("======TopicCourseTypeListener==="+e.getMessage());

        }
    }
}
