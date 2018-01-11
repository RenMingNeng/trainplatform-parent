package com.bossien.framework.shiro.service;

import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

public class ShiroSessionMessage extends DefaultMessage {

    public final MessageBody msgBody;

    public ShiroSessionMessage(byte[] channel, byte[] body) {
        super(channel, body);
        msgBody = (MessageBody) new JdkSerializationRedisSerializer().deserialize(body);
    }


    public static class MessageBody implements Serializable {

        public final Serializable sessionId;
        public final String nodeId;
        public String msg = "";

        public MessageBody(Serializable sessionId, String nodeId) {
            this.sessionId = sessionId;
            this.nodeId = nodeId;
        }

        public Serializable getSessionId() {
            return sessionId;
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "MessageBody{" +
                    "sessionId=" + sessionId +
                    ", nodeId='" + nodeId + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
