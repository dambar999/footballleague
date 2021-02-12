package com.example.football.common;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class ServiceClientUtility {
    @Autowired
    private RestTemplate restTemplate;

    public static final String FAILED_TO_CALL_THE_API_ENDPOINT = "Failed to call the api endpoint %s with code: %s :: %s";


    public ResponseEntity<String> getApiData(String url, HttpEntity<String> header) {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, header, String.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            HttpStatus httpStatusCode = httpClientErrorException.getStatusCode();
            String message = String.format(FAILED_TO_CALL_THE_API_ENDPOINT,
                    url, httpStatusCode, httpClientErrorException);
          // log.error(Constants.ERROR_FORMATTER, Constants.ENTERPRISE_API_SERVER_ERROR_CODE, message);

            throw isRetryRequired(httpStatusCode)
                    ? new RetryableHttpStatusCodeException(httpStatusCode, message)
                    : new NonRetryableHttpStatusCodeException(httpStatusCode, message);
        }
        catch (HttpServerErrorException httpServerErrorException) {
            HttpStatus httpStatusCode = httpServerErrorException.getStatusCode();
            String message = String.format(FAILED_TO_CALL_THE_API_ENDPOINT,
                    url, httpStatusCode, httpServerErrorException);
           // log.error(Constants.ERROR_FORMATTER, Constants.ENTERPRISE_API_SERVER_ERROR_CODE, message);

            throw isRetryRequired(httpStatusCode)
                    ? new RetryableHttpStatusCodeException(httpStatusCode, message)
                    : new NonRetryableHttpStatusCodeException(httpStatusCode, message);
        }
    }

    private boolean isRetryRequired(HttpStatus statusCode) {

        List<HttpStatus> statusCodesForRetry = new ArrayList<>(
                Arrays.asList(
                        HttpStatus.UNAUTHORIZED,
                        HttpStatus.REQUEST_TIMEOUT,
                        HttpStatus.CONFLICT,
                        HttpStatus.BAD_GATEWAY,
                        HttpStatus.SERVICE_UNAVAILABLE,
                        HttpStatus.GATEWAY_TIMEOUT
                ));

        return statusCodesForRetry.contains(statusCode);
    }

    public static RuntimeException requestProcessingException( String message) {
        RuntimeException runtimeException = new RuntimeException(message);
       // log.error(message, runtimeException);

        return runtimeException;
    }

}
