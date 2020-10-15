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
	
	public List<Device> getDevices(String platformUrl, String locationId, String authToken) {
		return deviceClient.getDevices(platformUrl, locationId, authToken);
	}
	
	public List<DeviceMetaInfo> getDevicesNames(String platformUrl, String locationId, String authToken) {
		List<Device> devices = deviceClient.getDevices(platformUrl, locationId, authToken);
		
		 List<DeviceMetaInfo> deviceMetaInfoList = new ArrayList<DeviceMetaInfo>();
		 for(Device device: devices) {
			 deviceMetaInfoList.add(new DeviceMetaInfo(device.getDeviceId(), device.getLabel()));
		 }
		 
		return deviceMetaInfoList;
	}
	
	public DeviceStatus getDeviceStatus(String platformUrl, String deviceId, String authToken) {	
		return deviceClient.getDeviceStatus(platformUrl, deviceId, authToken);
    }
}
