package com.smartthings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartthings.common.Scene;
import com.smartthings.common.SceneExecuteResponse;
import com.smartthings.common.SceneMetaInfo;
import com.smartthings.service.SceneService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/st", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Favorite Test Location")
public class SceneController {
	
	@Autowired
	SceneService sceneService;

	// Scene API's
	
	@GetMapping(value = "/scenesNames", produces = { "application/json"})
    @Operation(summary = "List all my scene's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<SceneMetaInfo> getMySceneNames(@RequestParam(required = true, defaultValue="stg") String env) {
		return sceneService.listSceneNames(env);
	}
	
	@GetMapping(value = "/sceneDetails", produces = { "application/json"})
    @Operation(summary = "Fetch scene json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	Scene getMySceneDetails(@RequestParam(required = true) String sceneId, @RequestParam(required = true, defaultValue="stg") String env) {
		return sceneService.getSceneDetails(env, sceneId);
	}
	
	@GetMapping(value = "/scenes", produces = { "application/json"})
    @Operation(summary = "List all my scenes in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<Scene> getMyScenes(@RequestParam(required = true, defaultValue="stg") String env) {
		return sceneService.listScenes(env);
	}
	
	@PostMapping(value = "/executeScene", produces = { "application/json"})
    @Operation(summary = "Execute given scene Id")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	SceneExecuteResponse executeScene(@RequestParam(required = true) String sceneId, @RequestParam(required = true, defaultValue="stg") String env) {
		return sceneService.executeScene(sceneId, env);
	}
}
