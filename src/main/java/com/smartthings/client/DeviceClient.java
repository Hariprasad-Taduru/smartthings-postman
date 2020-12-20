package com.smartthings.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartthings.common.Constants;
import com.smartthings.common.DeviceCommandResponse;
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.sdk.client.ApiClient;
import com.smartthings.sdk.client.methods.DevicesApi;
import com.smartthings.sdk.client.models.CapabilityStatus;
import com.smartthings.sdk.client.models.CreateDeviceCommandsResponse;
import com.smartthings.sdk.client.models.Device;
import com.smartthings.sdk.client.models.DeviceCommand;
import com.smartthings.sdk.client.models.DeviceCommandsRequest;
import com.smartthings.sdk.client.models.DeviceStatus;
import com.smartthings.sdk.client.models.PagedDevices;
import com.smartthings.util.STObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeviceClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.acptUrl}")
	private String acptUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	
	private String platformUrl;
	private String authToken;
	private String locationId;
	
	@Autowired
	private STObjectMapper stObjectMapper;
	
	@Autowired
    private RestTemplate restTemplate;

	ApiClient apiClient = new ApiClient();
	
	public DeviceStatus getDeviceStatus(String deviceId, String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		apiClient.setBasePath(platformUrl);
		log.info("[getDeviceStatus] Requested for environment {}, locationId: {}", env, locationId);
        DevicesApi devicesApi = apiClient.buildClient(DevicesApi.class);
        
        DeviceStatus deviceStatus = devicesApi.getDeviceStatus(authToken, deviceId);
        log.info("[getDeviceStatus] Request success for environment {}, locationId: {}", env, locationId);
        return deviceStatus;
    }
	
	public CapabilityStatus getDeviceMainStatus(String deviceId, String capabilityId, String componentId, String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		apiClient.setBasePath(platformUrl);
		log.info("[getDeviceMainStatus] Requested for environment {}, deviceId: {}", env, deviceId);
        DevicesApi devicesApi = apiClient.buildClient(DevicesApi.class);
        try {
	        CapabilityStatus capabilityStatus = devicesApi.getDeviceStatusByCapability(authToken, deviceId, componentId, capabilityId);
	        log.info("[getDeviceMainStatus] Request success for environment {}, deviceId: {}", env, deviceId);
	        return capabilityStatus; 
        } catch (Exception e) {
        	return null;
        }
	}

	public List<Device> getDevices(String env) {
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		log.info("[getDevices] Requested for environment {}, locationId: {}", env, locationId);
		
	    List<PagedDevices> pagedDevices = new ArrayList<PagedDevices>();
	    pagedDevices = getPagedDevicesHelper(platformUrl, locationId, authToken, pagedDevices);
	    
	    List<Device> devices = new ArrayList<Device>();
	    
	    for (PagedDevices pagedDevice : pagedDevices) {
	    	devices.addAll(pagedDevice.getItems()); 
	    }
	    
	    //log.info("[getDevices] Request success for environment {}, locationId: {}, devices: {}", env, locationId, devices.toString());
	    log.info("[getDevices] Request success for environment {}, locationId: {}", env, locationId);
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
	
	public DeviceCommandResponse executeDeviceCommands(String deviceId, String component, String capability, String command, String argument, String isSimulatedDevice, String env) {
		
		if (env.equals("prd")) {
			platformUrl = prdUrl;
			authToken = "Bearer " + extConfig.getPrdToken();
			locationId = extConfig.getPrdTestLocationId();
		} else if (env.equals("acpt")) {
			platformUrl = acptUrl;
			authToken = "Bearer " + extConfig.getAcptToken();
			locationId = extConfig.getAcptTestLocationId();
		} else {
			platformUrl = stgUrl;
			authToken = "Bearer " + extConfig.getStgToken();
			locationId = extConfig.getStgTestLocationId();
		}
		
		log.info("[executeDeviceCommands] Requested for environment {}, deviceId: {}, component: {}, capability: {}, command: {}, argument: {}, isSimulatedDevice: {}", 
										env, deviceId, component, capability, command, argument, isSimulatedDevice);
		
		String loggingId = UUID.randomUUID().toString();
		try {
			if (isSimulatedDevice.equals("1")) {
				log.info("[executeDeviceCommands] This is sumilated device.");
				String url = platformUrl + "/elder/" + locationId + "/api/devices/" + deviceId + "/commands/action/" + command;
				
				HttpHeaders headers = new HttpHeaders();
		        headers.set("Authorization", authToken);
		        headers.set("Accept", "application/vnd.smartthings+json;v=20200501");
		        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
				
		        HttpEntity<String> deviceCommandHttpEntity = new HttpEntity<>(null, headers);
		        ResponseEntity<String> deviceCommandResponse = null;
		        
		        deviceCommandResponse = restTemplate.exchange(url, HttpMethod.POST, deviceCommandHttpEntity, String.class);
	            if (deviceCommandResponse.getStatusCode().is2xxSuccessful()) {
	            	log.info("[executeDeviceCommands] Request success for environment {}, deviceId: {}, logId: {}, commandResponse: {}, ",  env, deviceId, loggingId, deviceCommandResponse);
	            }
				
			} else {
				log.info("[executeDeviceCommands] This is NOT sumilated device.");
				DeviceCommand deviceCommand = new DeviceCommand();
				deviceCommand.component(component);
				deviceCommand.setCapability(capability);
				deviceCommand.setCommand(command);
				if (!argument.equals("null") && !argument.equals("undefined")) {
					log.info("[executeDeviceCommands] Command argument is not null.");
					if (capability.equals("switchLevel") || capability.equals("audioVolume") || capability.equals("ovenSetpoint")) {
						deviceCommand.addArgumentsItem(Integer.parseInt(argument));
					} else {
						deviceCommand.addArgumentsItem(argument);
					}
				}
				
				DeviceCommandsRequest commands = new DeviceCommandsRequest();
				commands.addCommandsItem(deviceCommand);
				
				apiClient.setBasePath(platformUrl);
		        DevicesApi devicesApi = apiClient.buildClient(DevicesApi.class);
		        
		        CreateDeviceCommandsResponse commandResponse = devicesApi.executeDeviceCommands(authToken, deviceId, commands);
		        log.info("[executeDeviceCommands] Request success for environment {}, deviceId: {}, commandResponse: {}", env, deviceId, commandResponse.toString());
			}
	        return new DeviceCommandResponse(deviceId, component, capability, command, argument, "success");
		} catch (Exception e) {
            log.error("[executeDeviceCommands] Exception: {}, LogId {}", e, loggingId);
        }
		 return new DeviceCommandResponse(deviceId, component, capability, command, argument, "Device is not reachable.");
        
	}
}
