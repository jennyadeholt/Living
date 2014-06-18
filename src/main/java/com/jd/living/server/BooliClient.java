package com.jd.living.server;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.jd.living.model.Result;

@Rest(rootUrl = "http://api.booli.se", converters = { GsonHttpMessageConverter.class })
@Accept(MediaType.APPLICATION_JSON)
public interface BooliClient {

    @Get("/listings?q={search}&callerId={id}&time={time}&unique={unique}&hash={hash}&minRooms={minRooms}&maxRooms={maxRooms}&objectType={objectType}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<Result> getListings(String search, String id, long time, String unique, String hash, String minRooms, String maxRooms, String objectType);

    @Get("/listings?{id}&time={time}&unique={unique}&hash={hash}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<Result> getListing( String id, long time, String unique, String hash);




    RestTemplate getRestTemplate();

    void setRestTemplate(RestTemplate restTemplate);
}

