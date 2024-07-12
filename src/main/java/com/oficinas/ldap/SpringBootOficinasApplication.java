package com.oficinas.ldap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringBootOficinasApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOficinasApplication.class, args);
    }

}
