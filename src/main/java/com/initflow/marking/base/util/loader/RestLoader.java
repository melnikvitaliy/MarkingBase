package com.initflow.marking.base.util.loader;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RestLoader {

    private Consumer<RestTemplate> restTemplateInfo;

    public RestLoader(){}
    public RestLoader(Consumer<RestTemplate> restTemplateInfo) {
        this.restTemplateInfo = restTemplateInfo;
    }

    public <T> T get(String url, Map<String, String> headers, Class<T> zlass){
        RestTemplate restTemplate = new RestTemplate();

        if(restTemplateInfo != null) {
            restTemplateInfo.accept(restTemplate);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        HttpEntity entity = new HttpEntity(httpHeaders);

        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET,  entity, zlass);
        return result.getBody();
    }

    public <T,K> T post(String url, K params, Map<String, String> headers, Class<T> zlass){
        RestTemplate restTemplate = new RestTemplate();

        if(restTemplateInfo != null) {
            restTemplateInfo.accept(restTemplate);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        HttpEntity<K> entity = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<T> result = restTemplate.postForEntity(url, entity, zlass);
        return result.getBody();
    }

    public <K> void put(String url, K params, Map<String, String> headers){
        RestTemplate restTemplate = new RestTemplate();

        if(restTemplateInfo != null) {
            restTemplateInfo.accept(restTemplate);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        HttpEntity<K> entity = new HttpEntity<>(params, httpHeaders);

        restTemplate.put(url, entity);
    }

    public <T,K> T getObjectByRequest(String httpMethod, String url, K params, Map<String, String> headers,
                                             Class<T> zlass){
        headers = headers != null ? headers : new HashMap<>();
        headers.put("Content-Type","application/json;charset=UTF-8");
        switch (httpMethod){
            case "GET":
                return get(url, headers, zlass);
            case "POST":
                return post(url, params, headers, zlass);
            case "PUT":
                put(url, params, headers);
                return null;
        }
        return null;
    }
}
