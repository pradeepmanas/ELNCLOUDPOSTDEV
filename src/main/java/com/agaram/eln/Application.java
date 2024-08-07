package com.agaram.eln;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
import com.agaram.eln.primary.service.starterRunner.StarterRunner;
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
		com.spire.doc.license.LicenseProvider.setLicenseKey("LqLmvzucS58BANOgcozJIxLwiEzAsy4rkJhtF1OHRVpWOPX/8052NUhZ4qZ3TjaFUh4FDjO5Hv1+dhhKmX+9WoJT2aLybrr5DT+KqZznoDN1YJv/WiUYiDeqgreZXotfz7SYCIFOn4eUrPm+aqrykq2rHCgi9xefvoGwMno20wXNJMFtbG7yrM0eVo5gEYhe9JdC3bdnASujmGyktjV45r4Nr18phifu++mjhFj3GfaYnNyPf/lEPZcX9ECuPW/l5djgendkMkedJKSkju2cK0s8oDqoHKvmdxaxtuskPrG20Fd7BwlVnKmWPxHP5cigTf3knvM6P51zU2eCRb+X3FoUEJ6aLawNptWViq6IowiDFTC/7r+48BgRx9lYvPEu9L2xngiW2vvipe+BCzITfVbNLM3RLbRCY4Jks+b5+HgiieNZ/SWw/J0ln4MkfAQrIpxqmIPka7yhjWypLxcXljWCpq9AafJMFxbi4Z8JeIwoorIRC0eEGayKGiu5vU4MT32/9vvTU53BFc5407JuLfRQHcvEm8mg/zInN1Y9EHTBs1dw56PUSAQ3XF1nd+EBVYtEljIZj1Q64pfLX9JGVUsp/kbO27tYcqmI0ab9PSGRfM+NnFs4+3xonaNbNtqZGxaCqHIluBOEZboEV5508R6K425vgCLiY4LMJMz+IeqVt92QJt4+snAMks8GtenhxujPY8EhNrUCk20FgvER4L5LCXJoYYK2Yp7NcmEJQV9dOFplpPx0f06u63K529L4eYpKf9VypYqK0ARqkdUPMLj/lAGHn53R9ORjkS2iIGnoLThex3K08ZXqMDc9cXxvLl8c/01xGdaZNZn8UG/76i4QaP01FMOnNKCrGjHUSJhDBJe6qQFliYIZBD8HCrBHIrPykO1bYby1fhTFJNmmBFrbDPO7XAyOXC5jGDJuhji5gDUMuP+C+BNAVdnx5cbhcbvAhH/Ep0mpDUPoNsGIhCGEKFKBUTW4fCscTHgicNOJyBlDQ2L8R7EHkcrZ8S6ktfVhzWqt4ufmYZTwegvDndz7L+uSRWACmYG38a7lIFTUVIh8WMT5NnSBeZoaddPCfhpUtd3yvisgUPRBKUlHbAGkmX0RwF6hZhUQVSvBCpsIbxDWN4KpQLkjY5EunslaT6mSiXslIx1nYtQXddivEY1jI4z6NIxdU02JkMB6FmJX874Rrgz210ZENyr1U9hqCic0KvLmngltpHnkwYeOkiukdtd/rbpzqgnnN5hsLiZFYUt8zzNwR4LkBY9OG60lvUJtjwRHn1Tq9HPH74uOzzeTUodQeyD11Hpe7teiaZW3bki1dtVmmQWzF1t64aGhQsOeZ0IJpt16TnQZ5h255kdFj/I7RtXY1fdEa21b+MZCKi0yjTkkj/isi2Iv/HAJAvtIGsWyXDZ+8h+LgLsyv3I0T0opKe6rUVkQMvRVZX2BmL7e0Sr+mECqScS6t3GXK7iN3EhwopoHGr4HMBXNAdrMnsHfKRri+3e7MOAur35uBvXLtqI0Vh4Jy1ndjCD/OE0pb7M941osStMh/yJopBIYqBbUpFCIiyNNWU85dqjMuv0+eVjeJ8YXCtOAlYS2GCf5hF+KaAoFh8hewyZXckre0sbskpRTsBpZs8wMZucRb4DxD8cI7gFYhgmf1SLB");
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
	    
	    @Autowired
	    private StarterRunner starterRunner;

		public void run(String... args) throws Exception {
			starterRunner.executeOnStartup();
		}	 
}

