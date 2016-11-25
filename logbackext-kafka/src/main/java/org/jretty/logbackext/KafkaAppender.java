package org.jretty.logbackext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.jretty.logbackext.kafka.ProducerProcessor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class KafkaAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	
	JSONEventLayout layout;
	ProducerProcessor producer = null;
	String broker = null;
	String topicid = null;
	//在producer queue的缓存的数据最大时间，仅仅for asyc
	String queueBuffMaxMs = null;
	//producer 缓存的消息的最大数量，仅仅for asyc
	String queueBuffMaxMsgSize = null;
	//一批消息的数量，仅仅for asyc
	String batchMsgNum = null;
	//确认超时时间
	String requestTimeoutMs = null;

	public KafkaAppender() {
		layout = new JSONEventLayout();
		try {
			setSourceHost(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
		}
	}

	@Override
	protected void append(ILoggingEvent event) {
		try {
			String json = layout.doLayout(event);
			producer.sendMsg(json);
		} catch (Exception e) {
		}
	}

	public String getSource() {
		return layout.getSource();
	}

	public void setSource(String source) {
		layout.setSource(source);
	}

	public String getSourceHost() {
		return layout.getSourceHost();
	}

	public void setSourceHost(String sourceHost) {
		layout.setSourceHost(sourceHost);
	}

	public String getSourcePath() {
		return layout.getSourcePath();
	}

	public void setSourcePath(String sourcePath) {
		layout.setSourcePath(sourcePath);
	}

	public String getTags() {
		if (layout.getTags() != null) {
			Iterator<String> i = layout.getTags().iterator();
			StringBuilder sb = new StringBuilder();
			while (i.hasNext()) {
				sb.append(i.next());
				if (i.hasNext()) {
					sb.append(',');
				}
			}
			return sb.toString();
		}
		return null;
	}

	public void setTags(String tags) {
		if (tags != null) {
			String[] atags = tags.split(",");
			layout.setTags(Arrays.asList(atags));
		}
	}

	public String getType() {
		return layout.getType();
	}

	public void setType(String type) {
		layout.setType(type);
	}

	public void setMdc(boolean flag) {
		layout.setProperties(flag);
	}

	public boolean getMdc() {
		return layout.getProperties();
	}

	public void setLocation(boolean flag) {
		layout.setLocationInfo(flag);
	}

	public boolean getLocation() {
		return layout.getLocationInfo();
	}

	public void setCallerStackIndex(int index) {
		layout.setCallerStackIdx(index);
	}

	public int getCallerStackIndex() {
		return layout.getCallerStackIdx();
	}

	public void addAdditionalField(AdditionalField p) {
		layout.addAdditionalField(p);
	}

	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}
	
	

	public String getQueueBuffMaxMs() {
		return queueBuffMaxMs == null || "".equals(queueBuffMaxMs)? "1000" : queueBuffMaxMs;
	}

	public void setQueueBuffMaxMs(String queueBuffMaxMs) {
		this.queueBuffMaxMs = queueBuffMaxMs;
	}

	public String getQueueBuffMaxMsgSize() {
		return queueBuffMaxMsgSize == null || "".equals(queueBuffMaxMsgSize)? "10000" : queueBuffMaxMsgSize;
	}

	public void setQueueBuffMaxMsgSize(String queueBuffMaxMsgSize) {
		this.queueBuffMaxMsgSize = queueBuffMaxMsgSize;
	}

	public String getBatchMsgNum() {
		return batchMsgNum == null || "".equals(batchMsgNum) ? "100" : batchMsgNum;
	}

	public void setBatchMsgNum(String batchMsgNum) {
		this.batchMsgNum = batchMsgNum;
	}

	public String getRequestTimeoutMs() {
		return requestTimeoutMs == null || "".equals(requestTimeoutMs) ? "5000" : requestTimeoutMs;
	}

	public void setRequestTimeoutMs(String requestTimeoutMs) {
		this.requestTimeoutMs = requestTimeoutMs;
	}

	@Override
	public void start() {
		super.start();
		Properties props = new Properties();
		props.put("serializer.class", "com.cst.logback.kafka.KeywordMessageEncoder");
		props.put("partitioner.class", "com.cst.logback.kafka.TopicPartitioner");
		props.put("metadata.broker.list", this.getBroker());
		/**
		 * async:异步，sync:同步
		 */
		props.put("producer.type", "async");
		props.put("message.send.max.retries", "2");
		props.put("retry.backoff.ms", "300");
		props.put("queue.buffering.max.ms", this.getQueueBuffMaxMs());
		props.put("queue.buffering.max.message", this.getQueueBuffMaxMsgSize());
		//0当queue满时丢掉
		props.put("queue.enqueue.timeout.ms", "0");
		props.put("batch.num.messages", this.getBatchMsgNum());
		props.put("request.timeout.ms", this.getRequestTimeoutMs());
		producer = new ProducerProcessor(props, this.getTopicid());
	}

	@Override
	public void stop() {
		super.stop();
		if(producer != null){
			producer.close();
		}
	}

}
