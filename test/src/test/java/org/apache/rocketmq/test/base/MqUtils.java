package org.apache.rocketmq.test.base;

//import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
//import com.alibaba.rocketmq.client.producer.SendResult;
//import com.alibaba.rocketmq.common.message.Message;
//import com.alibaba.rocketmq.common.message.MessageQueue;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MqUtils {
    private static MqUtils INSTANCE = null;
    private static String lock = "lock";
    private static String namesrvAddr;
    private static String producerGroupName;
    private static DefaultMQProducer producer;

    public static MqUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new MqUtils();
                    initConfig();
                }
            }
        }
        return INSTANCE;
    }


    private static void initConfig() {
        if (StringUtils.isBlank(namesrvAddr)) {
            initProperties();
        }
        initProducer();
    }

    private static void initProducer() {
        try {
            producer = new DefaultMQProducer(producerGroupName+"");
            producer.setNamesrvAddr(namesrvAddr);
            producer.setDefaultTopicQueueNums(8);
            producer.start();
        } catch (Exception e) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            initProducer();
        }
    }

    /**
     * 根据Message中的key哈希选择队列，key为空则随机选择队列
     * @param msg
     * @return
     */
    public boolean producerMsg(Message msg) {
//        String oldTopic = msg.getTopic();
//        msg.setTopic(topicPrefix.concat(oldTopic));
        try {
            if(StringUtils.isNotBlank(msg.getKeys()+"")){
                SendResult sendResult =producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        int index = Math.abs(msg.getKeys().hashCode()) % mqs.size();
                        return mqs.get(index);
                    }
                }, 0);
            }else{
                SendResult sendResult=producer.send(msg);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private static void initProperties() {
        try {
            Properties pros = new Properties();
            InputStream in = MqUtils.class.getClassLoader().getResourceAsStream("rocketmq.properties");
            pros.load(in);
            producerGroupName = pros.getProperty("rocketmq.config.producerGroupName");
            namesrvAddr = pros.getProperty("rocketmq.config.namesrvAddr");
            System.setProperty("rocketmq.client.log.loadconfig", "false");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
