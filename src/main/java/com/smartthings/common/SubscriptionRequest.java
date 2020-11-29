package com.smartthings.common;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubscriptionRequest {
	@JsonProperty("name")
	String name;
	
	@JsonProperty("version")
	String version;
	
	@JsonProperty("subscriptionFilters")
	ArrayList<SubscriptionFilters> subscriptionFilters;
}
