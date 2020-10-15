package com.smartthings.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import lombok.Data;

@Configuration
@ConfigurationProperties
@PropertySources({
@PropertySource(value = {"file:C:/env.config.txt", 
						 "file:/tmp/env.config"}, //if same key, this will 'win' 
				ignoreResourceNotFound = true)})
@Data
public class ExternalConfiguration {
	@Value("${st.stg.pat.token}")
	String stgToken;
	
	@Value("${st.prd.pat.token}")
	String prdToken;
	
	@Value("${st.stg.favorite.test.location}")
	String stgFavoriteTestLocationId;
	
	@Value("${st.prd.favorite.test.location}")
	String prdFavoriteTestLocationId;
}
