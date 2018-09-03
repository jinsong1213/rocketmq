package org.apache.rocketmq.test.base;

import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

/**
 * @author 金松
 * @date 2018/7/2 下午2:49
 */
public class TestRocketMQ {

    @Test
    public void test1() throws InterruptedException {

        /**
         * rocketmq.t1.group=reinhardt1_group
         rocketmq.t1.topic=reinhardt1_topic
         rocketmq.t1.tags=reinhardt1_tags

         */
        Message msg = new Message();
        msg.setTopic("reinhardt1_topic");
        msg.setTags("reinhardt1_tags");
        msg.setBody("HelloWorld".getBytes());
        MqUtils mqUtils = MqUtils.getInstance();
        mqUtils.producerMsg(msg);

        Thread.currentThread().join();
    }
}
