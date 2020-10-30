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

import com.smartthings.common.Constants;
import com.smartthings.common.PagedScenes;
import com.smartthings.common.Scene;
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.util.STObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SceneClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.acptUrl}")
	private String acptUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	
	private String platformUrl;
	private String authToken;
	private String locationId;
	
	@Autowired
    private STObjectMapper stObjectMapper;
	
	@Autowired
    private RestTemplate restTemplate;
		
    public List<Scene> listScenes(String env) {
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
		
		String url = platformUrl + "/scenes?locationId=" + locationId;
			
		String loggingId = UUID.randomUUID().toString();
			
		log.info("[listScenes] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
			
		HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authToken);
	        //headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
		headers.set("Accept", "application/vnd.smartthings+json;");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> sceneHttpEntity = new HttpEntity<>(null, headers);
        List<Scene> scenes = new ArrayList<Scene>();
        
        scenes = getScenesHelper(url, sceneHttpEntity, scenes);
        log.info("[listScenes] Request success for environment {}, locationId: {}, logId {}, scenes: {}", env, locationId, loggingId, scenes.toString());
        return scenes;
    }
	
	private List<Scene> getScenesHelper(String url, HttpEntity<String> sceneHttpEntity, List<Scene> scenes) {
        if (url == null) {
            return scenes;
        }

        ResponseEntity<String> sceneResponse = restTemplate.exchange(url,
                HttpMethod.GET, sceneHttpEntity, String.class);

        if (sceneResponse.getStatusCode().is2xxSuccessful()) {
            try {
                PagedScenes pagedScenes = stObjectMapper.readValue(sceneResponse.getBody(), PagedScenes.class);
                scenes.addAll(pagedScenes.getItems());
                if (pagedScenes.getLinks() != null && pagedScenes.getLinks().getNext() != null)     {
                    return getScenesHelper(pagedScenes.getLinks().getNext().getHref(), sceneHttpEntity, scenes);
                }
            } catch (IOException e) {
                log.info("[getScenesHelper] Exception: {}", e);
            }
        }
        return scenes;
    }
	
    public Scene getSceneDetails(String sceneId, String env) {
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
		
		String url = platformUrl + "/scenes/" + sceneId + "?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
			
		log.info("[getSceneDetails] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
			
		HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", authToken);
	        //headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
		headers.set("Accept", "application/vnd.smartthings+json;");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> sceneHttpEntity = new HttpEntity<>(null, headers);
        
        ResponseEntity<String> sceneResponse = restTemplate.exchange(url,
                HttpMethod.GET, sceneHttpEntity, String.class);

        if (sceneResponse.getStatusCode().is2xxSuccessful()) {
        	log.info("[getSceneDetails] Request success for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
            try {
                return stObjectMapper.readValue(sceneResponse.getBody(), Scene.class);
            } catch (IOException e) {
                log.info("[getSceneDetails] Exception: {}", e);
            }
        }
        return null;
    }

}
