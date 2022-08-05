package com.sky31;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Sky31WelcomeApplication {

    @PostConstruct
    public void init(){
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }

    public static void main(String[] args) {
        SpringApplication.run(Sky31WelcomeApplication.class, args);
    }

}
