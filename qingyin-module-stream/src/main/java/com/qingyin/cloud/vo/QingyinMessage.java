package com.qingyin.cloud.vo;


import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson2.JSON;
import com.qingyin.cloud.util.YmlUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QingyinMessage {
	private Integer msgOwnerId;
	private String appName;
	private String content;
	
	public static QingyinMessage payload(String content) {
		return QingyinMessage.builder()
				.msgOwnerId(Integer.valueOf(YmlUtils.get("server.port")))
				.appName(YmlUtils.get("spring.application.name"))
				.content(content)
				.build();
	}
	
	/**
	 * 消息体序列化
	 * @param <T>
	 * @param data
	 * @return
	 */
	public static <T> Message<QingyinMessage> buildData(T data) {
		String content = data instanceof String ? (String) data : JSON.toJSONString(data);
		QingyinMessage payloadMessage = QingyinMessage.payload(content);
		return MessageBuilder
					.withPayload(payloadMessage)
					.setHeader("appName", YmlUtils.get("spring.application.name"))
					.build();
	}
}
