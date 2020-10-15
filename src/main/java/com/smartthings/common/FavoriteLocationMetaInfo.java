package com.smartthings.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class FavoriteLocationMetaInfo {
	
	List<DeviceMetaInfo> deviceMetaInfo;
	List<SceneMetaInfo> sceneMetaInfo;
	List<RuleMetaInfo> ruleMetaInfo;
}
