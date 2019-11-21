package com.aiqin.mgs.order.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UrlProperties
 *
 * @author zhangtao
 * @createTime 2018/12/29 下午2:00
 * @description
 */
@ConfigurationProperties(
        prefix = "bridge.url"
)
@Data
public class UrlProperties {

    /**
     * 商品主数据api
     */
    private String productApi;

    /**
     * 支付中心api
     */
    private String paymentApi;

}
