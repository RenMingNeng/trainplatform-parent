package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Question;
import com.bossien.train.domain.dto.QuestionDTO;
import com.bossien.train.domain.dto.QuestionMessage;
import com.bossien.train.service.IQuestionService;
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
 * 课程题库的监听器
 */
@Component("topicQuestionListener")
public class TopicQuestionListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicQuestionListener.class);
    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private IQuestionService questionService;
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
        logger.info("======TopicQuestionListener===题库监听器========" + json);
        Map<String, Object> maps = JsonUtils.readJson2Map(json);
        if (!platform_id.equals(maps.get("platformCode"))) {
            return;
        }
        if (maps.get("cmdType").equals("add")) {
            List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
            QuestionDTO dto = JsonUtils.readValue(json, QuestionDTO.class);//将json数据转换成javabean
            Map<String, Object> params = new HashMap<String, Object>();
            List<Question> list_add = new ArrayList<>();//新增集合
            List<Question> list_update = new ArrayList<>();//更新集合

            for (QuestionMessage questionMessage : dto.getDataObj()) {
                Question questionItem = new Question();
                questionItem.setIntId(questionMessage.getQuestionId());
                questionItem.setVarNo(questionMessage.getQuestionNo());
                questionItem.setVarTitle(questionMessage.getQuestionTitle());
                questionItem.setVarContent(questionMessage.getQuestionContent());
                questionItem.setChrCategory(questionMessage.getQuestionCategory());
                questionItem.setChrType(questionMessage.getQuestionType());
                questionItem.setIntDifficult(questionMessage.getQuestionDifficult());
                questionItem.setChrValid(questionMessage.getQuestionValid());
                questionItem.setVarAnswer(questionMessage.getQuestionAnswer());
                questionItem.setVarAnswerDesc(questionMessage.getQuestionAnswerDesc());
                questionItem.setVarSource(questionMessage.getQuestionSource());
                questionItem.setVarAnalysis(questionMessage.getQuestionAnalysis());
                questionItem.setVarExamPoint(questionMessage.getQuestionExamPoint());
                questionItem.setIntImportant(questionMessage.getQuestionImportant());
                questionItem.setVarIndustry(questionMessage.getQuestionIndustry());
                questionItem.setVarCreateUser(questionMessage.getCreateUser());
                questionItem.setDatCreateDate(questionMessage.getDatCreateDate());
                questionItem.setVarOperUser(questionMessage.getOperUser());
                questionItem.setDatOperDate(questionMessage.getDatOperDate());
                params.put("intId", questionMessage.getQuestionId());
                Question question = questionService.selectById(params);//通过编号查询是否有值
                if (null == question) { //没有重复的数据
                    if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                        questionService.insertBatch(list_add);
                        list_add = new ArrayList<>();
                        logger.info("======TopicQuestionListener=QuestionId:" + questionMessage.getQuestionId() + "== Question add success==题库添加成功========");
                    } else {
                        list_add.add(questionItem);
                    }
                    logger.info("======TopicQuestionListener=QuestionId:" + questionMessage.getQuestionId() + "== Question add success==题库添加成功========");
                } else { //重复数据更新
                    if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                        questionService.updateBatch(list_update);
                        list_update = new ArrayList<>();
                        logger
                            .info("======TopicQuestionListener=QuestionId:" + questionMessage.getQuestionId() + "== Question update success==题库更新成功========"+JSONObject.toJSONString(list_update));
                    } else {
                        list_update.add(questionItem);
                    }
                }
            }
            if (list_add.size() != 0) {
                questionService.insertBatch(list_add);
                logger.info("======TopicQuestionListener=== Question add success==题库添加成功========"+JSONObject.toJSONString(list_add));

            }
            if (list_update.size() != 0) {
                questionService.updateBatch(list_update);
                logger.info("======TopicQuestionListener== Question update success==题库更新成功========"+JSONObject.toJSONString(list_update));

            }
        }
       }catch (Exception e){

           logger.error("======TopicQuestionListener=="+e.getMessage());

       }
    }
}
