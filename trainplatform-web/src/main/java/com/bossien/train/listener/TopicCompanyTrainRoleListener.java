package com.bossien.train.listener;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.CompanyTrainRole;
import com.bossien.train.service.ICompanyTrainRoleService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/28.
 * 公司和受训角色关联关系
 */

@Component("topicCompanyTrainRoleListener")
public class TopicCompanyTrainRoleListener extends AbstractListener {

    private static final Logger logger = LogManager.getLogger(TopicSubjectListener.class);

    @Autowired
    private ICompanyTrainRoleService companyTrainRoleService;

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
            logger.info("=======topicCompanyTrainRoleListener==公司和受训角色关联关系========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);
            List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
            logger.info("=======topicCompanyTrainRoleListener==公司和受训角色关联关系========" + maps.get("platformCode"));
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {
                List<CompanyTrainRole> list_add = new ArrayList<>();//新增集合
                for (Map<String, String> objectMap : dataObj) {//添加数据
                    String companyId = objectMap.get("companyId");
                    String[] ids = objectMap.get("id").replaceAll(" ", "").split(",");
                    Map<String, Object> params = new HashMap<>();
                    for (String id : ids) {
                        params.put("intTrainRoleId", id);
                        params.put("intCompanyId", companyId);
                        CompanyTrainRole companyTrainRole = companyTrainRoleService.selectById(params);
                        CompanyTrainRole record = new CompanyTrainRole();
                        record.setIntCompanyId(companyId);
                        record.setIntTrainRoleId(id);
                        if (null != companyTrainRole) {//说明有值
                            logger.info("======TopicCompanyTrainRoleListener==CompanyTrainRole_id:" + id
                                + "=CompanyRole update success ==受训角色关联关系已经有值========");
                        } else {
                            if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                                companyTrainRoleService.insertBatch(list_add);
                                list_add = new ArrayList<>();
                                logger.info("======TopicCompanyTrainRoleListener=CompanyTrainRole_id:" + id
                                    + "== CompanyRole add  success ==受训角色和单位关联关系增加成功========");
                            } else {
                                list_add.add(record);
                            }
                        }
                    }
                }
                if (list_add.size() != 0) {
                    companyTrainRoleService.insertBatch(list_add);
                    logger.info("======TopicCompanyTrainRoleListener=== CompanyRole add  success ==受训角色和单位关联关系增加成功========" + JSONObject
                        .toJSONString(list_add));
                }
            } else {
                for (Map<String, String> objectMap : dataObj) {//删除数据
                    Map<String, Object> params = new HashMap<>();
                    String companyId = String.valueOf(objectMap.get("companyId"));
                    String[] ids = String.valueOf(objectMap.get("id")).replaceAll(" ", "").split(",");
                    params.put("intCompanyId", companyId);
                    params.put("ids", ids);
                    companyTrainRoleService.deleteByAllId(params);
                    logger.info("======TopicCompanyTrainRoleListener=" + JSONObject.toJSONString(ids) + companyId
                        + "==CompanyRole delete success  ==受训角色和单位关联关系删除成功========");
                }
            }
        } catch (Exception e) {

            logger.error("======TopicCompanyTrainRoleListener=" + e.getMessage());

        }
    }


}
