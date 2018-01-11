package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import org.springframework.stereotype.Component;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * topic 监听器
 * @author DF
 *
 */
@Component("topicListener")
public class TopicListener extends AbstractListener {

}
