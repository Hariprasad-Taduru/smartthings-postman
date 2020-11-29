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
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.sdk.client.models.Location;
import com.smartthings.service.LocationService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/st", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Location Controller")
@Slf4j
public class LocationController {
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	ExternalConfiguration extConfig;
	
	// Location API's
	
	@GetMapping(value = "/testlocation", produces = { "application/json"})
    @Operation(summary = "Get test location details")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	Location getMyTestLocation(@RequestParam(required = true, defaultValue="stg") String env) {
		return locationService.getTestLocation(env);
	}
	
	@GetMapping(value = "/locations", produces = { "application/json"})
    @Operation(summary = "List all my locations.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<Location> getMyLocations(@RequestParam(required = true, defaultValue="stg") String env) {
		return locationService.getLocations(env);
	}
	
	@GetMapping(value = "/livetrail", produces = { "application/json"})
    @Operation(summary = "Get live logs.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getLiveStream(@RequestParam(required = true, defaultValue="stg") String env) {
		log.info("[getLiveStream] env: {}", env);
		return locationService.getLiveTrailLogs(env);
	}
}
