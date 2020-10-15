package com.smartthings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartthings.client.SceneClient;
import com.smartthings.common.Scene;
import com.smartthings.common.SceneMetaInfo;

@Service
public class SceneService {
	
	@Autowired
	private SceneClient sceneClient;
	
	public List<Scene> listScenes(String platformUrl, String locationId, String authToken) {
		return sceneClient.listScenes(platformUrl, locationId, authToken);
	}
	
	public List<SceneMetaInfo> listSceneNames(String platformUrl, String locationId, String authToken) {
		List<Scene> scenes = sceneClient.listScenes(platformUrl, locationId, authToken);
		
		List<SceneMetaInfo> sceneMetaInfoList = new ArrayList<SceneMetaInfo>();
		
		for (Scene scene: scenes) {
			sceneMetaInfoList.add(new SceneMetaInfo(scene.getSceneId(), scene.getSceneName()));
		}
		return sceneMetaInfoList;
		
	}
	
	public Scene getSceneDetails(String platformUrl, String sceneId, String locationId, String authToken) {
	
		return sceneClient.getSceneDetails(platformUrl, sceneId, locationId, authToken);
	}
}
