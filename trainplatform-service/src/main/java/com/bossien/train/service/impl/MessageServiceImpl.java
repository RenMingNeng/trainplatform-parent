package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.MessageMapper;
import com.bossien.train.domain.Message;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.CompanyRegisterStatusEnum;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private IBaseService<Message> messageBaseService;

    @Override
    public void send(String userId, String companyId, String messageTitle,
        String messageContent, String sendId, String sendName) {
        Message message = messageBaseService.build(sendName,
            new Message(userId, companyId, messageTitle, messageContent, sendId, sendName));
        messageMapper.insert(message);
    }

    @Override
    public void sendList(List<Message> messages) {

        messageMapper.insertBatch(messages);
    }

    @Override
    public void update(Map<String, Object> params) {

        messageMapper.update(params);
    }

    @Override
    public Page<Message> queryForPagination(Map<String, Object> params, Integer pageNum, Integer pageSize, User user) {
        Integer count = messageMapper.selectCount(params);
        Page<Message> page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Message> list = messageMapper.selectList(params);
        page.setDataList(list);
        return page;
    }

    @Override
    public List<Message> selectList(Map<String, Object> params) {

        return messageMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return messageMapper.selectCount(params);
    }

    @Override
    public Message selectOne(Map<String, Object> params) {

        return messageMapper.selectOne(params);
    }

    @Override
    public void deleteBatch(List<String> ids) {

        messageMapper.deleteBatch(ids);
    }

    @Override
    public void sendMessageToUser(Map<String, Object> params) {

        String messageTitle = Message.MSG_TPL_TITLE_0004;
        String messageContent = "";
        String userName = (String) params.get("userName");
        String userId = (String) params.get("userId");
        String result = (String) params.get("result");
        String registerStatus = (String) params.get("cmdType");
        String companyName = (String) params.get("companyName");
        String companyId = (String) params.get("companyCode");

        if (StringUtils.isEmpty(userId)) {
            return;
        }


        //审核通过
        if (CompanyRegisterStatusEnum.AUDIT_PASS.getCmd().equals(registerStatus)) {
            messageContent = Message.MSG_TPL_CONTENT_0004_success;
            messageContent = MessageFormat.format(messageContent, companyName,CompanyRegisterStatusEnum.AUDIT_PASS.getDesc(),companyId,companyName);

        } else if (CompanyRegisterStatusEnum.AUDIT_REJECT.getCmd().equals(registerStatus)) { //审核未通过
            messageContent = Message.MSG_TPL_CONTENT_0004_fail;
            messageContent = MessageFormat.format(messageContent, companyName, CompanyRegisterStatusEnum.AUDIT_REJECT.getDesc(), result);
        }


        Message message = messageBaseService.build(userName,
            new Message(userId,
                "",
                messageTitle,
                messageContent,
                userId,
                userName
            ));

        //添加消息
        messageMapper.insert(message);
    }
}
