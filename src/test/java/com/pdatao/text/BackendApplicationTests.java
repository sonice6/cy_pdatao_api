package com.pdatao.text;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableScheduling
@EnableAutoConfiguration
@ImportResource("classpath:spring/spring-context.xml")
public class BackendApplicationTests {

   

  }
