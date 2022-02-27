package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.documents.EarthquakeFeature;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;
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

    @Autowired
    EarthquakeQueryService earthquakeQueryService;
    
    @ApiOperation(value = "Get earthquakes within a certain distance of UCSB's Storke Tower and above a certain magnitude, and add them to the MongoDB database")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/retrieve")
    public ResponseEntity<String> retrieveEarthquakes(
            @ApiParam("distance in km, e.g. 100") @RequestParam String dist,
            @ApiParam("minimum magnitude, e.g. 2.5") @RequestParam String minMag
        ) throws JsonProcessingException {
            log.info("retrieveEarthquakes: dist={} minMag={}", dist, minMag);
            String results = earthquakeQueryService.getJSON(dist, minMag);
            EarthquakesCollection collection = mapper.readValue(results, EarthquakesCollection.class);
            List<EarthquakeFeature> features = collection.getFeatures();
            earthquakeCollection.saveAll(features);

            return ResponseEntity.ok().body(results);
    }
    @ApiOperation(value = "List all earthquakes")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<EarthquakeFeature> allEarthQuakes() {
        Iterable<EarthquakeFeature> quakes = earthquakeCollection.findAll();
        return quakes;
    }
    @ApiOperation(value = "Purge all earthquakes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/purge")
    public void purgeAll() {
        earthquakeCollection.deleteAll();
    }
}
