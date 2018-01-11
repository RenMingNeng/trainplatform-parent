package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Company;
import com.bossien.train.domain.CompanyType;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.ICompanyTypeService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Administrator on 2017/8/16.
 * <p>
 * 消息队列
 * 单位种类监听器
 */
@Component("topicCompanyTypeListener")
public class TopicCompanyTypeListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCompanyTypeListener.class);

    @Autowired
    private ICompanyTypeService companyTypeService;
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
            logger.info("=======TopicCompanyTypeListener==公司类型监听器========" + json);
            Map<String, Object> maps = JsonUtils.readJson2Map(json);//接受数据将数据转成MAP
            if (!platform_id.equals(maps.get("platformCode"))) {
                return;
            }
            if (maps.get("cmdType").equals("add")) {//添加数据
                List<Map<String, Object>> dataObj = (List<Map<String, Object>>) maps.get("dataObj");
                List<Map<String, Object>> list_add = new ArrayList<>();//新增集合
                List<Map<String, Object>> list_update = new ArrayList<>();//更新集合
                for (Map<String, Object> params : dataObj) {
               /*分装数据*/
                    Map<String, Object> par = new HashMap<String, Object>();
                    par.put("intId", params.get("id"));
                    par.put("intPid", params.get("pid"));
                    par.put("varName", params.get("name"));
                    par.put("intOrder", params.get("order"));
                    par.put("varCreateUser", params.get("createUser"));
                    par.put("varCreateUser", params.get("createUser"));
                    par.put("datCreateDate", params.get("createDate"));
                    par.put("varOperUser", params.get("operUser"));
                    par.put("datOperDate", params.get("operDate"));
                    CompanyType companyType = companyTypeService.selectById(par);
                    if (null == companyType) {//没有数据需要添加
                        if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                            companyTypeService.insertBatch(list_add);
                            list_add = new ArrayList<>();
                            logger.info(
                                "=======TopicCompanyTypeListener=CompanyType_id:" + params.get("id") + "= CompanyType add success==单位类型添加成功========");
                        } else {
                            list_add.add(par);
                        }
                    } else {//有数据需要更新
                        if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                            companyTypeService.updateBatch(list_update);
                            list_update = new ArrayList<>();
                            logger.info(
                                "=======TopicCompanyTypeListener=CompanyType_id:" + params.get("id")
                                    + "=CompanyType update success ==单位类型更新成功========");
                        } else {
                            list_update.add(par);
                        }
                    }
                }
                if (list_add.size() != 0) {
                    companyTypeService.insertBatch(list_add);
                    logger.info("======TopicCompanyTypeListener== CompanyType add success==单位类型添加成功========" + JSONObject.toJSONString(list_add));
                }
                if (list_update.size() != 0) {
                    companyTypeService.updateBatch(list_update);
                    logger.info(
                        "=======TopicCompanyTypeListener==CompanyType update success ==单位类型更新成功========" + JSONObject.toJSONString(list_update));

                }
            }
        } catch (Exception e) {

            logger.error( "=======TopicCompanyTypeListener=="+e.getMessage());

        }
    }
}
