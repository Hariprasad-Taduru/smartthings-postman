package com.smartthings.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartthings.client.LocationClient;
import com.smartthings.common.LiveLogSubscriptionResponse;
import com.smartthings.sdk.client.models.Location;

@Service
public class LocationService {
	
	
	@Autowired
	private LocationClient locationClient;
	
	public Location getTestLocation(String env) {
		return locationClient.getTestLocation(env);
	}
	
	public List<Location> getLocations(String env) {
		return locationClient.getLocations(env);
	}
	
	public LiveLogSubscriptionResponse getLiveTrailLogs(String env) {
		return locationClient.getLiveTrailLogs(env);
	}
}
