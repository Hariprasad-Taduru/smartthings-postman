package com.smartthings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.client.RuleClient;
import com.smartthings.common.RuleMetaInfo;

@Service
public class RuleService {
	
	@Autowired
	private RuleClient ruleClient;
	
	public JsonNode listRules(String platformUrl, String locationId, String authToken) {
		return ruleClient.listRules(platformUrl, locationId, authToken);
	}
	
    public List<RuleMetaInfo> listRuleNames(String platformUrl, String locationId, String authToken) {
    	
    	List<RuleMetaInfo> ruleMetaInfoList = new ArrayList<RuleMetaInfo>();
    	JsonNode rules = ruleClient.listRules(platformUrl, locationId, authToken);
		
    	if (rules != null && rules.isArray()) {
    	    for (final JsonNode rule : rules) {
    	        ruleMetaInfoList.add(new RuleMetaInfo(rule.get("id").asText(), rule.get("name").asText()));
    	    }
    	}
        return ruleMetaInfoList;
    }
    
    public JsonNode getRuleDetails(String platformUrl, String ruleId, String locationId, String authToken) {
		return ruleClient.getRuleDetails(platformUrl, ruleId, locationId, authToken);
	}
}
