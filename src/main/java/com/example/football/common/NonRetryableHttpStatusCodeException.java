package com.example.football.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class NonRetryableHttpStatusCodeException extends HttpStatusCodeException {

   public NonRetryableHttpStatusCodeException(HttpStatus statusCode, String message){
        super(statusCode,message);
    }
}
