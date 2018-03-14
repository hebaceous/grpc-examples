package me.hebaceous.grpc.demo.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.locks.LockSupport;

@SpringBootApplication
public class DemoClientApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoClientApplication.class);
        springApplication.setWebEnvironment(false);
        springApplication.run(args);
        LockSupport.park();
    }
}
