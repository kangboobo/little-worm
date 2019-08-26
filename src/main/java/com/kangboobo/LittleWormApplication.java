package com.kangboobo;

import com.kangboobo.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 *
 * Created by kangboobo on 2019/8/20.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.kangboobo.dao")
public class LittleWormApplication {

    public static void main(String[] args) {
        SpringApplication.run(LittleWormApplication.class, args);
    }

    /**
     * 初始化唯一标识号生成组件
     *
     * @return
     */
    @Bean(name = "idWorker")
    public IdWorker curatorIdWorker() {
        IdWorker idWorker = new IdWorker(1, 1);
        return idWorker;
    }
}
