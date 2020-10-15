package com.smartthings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.common.DeviceMetaInfo;
import com.smartthings.common.RuleMetaInfo;
import com.smartthings.common.Scene;
import com.smartthings.common.SceneMetaInfo;
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.sdk.client.models.Location;
import com.smartthings.service.DeviceService;
import com.smartthings.service.LocationService;
import com.smartthings.service.RuleService;
import com.smartthings.service.SceneService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/stg", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "My Stage Engironment")
public class StagingEnvironment {

	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Autowired
	ExternalConfiguration exteConfig;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	SceneService sceneService;
	
	@Autowired
	RuleService ruleService;
	
	@GetMapping(value = "/locations", produces = { "application/json"})
    @Operation(summary = "List all my locations.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<Location> getMyLocations() {
		return locationService.getLocations(stgUrl, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/devicesNames", produces = { "application/json"})
    @Operation(summary = "List all my device's id, name in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<DeviceMetaInfo> getMyDevicesNames(@RequestParam(required = false) String locationId) {
		return deviceService.getDevicesNames(stgUrl, locationId, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/deviceDetails", produces = { "application/json"})
    @Operation(summary = "Fetch full status of the given device id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	DeviceStatus getMyDeviceStatus(@RequestParam(required = true) String deviceId) {
		return deviceService.getDeviceStatus(stgUrl, deviceId, exteConfig.getStgToken());
	}

	@GetMapping(value = "/scenesNames", produces = { "application/json"})
    @Operation(summary = "List all my scene's id, name in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<SceneMetaInfo> getMySceneNames(@RequestParam(required = true) String locationId) {
		return sceneService.listSceneNames(stgUrl, locationId, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/scenes", produces = { "application/json"})
    @Operation(summary = "List all my scenes in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<Scene> getMyScenes(@RequestParam(required = true) String locationId) {
		return sceneService.listScenes(stgUrl, locationId, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/sceneDetails", produces = { "application/json"})
    @Operation(summary = "Fetch scene json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	Scene getMySceneDetails(@RequestParam(required = true) String sceneId, @RequestParam(required = true) String locationId) {
		return sceneService.getSceneDetails(stgUrl, sceneId, locationId, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/rulesNames", produces = { "application/json"})
    @Operation(summary = "List all my rule's id, name in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<RuleMetaInfo> getMyRuleeNames(@RequestParam(required = true) String locationId) {
		return ruleService.listRuleNames(stgUrl, locationId, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/rules", produces = { "application/json"})
    @Operation(summary = "List all my rules in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyRules(@RequestParam(required = true) String locationId) {
		return ruleService.listRules(stgUrl, locationId, exteConfig.getStgToken());
	}
	
	@GetMapping(value = "/ruleDetails", produces = { "application/json"})
    @Operation(summary = "Fetch rule json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyRuleDetails(@RequestParam(required = true) String ruleId, @RequestParam(required = true) String locationId) {
		return ruleService.getRuleDetails(stgUrl, ruleId, locationId, exteConfig.getStgToken());
	}

}
