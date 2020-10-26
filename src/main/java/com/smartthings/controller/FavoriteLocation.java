package com.smartthings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.common.DeviceMetaInfo;
import com.smartthings.common.FavoriteLocationMetaInfo;
import com.smartthings.common.RuleMetaInfo;
import com.smartthings.common.Scene;
import com.smartthings.common.SceneMetaInfo;
import com.smartthings.common.SmartAppMetaInfo;
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.sdk.client.models.Location;
import com.smartthings.service.DeviceService;
import com.smartthings.service.LocationService;
import com.smartthings.service.RuleService;
import com.smartthings.service.SceneService;
import com.smartthings.service.SmartAppService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/st", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Favorite Test Location")
public class FavoriteLocation {
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	SceneService sceneService;
	
	@Autowired
	RuleService ruleService;
	
	@Autowired
	SmartAppService smartAppService;
	
	
	// Location API's
	
	@GetMapping(value = "/locationFullStatus", produces = { "application/json"})
    @Operation(summary = "Get meta info for my fevorite test location in STG environemt. Set favorite test location in env.config")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	FavoriteLocationMetaInfo getMyFavoriteTestLocationInfo(
			@RequestParam(required = true) String env,
			@RequestParam(required = false, defaultValue = "0") String devices,
			@RequestParam(required = false, defaultValue = "0") String scenes,
			@RequestParam(required = false, defaultValue = "0") String rules) {
		
		List<DeviceMetaInfo> devicesMetaInfo = null;
		List<SceneMetaInfo> sceneMetaInfo = null;
		List<RuleMetaInfo> ruleMetaInfo = null;
		
		if (devices.equalsIgnoreCase("1")) {
			devicesMetaInfo = deviceService.getDevicesNames(env);
		}
		
		if (scenes.equalsIgnoreCase("1")) {
			sceneMetaInfo = sceneService.listSceneNames(env);
		}
		
		if (rules.equalsIgnoreCase("1")) {
			ruleMetaInfo = ruleService.listRuleNames(env);
		}
		
		return new FavoriteLocationMetaInfo(devicesMetaInfo, sceneMetaInfo, ruleMetaInfo);
	}
	
	@GetMapping(value = "/locations", produces = { "application/json"})
    @Operation(summary = "List all my locations.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<Location> getMyLocations(@RequestParam(required = true) String env) {
		return locationService.getLocations(env);
	}
	
	
	// Device API's
	
	@GetMapping(value = "/devicesNames", produces = { "application/json"})
    @Operation(summary = "List all my device's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<DeviceMetaInfo> getMyDevicesNames(@RequestParam(required = true) String env) {
		return deviceService.getDevicesNames(env);
	}
	
	@GetMapping(value = "/deviceDetails", produces = { "application/json"})
    @Operation(summary = "Fetch full status of the given device id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	DeviceStatus getMyDeviceStatus(@RequestParam(required = true) String deviceId, @RequestParam(required = true) String env) {
		return deviceService.getDeviceStatus(deviceId, env);
	}
	
	
	// Rule API's
	
	@GetMapping(value = "/rulesNames", produces = { "application/json"})
    @Operation(summary = "List all my rule's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<RuleMetaInfo> getMyRuleeNames(@RequestParam(required = true) String env) {
		return ruleService.listRuleNames(env);
	}
	
	@GetMapping(value = "/ruleDetails", produces = { "application/json"})
    @Operation(summary = "Fetch rule json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyRuleDetails(@RequestParam(required = true) String ruleId, @RequestParam(required = true) String env) {
		return ruleService.getRuleDetails(ruleId, env);
	}
	
	@GetMapping(value = "/rules", produces = { "application/json"})
    @Operation(summary = "List all my rules in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyRules(@RequestParam(required = true) String env) {
		return ruleService.listRules(env);
	}
	
	// Scene API's
	
	@GetMapping(value = "/scenesNames", produces = { "application/json"})
    @Operation(summary = "List all my scene's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<SceneMetaInfo> getMySceneNames(@RequestParam(required = true) String env) {
		return sceneService.listSceneNames(env);
	}
	
	@GetMapping(value = "/sceneDetails", produces = { "application/json"})
    @Operation(summary = "Fetch scene json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	Scene getMySceneDetails(@RequestParam(required = true) String sceneId, @RequestParam(required = true) String env) {
		return sceneService.getSceneDetails(env, sceneId);
	}
	
	@GetMapping(value = "/scenes", produces = { "application/json"})
    @Operation(summary = "List all my scenes in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<Scene> getMyScenes(@RequestParam(required = true) String env) {
		return sceneService.listScenes(env);
	}
	
	// Smart App API's
	
	@GetMapping(value = "/smartAppNames", produces = { "application/json"})
    @Operation(summary = "List all my SmartApp's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<SmartAppMetaInfo> getMySmartAppNames(@RequestParam(required = true) String env) {
		return smartAppService.listSmartAppNames(env);
	}
	
	@GetMapping(value = "/smartAppDetails", produces = { "application/json"})
    @Operation(summary = "Fetch SmartApp json for the given app id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMySmartAppDetails(@RequestParam(required = true) String appId, @RequestParam(required = true) String env) {
		return smartAppService.getSmartAppDetails(appId, env);
	}
	
}
