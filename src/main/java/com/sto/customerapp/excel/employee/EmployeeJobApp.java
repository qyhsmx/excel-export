package com.sto.customerapp.excel.employee;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.cxytiandi.elasticjob.annotation.EnableElasticJob;


@SpringBootApplication(scanBasePackages = "com.sto.customerapp")
@ComponentScan(basePackages = "com.sto.customerapp")
@EnableElasticJob
public class EmployeeJobApp 
{
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        SpringApplication.run(EmployeeJobApp.class, args);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
