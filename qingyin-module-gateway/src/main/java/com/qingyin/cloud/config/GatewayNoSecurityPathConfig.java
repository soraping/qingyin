package com.qingyin.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "validate.skips")
public class GatewayNoSecurityPathConfig {
    private Set<String> paths;
}
