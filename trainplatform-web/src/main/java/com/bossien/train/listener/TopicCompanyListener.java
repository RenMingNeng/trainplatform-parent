package com.bossien.train.listener;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;
import com.bossien.train.domain.Company;
import com.bossien.train.service.ICompanyService;
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
 * 单位监听器
 */
@Component("topicCompanyListener")
public class TopicCompanyListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCompanyListener.class);
    private String updateBatchCount = PropertiesUtils.getValue("updateBatchCount");//批量更新数量
    private String insertBatchCount = PropertiesUtils.getValue("insertBatchCount");//批量插入数量
    @Autowired
    private ICompanyService companyService;
    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件
    private String no_avail = PropertiesUtils.getValue("NO_AVAIL");//获取配置文件 无效

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try{
        if (StringUtils.isEmpty(text)) {
            return;
        }
        String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
        logger.info("=======topicCompanyListener==单位监听器========"+json);
        Map<String, Object> maps = JsonUtils.readJson2Map(json);//接受数据将数据转成MAP
        logger.info("=======topicCompanyListener==单位监听器========"+maps.get("platformCode"));
        if (!platform_id.equals(maps.get("platformCode"))) {
            return;
        }
        if (maps.get("cmdType").equals("add")) {//添加数据
            List<Map<String, Object>> list_add = new ArrayList<>();//新增集合
            List<Map<String, Object>> list_update = new ArrayList<>();//更新集合
            List<Map<String, Object>> dataObj = (List<Map<String, Object>>) maps.get("dataObj");
            for (Map<String, Object> params : dataObj) {
               /*分装数据*/
                Map<String, Object> par = new HashMap<String, Object>();
                par.put("intId", params.get("id"));
                par.put("varCode", params.get("code"));
                par.put("intTypeId", params.get("typeId"));
                par.put("varName", params.get("name"));
                par.put("varBusinessId", params.get("businessId"));
                if (params.get("categoryId").equals("")) {
                    par.put("intCategoryId", "4");
                } else {
                    par.put("intCategoryId", params.get("categoryId"));
                }
                par.put("intRegionId", params.get("regionId"));
                par.put("varSupporter", params.get("supporter"));
                par.put("varLegalPerson", params.get("legalPerson"));
                par.put("varContacter", params.get("contacter"));
                par.put("varContactInfo", params.get("contactInfo"));
                par.put("varPostCode", params.get("postCode"));
                par.put("varAddress", params.get("address"));
                par.put("varBankAccount", params.get("bankAccount"));
                par.put("chrIsRegulator", params.get("isRegulator"));
                par.put("chrIsValid", params.get("isValid"));
                par.put("chrIsTermination", params.get("isTermination"));
                par.put("intPid", params.get("pid"));
                par.put("intOrder", params.get("order"));
                par.put("varRemark", params.get("remark"));
                par.put("varCreateUser", params.get("createUser"));
                par.put("varOperUser", params.get("operUser"));
                par.put("datCreateDate", params.get("createDate"));
                par.put("datOperDate", params.get("operDate"));
                par.put("fullPath", params.get("fullPath"));
                Company company = companyService.selectById(par);
                if (null == company) {//没有数据，需要添加
                    if (list_add.size() >= Integer.parseInt(insertBatchCount)) {
                        companyService.insertBatch(list_add);
                        list_add = new ArrayList<>();
                        logger.info("=======TopicCompanyListener=" + params.get("id") + "= Company add success ==添加单位成功========");
                    } else {
                        list_add.add(par);
                    }
                } else {//有数据
                    if (list_update.size() >= Integer.parseInt(updateBatchCount)) {
                        companyService.updateBatch(list_update);
                        list_update = new ArrayList<>();
                        logger.info("=======TopicCompanyListener===Company_id:" + params.get("id") + "= Company update success ==更新单位成功========");
                    } else {
                        list_update.add(par);
                    }
                }
            }
            if (list_add.size() != 0) {
                companyService.insertBatch(list_add);
                logger.info("=======TopicCompanyListener==== Company add success ==添加单位成功========"+JSONObject.toJSONString(list_add));
            }
            if (list_update.size() != 0) {
                companyService.updateBatch(list_update);
                logger.info("=======TopicCompanyListener==== Company update success ==更新单位成功========"+JSONObject.toJSONString(list_update));
            }
        } else if (maps.get("cmdType").equals("delete")) {//数据不能真删除
            Map<String, Object> dataObj = (Map<String, Object>) maps.get("dataObj");
            List<Object> list = (List<Object>) dataObj.get("companyId");
            for (Object id : list) {
                Map<String, Object> par = new HashMap<String, Object>();
                par.put("intId", id);
                par.put("chrIsValid", no_avail);//无效chrIsValid
                companyService.updateByPrimaryKeySelective(par);
                logger.info("===TopicCompanyListener=Company_id:" + id + "= Company  delete success ==删除单位成功========");
            }

        }
        }catch (Exception e){

            logger.error("===TopicCompanyListener="+e.getMessage());

        }

    }
}
