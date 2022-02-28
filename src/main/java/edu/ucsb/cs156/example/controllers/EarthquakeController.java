package edu.ucsb.cs156.example.controllers;


import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.documents.EarthquakeFeature;
import edu.ucsb.cs156.example.documents.EarthquakeFeatureCollection;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

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
import java.util.List;


@Api(description = "Earthquake info")
@RequestMapping("/api/earthquakes")
@RestController
@Slf4j
public class EarthquakeController extends ApiController {
    @Autowired
    EarthquakesCollection earthquakesCollection;


    @Autowired
    ObjectMapper mapper;

    @Autowired
    EarthquakeQueryService earthquakeQueryService;
    
    @ApiOperation(value = "List all earthquakes")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<EarthquakeFeature> allEarthQuakes() {
        Iterable<EarthquakeFeature> quakes = earthquakesCollection.findAll();
        return quakes;
    }
    @ApiOperation(value = "Purge all earthquakes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/purge")
    public void purgeAll() {
        earthquakesCollection.deleteAll();
    }
    @Autowired
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Store one earthquake from USGS Earthquake API", notes = "")
    @PostMapping("/retrieve")
    public ResponseEntity<List<EarthquakeFeature>> getEarthquake(
            @ApiParam("distance in km, e.g. 100") @RequestParam String distance,
            @ApiParam("minimum magnitude, e.g 2.5") @RequestParam String minMag
            ) throws JsonProcessingException {
        log.info("getEarthquake: distance={} minMag ={}", distance, minMag);
        
        String featuresAsJSON = earthquakeQueryService.getJSON(distance, minMag);
        EarthquakeFeatureCollection featureCollection = mapper.readValue(featuresAsJSON, EarthquakeFeatureCollection.class); 
        List<EarthquakeFeature> features = featureCollection.getFeatures();
        List<EarthquakeFeature> featuresSaved = earthquakesCollection.saveAll(features);
        return ResponseEntity.ok().body(featuresSaved);
    }
}
