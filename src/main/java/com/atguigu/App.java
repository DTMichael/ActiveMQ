package com.atguigu;

import org.apache.activemq.ActiveMQConnectionFactory;


import javax.jms.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final String ACTIVEMQ_URL="tcp://192.168.125.140:61616";
    public static final String QUERY_NAME="query02";
    public static void main( String[] args ) throws JMSException {
        //1 创建连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2 通过连接工厂，获取connection并启动
        Connection connection=activeMQConnectionFactory.createConnection();

        connection.start();
        //3 创建会话
        //3.1 params 事务，签收
        Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4 创建目的地，具体是队列还是主题
        //Destination destination=session.createQueue(QUERY_NAME);
        Queue quere=session.createQueue(QUERY_NAME);
        //5 创建消息生产者
        MessageProducer messageProducer=session.createProducer(quere);
        // 通过使用消息生产者messageproducer生产三条消息发送到mq的队列里
        for(int i=1;i<=6;i++)
        {
            //7 创建消息,就好比学生们按照要求写出的面试题消息
            TextMessage textMessage=session.createTextMessage("*****message**"+i);//理解为一个字符串
            //8 通过messageproducer,发送给mq
            messageProducer.send(textMessage);
        }
        //9 关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("消息发布完成");



    }
}
