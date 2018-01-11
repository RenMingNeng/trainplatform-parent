package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.TrainRole;
import com.bossien.train.domain.dto.TrainRoleDTO;
import com.bossien.train.domain.dto.TrainRoleMessage;
import com.bossien.train.service.ITrainRoleService;
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
 * 授权角色的监听器
 */
@Component("topicRoleListener")
public class TopicRoleListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicRoleListener.class);


    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    @Autowired
    private ITrainRoleService trainRoleService;

    private String system_number = PropertiesUtils.getValue("SYSTEM_NUMBER");//系统自带
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try {
            if (StringUtils.isEmpty(text)) {
                return;
            }
//
            String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
            logger.info("======TopicRoleListener===授权角色监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);//接受数据将数据转成MAP
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {//添加数据
                List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
                TrainRoleDTO dto = JsonUtils.readValue(json, TrainRoleDTO.class);//将json数据转换成javabean
                List<Map<String, Object>> list_add = new ArrayList<>();//新增集合
                List<Map<String, Object>> list_update = new ArrayList<>();//更新集合

                for (TrainRoleMessage trainRoleMessage : dto.getDataObj()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("varId", trainRoleMessage.getId());
                    map.put("roleName", trainRoleMessage.getRoleName());
                    map.put("roleDesc", trainRoleMessage.getRoleDesc());
                    map.put("source", system_number);
                    map.put("companyId", trainRoleMessage.getCompanyId());
                    map.put("isValid", trainRoleMessage.getIsValid());
                    map.put("createDate", trainRoleMessage.getCreateDate());
                    map.put("createUser", trainRoleMessage.getCreateUser());
                    map.put("operUser", trainRoleMessage.getOperUser());
                    map.put("operDate", trainRoleMessage.getOperDate());
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("varId", trainRoleMessage.getId());
                    TrainRole trainRole = trainRoleService.selectById(params);
                    if (null == trainRole) {//需要添加
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            trainRoleService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info("======TopicRoleListener==Role_id" + trainRoleMessage.getId() + "=trainrole add success==授权角色添加成功========");
                        } else {
                            list_add.add(map);
                        }
                    } else {//更新
                        if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                            trainRoleService.updateBatch(list_update);
                            list_update = new ArrayList<>();
                            logger
                                .info("======TopicRoleListener==Role_id" + trainRoleMessage.getId() + "= trainrole update success==授权角色更新成功========");
                        } else {
                            list_update.add(map);
                        }
                    }
                }
                if (list_add.size() != 0) {
                    trainRoleService.insertBatch(list_add);
                    logger.info("======TopicRoleListener===trainrole add success==授权角色添加成功========" + JSONObject.toJSONString(list_add));

                }
                if (list_update.size() != 0) {
                    trainRoleService.updateBatch(list_update);
                    logger.info("======TopicRoleListener=== trainrole update success==授权角色更新成功========" + JSONObject.toJSONString(list_update));

                }
            } else {
            /*删除*/
                Map<String, String> dataObj = (Map<String, String>) maps.get("dataObj");
                String[] ids = dataObj.get("id").replaceAll(" ", "").split(",");
                Map<String, Object> map = new HashMap<>();
                map.put("ids", ids);
                map.put("chrSource", system_number);
                trainRoleService.delete(map);
                logger.info("======TopicRoleListener===trainrole delete success==授权角色删除成功========" + JSONObject.toJSONString(ids));
            }
        } catch (Exception e) {

            logger.error("======TopicRoleListener==="+e.getMessage());

        }
    }
}
