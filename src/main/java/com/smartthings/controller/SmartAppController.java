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
import com.smartthings.common.SmartAppMetaInfo;
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
public class SmartAppController {
	
	@Autowired
	SmartAppService smartAppService;
	
	// Smart App API's
	
	@GetMapping(value = "/smartAppNames", produces = { "application/json"})
    @Operation(summary = "List all my SmartApp's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<SmartAppMetaInfo> getMySmartAppNames(@RequestParam(required = true, defaultValue="stg") String env) {
		return smartAppService.listSmartAppNames(env);
	}
	
	@GetMapping(value = "/smartAppDetails", produces = { "application/json"})
    @Operation(summary = "Fetch SmartApp json for the given app id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMySmartAppDetails(@RequestParam(required = true) String appId, @RequestParam(required = true) String env) {
		return smartAppService.getSmartAppDetails(appId, env);
	}
	
}
