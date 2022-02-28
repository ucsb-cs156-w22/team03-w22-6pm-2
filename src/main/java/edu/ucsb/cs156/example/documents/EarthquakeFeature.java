package edu.ucsb.cs156.example.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthquakeFeature{
    @Id
    private String _id;

    private String type;
    private EarthquakeFeatureProperties properties;
    private String id;
}
