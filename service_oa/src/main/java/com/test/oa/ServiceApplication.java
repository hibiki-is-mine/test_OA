package com.test.oa;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @return
 */
@SpringBootApplication
@ComponentScan("com.test.*")
public class ServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceApplication.class,args);
    }
}
