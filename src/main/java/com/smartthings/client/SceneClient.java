package com.smartthings.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartthings.common.Constants;
import com.smartthings.common.PagedScenes;
import com.smartthings.common.Scene;
import com.smartthings.util.STObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SceneClient {
	
	@Autowired
    private STObjectMapper stObjectMapper;
	
	@Autowired
    private RestTemplate restTemplate;
		
	public List<Scene> listScenes(String platformUrl, String locationId, String authToken) {
		
		String url = platformUrl + "/scenes?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[listScenes] Requested with LogId {}", loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> sceneHttpEntity = new HttpEntity<>(null, headers);
        List<Scene> scenes = new ArrayList<Scene>();
        
        scenes = getScenesHelper(url, sceneHttpEntity, scenes);
        log.info("[listScenes] Request success with LogId {}", loggingId);
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
	
	public Scene getSceneDetails(String platformUrl, String sceneId, String locationId, String authToken) {
		
		String url = platformUrl + "/scenes/" + sceneId + "?locationId=" + locationId;
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[getSceneDetails] Requested with LogId {}", loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
		
        HttpEntity<String> sceneHttpEntity = new HttpEntity<>(null, headers);
        
        ResponseEntity<String> sceneResponse = restTemplate.exchange(url,
                HttpMethod.GET, sceneHttpEntity, String.class);

        if (sceneResponse.getStatusCode().is2xxSuccessful()) {
        	log.info("[getSceneDetails] Request success with LogId {}", loggingId);
            try {
                return stObjectMapper.readValue(sceneResponse.getBody(), Scene.class);
            } catch (IOException e) {
                log.info("[getSceneDetails] Exception: {}", e);
            }
        }
        return null;
    }

}
