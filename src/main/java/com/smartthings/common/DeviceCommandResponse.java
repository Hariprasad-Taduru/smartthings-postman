package com.smartthings.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeviceCommandResponse {
	String deviceId;
	String component;
	String capability;
	String command;
	String argument;
	String commandResponse;
	

}
