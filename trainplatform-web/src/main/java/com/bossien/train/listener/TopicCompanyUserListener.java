package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.common.AppConstant;

import com.bossien.train.domain.*;
import com.bossien.train.domain.dto.CompanyUserDTO;
import com.bossien.train.domain.dto.CompanyUserMessage;
import com.bossien.train.service.ICompanyUserService;
import com.bossien.train.service.IUserRoleService;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.JsonUtils;
import com.bossien.train.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 * <p>
 * 单位管理员
 *
 * @author DF
 */

@Component("topicCompanyUserListener")
public class TopicCompanyUserListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(TopicCompanyUserListener.class);

    private String platform_id = PropertiesUtils.getValue("PLATFORM_ID");//获取配置文件


    @Autowired
    private ICompanyUserService companyUserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    @SuppressWarnings("unchecked")
    public void textMessageHander(String text) throws UnsupportedEncodingException {
        try{
        if (StringUtils.isEmpty(text)) {
            return;
        }
        String json = URLDecoder.decode(text, AppConstant.DEFAULT_CHARSET);//首先对数据进行格式转码
        logger.info("==========TopicCompanyUserListener =====单位管理员消息监听==================" + json);
        Map<String, Object> maps = JsonUtils.readJson2Map(json);
        if (!platform_id.equals(maps.get("platformCode"))) {
            logger.info("==========TopicCompanyUserListener =====单位管理员消息监听===========不是平台=======");
            return;
        }
        if (maps.get("cmdType").equals("add")) {
            List<Map<String, String>> dataObj = (List<Map<String, String>>) maps.get("dataObj");
            CompanyUserDTO dto = JsonUtils.readValue(json, CompanyUserDTO.class);//将json数据转换成javabean
            Map<String, Object> params = new HashMap<String, Object>();
            for (CompanyUserMessage companyUserMessage : dto.getDataObj()) {
                params.put("varAccount", companyUserMessage.getAccount());
                params.put("intId", companyUserMessage.getId());
                params.put("id", companyUserMessage.getId());//用账号进行查询
                CompanyUser companyUser = companyUserService.selectById(params);//查询companyUser
                if (null == companyUser) {
                    companyUserService.insertMessage(companyUserMessage);//向companyUser表中插入数据
                    logger.info(
                        "==========TopicCompanyUserListener==Account:" + companyUserMessage.getAccount() + "===companyUser_id=:" + companyUserMessage
                            .getId()
                            + "CompanyId：" + companyUserMessage.getCompanyId()
                            + "=== CompanyUser add success 单位管理员消息添加companyUser表==================");
                } else {
                    companyUserService.updateMessage(companyUserMessage);
                    logger.info("==========TopicCompanyUserListener ====Account:" + companyUserMessage.getAccount() +
                        "===companyUser_id=:" + companyUserMessage.getId() + "CompanyId：" + companyUserMessage.getCompanyId() +
                        "= CompanyUser update success 单位管理员消息更新companyUser表==================");
                }
                User user = userService.selectById(params);//查询user表
                if (null == user) {//两个表中都没有数据
                    userService.insertMessage(companyUserMessage);//插入user表和userRole表
                    logger.info(
                        "==========TopicCompanyUserListener ==Account:" + companyUserMessage.getAccount() + "===id=:" + companyUserMessage.getId()
                            + "CompanyId：" + companyUserMessage.getCompanyId()
                            + "=== user and  userRole  add success ==单位管理员消息插入user表和userRole表==================");
                } else {
                    //如果角色存在，则修改
                    userService.updateMessage(companyUserMessage,user.getId());//
                    logger.info("==========TopicCompanyUserListener ==帐号" + companyUserMessage.getAccount() + "===id=:" + companyUserMessage.getId()
                        + "CompanyId：" + companyUserMessage.getCompanyId()
                        + "==user and  userRole  update success =单位管理员消息更新user表和userRole表==================");
                }
            }


        } else {//删除单位管理员
            List<Map<String, String>> prams = (List<Map<String, String>>) maps.get("dataObj");
            for (Map<String, String> map : prams) {
                String id = String.valueOf(map.get("id"));
                String intCompanyId = String.valueOf(map.get("companyId"));
                String account = map.get("account");
                //*需要删除三个表中的数据 消息表  管理员表和关联表
                List<String> list = new ArrayList<>();
                list.add(id);
                userRoleService.deleteByUserRole(list);//批量删除user_role表
                userService.deleteByUser(id);//删除user表
                Map<String, Object> map1 = new HashMap<>();
                map1.put("codes", String.valueOf(map.get("id")).replaceAll(" ", "").split(","));
                map1.put("intCompanyId", intCompanyId);
                map1.put("varAccount", account);
                companyUserService.deleteByPrimaryKey(map1);//删除ap_company_user表
                logger.info("==========TopicCompanyUserListener ==id:" + id + "Account" + account + "CompanyId：" + intCompanyId
                    + "=== user and  userRole and  CompanyUser delete successs ==单位管理员消息删除user表和userRole表和companyUser表删除成功==================");
            }
        }
        }catch (Exception e){
            logger.error("==========TopicCompanyUserListener =="+e.getMessage());
        }
    }
}

