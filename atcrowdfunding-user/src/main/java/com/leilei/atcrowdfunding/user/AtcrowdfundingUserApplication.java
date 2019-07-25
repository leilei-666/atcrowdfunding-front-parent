package com.leilei.atcrowdfunding.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@ServletComponentScan(basePackages = "com.leilei.atcrowdfunding.user.component")     //自动扫描servlet组件（Filter、Listener、Servlet）
//1.开启从注册中心中发现服务的功能
@EnableDiscoveryClient
//2.开启基于Feign的声明式远程调用
@EnableFeignClients
//3.开启断路器功能
//SpringBoot默认使用的连接池是HikariCP
@EnableCircuitBreaker
@EnableSwagger2
@MapperScan("com.leilei.atcrowdfunding.user.mapper")
@SpringBootApplication
public class AtcrowdfundingUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowdfundingUserApplication.class, args);
    }

}
