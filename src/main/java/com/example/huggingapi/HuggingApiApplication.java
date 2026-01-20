package com.example.huggingapi;
/**
 * @author Emilija Sankauskaitė
 * Main class to run the app (an entry point).
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication is being used here, because it combines:
 * @Configuration && @EnableAutoConfiguration && @ComponentScan
 */
@SpringBootApplication
public class HuggingApiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(HuggingApiApplication.class, args);
        System.out.println("Application started");
    }
}
