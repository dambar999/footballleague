package com.example.football.controller;

import com.example.footbball.entity.LeagueDetails;
import com.example.footbball.service.IFootballLeagueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
public class FootballLeagueResource {


    @Autowired
    IFootballLeagueService iFootballLeagueService;



    @GetMapping(value = "/v1/products/{id}", produces = "application/json")
    public ResponseEntity<String> productDetailsAPI(@RequestHeader Map<String,String> headers,

                                                           @RequestParam("id") String id){

        String leagueDetails=null;
        try{

            leagueDetails= iFootballLeagueService.getLeagueStandings(id);

        } catch (Exception e){
            //log.error("Error occurred while calling league Dtails API, error = {0}", e);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(leagueDetails,HttpStatus.OK);
    }



}