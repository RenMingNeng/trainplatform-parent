package com.bossien.framework.mq.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


/**
 * 人员档案队列发送
 *
 */
@Component
public class QueuePersonDossierInfoSender extends AbstractSender {

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}
}
