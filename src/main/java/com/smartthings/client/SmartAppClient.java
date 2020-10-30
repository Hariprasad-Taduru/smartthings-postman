package com.smartthings.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.smartthings.common.PagedScenes;
import com.smartthings.common.PagedSmartApps;
import com.smartthings.common.Scene;
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.util.STObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmartAppClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	
	private String platformUrl;
	private String authToken;
	private String locationId;
	
	@Autowired
	private STObjectMapper stObjectMapper;
	
	@Autowired
    private RestTemplate restTemplate;
		
	public List<JsonNode> listSmartApps(String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdFavoriteTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgFavoriteTestLocationId();
		}
		
		String url = platformUrl + "/installedapps?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[listSmartApps] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=1");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
        

        HttpEntity<String> smartAppHttpEntity = new HttpEntity<>(null, headers);
        List<JsonNode> smartApps = new ArrayList<JsonNode>();
        
        smartApps = getSmartAppsHelper(url, smartAppHttpEntity, smartApps);
        log.info("[listSmartApps] Request success for environment {}, locationId: {}, logId {}, smartApps: {}", env, locationId, loggingId, smartApps.toString());
        return smartApps;
    }
	
	private List<JsonNode> getSmartAppsHelper(String url, HttpEntity<String> smartAppHttpEntity, List<JsonNode> smartApps) {
        if (url == null) {
            return smartApps;
        }

        ResponseEntity<String> smartAppResponse = restTemplate.exchange(url,
                HttpMethod.GET, smartAppHttpEntity, String.class);

        if (smartAppResponse.getStatusCode().is2xxSuccessful()) {
            try {
                PagedSmartApps pagedSmartApps = stObjectMapper.readValue(smartAppResponse.getBody(), PagedSmartApps.class);
                smartApps.addAll(pagedSmartApps.getItems());
                if (pagedSmartApps.getLinks() != null && pagedSmartApps.getLinks().getNext() != null)     {
                    return getSmartAppsHelper(pagedSmartApps.getLinks().getNext().getHref(), smartAppHttpEntity, smartApps);
                }
            } catch (IOException e) {
                log.info("[getScenesHelper] Exception: {}", e);
            }
        }
        return smartApps;
    }
	
	public JsonNode getSmartAppDetails(String appId, String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdFavoriteTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgFavoriteTestLocationId();
		}
			
		String url = platformUrl + "/installedapps/" + appId + "?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[getSmartAppDetails] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=1");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> ruleHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> ruleResponse = null;
        
        try {
        	ruleResponse = restTemplate.exchange(url, HttpMethod.GET, ruleHttpEntity, String.class);
            if (ruleResponse.getStatusCode().is2xxSuccessful()) {
            	log.info("[getSmartAppDetails] Request success for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
            	return stObjectMapper.readTree(ruleResponse.getBody());
            }
        } catch (Exception e) {
            log.error("[getSmartAppDetails] Exception: {}, Response: {}, LogId {}", e, ruleResponse, loggingId);
        }
        return null;
	}
}
