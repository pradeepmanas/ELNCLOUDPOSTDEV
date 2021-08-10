package com.agaram.eln;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import com.agaram.eln.primary.viewResolver.ExcelViewResolver;
import com.agaram.eln.primary.viewResolver.PdfViewResolver;
import com.agaram.eln.primary.viewResolver.CsvViewResolver; 

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
	 
	 	/*
	     * Configure ContentNegotiatingViewResolver
	     */
	    @Bean 
	    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
	        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
	        resolver.setContentNegotiationManager(manager);

	        // Define all possible view resolvers
	        List<ViewResolver> resolvers = new ArrayList<>();        

	        resolvers.add(excelViewResolver());
	        resolvers.add(csvViewResolver());
	        resolvers.add(pdfViewResolver());
	        
	        resolver.setViewResolvers(resolvers);
	        return resolver;
	    }
	    
	    /*
	     * Configure View resolver to provide XLS output using Apache POI library to
	     * generate XLS output for an object content
	     */
	    @Bean
	    public ViewResolver excelViewResolver() {
	        return new ExcelViewResolver();
	    }

	    /*
	     * Configure View resolver to provide Csv output using Super Csv library to
	     * generate Csv output for an object content
	     */
	    @Bean
	    public ViewResolver csvViewResolver() {
	        return new CsvViewResolver();
	    }

	    /*
	     * Configure View resolver to provide Pdf output using iText library to
	     * generate pdf output for an object content
	     */
	    @Bean
	    public ViewResolver pdfViewResolver() {
	        return new PdfViewResolver();
	    }	 
}

