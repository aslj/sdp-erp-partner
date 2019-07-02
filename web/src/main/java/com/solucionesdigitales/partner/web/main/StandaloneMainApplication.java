package com.solucionesdigitales.partner.web.main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.solucionesdigitales.partner.service.storage.StorageService;
import com.solucionesdigitales.partner.service.storage.properties.StorageProperties;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
@ComponentScan("com.solucionesdigitales.partner")
@EntityScan( {"com.solucionesdigitales.partner"} )
@EnableJpaRepositories( {"com.solucionesdigitales.partner"} )
@EnableSwagger2
@EnableScheduling
public class StandaloneMainApplication  {

	public static void main(String[] args) {
		SpringApplication.run(StandaloneMainApplication.class, args);
	}
	
	@Bean
	public Docket api() {                      
		return new Docket(DocumentationType.SWAGGER_2)  
		          .select()                                  
		          .apis(RequestHandlerSelectors.basePackage("com.solucionesdigitales.partner"))              
		          .paths(PathSelectors.any())                          
		          .build();     
	}
	
	@Bean
	  CommandLineRunner init(StorageService storageService) {
	      return (args) -> {
	    	  // CREATES DIR FOR FILES
	          storageService.init();
	      };
	  }
}
