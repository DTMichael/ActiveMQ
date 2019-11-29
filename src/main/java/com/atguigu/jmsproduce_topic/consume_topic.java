package com.atguigu.jmsproduce_topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class consume_topic {
    public static final String ACTIVEMQ_URL="tcp://192.168.125.140:61616";
    public static final String TOPIC_NAME="atguigu_topic";

    //同步阻塞方式（recive()）
    //订阅者或接受者调用MessageConsume的recive方法来接受消息时，recive能够在接收到消息之前一直阻塞
    //每个消息只能由一个消费者消费
    public void Consume() throws JMSException {
        System.out.println("消费者1已经成功接收到消息");
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection=activeMQConnectionFactory.createConnection();
        connection.start();
        Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue query=session.createQueue(TOPIC_NAME);
        MessageConsumer messageConsumer=session.createConsumer(query);
        while(true)
        {
            //同步阻塞方式（recive）
            TextMessage textMessage = (TextMessage) messageConsumer.receive(8000);
            if(null!=textMessage)
            {
                System.out.println("*******消费者接收到消息*****"+textMessage.getText());
            }
            else{
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();
    }



    //通过监听的方法来获得消息
    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("2号消费者就位");
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection=activeMQConnectionFactory.createConnection();
        connection.start();
        Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic=session.createTopic(TOPIC_NAME);
        MessageConsumer messageConsumer=session.createConsumer(topic);
//        异步非阻塞方式（通过setMessageListener注册一个消息监听器，当消息到达，系统自动调用监听器MessageListener的onmessage方法）
        /*messageConsumer.setMessageListener(new MessageListener() {
              @Override
              public void onMessage(Message message) {
                  if(message!=null&&message instanceof TextMessage)
                  {
                      TextMessage textMessage=(TextMessage)message;
                      try {
                          System.out.println("消费者接收到消息"+textMessage.getText());
                      } catch (JMSException e) {
                          e.printStackTrace();
                      }
                  }
              }
          });*/
        //代替原有的匿名内部类，采用lambda表达式
        messageConsumer.setMessageListener((message)->{
            if(message!=null&&message instanceof TextMessage)
            {
                TextMessage textMessage=(TextMessage)message;
                try {
                    System.out.println("消费者接收到TOPIC消息"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();

        messageConsumer.close();
        session.close();
        connection.close();
    }
}
