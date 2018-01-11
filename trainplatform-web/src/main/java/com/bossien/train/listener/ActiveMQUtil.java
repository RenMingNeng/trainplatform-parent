package com.bossien.train.listener;

import com.bossien.train.util.PropertiesUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息队列工具类
 * activemq/conf/activemq.xml文件中 broker节点下，修改managementContext节点内容为
 * <managementContext>
 * <managementContext createConnector="true" connectorPort="1093" connectorPath="/jmxrmi" jmxDomainName="org.apache.activemq"/>
 * </managementContext>
 */
public class ActiveMQUtil {

    public static final Logger logger = LoggerFactory.getLogger(ActiveMQUtil.class);
   /* public static final String reportQueueName = "zc-queue-actual";//生成核对报告队列名*/

    private static final String connectorPort = PropertiesUtils.getValue("connectorPort");
    private static final String connectorPath = PropertiesUtils.getValue("connectorPath");
    private static final String jmxDomain = PropertiesUtils.getValue("jmxDomain");
    private static final String Address = PropertiesUtils.getValue("Address");
    private static final String brokerName = PropertiesUtils.getValue("brokerName");


    public static Long getQueueSize(String queueName) throws Exception {
        Map<String, Long> queueMap = getAllQueueSize();
        Long queueSize = 0L;
        if (queueMap.size() > 0) {
            queueSize = queueMap.get(queueName);
        }
        return queueSize;
    }


    public static Map<String, Long> getAllQueueSize() throws Exception {
        Map<String, Long> queueMap = new HashMap<String, Long>();
        BrokerViewMBean mBean = null;
        MBeanServerConnection connection = null;
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + Address + ":" + connectorPort + connectorPath);
        JMXConnector connector = JMXConnectorFactory.connect(url);
        connector.connect();
        connection = connector.getMBeanServerConnection();
        ObjectName name = new ObjectName(jmxDomain + ":brokerName=" + brokerName + ",type=Broker");
        mBean = MBeanServerInvocationHandler.newProxyInstance(connection, name, BrokerViewMBean.class, true);
        if (mBean != null) {
            for (ObjectName queueName : mBean.getQueues()) {
                QueueViewMBean queueMBean = MBeanServerInvocationHandler.newProxyInstance(connection, queueName, QueueViewMBean.class, true);
                queueMap.put(queueMBean.getName(), queueMBean.getQueueSize());
                //System.out.println("Queue Name --- " + queueMBean.getName());// 消息队列名称
                //System.out.println("Queue Size --- " + queueMBean.getQueueSize());// 队列中剩余的消息数
                //System.out.println("Number of Consumers --- " + queueMBean.getConsumerCount());// 消费者数
                //System.out.println("Number of Dequeue ---" + queueMBean.getDequeueCount());// 出队数
            }
        }
        return queueMap;
    }
}
/*
    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        String url = "service:jmx:rmi:///jndi/rmi://10.36.1.17:11099/jmxrmi";
        JMXServiceURL urls = new JMXServiceURL(url);
        JMXConnector connector = JMXConnectorFactory.connect(urls,null);
        connector.connect();
        MBeanServerConnection conn = connector.getMBeanServerConnection();
        //这里brokerName的b要小些，大写会报错
        ObjectName name = new ObjectName("org.apache.activemq:brokerName=loaclhost,type=Broker");
        BrokerViewMBean mBean = (BrokerViewMBean)MBeanServerInvocationHandler.newProxyInstance(conn, name, BrokerViewMBean.class, true);
        for(ObjectName na : mBean.getQueues()){//获取点对点的队列       mBean.getTopics() 获取订阅模式的队列
            QueueViewMBean queueBean = (QueueViewMBean)
                MBeanServerInvocationHandler.newProxyInstance(conn, na, QueueViewMBean.class, true);
            System.out.println("******************************");
            System.out.println("队列的名称："+queueBean.getName());
            System.out.println("队列中剩余的消息数："+queueBean.getQueueSize());
            System.out.println("消费者数："+queueBean.getConsumerCount());
            System.out.println("出队列的数量："+queueBean.getDequeueCount());
        }
    }

}*/
