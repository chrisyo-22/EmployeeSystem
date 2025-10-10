package com.chrisyo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
//@ComponentScan({"com.chrisyo"}) can specify which package/s to scan.
public class EmployeeManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);
        log.info("Project started");
    }

}
