package com.springer.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpUtils {
    private static HttpClient httpClient = HttpClients.createDefault();;

    public static HttpResponse sendGetRequest(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return httpClient.execute(request);
    }

    public static HttpResponse sendPostRequest(String url, String body) throws IOException {
        HttpPost request = new HttpPost(url);
        HttpEntity entity = new StringEntity(body);
        request.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        request.setEntity(entity);
        return httpClient.execute(request);
    }

}
