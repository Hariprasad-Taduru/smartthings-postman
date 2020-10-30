package com.smartthings.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smartthings.config.ExternalConfiguration;
import com.smartthings.sdk.client.ApiClient;
import com.smartthings.sdk.client.methods.DevicesApi;
import com.smartthings.sdk.client.models.Device;
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.sdk.client.models.PagedDevices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeviceClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	
	private String platformUrl;
	private String authToken;
	private String locationId;

	ApiClient apiClient = new ApiClient();
	
	public DeviceStatus getDeviceStatus(String deviceId, String env) {
		if (env == "prd") {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdFavoriteTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgFavoriteTestLocationId();
		}
		
		apiClient.setBasePath(platformUrl);
		log.info("[getDeviceStatus] Requested for environment {}, locationId: {}", env, locationId);
        DevicesApi devicesApi = apiClient.buildClient(DevicesApi.class);
        
        DeviceStatus deviceStatus = devicesApi.getDeviceStatus(authToken, deviceId);
        log.info("[getDeviceStatus] Request success for environment {}, locationId: {}", env, locationId);
        return deviceStatus;
    }
	

	public List<Device> getDevices(String env) {
		if (env == "prd") {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdFavoriteTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgFavoriteTestLocationId();
		}
		
		log.info("[getDevices] Requested for environment {}, locationId: {}", env, locationId);
		
	    List<PagedDevices> pagedDevices = new ArrayList<PagedDevices>();
	    pagedDevices = getPagedDevicesHelper(platformUrl, locationId, authToken, pagedDevices);
	    
	    List<Device> devices = new ArrayList<Device>();
	    
	    for (PagedDevices pagedDevice : pagedDevices) {
	    	devices.addAll(pagedDevice.getItems()); 
	    }
	    
	    log.info("[getDevices] Request success for environment {}, locationId: {}, devices: {}", env, locationId, devices.toString());
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
        
        PagedDevices pagedDevices = devicesApi.getDevices(authToken, queryParam);
        
        completePagedDevices.add(pagedDevices);
        
        if (pagedDevices.getLinks() != null && pagedDevices.getLinks().getNext() != null)     {
            return getPagedDevicesHelper(pagedDevices.getLinks().getNext().getHref(), locationId, authToken, completePagedDevices);
        }
        
        return completePagedDevices;
    }
}
