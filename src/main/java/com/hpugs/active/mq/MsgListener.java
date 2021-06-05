package com.hpugs.active.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Description 消息监听器
 * @author 高尚
 * @version 1.0
 * @date 创建时间：2018年1月31日 下午4:52:36
 */
public class MsgListener implements MessageListener {
	
	/**
	 * 监听的会话队列
	 */
	private static String queryName;

	@Override
	public void onMessage(Message msg) {
		TextMessage textMsg = (TextMessage) msg;
		try {
			if(null != textMsg){
				System.out.println("【" + queryName + "】发送的消息：" + textMsg.getText());
			}
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("获取会话消息异常");
		}
	}

	public MsgListener(String queryName) {
		super();
		// TODO Auto-generated constructor stub
		this.queryName = queryName;
	}
	
}
