package com.smartthings.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.client.DeviceClient;
import com.smartthings.common.DeviceMetaInfo;
import com.smartthings.sdk.client.models.CapabilityStatus;
import com.smartthings.sdk.client.models.ComponentStatus;
import com.smartthings.sdk.client.models.Device;
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.service.DeviceService;
import com.smartthings.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/st", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Dashboard Controller")

public class DashboardController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	DeviceService deviceService;
	

	// Device Type and Main capability mapping
	private Map<String, String> capabilityStatus = null;
	
	@PostConstruct
	void initCapabilities() {
		capabilityStatus = new HashMap<String, String>();
		
		// Device Type and Main capability mapping
		
		// Real devices
		capabilityStatus.put("SmartSense Multi Sensor", "contactSensor");
		capabilityStatus.put("SmartSense Motion Sensor", "motionSensor");
		capabilityStatus.put("Samsung OCF TV", "switch");
		//capabilityStatus.put("Samsung OCF Robot Vacuum", "battery"); Giving ConstraintViolationError for all capabilities
		capabilityStatus.put("Samsung OCF Air Purifier", "switch");
		capabilityStatus.put("ZigBee Dimmer", "switch");
		capabilityStatus.put("Samsung OCF Oven", "switch");
		capabilityStatus.put("Samsung OCF Air Conditioner", "switch"); 
		capabilityStatus.put("Samsung OCF Dishwasher", "switch");
		capabilityStatus.put("Samsung OCF Network Audio Player", "switch");
		capabilityStatus.put("Samsung OCF Refrigerator", "contactSensor"); 
		capabilityStatus.put("Samsung OCF Dryer", "switch");
		capabilityStatus.put("Samsung OCF Washer", "switch");
		
		//Simulated devices
		capabilityStatus.put("Simulated Motion Sensor", "motionSensor");
		capabilityStatus.put("Simulated Lock", "lock");
		capabilityStatus.put("Simulated Presence Sensor", "presenceSensor");
		capabilityStatus.put("Simulated Smoke Alarm", "smokeDetector");
		capabilityStatus.put("Simulated Contact Sensor", "contactSensor");
		capabilityStatus.put("Simulated RGBW Bulb", "switch");
	}
	
	@GetMapping(value = "/me", produces = { "application/json"})
    @Operation(summary = "Get user details")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyDetails(@RequestParam(required = true, defaultValue="stg") String env) {
		return userService.getUserDetails(env);
	}
	
	@GetMapping(value = "/alldevicesstatus", produces = { "application/json"})
    @Operation(summary = "Get all devices status")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<DeviceMetaInfo> getAllDevicesStatus(@RequestParam(required = true, defaultValue="stg") String env) {
		
		List<DeviceMetaInfo> devicesMetaDataList = deviceService.getDevicesNames(env);
		
		String mainCapability = null;
		String componentId = "main";
		
		for (DeviceMetaInfo device: devicesMetaDataList) {
			mainCapability = componentId = null;
			mainCapability = capabilityStatus.get(device.getDeviceTypeName());
			if (mainCapability != null) {
				//if (mainCapability.equalsIgnoreCase("Samsung OCF Robot Vacuum") == true) {
				//	componentId = "execute";
				//} 
				CapabilityStatus deviceMainStatus = deviceService.getDeviceMainStatus(device.getDeviceId(), mainCapability, componentId, env);
				device.setMainStatus(deviceMainStatus);
			}
		}
		return devicesMetaDataList;
	}

}
