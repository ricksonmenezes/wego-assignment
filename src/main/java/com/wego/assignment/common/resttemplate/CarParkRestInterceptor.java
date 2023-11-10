package com.wego.assignment.common.resttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CarParkRestInterceptor  implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        try {

            response = execution.execute(request, body);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }

        return response;
    }

}
