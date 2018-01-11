package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Attachment;
import com.bossien.train.domain.dto.AttachmentDTO;
import com.bossien.train.domain.dto.AttachmentMessage;
import com.bossien.train.service.IAttachmentService;
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
 * 单位课程附件的消息队列
 */
@Component("topicAttachmentListener")
public class TopicAttachmentListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicAttachmentListener.class);

    @Autowired
    private IAttachmentService attachmentService;
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
            logger.info("=======topicAttachmentListener==附件监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            logger.info("=======topicAttachmentListener==附件监听器========" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                AttachmentDTO dto = JsonUtils.readValue(json, AttachmentDTO.class);//将json数据转换成javabean
                Map<String, Object> params = new HashMap<String, Object>();
                List<Attachment> list_add = new ArrayList<>();//新增集合
                List<Attachment> list_update = new ArrayList<>();//更新集合
                for (AttachmentMessage attachmentMessage : dto.getDataObj()) {
                    params.put("intId", attachmentMessage.getAttachmentId());
                    Attachment attachment = attachmentService.selectById(params);//通过ID查询是否有值
                    Attachment attachmentItem = new Attachment();
                    attachmentItem.setIntId(attachmentMessage.getAttachmentId());
                    attachmentItem.setVarFid(attachmentMessage.getAttachmentFid());
                    attachmentItem.setVarLocalName(attachmentMessage.getAttachmentLocalName());
                    attachmentItem.setVarRemotePath(attachmentMessage.getAttachmentRemotePath());
                    attachmentItem.setVarExt(attachmentMessage.getAttachmentExt());
                    attachmentItem.setVarType(attachmentMessage.getAttachmentType());
                    attachmentItem.setIntBusinessId(attachmentMessage.getAttachmentBusinessId());
                    attachmentItem.setVarMd5(attachmentMessage.getAttachmentMd5());
                    attachmentItem.setChrComplete(attachmentMessage.getAttachmentStatus());
                    attachmentItem.setDatCreateDate(attachmentMessage.getDatCreateDate());
                    attachmentItem.setDatOperDate(attachmentMessage.getDatOperDate());
                    attachmentItem.setVarCreateUser(attachmentMessage.getCreateUser());
                    attachmentItem.setVarOperUser(attachmentMessage.getOperUser());
                    if (attachment == null) {//说明没有重复的数据
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            attachmentService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info("=======TopicAttachmentListener===Attachment_id:" + attachmentItem.getIntId()
                                + "=  Attachment add  success ===附件增加成功========");
                        } else {
                            list_add.add(attachmentItem);
                        }
                    } else {
                        if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                            attachmentService.updateBatch(list_update);
                            list_update = new ArrayList<>();
                            logger.info("=======TopicAttachmentListener===Attachment_id:" + attachmentItem.getIntId()
                                + "=  Attachment add  success ===附件更新成功========");
                        } else {
                            list_update.add(attachmentItem);
                        }
                    }
                }
                if (list_add.size() != 0) {
                    attachmentService.insertBatch(list_add);
                    logger.info("=======TopicAttachmentListener==  Attachment add  success ===附件增加成功========" + JSONObject.toJSONString(list_add));
                }
                if (list_update.size() != 0) {
                    attachmentService.updateBatch(list_update);
                    logger.info(
                        "=======TopicAttachmentListener==  Attachment update  success ===附件更新成功========" + JSONObject.toJSONString(list_update));
                }
            }
        } catch (Exception e) {
            logger.error("=======TopicAttachmentListener=="+e.getMessage());

        }
    }


}

