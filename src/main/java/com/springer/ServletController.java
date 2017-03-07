package com.springer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springer.model.Book;
import com.springer.model.Document;
import com.springer.model.Journal;
import com.springer.service.ResponseGenerator;
import com.springer.service.WatermarkStoreImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.Callable;

@Controller
@RequestMapping("/v1")
public class ServletController {

    @Autowired
    private WatermarkStoreImpl watermarkStore;
    @Autowired
    private ResponseGenerator responseGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(
            value = {"/status/{id}"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public WebAsyncTask<String> getWatermarkStatus(
            final HttpServletRequest request,
            @PathVariable String id) {

        return toTask(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return responseGenerator.getResponse(watermarkStore.getStatus(id));
            }
        });
    }

    @RequestMapping(
            value = {"/watermark"},
            method = RequestMethod.POST,
            produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public WebAsyncTask<String> process(
            final HttpServletRequest request,
            @RequestBody final JSONObject documentJson) {

        return toTask(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {

                    if (documentJson==null || documentJson.size()==0) {
                        throw new Exception("Json cannot be empty");
                    }

                    Document document;

                    if (documentJson.get("topic") != null) {
                        document = objectMapper.readValue(documentJson.toJSONString(), Book.class);
                    } else {
                        document = objectMapper.readValue(documentJson.toJSONString(), Journal.class);
                    }

                    return watermarkStore.processDocument(document);
                } catch (com.fasterxml.jackson.core.JsonParseException | com.fasterxml.jackson.databind.JsonMappingException ex) {
                    return "Incorrect document";
                } catch (Exception ex) {
                    return "Service error";
                }
            }
        });
    }

    public void setWatermarkStore(WatermarkStoreImpl watermarkStore) {
        this.watermarkStore = watermarkStore;
    }

    private <T> WebAsyncTask<T> toTask(final Callable<T> callable) {
        return new WebAsyncTask<T>(10000, callable);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
