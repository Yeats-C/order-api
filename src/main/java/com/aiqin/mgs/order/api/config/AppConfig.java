package com.aiqin.mgs.order.api.config;

import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Configuration
@EnableConfigurationProperties({UrlProperties.class})
public class AppConfig {
}
