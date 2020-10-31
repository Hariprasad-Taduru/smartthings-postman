package com.smartthings.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smartthings.config.ExternalConfiguration;
import com.smartthings.sdk.client.ApiClient;
import com.smartthings.sdk.client.methods.LocationsApi;
import com.smartthings.sdk.client.models.Location;
import com.smartthings.sdk.client.models.PagedLocation;
import com.smartthings.sdk.client.models.PagedLocations;

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
        	log.info("[getTestLocation] Request success for environment {}, locationId: {}, details: {}", env, locationId, testLocation.toString());
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
}
