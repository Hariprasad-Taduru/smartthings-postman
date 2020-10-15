package com.smartthings.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartthings.sdk.client.ApiClient;
import com.smartthings.sdk.client.methods.LocationsApi;
import com.smartthings.sdk.client.models.Location;
import com.smartthings.sdk.client.models.PagedLocation;
import com.smartthings.sdk.client.models.PagedLocations;

@Service
public class LocationClient {

	ApiClient apiClient = new ApiClient();
	
	public List<Location> getLocations(String platformUrl, String authToken) {
		
	    List<PagedLocation> pagedLocations = new ArrayList<PagedLocation>();
	    pagedLocations = getPagedLocationsHelper(platformUrl, authToken, pagedLocations);
	    
	    List<Location> locations = new ArrayList<Location>();
	    
	    for (PagedLocation pagedLocation : pagedLocations) {
	    	locations = getLocationsHelper(platformUrl, authToken, pagedLocation.getLocationId(), locations);
	    }
	    
	    return locations;
	}
	
	private List<PagedLocation> getPagedLocationsHelper(String completeUrl, String authToken, List<PagedLocation> completePagedLocations) {
        if (completeUrl == null) {
            return completePagedLocations;
        }

        apiClient.setBasePath(completeUrl);
        
        LocationsApi LocationsApi  =  apiClient.buildClient(LocationsApi.class);
        
        PagedLocations pagedLocations = LocationsApi.listLocations("Bearer " + authToken);
        
        completePagedLocations.addAll(pagedLocations.getItems());
        
        if (pagedLocations.getLinks() != null && pagedLocations.getLinks().getNext() != null)     {
            return getPagedLocationsHelper(pagedLocations.getLinks().getNext().getHref(), authToken, completePagedLocations);
        }
        
        return completePagedLocations;
    }
	
	private List<Location> getLocationsHelper(String completeUrl, String authToken, String locationId, List<Location> completeLocations) {
      
		apiClient.setBasePath(completeUrl);
        
        LocationsApi locationApi  =  apiClient.buildClient(LocationsApi.class);
        
        Location location = locationApi.getLocation("Bearer " + authToken, locationId);
        
        completeLocations.add(location);
        
       return completeLocations;
    }
}
