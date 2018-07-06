package org.apache.rocketmq.common;

/**
 * 2 NameServer 2m-2s-async
 * @author 金松
 * @date 2018/6/22 下午3:30
 */
public class DebugConfig {

    public static String rocketHome(){
        return "/Users/jinsong/source/github/rocketmq";
    }

    public static int getNameServerPort(){
        //return System.currentTimeMillis() % 2 == 0?9876:9877;
        return 9876;
    }

    public static String getNameServerAddrs(){
        //return "127.0.0.1:9876;127.0.0.1:9877";
        return "192.168.37.175:9876";
    }

//    public static String getBrokerA(){
//        String master =  "/Users/jinsong/source/github/rocketmq/conf/2m-2s-async/broker-a.properties";
//        String slave = "/Users/jinsong/source/github/rocketmq/conf/2m-2s-async/broker-a-s.properties";
//        return System.currentTimeMillis() % 2==0?master:slave;
//    }
//
//    public static String getBrokerB(){
//        String master =  "/Users/jinsong/source/github/rocketmq/conf/2m-2s-async/broker-b.properties";
//        String slave = "/Users/jinsong/source/github/rocketmq/conf/2m-2s-async/broker-b-s.properties";
//        return System.currentTimeMillis() % 2==0?master:slave;
//    }

    public static void configBrokerA(BrokerConfig brokerConfig) {
        brokerConfig.setRocketmqHome(DebugConfig.rocketHome());
        brokerConfig.setBrokerClusterName("DefaultCluster");
        brokerConfig.setBrokerName("broker-a");
        configBrokerMaster(brokerConfig);
//        if(System.currentTimeMillis() % 2 ==0){
//            configBrokerMaster(brokerConfig);
//        }else{
//            configBrokerSlave(brokerConfig);
//        }
    }

    public static void configBrokerB(BrokerConfig brokerConfig) {
        brokerConfig.setRocketmqHome(DebugConfig.rocketHome());
        brokerConfig.setBrokerClusterName("DefaultCluster");
        brokerConfig.setBrokerName("broker-b");
        if(System.currentTimeMillis() % 2 ==0){
            configBrokerMaster(brokerConfig);
        }else{
            configBrokerSlave(brokerConfig);
        }
    }

    /**
    brokerClusterName=DefaultCluster
            brokerName=broker-a
    brokerId=0
    deleteWhen=04
    fileReservedTime=48
    brokerRole=ASYNC_MASTER
            flushDiskType=ASYNC_FLUSH
    **/
    private static void configBrokerMaster(BrokerConfig brokerConfig) {
        brokerConfig.setBrokerId(0);
    }

    private static void configBrokerSlave(BrokerConfig brokerConfig) {
        brokerConfig.setBrokerId(1);
    }


}
