package com.github.ivanig.bankserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ivanig.bankserver.dto.ResponseToAtm;

public class JSONUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJSON(ResponseToAtm response) {
        String json = "{\"Ooops!\":\"Mapping error\"}";
        try {
            json = OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
