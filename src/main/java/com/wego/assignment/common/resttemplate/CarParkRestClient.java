package com.wego.assignment.common.resttemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CarParkRestClient {

    RestTemplate restTemplate = new RestTemplate();

    @Bean(name = "carparkRestClient")
    public RestTemplate restTemplate() {

        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();

        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }

        interceptors.add(new CarParkRestInterceptor());

        restTemplate.setInterceptors(interceptors);
        restTemplate.setErrorHandler(new CarParkRestTemplateErrorHandler());

        return restTemplate;
    }
}
