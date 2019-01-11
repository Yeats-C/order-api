package com.aiqin.mgs.order.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OssProperties
 *
 * @author zhangtao
 * @createTime 2019/1/2 下午6:49
 * @description
 */
@ConfigurationProperties(
        prefix = "visit.oss.config"
)
@Data
public class OssProperties {
    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String remoteEndpoint;
}
