package com.nuc.ICmonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class ICmonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ICmonitoringApplication.class, args);
    }

}
