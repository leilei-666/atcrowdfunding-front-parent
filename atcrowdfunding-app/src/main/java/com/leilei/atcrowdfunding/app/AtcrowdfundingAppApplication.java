package com.leilei.atcrowdfunding.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient  //开启服务发现功能
@SpringBootApplication
/**
 * 静态资源；
 * Sb觉得：
 *      "classpath:/META-INF/resources/", "classpath:/resources/",
 * 	    "classpath:/static/", "classpath:/public/" ;
 *
 */
public class AtcrowdfundingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcrowdfundingAppApplication.class, args);
    }

}
