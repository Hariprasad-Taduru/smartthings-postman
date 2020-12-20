package com.smartthings.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.common.Constants;
import com.smartthings.common.LiveLogSubscriptionResponse;
import com.smartthings.common.SubscriptionFilters;
import com.smartthings.common.SubscriptionRequest;
import com.smartthings.config.ExternalConfiguration;
import com.smartthings.sdk.client.ApiClient;
import com.smartthings.sdk.client.methods.LocationsApi;
import com.smartthings.sdk.client.models.Location;
import com.smartthings.sdk.client.models.PagedLocation;
import com.smartthings.sdk.client.models.PagedLocations;
import com.smartthings.util.STObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationClient {
	
	@Autowired
	ExternalConfiguration extConfig;
	
	@Value("${smartthings.stgUrl}")
	private String stgUrl; 
	
	@Value("${smartthings.acptUrl}")
	private String acptUrl; 
	
	@Value("${smartthings.prdUrl}")
	private String prdUrl; 
	

	private ApiClient apiClient = new ApiClient();
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    private STObjectMapper stObjectMapper;
	
	private String platformUrl;
	private String authToken;
	private String locationId;
	
	public Location getTestLocation(String env) {
		Location testLocation = null;
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
		
		log.info("[getTestLocation] Requested for environment {}, locationId: {}", env, locationId);
		
		apiClient.setBasePath(platformUrl);
        
        LocationsApi locationApi  =  apiClient.buildClient(LocationsApi.class);
        try {
        	testLocation = locationApi.getLocation(authToken, locationId);
        	//log.info("[getTestLocation] Request success for environment {}, locationId: {}, details: {}", env, locationId, testLocation.toString());
        	log.info("[getTestLocation] Request success for environment {}, locationId: {}", env, locationId);
        } catch (Exception e) {
            log.error("[getTestLocation] Exception: {}, For the location: {}", e, locationId);
        }
        return testLocation;
	}
	public List<Location> getLocations(String env) {
		
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
		
		log.info("[getLocations] Requested for environment {}, locationId: {}", env, locationId);
		
	    List<PagedLocation> pagedLocations = new ArrayList<PagedLocation>();
	    pagedLocations = getPagedLocationsHelper(platformUrl, authToken, pagedLocations);
	    
	    List<Location> locations = new ArrayList<Location>();
	    
	    for (PagedLocation pagedLocation : pagedLocations) {
	    	locations = getLocationsHelper(platformUrl, authToken, pagedLocation.getLocationId(), locations);
	    }
	    
	    log.info("[getLocations] Request success for environment {}, locationId: {}", env, locationId);
	    return locations;
	}
	
	private List<PagedLocation> getPagedLocationsHelper(String completeUrl, String authToken, List<PagedLocation> completePagedLocations) {
        if (completeUrl == null) {
            return completePagedLocations;
        }

        apiClient.setBasePath(completeUrl);
        
        LocationsApi LocationsApi  =  apiClient.buildClient(LocationsApi.class);
        
        PagedLocations pagedLocations = LocationsApi.listLocations(authToken);
        
        completePagedLocations.addAll(pagedLocations.getItems());
        
        if (pagedLocations.getLinks() != null && pagedLocations.getLinks().getNext() != null)     {
            return getPagedLocationsHelper(pagedLocations.getLinks().getNext().getHref(), authToken, completePagedLocations);
        }
        
        return completePagedLocations;
    }
	
	private List<Location> getLocationsHelper(String completeUrl, String authToken, String locationId, List<Location> completeLocations) {
      
		apiClient.setBasePath(completeUrl);
        
        LocationsApi locationApi  =  apiClient.buildClient(LocationsApi.class);
        try {
        	Location location = locationApi.getLocation(authToken, locationId);
        
        	completeLocations.add(location);
        } catch (Exception e) {
            log.error("[getLocationsHelper] Exception: {}, For the location: {}", e, locationId);
        }
        
       return completeLocations;
    }
	
public LiveLogSubscriptionResponse getLiveTrailLogs(String env) {
		
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
		
		log.info("[getLiveTrailLogs] Requested for environment {}, locationId: {}", env, locationId);
		
		String url = platformUrl + "/subscriptions";
		
		String loggingId = UUID.randomUUID().toString();
		
		log.info("[getLiveTrailLogs] Requested for environment {}, locationId: {}, logId: {}", env, locationId, loggingId);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/json");
        headers.set("Contect-Type", "application/json");
        headers.set(Constants.API_HEADER_CORRELATION_ID, loggingId);
        ArrayList<String> locationArray = new ArrayList<String>();
        locationArray.add(locationId);
        SubscriptionFilters filters = new SubscriptionFilters("LOCATIONIDS", locationArray);
        
        ArrayList<SubscriptionFilters> filterList= new ArrayList<SubscriptionFilters>();
        filterList.add(filters);
        
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setName("locationSubscription");
        subscriptionRequest.setVersion("1");
        subscriptionRequest.setSubscriptionFilters(filterList);
        String requestBody = null;
		try {
			requestBody = stObjectMapper.writeValueAsString(subscriptionRequest);
		} catch (JsonProcessingException e1) {
			log.error("[getLiveTrailLogs]Request creation failed: {}", e1.getMessage());
		}
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> response = null;
        
        log.info("[getLiveTrailLogs] Http request body: {}", requestBody);
        
        try {
        	response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
            	
            	log.info("[getLiveTrailLogs] Request success for environment {}, locationId: {}, logId: {}",  env, locationId, loggingId);
            	JsonNode json = stObjectMapper.readTree(response.getBody());
            	return new LiveLogSubscriptionResponse(json.get("registrationUrl").asText(), authToken);
            }
        } catch (Exception e) {
            log.error("[getLiveTrailLogs] Exception: {}, Response: {}, LogId {}", e, response, loggingId);
        }
        return null;
	}
}
