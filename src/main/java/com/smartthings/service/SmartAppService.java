package com.smartthings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.client.SmartAppClient;
import com.smartthings.common.SmartAppMetaInfo;

@Service
public class SmartAppService {
	
	@Autowired
	private SmartAppClient smartAppClient;
	
	public List<JsonNode> listSmartApps(String env) {
		return smartAppClient.listSmartApps(env);
	}
	
    public List<SmartAppMetaInfo> listSmartAppNames(String env) {
    	
    	List<SmartAppMetaInfo> smartAppMetaInfoList = new ArrayList<SmartAppMetaInfo>();
    	List<JsonNode> smartApps = smartAppClient.listSmartApps(env);
		
    	if (smartApps != null) {
    	    for (final JsonNode smartApp : smartApps) {
    	    	smartAppMetaInfoList.add(new SmartAppMetaInfo(smartApp.get("installedAppId").asText(), smartApp.get("displayName").asText()));
    	    }
    	}
        return smartAppMetaInfoList;
    }
    
    public JsonNode getSmartAppDetails(String appId, String env) {
		return smartAppClient.getSmartAppDetails(appId, env);
	}
}
