package com.aiqin.mgs.order.api;

import com.aiqin.ground.spring.boot.core.annotations.GroundBoot;
import com.aiqin.ground.spring.boot.core.annotations.GroundDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 * <p>
 */
@EnableScheduling //定时任务
@SuppressWarnings("ALL")
@Configuration
@ComponentScan
@GroundBoot
@GroundDataSource
@MapperScan(basePackages = {"com.aiqin.mgs.order.api.dao"})
public class OrderApiBootApplication extends SpringBootServletInitializer {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderApiBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderApiBootApplication.class, args);
        LOGGER.info("============= SpringBoot Start Success =============");
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OrderApiBootApplication.class);
    }
}
