package com.aiqin.mgs.order.api.base;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Createed by sunx on 2018/10/8.<br/>
 */
@ConfigurationProperties(prefix = "oss.config")
@Data
@Component
public class OssProperties {
    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String remoteEndpoint;
}
