package edu.ucsb.cs156.example.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthquakeFeatureProperties {
    private double mag;
    private String place;
    private long time;
    private long updated;
    private int tz; 
    private String url;
    private String detail;
    private int felt;
    private double cdi;
    private double mmi; //NOT SURE
    private String alert; 
    private String status;
    private int tsunami;
    private int sig;
    private String net;
    private String code;
    private String ids;
    private String sources;
    private String types;
    private int nst;
    private double dmin;
    private double rms;
    private int gap;
    private String magType;
    private String type;
    private String title;
}