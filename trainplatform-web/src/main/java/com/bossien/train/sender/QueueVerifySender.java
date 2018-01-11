package com.bossien.train.sender;

import com.bossien.framework.mq.sender.AbstractSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


/**
 * 队列发送
 * @author DF
 *
 */
@Component
public class QueueVerifySender extends AbstractSender {

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}
}
