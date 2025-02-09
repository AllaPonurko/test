package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"org.example.config","org.example.controller"})
// потрібно за умови якщо config та controller знаходяться поза межі org.example
//@ComponentScan(basePackages = "org.example.controller")
//@EnableScheduling
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
        System.out.println( "Hello World!" );
    }
}
