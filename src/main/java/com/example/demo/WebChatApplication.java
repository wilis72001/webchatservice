package com.example.demo;


import WebSocket.WebSocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan(basePackages = {"register"})
@ComponentScan(basePackages = {"WebSocket"})
@ComponentScan(basePackages = {"Controller"})
@ComponentScan(basePackages = {"Util"})


public class WebChatApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(WebChatApplication.class, args);
    }
    // 注册 WebSocket 配置类

}

