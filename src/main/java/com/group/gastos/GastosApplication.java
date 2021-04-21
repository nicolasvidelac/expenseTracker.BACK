package com.group.gastos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GastosApplication {

    public static void main(String[] args) {
//        MyTaskExecutor myTaskExecutor = new MyTaskExecutor();
//        myTaskExecutor.startExecutionAt(16, 18, 0);
        SpringApplication.run(GastosApplication.class, args);
    }

}
