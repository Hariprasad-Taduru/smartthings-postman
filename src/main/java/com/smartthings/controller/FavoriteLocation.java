package com.smartthings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartthings.common.DeviceMetaInfo;
import com.smartthings.common.FavoriteLocationMetaInfo;
import com.smartthings.common.RuleMetaInfo;
import com.smartthings.common.SceneMetaInfo;
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
			@RequestParam(required = true, defaultValue="stg") String env,
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
}
