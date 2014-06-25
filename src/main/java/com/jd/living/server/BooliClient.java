package com.jd.living.server;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.jd.living.model.ListingsResult;
import com.jd.living.model.Result;
import com.jd.living.model.SoldResult;

@Rest(rootUrl = "http://api.booli.se", converters = { GsonHttpMessageConverter.class })
@Accept(MediaType.APPLICATION_JSON)
public interface BooliClient {

    @Get("/listings?q={search}&minRooms={minRooms}&maxRooms={maxRooms}&objectType={objectType}&isNewConstruction={isNewConstruction}&limit={limit}}" + BooliServer.common)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<ListingsResult> getListings(String search, String callerId, long time, String unique, String hash, String minRooms, String maxRooms, String objectType, String isNewConstruction, int limit);

    @Get("/listings/{booliId}" + BooliServer.common)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<Result> getListing(int booliId, String callerId, long time, String unique, String hash);

    @Get("/sold?q={search}&minRooms={minRooms}&maxRooms={maxRooms}&objectType={objectType}&isNewConstruction={isNewConstruction}&limit={limit}}" + BooliServer.common)
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SoldResult> getObjectsSold(String search, String callerId, long time, String unique, String hash, String minRooms, String maxRooms, String objectType, String isNewConstruction, int limit);


    RestTemplate getRestTemplate();

    void setRestTemplate(RestTemplate restTemplate);
}

