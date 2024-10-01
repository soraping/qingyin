package com.qingyin.cloud.msg;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StreamListener {

	@Bean
	public Consumer<Message<String>> registerConsumer() {
		return (data) -> {
			String payload = data.getPayload();
			MessageHeaders headers = data.getHeaders();
			String appNameString = (String) headers.get("appName");
			log.info("Consumer header appName => {}", appNameString);
		};
	}
	
}
