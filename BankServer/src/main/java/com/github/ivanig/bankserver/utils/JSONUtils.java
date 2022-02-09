package com.github.ivanig.bankserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ivanig.bankserver.domain.Account;

import java.util.Set;

public class JSONUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

//    public static String toJSON(ResponseDTO response) {
//        String json = "{\"Ooops!\":\"Mapping error\"}";
//        try {
//            json = OBJECT_MAPPER.writeValueAsString(response);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }

    public static String setToJSON(Set<Account> response) {
        String json = "{\"Ooops!\":\"Mapping error\"}";
        try {
            json = OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
