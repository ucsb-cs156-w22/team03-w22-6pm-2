package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.documents.EarthquakeFeature;
//import edu.ucsb.cs156.example.services.EarthquakeQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "Earthquake info")
@RequestMapping("/api/earthquakes")
@RestController
@Slf4j
public class EarthquakeController extends ApiController {
    @Autowired
    EarthquakesCollection earthquakeCollection;


    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all earthquakes")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<EarthquakeFeature> allEarthQuakes() {
        Iterable<EarthquakeFeature> quakes = earthquakeCollection.findAll();
        return quakes;
    }
    @ApiOperation(value = "Purge all earthquakes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/purge")
    public void purgeAll() {
        earthquakeCollection.deleteAll();
    }
}
