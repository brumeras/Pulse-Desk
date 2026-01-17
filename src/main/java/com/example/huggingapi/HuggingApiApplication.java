package com.example.huggingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HuggingApiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(HuggingApiApplication.class, args);
        System.out.println("Application started");
    }

}
