package com.example.football.service.impl;

import com.example.football.common.RetryableHttpStatusCodeException;
import com.example.football.common.ServiceClientUtility;
import com.example.footbball.entity.LeagueDetails;
import com.example.footbball.service.IFootballLeagueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static com.example.football.common.ServiceClientUtility.requestProcessingException;


@Service
public class FootbalLeagueServiceImpl implements IFootballLeagueService {

  @Autowired ServiceClientUtility serviceClientUtility;

  @Value("${external.endpoint}")
  private String externalBaseEndpoint;

  @Value("${external.apikey}")
  private String externalApikey;

  @Override
  @Retryable(
      value = {RetryableHttpStatusCodeException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 3000))
  public String getLeagueStandings(String leagueId) {

   // log.info("getting league details from external API");

    try {
      String externalBaseEndpointUrl = buildGetProductNameUrl(externalBaseEndpoint, leagueId);
      HttpHeaders headers = new HttpHeaders();

      HttpEntity<String> requestEntity = new HttpEntity<>(headers);
      ResponseEntity<String> responseEntity =
          serviceClientUtility.getApiData(externalBaseEndpointUrl, requestEntity);
      return responseEntity.getBody();
    } catch (Exception e) {
      throw requestProcessingException(
           String.format("exception Calling External  API : %s", e));
    }
  }

  private String buildGetProductNameUrl(String baseEndPoint, String leagueId) {

    return baseEndPoint + leagueId + "&APIkey="+externalApikey;
  }

}