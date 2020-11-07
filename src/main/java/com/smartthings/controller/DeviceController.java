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
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.service.DeviceService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/st", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Device Controller")
public class DeviceController {
	
	@Autowired
	DeviceService deviceService;
	
	// Device API's
	
	@GetMapping(value = "/devicesNames", produces = { "application/json"})
    @Operation(summary = "List all my device's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<DeviceMetaInfo> getMyDevicesNames(@RequestParam(required = true, defaultValue="stg") String env) {
		return deviceService.getDevicesNames(env);
	}
	
	@GetMapping(value = "/deviceDetails", produces = { "application/json"})
    @Operation(summary = "Fetch full status of the given device id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	DeviceStatus getMyDeviceStatus(@RequestParam(required = true) String deviceId, @RequestParam(required = true, defaultValue="stg") String env) {
		return deviceService.getDeviceStatus(deviceId, env);
	}
	
	@GetMapping(value = "/deviceCommands", produces = { "application/json"})
    @Operation(summary = "Execute commands on the given device id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	String executeDeviceCommands(@RequestParam(required = true) String deviceId, 
									   @RequestParam(required = true) String component,
									   @RequestParam(required = true) String capability,
									   @RequestParam(required = true) String command,
									   @RequestParam(required = true, defaultValue="stg") String env) {
		return deviceService.executeDeviceCommands(deviceId, component, capability, command, env);
	}
}
