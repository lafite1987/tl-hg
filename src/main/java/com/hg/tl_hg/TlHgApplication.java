package com.hg.tl_hg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.lfy.base", "com.hg.tl_hg"})
public class TlHgApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlHgApplication.class, args);
	}
}
