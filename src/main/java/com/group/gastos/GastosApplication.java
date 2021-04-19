package com.group.gastos;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "Expense Tracker", version = "0.1", description = "para mi gordita, con amor"))
public class GastosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GastosApplication.class, args);
    }

}
