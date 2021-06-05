package com.hpugs.active.mq.test;

import org.junit.Test;

import com.hpugs.active.mq.JMS;

/**
 * @Description 会话测试
 * @author 高尚
 * @version 1.0
 * @date 创建时间：2018年1月31日 下午3:55:38
 */
public class JMSTest {
	
	/**
	 * @Description 发送消息
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:22:06
	 */
	@Test
	public void producerSendQueryMessage(){
		System.out.println("producerSendQueryMessage");
		boolean flag = JMS.producerSendQueryMessage("First Query2", "Hello ActiveMQ");
		if(flag){
			System.out.println("发送成功");
		}else{
			System.out.println("发送失败");
		}
	}
	
	/**
	 * @Description 发布消息
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:22:06
	 */
	@Test
	public void producerSendTopicMessage(){
		System.out.println("producerSendTopicMessage");
		boolean flag = JMS.producerSendTopicMessage("First Topic1", "Hello ActiveMQ");
		if(flag){
			System.out.println("发送成功");
		}else{
			System.out.println("发送失败");
		}
	}
	
	/**
	 * @Description 接收消息
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:36:01
	 */
	@Test
	public void consumerGetQueryMessage(){
		System.out.println("consumerGetQueryMessage");
		JMS.consumerGetQueryMessage("First Query1");
	}
	
	/**
	 * @Description 接收消息
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:36:01
	 */
	@Test
	public void consumerGetQueryMessageByListener(){
		System.out.println("consumerGetQueryMessageByListener");
		JMS.consumerGetQueryMessageListener("First Query2");
	}
	
	/**
	 * @Description 订阅消息
	 *
	 * @author 高尚
	 * @version 1.0
	 * @date 创建时间：2018年1月31日 下午4:36:01
	 */
	@Test
	public void consumerGetTopicMessageByListener(){
		System.out.println("consumerGetTopicMessageByListener");
		JMS.consumerGetTopicMessageByListener("First Topic1");
	}

}
