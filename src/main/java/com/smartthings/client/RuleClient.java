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
public class RuleClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.acptUrl}")
	private String acptUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	
	// OCF Stuff
	@Value("${smartthings.stgOCFRuleUrl}")
	private String stgOCFRuleUrl;
	
	@Value("${smartthings.acptOCFRuleUrl}")
	private String acptOCFRuleUrl;
	
	@Value("${smartthings.prdOCFRuleUrl}")
	private String prdOCFRuleUrl;
	
	private String platformUrl;
	private String authToken;
	private String locationId;
	
	@Autowired
	private STObjectMapper stObjectMapper;
	
	@Autowired
    private RestTemplate restTemplate;
		
	public JsonNode listRules(String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		String url = platformUrl + "/behaviors?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[listRules] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> ruleHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> ruleResponse = null;
        
        try {
        	ruleResponse = restTemplate.exchange(url, HttpMethod.GET, ruleHttpEntity, String.class);
            if (ruleResponse.getStatusCode().is2xxSuccessful()) {
            	//log.info("[listRules] Request success for environment {}, locationId: {}, logId: {} rules: {} ",  env, locationId, loggingId, ruleResponse.toString());
            	log.info("[listRules] Request success for environment {}, locationId: {}, logId: {}",  env, locationId, loggingId);
            	JsonNode rules = stObjectMapper.readTree(ruleResponse.getBody()).get("items");
            	return rules;
            }
        } catch (Exception e) {
            log.error("[listRules] Exception: {}, Response: {}, LogId {}", e, ruleResponse, loggingId);
        }
        return null;
    }
	
	public JsonNode getRuleDetails(String ruleId, String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
			
		String url = platformUrl + "/behaviors/" + ruleId + "?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[getRuleDetails] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> ruleHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> ruleResponse = null;
        
        try {
        	ruleResponse = restTemplate.exchange(url, HttpMethod.GET, ruleHttpEntity, String.class);
            if (ruleResponse.getStatusCode().is2xxSuccessful()) {
            	log.info("[getRuleDetails] Request success for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
            	return stObjectMapper.readTree(ruleResponse.getBody());
            }
        } catch (Exception e) {
            log.error("[getRuleDetails] Exception: {}, Response: {}, LogId {}", e, ruleResponse, loggingId);
        }
        return null;
	}
	
	public JsonNode listOCFRulesAndScenes(String env) {
		if (env.equals("prd")) {
			platformUrl = prdOCFRuleUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptOCFRuleUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgOCFRuleUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		String url = platformUrl + "?gid=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[listOCFRulesAndScenes] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> ruleHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> ruleResponse = null;
        
        try {
        	ruleResponse = restTemplate.exchange(url, HttpMethod.GET, ruleHttpEntity, String.class);
            if (ruleResponse.getStatusCode().is2xxSuccessful()) {
            	//log.info("[listOCFRulesAndScenes] Request success for environment {}, locationId: {}, logId: {} rules: {} ",  env, locationId, loggingId, ruleResponse.toString());
            	log.info("[listOCFRulesAndScenes] Request success for environment {}, locationId: {}, logId: {}",  env, locationId, loggingId);
            	JsonNode ocfRules = stObjectMapper.readTree(ruleResponse.getBody());
            	return ocfRules;
            }
        } catch (Exception e) {
            log.error("[listOCFRulesAndScenes] Exception: {}, Response: {}, LogId {}", e, ruleResponse, loggingId);
        }
        return null;
    }
	
	public JsonNode getOCFRuleOrSceneDetails(String ruleId, String env) {
		if (env.equals("prd")) {
			platformUrl = prdOCFRuleUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptOCFRuleUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgOCFRuleUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		String url = platformUrl + "/" + ruleId + "?gid=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[getOCFRuleOrSceneDetails] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/vnd.smartthings+json;");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> ruleHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> ruleResponse = null;
        
        try {
        	ruleResponse = restTemplate.exchange(url, HttpMethod.GET, ruleHttpEntity, String.class);
            if (ruleResponse.getStatusCode().is2xxSuccessful()) {
            	//log.info("[getOCFRuleOrSceneDetails] Request success for environment {}, locationId: {}, logId: {} rules: {} ",  env, locationId, loggingId, ruleResponse.toString());
            	log.info("[getOCFRuleOrSceneDetails] Request success for environment {}, locationId: {}, logId: {}",  env, locationId, loggingId);
            	JsonNode ocfRule = stObjectMapper.readTree(ruleResponse.getBody());
            	return ocfRule;
            }
        } catch (Exception e) {
            log.error("[getOCFRuleOrSceneDetails] Exception: {}, Response: {}, LogId {}", e, ruleResponse, loggingId);
        }
        return null;
    }
}
