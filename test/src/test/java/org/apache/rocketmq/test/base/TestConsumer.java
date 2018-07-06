package org.apache.rocketmq.test.base;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

public class TestConsumer {

    private String nameSrvAddr = "192.168.37.175:9876";
    private String topic = "reinhardt1_topic";
    private String group = "reinhardt1_group";

    private final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

    public void test() throws InterruptedException {
        Thread thread = new Thread(new ConsumerTask(), "rocketmq_callback_due_consumer_task");
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        TestConsumer testConsumer = new TestConsumer();
        testConsumer.test();
        Thread.currentThread().join();
    }

    private class ConsumerTask implements Runnable {

        @Override
        public void run() {
            consumer.setNamesrvAddr(nameSrvAddr);
            consumer.setConsumerGroup(group);
            try {
                //订阅
                consumer.subscribe(topic, "*");
                consumer.registerMessageListener(new MessageListenerConcurrently() {

                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        for (MessageExt msg : msgs) {
                            String msgBody = new String(msg.getBody());
                            if(!dueCallback(msg.getMsgId(), msg.getTopic(), msgBody, msg.getReconsumeTimes())) {
                                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                            }
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                });
                consumer.start();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private boolean dueCallback(String msgId, String topic, String msgBody, int reconsumeTimes) {
        System.out.println(msgId);
        System.out.println(topic);
        System.out.println(msgBody);
        System.out.println(reconsumeTimes);
        return true;
    }
}

