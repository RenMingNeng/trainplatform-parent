package com.bossien.train.listener;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.springframework.jms.support.converter.MessageConversionException;

/**
 * Created by Administrator on 2017/11/21.
 */
public class MessageConverter implements org.springframework.jms.support.converter.MessageConverter {

    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        Message message=session.createObjectMessage((Serializable) o);
        /*int group=Integer.parseInt((()o).getContent());*/
        message.setStringProperty("JMSXGroupID","001");
//        message.setIntProperty("JMSXGroupSeq", -1);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        ObjectMessage objMessage = (ObjectMessage) message;
        return objMessage.getObject();
    }
}
