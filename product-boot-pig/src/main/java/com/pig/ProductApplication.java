package com.pig;

import com.pig.basic.listener.PropertiesListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.pig.modules.*.mapper,com.pig.modules.*.dao")
@EnableScheduling
@EnableTransactionManagement
@EnableJpaAuditing
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ProductApplication.class);
        // 第四种方式：注册监听器(监听配置文件)
        application.addListeners(new PropertiesListener("config.properties"));
        application.run(args);

    }
}
