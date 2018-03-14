package me.hebaceous.grpc.demo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.locks.LockSupport;

@SpringBootApplication
public class DemoServiceApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(DemoServiceApplication.class);
		springApplication.setWebEnvironment(false);
		springApplication.run(args);
        LockSupport.park();
	}
}
