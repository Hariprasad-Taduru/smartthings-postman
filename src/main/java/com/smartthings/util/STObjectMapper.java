package com.smartthings.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class STObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 6949142999181671141L;

	public STObjectMapper() {
        super();
        enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
