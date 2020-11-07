package com.smartthings.common;

import com.smartthings.sdk.client.models.Device;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeviceMetaInfo {
	String deviceId;
	
	String deviceLabelName;
	
	String deviceTypeName;
	
	Device deviceSnapshot;
}
