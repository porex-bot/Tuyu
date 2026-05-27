package com.qiu.qiupicturebackend;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@MapperScan({"com.qiu.qiupicturebackend.mapper", "com.qiu.qiupicturebackend.asset.infrastructure.persistence.mapper", "com.qiu.qiupicturebackend.collection.infrastructure.persistence.mapper", "com.qiu.qiupicturebackend.activity.infrastructure.persistence.mapper", "com.qiu.qiupicturebackend.ai.infrastructure.persistence.mapper", "com.qiu.qiupicturebackend.governance.infrastructure.persistence.mapper"})
@EnableAspectJAutoProxy(exposeProxy = true)
public class QiuPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QiuPictureBackendApplication.class, args);
    }

}
