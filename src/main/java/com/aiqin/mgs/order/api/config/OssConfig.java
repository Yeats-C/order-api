package com.aiqin.mgs.order.api.config;

import com.aiqin.mgs.order.api.base.OssProperties;
import com.aliyun.oss.OSSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Createed by sunx on 2018/10/8.<br/>
 */
@Configuration
public class OssConfig {

    @Resource
    private OssProperties ossProperties;

    @Bean
    OSSClient getOssClient() {
        return new OSSClient(this.ossProperties.getEndpoint(), this.ossProperties.getAccessKeyId(), this.ossProperties.getSecretAccessKey());
    }

    public OssConfig() {
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof OssConfig)) {
            return false;
        } else {
            OssConfig other = (OssConfig) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71:
                {
                    Object this$bucketName = this.ossProperties.getBucketName();
                    Object other$bucketName = other.ossProperties.getBucketName();
                    if (this$bucketName == null) {
                        if (other$bucketName == null) {
                            break label71;
                        }
                    } else if (this$bucketName.equals(other$bucketName)) {
                        break label71;
                    }

                    return false;
                }

                Object this$endpoint = this.ossProperties.getEndpoint();
                Object other$endpoint = other.ossProperties.getEndpoint();
                if (this$endpoint == null) {
                    if (other$endpoint != null) {
                        return false;
                    }
                } else if (!this$endpoint.equals(other$endpoint)) {
                    return false;
                }

                label57:
                {
                    Object this$accessKeyId = this.ossProperties.getAccessKeyId();
                    Object other$accessKeyId = other.ossProperties.getAccessKeyId();
                    if (this$accessKeyId == null) {
                        if (other$accessKeyId == null) {
                            break label57;
                        }
                    } else if (this$accessKeyId.equals(other$accessKeyId)) {
                        break label57;
                    }

                    return false;
                }

                Object this$secretAccessKey = this.ossProperties.getSecretAccessKey();
                Object other$secretAccessKey = other.ossProperties.getSecretAccessKey();
                if (this$secretAccessKey == null) {
                    if (other$secretAccessKey != null) {
                        return false;
                    }
                } else if (!this$secretAccessKey.equals(other$secretAccessKey)) {
                    return false;
                }

                Object this$remoteEndpoint = this.ossProperties.getRemoteEndpoint();
                Object other$remoteEndpoint = other.ossProperties.getRemoteEndpoint();
                if (this$remoteEndpoint == null) {
                    if (other$remoteEndpoint == null) {
                        return true;
                    }
                } else if (this$remoteEndpoint.equals(other$remoteEndpoint)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof OssConfig;
    }
    @Override
    public int hashCode() {
        int result = 1;
        Object $bucketName = this.ossProperties.getBucketName();
        result = result * 59 + ($bucketName == null ? 43 : $bucketName.hashCode());
        Object $endpoint = this.ossProperties.getEndpoint();
        result = result * 59 + ($endpoint == null ? 43 : $endpoint.hashCode());
        Object $accessKeyId = this.ossProperties.getAccessKeyId();
        result = result * 59 + ($accessKeyId == null ? 43 : $accessKeyId.hashCode());
        Object $secretAccessKey = this.ossProperties.getSecretAccessKey();
        result = result * 59 + ($secretAccessKey == null ? 43 : $secretAccessKey.hashCode());
        Object $remoteEndpoint = this.ossProperties.getRemoteEndpoint();
        result = result * 59 + ($remoteEndpoint == null ? 43 : $remoteEndpoint.hashCode());
        return result;
    }
    @Override
    public String toString() {
        return "OssConfig(" + this.ossProperties.toString() + ")";
    }
}
