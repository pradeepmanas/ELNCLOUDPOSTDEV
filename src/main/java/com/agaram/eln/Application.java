package com.agaram.eln;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.WebApplicationInitializer;


@SpringBootApplication
@EnableJpaRepositories("com.agaram.eln.primary")
//@ComponentScan(basePackages = {"com.agaram.eln.*"})
//@EntityScan({"com.agaram.eln.*"})
//@EnableJpaRepositories({"com.agaram.eln.*"})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {

	public static int initTimer = 0;
	public static String SDMSDB = "";
	public static String ELNDB = "";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	 @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	      return builder.sources(Application.class);
	  }
	 
	 
}

