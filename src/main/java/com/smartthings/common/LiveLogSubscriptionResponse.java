package com.smartthings.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LiveLogSubscriptionResponse {
	@JsonProperty("registrationUrl")
	String registrationUrl;
	
	@JsonProperty("accessToken")
	String accessToken;
}
