package com.smartthings.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartthings.client.UserClient;


@Service
public class UserService {
	
	@Autowired
	private UserClient userClient;
	
    // User details
	public JsonNode getUserDetails(String env) {
		return userClient.getUserDetails(env);
	}
}
