package edu.ucsb.cs156.example.documents;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthquakeFeatureCollection {
    private String type;
    private EarthquakeMetadata metadata;
    private List<EarthquakeFeature> features;
    //IDK IF BELOW SHOULD BE HERE
    //ObjectMapper mapper = new ObjectMapper(); 
    //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
}
