package com.anson.localservices;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.anson.localservices.mapper")
@SpringBootApplication
public class LocalServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalServicesApplication.class, args);
    }

}
