package com.springer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springer.model.Document;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseGenerator {

    //@Autowired
    private ObjectMapper objectMapper;

    public String getResponse(Document document) throws JsonProcessingException {
        if (document==null) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("status","not found");
            messageMap.put("message", "document id not found. please check your id");
            return new JSONObject(messageMap).toJSONString();
        } else if (document.getWatermark()==null) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("status","in progress");
            messageMap.put("message", "document is being processed by the service. please wait");
            return new JSONObject(messageMap).toJSONString();
        } else {
            return objectMapper.writeValueAsString(document);
        }
    }

    @Required
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
