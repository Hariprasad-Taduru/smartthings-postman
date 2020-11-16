package com.smartthings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartthings.client.SceneClient;
import com.smartthings.common.Scene;
import com.smartthings.common.SceneExecuteResponse;
import com.smartthings.common.SceneMetaInfo;

@Service
public class SceneService {
	
	@Autowired
	private SceneClient sceneClient;
	
	public List<Scene> listScenes(String env) {
		return sceneClient.listScenes(env);
	}
	
	public List<SceneMetaInfo> listSceneNames(String env) {
		List<Scene> scenes = sceneClient.listScenes(env);
		
		List<SceneMetaInfo> sceneMetaInfoList = new ArrayList<SceneMetaInfo>();
		
		for (Scene scene: scenes) {
			sceneMetaInfoList.add(new SceneMetaInfo(scene.getSceneId(), scene.getSceneName()));
		}
		return sceneMetaInfoList;
		
	}
	
	public Scene getSceneDetails(String sceneId, String env) {
		return sceneClient.getSceneDetails(sceneId, env);
	}
	
	public SceneExecuteResponse executeScene(String sceneId, String env) {
		return sceneClient.executeScene(sceneId, env);
	}
}
