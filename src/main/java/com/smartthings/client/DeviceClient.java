package com.smartthings.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smartthings.sdk.client.ApiClient;
import com.smartthings.sdk.client.methods.DevicesApi;
import com.smartthings.sdk.client.models.Device;
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.sdk.client.models.PagedDevices;

@Service
public class DeviceClient {

	ApiClient apiClient = new ApiClient();
	
	public DeviceStatus getDeviceStatus(String platformUrl, String deviceId, String authToken) {
		
		apiClient.setBasePath(platformUrl);
		
        DevicesApi devicesApi = apiClient.buildClient(DevicesApi.class);
        
        return devicesApi.getDeviceStatus("Bearer " + authToken, deviceId);
    }
	

	public List<Device> getDevices(String platformUrl, String locationId, String authToken) {
		
	    List<PagedDevices> pagedDevices = new ArrayList<PagedDevices>();
	    pagedDevices = getPagedDevicesHelper(platformUrl, locationId, authToken, pagedDevices);
	    
	    List<Device> devices = new ArrayList<Device>();
	    
	    for (PagedDevices pagedDevice : pagedDevices) {
	    	devices.addAll(pagedDevice.getItems()); 
	    }
	    
	    return devices;
	}
	
	private List<PagedDevices> getPagedDevicesHelper(String platformUrl, String locationId, String authToken, List<PagedDevices> completePagedDevices) {
        if (platformUrl == null) {
            return completePagedDevices;
        }

        apiClient.setBasePath(platformUrl);
        
        DevicesApi devicesApi  =  apiClient.buildClient(DevicesApi.class);
        
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("locationId", locationId);
        
        PagedDevices pagedDevices = devicesApi.getDevices("Bearer " + authToken, queryParam);
        
        completePagedDevices.add(pagedDevices);
        
        if (pagedDevices.getLinks() != null && pagedDevices.getLinks().getNext() != null)     {
            return getPagedDevicesHelper(pagedDevices.getLinks().getNext().getHref(), locationId, authToken, completePagedDevices);
        }
        
        return completePagedDevices;
    }
}
