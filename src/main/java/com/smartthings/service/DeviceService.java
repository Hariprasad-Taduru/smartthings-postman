package com.smartthings.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartthings.client.DeviceClient;
import com.smartthings.common.DeviceMetaInfo;
import com.smartthings.sdk.client.models.Device;
import com.smartthings.sdk.client.models.DeviceStatus;

@Service
public class DeviceService {
	
	@Autowired
	private DeviceClient deviceClient;
	
	public List<Device> getDevices(String env) {
		return deviceClient.getDevices(env);
	}
	
	public List<DeviceMetaInfo> getDevicesNames(String env) {
		List<Device> devices = deviceClient.getDevices(env);
		
		 List<DeviceMetaInfo> deviceMetaInfoList = new ArrayList<DeviceMetaInfo>();
		 for(Device device: devices) {
			 deviceMetaInfoList.add(new DeviceMetaInfo(device.getDeviceId(), device.getLabel(), device.getDeviceTypeName()));
		 }
		 
		return deviceMetaInfoList;
	}
	
	public DeviceStatus getDeviceStatus(String deviceId, String env) {	
		return deviceClient.getDeviceStatus(deviceId, env);
    }
}
