package com.smartthings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.client.RuleClient;
import com.smartthings.common.OCFRuleMetaInfo;
import com.smartthings.common.RuleMetaInfo;

@Service
public class RuleService {
	
	@Autowired
	private RuleClient ruleClient;
	
    // ST Rules
	public JsonNode listRules(String env) {
		return ruleClient.listRules(env);
	}
	
    public List<RuleMetaInfo> listRuleNames(String env) {
    	
    	List<RuleMetaInfo> ruleMetaInfoList = new ArrayList<RuleMetaInfo>();
    	JsonNode rules = ruleClient.listRules(env);
		
    	if (rules != null && rules.isArray()) {
    	    for (final JsonNode rule : rules) {
    	        ruleMetaInfoList.add(new RuleMetaInfo(rule.get("id").asText(), rule.get("name").asText()));
    	    }
    	}
        return ruleMetaInfoList;
    }
    
    public JsonNode getRuleDetails(String ruleId, String env) {
		return ruleClient.getRuleDetails(ruleId, env);
	}
    
    // OCF Rules
    public List<OCFRuleMetaInfo> listOCFRuleNames(String env) {
    	
    	List<OCFRuleMetaInfo> ocfRuleMetaInfoList = new ArrayList<OCFRuleMetaInfo>();
    	JsonNode ocfRules = ruleClient.listOCFRulesAndScenes(env);
		
    	if (ocfRules != null && ocfRules.isArray()) {
    	    for (final JsonNode ocfRule : ocfRules) {
    	    	if (ocfRule.get("type").asText().equals("Rule")) {
    	    		ocfRuleMetaInfoList.add(new OCFRuleMetaInfo(ocfRule.get("id").asText(), ocfRule.get("n").asText()));
    	    	}
    	    }
    	}
        return ocfRuleMetaInfoList;
    }
    
    public JsonNode getOCFRuleDetails(String ruleId, String env) {
    	return ruleClient.getOCFRuleOrSceneDetails(ruleId, env);
    }
    
    // OCF Scenes
    public List<OCFRuleMetaInfo> listOCFScenesNames(String env) {
    	
    	List<OCFRuleMetaInfo> ocfRuleMetaInfoList = new ArrayList<OCFRuleMetaInfo>();
    	JsonNode ocfRules = ruleClient.listOCFRulesAndScenes(env);
		
    	if (ocfRules != null && ocfRules.isArray()) {
    	    for (final JsonNode ocfRule : ocfRules) {
    	    	if (ocfRule.get("type").asText().equals("Mode")) {
    	    		ocfRuleMetaInfoList.add(new OCFRuleMetaInfo(ocfRule.get("id").asText(), ocfRule.get("n").asText()));
    	    	}
    	    }
    	}
        return ocfRuleMetaInfoList;
    }
    
    public JsonNode getOCFSceneDetails(String ruleId, String env) {
    	return ruleClient.getOCFRuleOrSceneDetails(ruleId, env);
    }
}
