package org.jretty.logbackext.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
/**
 * 消息发送端实现
 * @author kevin
 *
 */
public class ProducerProcessor {
	private Producer<Integer, String> producer = null;
	private String topic;
	public ProducerProcessor(Properties props,String topic) {
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<Integer, String>(config);
		this.topic = topic;
	}

	public boolean sendMsg(String message) {
		boolean sendFlag = false;
		if (message == null || "".equals(message)) {
			return sendFlag;
		}

		if (topic == null || "".equals(topic)) {
			return sendFlag;
		}
		try {
			KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic,  message.hashCode(), message);
			producer.send(data);
			sendFlag =  true;
		} catch (Exception ex) {
		}
		return sendFlag;
	}
	
	public void close(){
		producer.close();
	}

}
