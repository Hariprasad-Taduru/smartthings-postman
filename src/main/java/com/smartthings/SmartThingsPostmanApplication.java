package com.smartthings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import com.smartthings.config.ExternalConfiguration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(
     info = @Info(
          title = "SmartThings Postman",
          description = "<h3>Web Client for SmartThings API's.</h3>",
          version = "",
          contact = @Contact(
             name = "Hari",
		     url = "https://github.com/Hariprasad-Taduru"
		  )
	  )
)
@EnableConfigurationProperties({ExternalConfiguration.class})
public class SmartThingsPostmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartThingsPostmanApplication.class, args);
	}
	
	@Bean
    @Scope("prototype")
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
