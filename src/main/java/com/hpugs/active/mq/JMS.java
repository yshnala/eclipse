package com.hpugs.active.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @Description 消息发送对象
 * @author 高尚
 * @version 1.0
 * @date 创建时间：2018年1月31日 下午3:25:53
 */
public class JMS {
	
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

	private static ConnectionFactory factory;//会话连接工厂
	private static Connection connection;//会话连接
	private static Session session;//会话接收或发送消息线程
	private static Destination destination;//消息的目的地
	private static MessageProducer messageProducer;//消息发送者
	private static MessageConsumer messageConsumer;//消息接收者
	
	static{
		factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);//创建会话连接工厂
	}
	
	/**
	 * @Description 发送消息
	 * @param queryName 消息队列名称
	 * @param msg 消息内容
	 * @return
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午3:54:22
	 */
	public static boolean producerSendQueryMessage(final String queryName, final String msg){
		boolean flag = true;
		try {
			connection = factory.createConnection();//创建会话连接
			connection.start();//启动会话连接
			/**
			 * Session session = connection.createSession(paramA,paramB);
				paramA是设置事务，paramB是设置acknowledgment mode 

				paramA 取值有：
				1、true：支持事务
				为true时：paramB的值忽略， acknowledgment mode被jms服务器设置为SESSION_TRANSACTED 。 　
				2、false：不支持事务 
				为false时：paramB的值可为Session.AUTO_ACKNOWLEDGE、Session.CLIENT_ACKNOWLEDGE、DUPS_OK_ACKNOWLEDGE其中一个。
				
				paramB 取值有：
				1、Session.AUTO_ACKNOWLEDGE：为自动确认，客户端发送和接收消息不需要做额外的工作。
				2、Session.CLIENT_ACKNOWLEDGE：为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会删除消息。 
				3、DUPS_OK_ACKNOWLEDGE：允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效。
				4、SESSION_TRANSACTED
			 */
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);//创建会话线程
			destination = session.createQueue(queryName);//创建消息队列
			messageProducer = session.createProducer(destination);//创建会话生成者
			Message message = session.createTextMessage(msg);//创建消息对象
			messageProducer.send(message);//发送消息
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
			flag = false;
		}finally {
			if(null != connection){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	public static boolean producerSendTopicMessage(final String queryName, final String msg) {
		boolean flag = true;
		try {
			connection = factory.createConnection();//创建会话连接
			connection.start();//启动会话连接
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);//创建会话线程
			destination = session.createTopic(queryName);//创建消息发布
			messageProducer = session.createProducer(destination);//创建会话生成者
			Message message = session.createTextMessage(msg);//创建消息对象
			messageProducer.send(message);//发送消息
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
			flag = false;
		}finally {
			if(null != connection){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	/**
	 * @Description 接收消息队列中的消息
	 * @param queryName 消息队列名称
	 * @return
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:24:14
	 */
	public static void consumerGetQueryMessage(final String queryName){
		try {
			connection = factory.createConnection();//创建会话连接
			connection.start();//启动会话连接
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queryName);
			messageConsumer = session.createConsumer(destination);
			while(true){
				TextMessage message = (TextMessage) messageConsumer.receive();
				if(null != message){
					System.out.println(queryName+"发送消息："+message.getText());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("消费消息异常");
		}finally {
			if(null != connection){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @Description 通过Listener接收消息队列中的消息
	 * @param queryName 消息队列名称
	 * @return
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:24:14
	 */
	public static void consumerGetQueryMessageListener(String queryName) {
		try {
			connection = factory.createConnection();//创建会话连接
			connection.start();//启动会话连接
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queryName);
			messageConsumer = session.createConsumer(destination);
			MsgListener msgListener = new MsgListener(queryName);
			messageConsumer.setMessageListener(msgListener);
			System.out.println("prod");
			while(true){
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("消费消息异常");
		}finally {
			if(null != connection){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void consumerGetTopicMessageByListener(String queryName) {
		try {
			connection = factory.createConnection();//创建会话连接
			connection.start();//启动会话连接
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(queryName);
			messageConsumer = session.createConsumer(destination);
			MsgListener msgListener = new MsgListener(queryName);
			messageConsumer.setMessageListener(msgListener);
			while(true){
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("消费消息异常");
		}finally {
			if(null != connection){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
