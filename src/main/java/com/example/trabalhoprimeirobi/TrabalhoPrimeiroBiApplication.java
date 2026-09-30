package com.example.trabalhoprimeirobi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"controller", "model"})
public class TrabalhoPrimeiroBiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrabalhoPrimeiroBiApplication.class, args);
    }
}
