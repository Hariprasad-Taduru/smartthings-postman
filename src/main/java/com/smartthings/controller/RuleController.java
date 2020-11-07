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
import com.smartthings.common.OCFRuleMetaInfo;
import com.smartthings.common.RuleMetaInfo;
import com.smartthings.service.RuleService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/st", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Rule Controller")
public class RuleController {
	
	@Autowired
	RuleService ruleService;
	
	// Rule API's
	
	@GetMapping(value = "/rulesNames", produces = { "application/json"})
    @Operation(summary = "List all my rule's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<RuleMetaInfo> getMyRulesNames(@RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.listRuleNames(env);
	}
	
	@GetMapping(value = "/ruleDetails", produces = { "application/json"})
    @Operation(summary = "Fetch rule json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyRuleDetails(@RequestParam(required = true) String ruleId, @RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.getRuleDetails(ruleId, env);
	}
	
	@GetMapping(value = "/rules", produces = { "application/json"})
    @Operation(summary = "List all my rules in the given location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyOCFRules(@RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.listRules(env);
	}
	
	@GetMapping(value = "/ocfRulesNames", produces = { "application/json"})
    @Operation(summary = "List all my OCF rule's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<OCFRuleMetaInfo> getMyOCFRulesNames(@RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.listOCFRuleNames(env);
	}
	
	@GetMapping(value = "/ocfRuleDetails", produces = { "application/json"})
    @Operation(summary = "Fetch scene json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyOCFRuleDetails(@RequestParam(required = true) String ruleId, @RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.getOCFRuleDetails(ruleId, env);
	}
	
	@GetMapping(value = "/ocfScenesNames", produces = { "application/json"})
    @Operation(summary = "List all my OCF scene's id, name in the test location.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	List<OCFRuleMetaInfo> getMyOCFScenesNames(@RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.listOCFScenesNames(env);
	}
	
	@GetMapping(value = "/ocfSceneDetails", produces = { "application/json"})
    @Operation(summary = "Fetch scene json for the given rule id.")
	@ApiResponse(content = @Content(schema = @Schema(hidden = true)))
	JsonNode getMyOCFSceneDetails(@RequestParam(required = true) String sceneId, @RequestParam(required = true, defaultValue="stg") String env) {
		return ruleService.getOCFSceneDetails(sceneId, env);
	}
}
