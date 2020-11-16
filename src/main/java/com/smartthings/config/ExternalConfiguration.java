package com.smartthings.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import lombok.Data;

@Configuration
@ConfigurationProperties
@PropertySources({
@PropertySource(value = {
			 "file:C:/st-postman-config/env.config.txt",
			 "file:C:/st-postman-config/env.config",
			 "file:/etc/st-postman-config/env.config"}, //if same key, this will 'win' 
			 ignoreResourceNotFound = true)})
@Data
@Primary
public class ExternalConfiguration {
	
	//STG parameters
	@Value("${st.stg.pat.token:0}")
	String stgToken;
	
	@Value("${st.stg.test.location:0}")
	String stgTestLocationId;
	
	//ACPT parameters
	@Value("${st.acpt.pat.token:0}")
	String acptToken;
			
	@Value("${st.acpt.test.location:0}")
	String acptTestLocationId;
		
	//PRD parameters
	@Value("${st.prd.pat.token:0}")
	String prdToken;
		
	@Value("${st.prd.test.location:0}")
	String prdTestLocationId;
}
