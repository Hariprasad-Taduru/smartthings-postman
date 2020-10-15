package com.smartthings.common;

import lombok.Data;

@Data
public class Scene {
	private String sceneId;
	private String sceneName;
	private String sceneIcon;
	private String sceneColor;
	private String locationId;
	private String createdBy;
	private String createdDate;
	private String lastUpdatedDate;
	private String lastExecutedDate;
	private String editable;
	private String apiVersion;
}
