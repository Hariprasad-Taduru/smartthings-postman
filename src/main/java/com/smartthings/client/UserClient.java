package com.smartthings.client;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.common.Constants;
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.util.STObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgAuthUrl}")
	private String authStgUrl; 
	
	@Value("${smartthings.acptAuthUrl}")
	private String authAcptUrl; 
	
	@Value("${smartthings.prdAuthUrl}")
	private String authPrdUrl; 
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.acptUrl}")
	private String acptUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	
	private String platformUrl;
	private String authUrl;
	private String authToken;
	private String locationId;
	
	@Autowired
	private STObjectMapper stObjectMapper;
	
	@Autowired
    private RestTemplate restTemplate;
		
	public JsonNode getUserDetails(String env) {
		if (env.equals("prd")) {
			authUrl = authPrdUrl;
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			authUrl = authAcptUrl;
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			authUrl = authStgUrl;
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
			
		String url = authUrl + "/users/me";
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[getUserDetails] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=1");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> ruleHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> ruleResponse = null;
        
        try {
        	ruleResponse = restTemplate.exchange(url, HttpMethod.GET, ruleHttpEntity, String.class);
            if (ruleResponse.getStatusCode().is2xxSuccessful()) {
            	log.info("[getUserDetails] Request success for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
            	return stObjectMapper.readTree(ruleResponse.getBody());
            }
        } catch (Exception e) {
            log.error("[getUserDetails] Exception: {}, Response: {}, LogId {}", e, ruleResponse, loggingId);
        }
        return null;
	}
}
