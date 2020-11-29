package com.smartthings.common;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionFilters {
	@JsonProperty("type")
	String type;
	
	@JsonProperty("value")
	ArrayList<String> value;
}
