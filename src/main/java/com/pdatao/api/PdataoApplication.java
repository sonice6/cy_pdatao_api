package com.pdatao.api;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ImportResource("classpath:spring/spring-context.xml")
@MapperScan(value="com.pdatao.api.dao")
public class PdataoApplication implements CommandLineRunner {
	
	public static void main(String[] args)  {
		SpringApplication.run(PdataoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
